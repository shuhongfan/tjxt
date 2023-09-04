package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 公告消息模板
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Data
@ApiModel(description = "通知任务的表单实体")
public class NoticeTaskFormDTO {

    @ApiModelProperty("任务id，新增时无需填写")
    private Long id;
    @ApiModelProperty("任务要发送的通知模板id")
    private Long templateId;
    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("true-通知所有人;false-通知部分人。默认false")
    private Boolean partial;
    @ApiModelProperty("任务预期执行时间，如果为null，或者小于等于当前时间，则立刻执行")
    private LocalDateTime pushTime;
    @ApiModelProperty("任务重复执行次数上限，0则不重复")
    private Integer maxTimes;
    @ApiModelProperty("任务重复执行时间间隔，单位是分钟")
    private Long interval;
    @ApiModelProperty("任务失效时间")
    private LocalDateTime expireTime;
}
