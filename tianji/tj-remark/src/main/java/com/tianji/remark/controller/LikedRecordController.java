package com.tianji.remark.controller;

import com.tianji.remark.domain.dto.LikeRecordFormDTO;
import com.tianji.remark.service.ILikedRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 点赞记录表 控制器
 * </p>
 *
 * @author 虎哥
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
@Api(tags = "点赞业务相关接口")
public class LikedRecordController {

    private final ILikedRecordService likedRecordService;

    @PostMapping
    @ApiOperation("点赞或取消点赞")
    public void addLikeRecord(@Valid @RequestBody LikeRecordFormDTO recordDTO) {
        likedRecordService.addLikeRecord(recordDTO);
    }

    @GetMapping("list")
    @ApiOperation("查询指定业务id的点赞状态")
    public Set<Long> isBizLiked(@RequestParam("bizIds") List<Long> bizIds){
        return likedRecordService.isBizLiked(bizIds);
    }
}
