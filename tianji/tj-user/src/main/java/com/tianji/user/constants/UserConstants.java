package com.tianji.user.constants;

import java.time.Duration;

public interface UserConstants {
    String DEFAULT_PASSWORD = "123456";

    Long STUDENT_ROLE_ID = 2L;
    String STUDENT_ROLE_NAME = "学生";

    Long TEACHER_ROLE_ID = 3L;
    String TEACHER_ROLE_NAME = "教师";

    // 验证码的Redis key前缀
    String USER_VERIFY_CODE_KEY = "sms:user:code:phone:";
    // 验证码有效期，5分钟
    Duration USER_VERIFY_CODE_TTL = Duration.ofMinutes(5);
}
