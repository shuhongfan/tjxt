package com.tianji.pay.third.wx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tj.pay.wx")
public class WxPayProperties{
    /**
     * appId
     */
    private String appId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商户证书序号
     */
    private String mchSerialNo;
    /**
     * 私钥字符串
     */
    private String privateKey;
    /**
     * APIv3秘钥
     */
    private String apiV3Key;
}
