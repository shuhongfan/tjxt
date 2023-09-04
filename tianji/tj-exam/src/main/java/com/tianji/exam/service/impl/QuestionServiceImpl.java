package com.tianji.exam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.cache.CategoryCache;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.api.dto.exam.QuestionDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.constants.Constant;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.exam.domain.dto.QuestionFormDTO;
import com.tianji.exam.domain.po.Question;
import com.tianji.exam.domain.po.QuestionBiz;
import com.tianji.exam.domain.po.QuestionDetail;
import com.tianji.exam.domain.query.QuestionPageQuery;
import com.tianji.exam.domain.vo.QuestionDetailVO;
import com.tianji.exam.domain.vo.QuestionPageVO;
import com.tianji.exam.mapper.QuestionMapper;
import com.tianji.exam.service.IQuestionBizService;
import com.tianji.exam.service.IQuestionDetailService;
import com.tianji.exam.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tianji.exam.constants.ExamErrorInfo.QUESTION_NOT_EXISTS;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    private final IQuestionDetailService detailService;
    private final IQuestionBizService bizService;
    private final UserClient userClient;
    private final CategoryCache categoryCache;

    @Override
    @Transactional
    public void addQuestion(QuestionFormDTO questionDTO) {
        // 1.保存题目信息
        Question question = BeanUtils.copyBean(questionDTO, Question.class);
        List<Long> cateIds = questionDTO.getCateIds();
        if (cateIds.size() < 3) {
            throw new BadRequestException("题目必须关联三级分类");
        }
        question.setCateId1(cateIds.get(0));
        question.setCateId2(cateIds.get(1));
        question.setCateId3(cateIds.get(2));
        save(question);

        // 2.保存详情
        QuestionDetail detail = new QuestionDetail()
                .setId(question.getId())
                .setAnalysis(questionDTO.getAnalysis())
                .setAnswer(questionDTO.getAnswer())
                .setOptions(questionDTO.getOptions());
        detailService.save(detail);
    }

    @Override
    public void updateQuestion(QuestionFormDTO questionDTO) {
        // 1.保存题目信息
        Question question = BeanUtils.copyBean(questionDTO, Question.class);
        List<Long> cateIds = questionDTO.getCateIds();
        if (CollUtils.isNotEmpty(cateIds) && cateIds.size() == 3) {
            question.setCateId1(cateIds.get(0));
            question.setCateId2(cateIds.get(1));
            question.setCateId3(cateIds.get(2));
        }
        updateById(question);

        // 2.保存详情
        QuestionDetail detail = new QuestionDetail()
                .setId(question.getId())
                .setAnalysis(questionDTO.getAnalysis())
                .setAnswer(questionDTO.getAnswer())
                .setOptions(questionDTO.getOptions());
        detailService.updateById(detail);
    }

    @Override
    public void deleteQuestionById(Long id) {
        // 1.查询题目和业务之间是否有关联
        int usedTimes = bizService.countUsedTimes(id);
        if (usedTimes > 0) {
            throw new BadRequestException("题目被使用中，无法删除");
        }
        // 2.删除题目
        removeById(id);
        // 3.删除详情
        detailService.removeById(id);
    }

    @Override
    public PageDTO<QuestionPageVO> queryQuestionByPage(QuestionPageQuery query) {
        // 1.分页搜索
        Page<Question> page = lambdaQuery()
                .eq(query.getDifficulty() != null, Question::getDifficulty, query.getDifficulty())
                .eq(query.getCreater() != null, Question::getCreater, query.getCreater())
                .in(CollUtils.isNotEmpty(query.getTypes()), Question::getType, query.getTypes())
                .in(CollUtils.isNotEmpty(query.getCateIds()), Question::getCateId3, query.getCateIds())
                .like(StringUtils.isNotBlank(query.getKeyword()), Question::getName, query.getKeyword())
                .page(query.toMpPage(Constant.DATA_FIELD_NAME_UPDATE_TIME, false));
        // 2.判空
        List<Question> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }
        // 3.查询VO信息，包含：引用次数、提问者信息
        Set<Long> qIds = new HashSet<>();
        Set<Long> uIds = new HashSet<>();
        for (Question record : records) {
            qIds.add(record.getId());
            uIds.add(record.getUpdater());
        }
        // 3.1.统计引用次数
        Map<Long, Integer> countMap = bizService.countUsedTimes(qIds);
        // 3.2.查询用户
        Map<Long, UserDTO> userMap = new HashMap<>(uIds.size());
        if (CollUtils.isNotEmpty(uIds)) {
            List<UserDTO> users = userClient.queryUserByIds(uIds);
            userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));
        }
        // 4.处理vo
        List<QuestionPageVO> list = new ArrayList<>(records.size());
        for (Question r : records) {
            // 4.1.转换为vo
            QuestionPageVO v = BeanUtils.toBean(r, QuestionPageVO.class);
            list.add(v);
            // 4.2.获取用户
            UserDTO u = userMap.get(r.getUpdater());
            v.setUpdater(u == null ? "" : u.getName());
            // 4.3.分类
            v.setCategories(categoryCache.getCategoryNameList(List.of(r.getCateId1(), r.getCateId2(), r.getCateId3())));
            // 4.4.引用次数
            v.setUseTimes(countMap.getOrDefault(r.getId(), 0));
        }
        return PageDTO.of(page, list);
    }

    @Override
    public QuestionDetailVO queryQuestionDetailById(Long id) {
        // 1.查询题目
        Question q = getById(id);
        if (q == null) {
            throw new BadRequestException(QUESTION_NOT_EXISTS);
        }
        // 2.查询详情
        QuestionDetail detail = detailService.getById(id);
        if (detail == null) {
            throw new BadRequestException(QUESTION_NOT_EXISTS);
        }
        // 3.查询题目的录入者
        UserDTO u = userClient.queryUserById(q.getCreater());
        // 4.转换vo
        QuestionDetailVO v = BeanUtils.copyBean(q, QuestionDetailVO.class);
        // 4.1.详情
        v.setOptions(detail.getOptions());
        v.setAnalysis(detail.getAnalysis());
        v.setAnswer(detail.getAnswer());
        // 4.2.用户
        v.setUpdater(u == null ? "" : u.getName());
        // 4.3.分类
        v.setCategories(categoryCache.getCategoryNameList(List.of(q.getCateId1(), q.getCateId2(), q.getCateId3())));
        // 4.4.引用次数
        v.setUseTimes(bizService.countUsedTimes(id));
        return v;
    }

    @Override
    public List<QuestionDTO> queryQuestionByIds(List<Long> ids) {
        // 1.查询题目集合
        List<Question> questions = listByIds(ids);
        if (CollUtils.isEmpty(questions)) {
            return CollUtils.emptyList();
        }

        // 2.查询详情
        List<QuestionDetail> details = detailService.listByIds(ids);
        if (details == null || questions.size() != details.size()) {
            throw new BadRequestException(QUESTION_NOT_EXISTS);
        }
        Map<Long, QuestionDetail> detailMap = details.stream().collect(Collectors.toMap(QuestionDetail::getId, d -> d));

        // 3.数据转换
        List<QuestionDTO> list = new ArrayList<>(questions.size());
        for (Question q : questions) {
            // 3.1.转vo
            QuestionDTO d = BeanUtils.toBean(q, QuestionDTO.class);
            list.add(d);
            // 3.2.获取详情
            QuestionDetail detail = detailMap.get(q.getId());
            d.setOptions(detail.getOptions());
            d.setAnalysis(detail.getAnalysis());
            d.setAnswer(detail.getAnswer());
        }
        return list;
    }

    @Override
    public Map<Long, Integer> countQuestionNumOfCreater(List<Long> createrIds) {
        // 1.统计
        List<IdAndNumDTO> list = baseMapper.countQuestionOfCreater(createrIds);
        // 2.处理结果
        return IdAndNumDTO.toMap(list);
    }

    @Override
    public List<QuestionDTO> queryQuestionByBizId(Long bizId) {
        // 1.查询中间表
        List<QuestionBiz> list = bizService.lambdaQuery()
                .eq(QuestionBiz::getBizId, bizId)
                .list();
        if (CollUtils.isEmpty(list)) {
            return CollUtils.emptyList();
        }
        // 2.获取问题id
        List<Long> ids = list.stream().map(QuestionBiz::getQuestionId).collect(Collectors.toList());
        // 3.查询数据集合
        return queryQuestionByIds(ids);
    }

    @Override
    public Boolean checkNameValid(String name) {
        return lambdaQuery()
                .eq(Question::getName, name)
                .count()<=0;
    }

    @Override
    public Map<Long, Integer> queryQuestionScores(List<Long> ids) {
        // 1.根据id查询题目
        List<Question> questions = listByIds(ids);
        // 2.判空
        if (CollUtils.isEmpty(questions)) {
            return CollUtils.emptyMap();
        }
        return questions.stream().collect(Collectors.toMap(Question::getId, Question::getScore));
    }
}
