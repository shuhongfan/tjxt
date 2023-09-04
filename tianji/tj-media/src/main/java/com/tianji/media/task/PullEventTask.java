package com.tianji.media.task;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.tianji.media.domain.po.Media;
import com.tianji.media.enums.FileStatus;
import com.tianji.media.service.IMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullEventTask {

    private static final String PROCEDURE_EVENT = "ProcedureStateChanged";
    private static final String UPLOAD_EVENT = "NewFileUpload";
    private static final String PROCEDURE_EVENT_FINISH = "FINISH";

    private final VodClient vodClient;
    private final IMediaService mediaService;

    @Scheduled(fixedDelay = 10000)
    public void pullEvent() {
        // 1.准备请求参数
        PullEventsRequest req = new PullEventsRequest();
        try {
            // 2.发出请求，拉取事件通知
            log.debug("准备拉取vod事件");
            PullEventsResponse response = vodClient.PullEvents(req);
            // 3.解析响应
            EventContent[] eventSet = response.getEventSet();
            // 3.1.遍历
            List<String> ehs = new ArrayList<>();
            for (EventContent ec : eventSet) {
                // 3.2.获取事件类型
                String eventType = ec.getEventType();
                // 3.3.处理事件
                if (PROCEDURE_EVENT.equals(eventType)) {
                    handleProcedureStateChangeEvent(ec);
                } /*else if(UPLOAD_EVENT.equals(eventType)){
                    handleUploadEvent(ec);
                }*/
                ehs.add(ec.getEventHandle());
            }
            ConfirmEventsRequest confirmReq = new ConfirmEventsRequest();
            confirmReq.setEventHandles(ehs.toArray(new String[0]));
            vodClient.ConfirmEvents(confirmReq);
            log.info("事件处理完毕");
        } catch (TencentCloudSDKException e) {
            if(e.getMessage().equals("no event")){
                log.debug("暂无event事件");
            }else{
                log.error("VOD事件处理异常", e);
            }
        }
    }

    private void handleUploadEvent(EventContent ec) {
        // 1.文件上传事件
        FileUploadTask fut = ec.getFileUploadEvent();
        String fileId = fut.getFileId();
        // 2.获取文件详情
        MediaMetaData md = fut.getMetaData();
        MediaBasicInfo info = fut.getMediaBasicInfo();
        // 3.组织结果
        Media media = new Media();
        media.setFileId(fut.getFileId());
        media.setFilename(info.getName());
        media.setMediaUrl(info.getMediaUrl());
        media.setCoverUrl(info.getCoverUrl());
        media.setDuration(md.getDuration());
        media.setSize(md.getSize());
        media.setStatus(FileStatus.UPLOADED);
        mediaService.updateMediaProcedureResult(media);
    }

    private void handleProcedureStateChangeEvent(EventContent ec) {
        // 3.3.1.任务流状态变更，判断是否结束
        ProcedureTask pt = ec.getProcedureStateChangeEvent();
        if (PROCEDURE_EVENT_FINISH.equals(pt.getStatus())) {
            // 3.3.2.任务流已经结束，获取视频元信息
            MediaMetaData md = pt.getMetaData();
            Optional<MediaProcessTaskResult> optional = Arrays.stream(pt.getMediaProcessResultSet())
                    .filter(r -> "CoverBySnapshot".equals(r.getType()))
                    .findFirst();
            String coverUrl = null;
            if (optional.isPresent()) {
                coverUrl = optional.get().getCoverBySnapshotTask().getOutput().getCoverUrl();
            }
            // 3.3.3.保存到数据库
            Media media = new Media();
            media.setFileId(pt.getFileId());
            media.setFilename(pt.getFileName());
            media.setMediaUrl(pt.getFileUrl());
            media.setCoverUrl(coverUrl);
            media.setDuration(md.getDuration());
            media.setSize(md.getSize());
            media.setStatus(FileStatus.PROCESSED);
            mediaService.updateMediaProcedureResult(media);
        }
    }
}
