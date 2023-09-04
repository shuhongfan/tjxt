package com.tianji.learning.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 笔记id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 章id
     */
    private Long chapterId;

    /**
     * 小节id
     */
    private Long sectionId;

    /**
     * 记录笔记时的视频播放时间，单位秒
     */
    private Integer noteMoment;

    /**
     * 笔记内容
     */
    private String content;

    /**
     * 是否是隐私笔记，默认false
     */
    private Boolean isPrivate;

    /**
     * 是否被折叠（隐藏）默认false
     */
    private Boolean hidden;

    /**
     * 被隐藏的原因
     */
    private String hiddenReason;

    /**
     * 笔记作者id
     */
    private Long authorId;

    /**
     * 被采集笔记的id
     */
    private Long gatheredNoteId;

    /**
     * 是否是采集他人的笔记，默认false
     */
    private Boolean isGathered;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
