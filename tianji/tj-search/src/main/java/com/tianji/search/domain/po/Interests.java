package com.tianji.search.domain.po;

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
 * 用户兴趣表，保存感兴趣的二级分类id
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("interests")
public class Interests implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，对应用户id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 感兴趣的二级分类id，以逗号分隔，例如：120,220,330
     */
    private String interests;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
