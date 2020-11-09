package com.example.scheduler2;



public class Course_Info {
    private String course_name;
    private String professor_name;
    private String classroom_number;
    private String course_day;
    private String start_time;
    private String end_time;
    private String  alarm_time;
    private String url_name;
    private String current_date;
    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }


    public Course_Info(String course_name, String professor_name, String classroom_number, String course_day, String start_time, String end_time, String alarm_time, String url_name, String current_date) {
        this.course_name = course_name;
        this.professor_name = professor_name;
        this.classroom_number = classroom_number;
        this.course_day = course_day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.alarm_time = alarm_time;
        this.url_name = url_name;
        this.current_date = current_date;
    }

    // empty constructor needed; don't delete
    public Course_Info() {}

    public String getCourse_name() {
        return course_name;
    }

    public String getProfessor_name() {
        return professor_name;
    }

    public String getClassroom_number() {
        return classroom_number;
    }

    public String getCourse_day() {
        return course_day;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public String getUrl_name() {
        return url_name;
    }


    public void setClassroom_number(String classroom_number) {
        this.classroom_number = classroom_number;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setProfessor_name(String professor_name) {
        this.professor_name = professor_name;
    }

    public void setCourse_day(String course_day) {
        this.course_day = course_day;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public void setUrl_name(String url_name) {
        this.url_name = url_name;
    }


}