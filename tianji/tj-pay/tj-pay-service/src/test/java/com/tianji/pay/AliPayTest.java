package com.tianji.pay;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AliPayTest {
    @BeforeEach
    public void init(){
        Factory.setOptions(getOptions());
    }

    String orderNo = "1564894253014872066";
    String refundOrderNo1 = "21294126713451";
    String refundOrderNo2 = "21294129213452";
    @Test
    void testPreCreate() {
        try {
            // 1. 发起API调用（以创建当面付收款二维码为例）
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                    .preCreate("pen lv2", orderNo, "2.00");
            // 2. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println(response.getQrCode());
                System.out.println(response.getHttpBody());
                System.out.println(response.getCode());
                System.out.println(response.getMsg());
                System.out.println(response.getSubCode());
                System.out.println(response.getSubMsg());
                System.out.println("调用成功");
            } else {
                System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    void testQueryPayStatus() throws Exception {
        AlipayTradeQueryResponse response = Factory.Payment.Common().query(orderNo);
        System.out.println("responseBody = " + response.getHttpBody());
        System.out.println("response = " + response);
    }

    @Test
    void testRefund() throws Exception {
        AlipayTradeRefundResponse response = Factory.Payment.Common()
                .optional("query_options", List.of("refund_detail_item_list"))
                .optional("out_request_no", refundOrderNo1)
                .refund(orderNo, "1");

        System.out.println("response = " + response.getHttpBody());
    }

    @Test
    void testQueryRefund() throws Exception {
        AlipayTradeFastpayRefundQueryResponse response = Factory.Payment.Common()
                .queryRefund(orderNo, refundOrderNo1);

        System.out.println("response = " + response.getHttpBody());
    }

    @Test
    void testClose() throws Exception {
        AlipayTradeCloseResponse response = Factory.Payment.Common().close(orderNo);
        System.out.println("response = " + response.getHttpBody());
    }


    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = "2021002173680104";
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYTgKj2xEIJi7lqjGRuQ/XWNDRuhoMtWvQg1DzF3fDWodh6WMed5V7mi3s4zXLnm4TvHVazgLnVXJlONxG4EsVJRGp3hY1fSGAAcByRC2E8E+NdhIvaOAWUuEid6AeshfNqmePnJyHxpxf9ZYn0Bij0kM8yMwv1izkBmsuydXroQegYfias6HT+CQ9mgU0awb6ZPeGqC6sw9Tv991jOohGv4xgEVjRh69pvB8Eo1vubYJ8mhOEf5xwWkx8/n7tDa4uA7ioUlgLRxhtOkCkpTQXzLAzm8gxoJlEYL/sFR3plaPBRAafWoKdfF55W0SBXDhhNqqce+r1H4pw4IyZxqyrAgMBAAECggEAYAcnoPpZlcLFZObXFCMTutpz5xgonoSwsqppGqxcRZ7Jp1FIvof1hxYiCK8FVxnQG7+CWrtzlzoHw4yDTmjSzkUuCuVNKXJ48cWo+iLEdII0FmQweRXt3AVrj5jPKyts2K6tVx4Oj4kJRXOJthZ9wqSq4iNUooCukyL852Y467P/Me+9Vr2vQb117qLaNTwvR2GV6OZUNQPl7qKNsp86e5lREIBHPk8uhQO3KS+QPPvTDkKBH+bNo7Bu4L4J0ITdvjU9FupaQ6xfFCOrgu7P2bw9Mk2JM6jHp1hEjuoLVGA97sL6CSPhQu9s9KJbup2DGOI1qQoEATBypHFueYv4yQKBgQD57xI1iPUl3tVNH5e3yYa08NLS+mV9N3tRTjIWHbMwQKMbraaO+dGQRGiytjejao3y0EVFuqOuhbXAxtvb1pUukASqY3zW01zzVNNw8nO3zeVPo+xWLAvlnyX3MekKMYjo1dAyWBzXuPgo3D413nMiPZ8oOgtbpTyu/dTeN3NJTQKBgQCcAFWSUinmZ3x9Xr779CeX1MsKG+++C4iLP7vNP8Lf8IcPE8NnHYZTQqDuvq1Itai7UbhZX95itYqjp9SlXT4hSrMGI7qwobJ0vXxXrNN6VwZtz1N75vnIrZNHnTFMWTUBKCySLcpsqs7qCSQEv6luOQUSUH/0gaN6txOq5W4R1wKBgH4GdLIV6zc7U2beJUyBC7G1NTk5FW+8SCxJN6w7MZ2FGjncp/20Ll2GgRyMESYPlp/3MNbmM57OwUUBgN8rJnIiIJgiLlLMpTP1c+CiAIOQCK7Nw1/4Oc+BHk21FwMS0yxElASutWx5UniYBa54CqobVGOeURfXC/BZAbtDTpiJAoGAMR3B03HfE1Xd0jM0emti0+EBlEs7bmB/Oyhz3qmGl69JNqwIR7z5/9johoKuWEgpueB+5FTU1ctGvUQoJXB4EU9Nkk9JhjdC0pKeRZR6ePhRY9108XvFhTNxPYj2bo1frN+TOOsF4rTctL7wAja+B6AYQq3pu3fdmtNtc88Mmr0CgYEAuDD/PzVTLMYgEqq0ruZcCyFd0HwMV8KoC07aXXeLZT/IGtoJASFkWIxcoyK8NVZvDqoGkpaLAw3G7CmzoxpqSvyMWwHGdZy7KDV55g5O9r4Lt/dti7xt6gBYm8XB1X7cLcu0x9lYs6KL01To+Ep8DxSO7LUODQ0Utu3Pkf9W1qc=";
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhUnjdAKwZApwZEcfq+5L0pa77Vg3mqcoXv+th8RR0SYotkPsH1f2JkbS48ySaSCM6YNWSMNfqp5qdOla2zUJOBnJ/yaBg7s7fVD6V3M2mEog8kCDYGKt/3P4VII3xYl8lFYMQ3IcFRELkxCBBCA8JDKmf5z2R4F/Z/jFFEuOwxaJvp+7Ke9OzZHYdWGNnU6QP8YYLYUeX7VNZLHEuly34ExAw6A+yJkNDsYEho2Lu31QjT2pLh9g+88MlRfiI92iN25O9NVdeM4f5RcpvBPrBQZQs9tlFmALYSFS3prIf3FAobWM+W7iwxT6J25nFIhst1DdJQfIBpaeRUJVTkn99QIDAQAB";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "https://89fde8c.r2.cpolar.top/notify/aliPay";
        return config;
    }
}
