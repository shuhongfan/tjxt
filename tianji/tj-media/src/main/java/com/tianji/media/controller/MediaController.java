package com.tianji.media.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.media.domain.dto.MediaDTO;
import com.tianji.media.domain.dto.MediaUploadResultDTO;
import com.tianji.media.domain.query.MediaQuery;
import com.tianji.media.domain.vo.MediaVO;
import com.tianji.media.domain.vo.VideoPlayVO;
import com.tianji.media.service.IMediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 媒资表，主要是视频文件 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-30
 */
@RestController
@RequestMapping("/medias")
@Api(tags = "媒资管理相关接口")
@RequiredArgsConstructor
public class MediaController {

    private final IMediaService mediaService;

    @ApiOperation("分页搜索已上传媒资信息")
    @GetMapping
    public PageDTO<MediaVO> queryMediaPage(MediaQuery query){
        return mediaService.queryMediaPage(query);
    }

    @ApiOperation("上传视频后保存媒资信息")
    @PostMapping
    public MediaDTO saveMedia(@RequestBody MediaUploadResultDTO result) {
        return mediaService.save(result);
    }

    @ApiOperation("获取上传视频的授权签名")
    @GetMapping("/signature/upload")
    public String getUploadSignature(){
        return mediaService.getUploadSignature();
    }

    @ApiOperation("获取播放视频的授权签名")
    @GetMapping("/signature/play")
    public VideoPlayVO getPlaySignature(
            @ApiParam(value = "小节id", example = "1", required = true) @RequestParam("sectionId") Long sectionId){
        return mediaService.getPlaySignatureBySectionId(sectionId);
    }

    @ApiOperation("管理端获取预览视频的授权签名")
    @GetMapping("/signature/preview")
    public VideoPlayVO getPreviewSignature(
            @ApiParam(value = "媒资id", example = "1", required = true) @RequestParam("mediaId") Long mediaId){
        return mediaService.getPlaySignatureByMediaId(mediaId);
    }

    @ApiOperation("删除媒资视频")
    @DeleteMapping("{mediaId}")
    public void deleteMedia(
            @ApiParam(value = "媒资id", example = "1", required = true) @PathVariable("mediaId") Long mediaId){
        mediaService.removeById(mediaId);
    }

    @ApiOperation("批量删除媒资视频")
    @DeleteMapping
    public void deleteMedias(
            @ApiParam(value = "媒资id集合，例如1,2,3", required = true) @RequestParam("ids") List<Long> mediaIds){
        mediaService.removeByIds(mediaIds);
    }
}
