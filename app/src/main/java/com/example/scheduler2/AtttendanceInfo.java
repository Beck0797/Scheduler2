package com.example.scheduler2;

public class AtttendanceInfo {
    private String class_name;
    private String attendance;
    private String absence;
    // empty construct needed don't delete
    public AtttendanceInfo() {

    }
    public AtttendanceInfo(String class_name, String attendance, String absence) {
        this.class_name = class_name;
        this.attendance = attendance;
        this.absence = attendance;
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
