package com.scheduler.beck.Models;

public class AtttendanceInfo {
    public String getClass_day() {
        return class_day;
    }

    public void setClass_day(String class_day) {
        this.class_day = class_day;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getProfessor_name() {
        return professor_name;
    }

    public void setProfessor_name(String professor_name) {
        this.professor_name = professor_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }

    public String getTardiness() {
        return tardiness;
    }

    public void setTardiness(String tardiness) {
        this.tardiness = tardiness;
    }

    private String class_day;
    private String room_number;
    private String professor_name;
    private String class_name;
    private String attendance;
    private String absence;
    private String tardiness;



    // empty construct needed don't delete
    public AtttendanceInfo() {

    }
    public AtttendanceInfo(String class_name, String class_day, String room_number, String professor_name,String attendance, String absence, String tardiness) {
        this.class_name = class_name;
        this.attendance = attendance;
        this.absence = absence;
        this.class_day = class_day;
        this.room_number = room_number;
        this.professor_name = professor_name;
        this.tardiness = tardiness;
    }


    public String getClass_name() {
        return class_name;
    }

    public String getAttendance() {
        return attendance;
    }

    public String getAbsence() {
        return absence;
    }



}
