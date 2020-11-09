package com.example.scheduler2;

import com.google.firebase.database.Exclude;

public class Course_display {
    private String Idkey;
    private String course_display_name;
    private String course_day_name;
    private String course_display_classroom;
    private String course_display_professor_name;

    public String getStart_display_time() {
        return start_display_time;
    }

    public void setStart_display_time(String start_display_time) {
        this.start_display_time = start_display_time;
    }

    public String getEnd_display_time() {
        return end_display_time;
    }

    public void setEnd_display_time(String end_display_time) {
        this.end_display_time = end_display_time;
    }

    public String getAlarm_display_time() {
        return alarm_display_time;
    }

    public void setAlarm_display_time(String alarm_display_time) {
        this.alarm_display_time = alarm_display_time;
    }

    public String getUrl_display_name() {
        return url_display_name;
    }

    public void setUrl_display_name(String url_display_name) {
        this.url_display_name = url_display_name;
    }

    private String start_display_time;
    private String end_display_time;
    private String  alarm_display_time;
    private String url_display_name;

    public Course_display(String idkey, String course_display_name, String course_day_name, String course_display_classroom, String course_display_professor_name, String start_display_time, String end_display_time, String alarm_display_time, String url_display_name) {
        Idkey = idkey;
        this.course_display_name = course_display_name;
        this.course_day_name = course_day_name;
        this.course_display_classroom = course_display_classroom;
        this.course_display_professor_name = course_display_professor_name;
        this.start_display_time = start_display_time;
        this.end_display_time = end_display_time;
        this.alarm_display_time = alarm_display_time;
        this.url_display_name = url_display_name;
    }


    @Exclude
    public String getIdkey() {
        return Idkey;
    }
    @Exclude
    public void setIdkey(String idkey) {
        Idkey = idkey;
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
