package com.project.sangil_be.dto;

import com.project.sangil_be.model.Course;
import lombok.Data;
import lombok.Getter;

@Data
public class CourseListDto {
    private Long courseId;
    private String course;
    private String courseTime;

    public CourseListDto(Course course) {
        this.courseId = course.getCourseId();
        this.course = course.getCourse();
        this.courseTime = course.getCourseTime();
    }


}
