package com.tianji.media.controller;


import com.tianji.media.domain.dto.FileDTO;
import com.tianji.media.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件表，可以是普通文件、图片等 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-30
 */
@RestController
@RequestMapping("/files")
@Api(tags = "媒资管理相关接口")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @ApiOperation("上传文件")
    @PostMapping
    public FileDTO uploadFile(
            @ApiParam(value = "文件数据") @RequestParam("file")MultipartFile file){
        return fileService.uploadFile(file);
    }

    @ApiOperation("获取文件信息")
    @GetMapping("/{id}")
    public FileDTO getFileInfo(
            @ApiParam(value = "文件id", example = "1") @PathVariable("id") Long id){
        return fileService.getFileInfo(id);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/{id}")
    public void deleteFileById(
            @ApiParam(value = "文件id", example = "1") @PathVariable("id") Long id) {
        fileService.removeById(id);
    }
}
