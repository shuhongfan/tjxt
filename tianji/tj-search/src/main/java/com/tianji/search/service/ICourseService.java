package com.tianji.search.service;

import java.util.List;

public interface ICourseService {

    void handleCourseDelete(Long courseId);

    void handleCourseUp(Long courseId);

    void updateCourseSold(List<Long> courseId, int amount);

    void handleCourseDeletes(List<Long> courseIds);
}
