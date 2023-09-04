package com.tianji.message.domain.dto;

import com.tianji.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 公告消息模板
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "通知任务")
public class NoticeTaskDTO extends BaseDTO {

    @ApiModelProperty("任务id，新增时无需填写")
    private Long id;
    @ApiModelProperty("任务要发送的通知模板id")
    private Long templateId;
    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("true-通知所有人;false-通知部分人。默认false")
    private Boolean partial;
    @ApiModelProperty("任务预期执行时间")
    private LocalDateTime pushTime;
    @ApiModelProperty("任务重复执行次数上限，0则不重复")
    private Integer maxTimes;
    @ApiModelProperty("任务重复执行时间间隔，单位是分钟")
    private Long interval;
    @ApiModelProperty("任务失效时间")
    private LocalDateTime expireTime;
    @ApiModelProperty("任务是否已经完成。默认false")
    private Boolean finished;
}
