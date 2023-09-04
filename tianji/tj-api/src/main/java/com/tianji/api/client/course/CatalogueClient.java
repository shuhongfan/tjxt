package com.tianji.api.client.course;

import com.tianji.api.dto.course.CataSimpleInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "catalogue", value = "course-service",path = "catalogues")
public interface CatalogueClient {

    /**
     * 根据目录id列表查询目录信息
     *
     * @param ids 目录id列表
     * @return id列表中对应的目录基础信息
     */
    @GetMapping("/batchQuery")
    List<CataSimpleInfoDTO> batchQueryCatalogue(@RequestParam("ids") Iterable<Long> ids);


}