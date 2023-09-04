package com.tianji.common.autoconfigure.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @ClassName SwaggerConfigProperties
 * @author wusongsong
 * @since 2022/6/27 13:47
 * @version 1.0.0
 **/
@Data
@ConfigurationProperties(prefix = "tj.swagger")
public class SwaggerConfigProperties implements Serializable {

    private Boolean enable = false;
    private Boolean enableResponseWrap = false;

    public String packagePath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;

    public String version;
}
