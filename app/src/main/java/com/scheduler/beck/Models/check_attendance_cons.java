package com.scheduler.beck.Models;

import java.io.Serializable;

public class check_attendance_cons implements Serializable {

    private String class_name;
    private String status;
    private String date;
    public check_attendance_cons() {}
    public check_attendance_cons(String class_name, String status, String date) {
        this.class_name = class_name;
        this.status = status;
        this.date = date;
    }




    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
