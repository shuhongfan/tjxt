package com.tianji.learning.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("points_board_season")
public class PointsBoardSeason implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长id，season标示
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 赛季名称，例如：第1赛季
     */
    private String name;

    /**
     * 赛季开始时间
     */
    private LocalDate beginTime;

    /**
     * 赛季结束时间
     */
    private LocalDate endTime;


}
