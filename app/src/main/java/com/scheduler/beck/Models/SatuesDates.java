package com.scheduler.beck.Models;

public class SatuesDates {

    private String date;
    private String statues;
    // empty construct needed
    public SatuesDates() {}
    public SatuesDates(String date, String statues) {
        this.date = date;
        this.statues = statues;
    }

    public String getDate() {
        return date;
    }

    public String getStatues() {
        return statues;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
