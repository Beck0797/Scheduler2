package com.example.scheduler2;

public class Course_display {
    private String course_display_name;
    private String course_day_name;
    private String course_display_classroom;
    private String course_display_professor_name;

    public Course_display(String course_display_name, String course_day_name, String course_display_classroom, String course_display_professor_name) {
        this.course_display_name = course_display_name;
        this.course_day_name = course_day_name;
        this.course_display_classroom = course_display_classroom;
        this.course_display_professor_name = course_display_professor_name;
    }


    public String getCourse_display_name() {
        return course_display_name;
    }

    public void setCourse_display_name(String course_display_name) {
        this.course_display_name = course_display_name;
    }

    public String getCourse_day_name() {
        return course_day_name;
    }

    public void setCourse_day_name(String course_day_name) {
        this.course_day_name = course_day_name;
    }

    public String getCourse_display_classroom() {
        return course_display_classroom;
    }

    public void setCourse_display_classroom(String course_display_classroom) {
        this.course_display_classroom = course_display_classroom;
    }

    public String getCourse_display_professor_name() {
        return course_display_professor_name;
    }

    public void setCourse_display_professor_name(String course_display_professor_name) {
        this.course_display_professor_name = course_display_professor_name;
    }


}
