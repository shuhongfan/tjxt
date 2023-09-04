package com.tianji.course.domain.dto;

import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.validate.Checker;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.constants.CourseErrorInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author wusongsong
 * @since 2022/7/11 16:49
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "章节")
public class CataSaveDTO implements Checker {
    @ApiModelProperty("章、节、练习id")
    private Long id;
    @ApiModelProperty("目录类型1：章，2：节，3：测试")
    @NotNull(message = "")
    private Integer type;
    @ApiModelProperty("章节练习名称")
    private String name;
    @ApiModelProperty("章排序，章一定要传，小节和练习不需要传")
    private Integer index;

    @ApiModelProperty("当前章的小节或练习")
    @Size(min = 1, message = "不能出现空章")
    private List<CataSaveDTO> sections;

    @Override
    public void check() {
        //名称为空校验
        if(type == CourseConstants.CataType.CHAPTER && StringUtils.isEmpty(name)) {
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_NAME_NULL);
        }else if(StringUtils.isEmpty(name)){
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_NAME_NULL2);
        }
        //名称长度问题
        if (type == CourseConstants.CataType.CHAPTER && name.length() > 30){
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_NAME_SIZE);
        }else if(name.length() > 30) {
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_NAME_SIZE2);
        }
        if(CollUtils.isEmpty(sections)){
            throw new BadRequestException("不能出现空章");
        }

    }
}
