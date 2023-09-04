package com.tianji.user.service.impl;

import com.tianji.user.domain.dto.StudentFormDTO;
import com.tianji.user.service.IStudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private IStudentService studentService;

    @Test
    void saveStudent() {
        for (int i = 1; i < 20; i++) {
            StudentFormDTO dto = new StudentFormDTO();
            dto.setCellPhone((13898675600L + i) + "");
            dto.setPassword("123321");
            studentService.saveStudent(dto);
        }
    }
}