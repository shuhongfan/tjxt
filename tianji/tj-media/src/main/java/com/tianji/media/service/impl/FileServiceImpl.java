package com.tianji.media.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.StringUtils;
import com.tianji.media.config.PlatformProperties;
import com.tianji.media.domain.dto.FileDTO;
import com.tianji.media.domain.po.File;
import com.tianji.media.enums.FileErrorInfo;
import com.tianji.media.enums.FileStatus;
import com.tianji.media.mapper.FileMapper;
import com.tianji.media.service.IFileService;
import com.tianji.media.storage.IFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * 文件表，可以是普通文件、图片等 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final IFileStorage fileStorage;
    private final PlatformProperties properties;

    @Override
    public FileDTO uploadFile(MultipartFile file) {
        // 1.获取文件名称
        String originalFilename = file.getOriginalFilename();
        // 2.生成新文件名
        String filename = generateNewFileName(originalFilename);
        // 3.获取文件流
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new CommonException("文件读取异常", e);
        }
        // 4.上传文件
        String requestId = fileStorage.uploadFile(filename, inputStream, file.getSize());
        // 5.写入数据库
        File fileInfo = null;
        try {
            fileInfo = new File();
            fileInfo.setFilename(originalFilename);
            fileInfo.setKey(filename);
            fileInfo.setStatus(FileStatus.UPLOADED);
            fileInfo.setRequestId(requestId);
            fileInfo.setPlatform(properties.getFile());
            save(fileInfo);
        } catch (Exception e) {
            log.error("文件信息保存异常", e);
            fileStorage.deleteFile(filename);
            throw new DbException(FileErrorInfo.Msg.FILE_UPLOAD_ERROR);
        }
        // 6.返回
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileInfo.getId());
        fileDTO.setPath(fileInfo.getPlatform().getPath() + filename);
        fileDTO.setFilename(originalFilename);
        return fileDTO;
    }

    @Override
    public FileDTO getFileInfo(Long id) {
        File file = getById(id);
        if (file == null) {
            return null;
        }
        return FileDTO.of(file.getId(), file.getFilename(), file.getPlatform().getPath() + file.getKey());
    }

    private String generateNewFileName(String originalFilename) {
        // 1.获取后缀
        String suffix = StringUtils.subAfter(originalFilename, ".", true);
        // 2.生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }
}
