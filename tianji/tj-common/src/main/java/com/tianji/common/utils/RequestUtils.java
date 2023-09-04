package com.tianji.common.utils;

import org.springframework.web.util.UriUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestUtils {
    public static final String UTF8_ENC = "UTF-8";


    /**
     * 将请求参数进行升序排序，重新组装
     *
     * @param originQueryParam 原始请求参数
     * @return 重组参数
     */
    public static String toSortQueryParams(String originQueryParam) {
        List<String> queryParams = new ArrayList<String>();

        //组装解码
        for (String kv : originQueryParam.split("&")) {
            String[] t = kv.split("=");
            if (t.length > 1) {
                queryParams.add(String.format("%s=%s", UriUtils.decode(t[0], UTF8_ENC), UriUtils.decode(t[1], UTF8_ENC)));
            } else {
                queryParams.add(String.format("%s=", UriUtils.decode(t[0], UTF8_ENC)));
            }
        }
        //排序
        Collections.sort(queryParams);
        StringBuffer buffer = new StringBuffer();
        //重新拼装
        for (String queryParm : queryParams) {
            buffer.append(queryParm).append("&");
        }

        return buffer.length() > 0 ? buffer.substring(0, buffer.length() - 2) : StringUtils.EMPTY;
    }
}
