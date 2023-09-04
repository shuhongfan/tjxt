package com.tianji.search.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Course {
    @Id
    private Long id;
    /** 课程名称 */
    private String name;
    /** 分类id */
    private Long categoryIdLv1;
    /** 分类id2 */
    private Long categoryIdLv2;
    /** 分类id3 */
    private Long categoryIdLv3;
    /** 是否免费 */
    private Boolean free;
    /** 课程类型：1：直播课，2：录播课 */
    private Integer type;
    /** 课程销量，报名人数 */
    private Integer sold;
    /** 价格 */
    private Integer price;
    /** 课程评分 */
    private Integer score;
    /** 老师id */
    private Long teacher;
    /** 章节数量 */
    private Integer sections;
    /** 课程封面 */
    private String coverUrl;
    private LocalDateTime publishTime;

    @JsonIgnore
    public List<Long> getCategoryIds(){
        return List.of(categoryIdLv1, categoryIdLv2, categoryIdLv3);
    }
}
