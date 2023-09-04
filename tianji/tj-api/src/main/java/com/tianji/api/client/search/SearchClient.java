package com.tianji.api.client.search;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("search-service")
public interface SearchClient {

    @GetMapping("/courses/name")
    List<Long> queryCoursesIdByName(
            @RequestParam(value = "keyword", required = false) String keyword);
}
