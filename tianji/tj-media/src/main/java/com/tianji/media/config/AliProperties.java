package com.tianji.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tj.ali")
public class AliProperties {
    private String accessId;
    private String accessKey;

    private OssProperties oos;

    @Data
    public static class OssProperties {
        /*区域*/
        private String region;
        /*域名*/
        private String endpoint;
        /*桶名称*/
        private String bucket;
    }
}
