package com.tianji.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.tianji.message.domain.dto.SmsThirdPlatformDTO;
import com.tianji.message.domain.dto.SmsThirdPlatformFormDTO;
import com.tianji.message.domain.query.SmsThirdPlatformPageQuery;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.message.domain.po.SmsThirdPlatform;
import com.tianji.message.mapper.SmsThirdPlatformMapper;
import com.tianji.message.service.ISmsThirdPlatformService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 第三方云通讯平台 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Service
public class SmsThirdPlatformServiceImpl extends ServiceImpl<SmsThirdPlatformMapper, SmsThirdPlatform> implements ISmsThirdPlatformService {

    @Resource
    private Cache<String, List<SmsThirdPlatform>> platformCache;

    @Override
    public List<SmsThirdPlatform> queryAllPlatform(){
        return platformCache.get("PLATFORM", key -> lambdaQuery().orderByAsc(SmsThirdPlatform::getPriority).list());
    }

    @Override
    public Long saveSmsThirdPlatform(SmsThirdPlatformFormDTO thirdPlatformDTO) {
        SmsThirdPlatform thirdPlatform = BeanUtils.copyBean(thirdPlatformDTO, SmsThirdPlatform.class);
        save(thirdPlatform);
        return thirdPlatform.getId();
    }

    @Override
    public void updateSmsThirdPlatform(SmsThirdPlatformFormDTO thirdPlatformDTO) {
        updateById(BeanUtils.copyBean(thirdPlatformDTO, SmsThirdPlatform.class));
    }

    @Override
    public PageDTO<SmsThirdPlatformDTO> querySmsThirdPlatforms(SmsThirdPlatformPageQuery query) {
        // 1.分页条件
        Page<SmsThirdPlatform> page = query.toMpPage();
        // 2.过滤条件
        page = lambdaQuery()
                .eq(query.getStatus() != null, SmsThirdPlatform::getStatus, query.getStatus())
                .like(StringUtils.isNotBlank(query.getKeyword()), SmsThirdPlatform::getName, query.getKeyword())
                .page(page);
        // 3.数据转换
        return PageDTO.of(page, SmsThirdPlatformDTO.class);
    }

    @Override
    public SmsThirdPlatformDTO querySmsThirdPlatform(Long id) {
        return BeanUtils.copyProperties(getById(id), SmsThirdPlatformDTO.class);
    }
}
