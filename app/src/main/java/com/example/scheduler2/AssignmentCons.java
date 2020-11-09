package com.example.scheduler2;

public class AssignmentCons {
   private String assign_coursename;
   private String assign_title;
   private String assign_duedate;
   private String assign_duetime;
   private String time_left;
   private String assign_key;

   public String getAssign_key() {
      return assign_key;
   }
   public void setAssign_key(String assign_key) {
      this.assign_key = assign_key;
   }

   public String getTime_left() {
      return time_left;
   }

   public void setTime_left(String time_left) {
      this.time_left = time_left;
   }




   public AssignmentCons() {

   }
   public AssignmentCons(String assign_key, String assign_coursename, String assign_title, String assign_duedate, String assign_duetime) {
      this.assign_coursename = assign_coursename;
      this.assign_title = assign_title;
      this.assign_duedate = assign_duedate;
      this.assign_duetime = assign_duetime;
      this.assign_key = assign_key;
   }
   public AssignmentCons( String assign_coursename, String assign_title, String assign_duedate, String assign_duetime) {
      this.assign_coursename = assign_coursename;
      this.assign_title = assign_title;
      this.assign_duedate = assign_duedate;
      this.assign_duetime = assign_duetime;
      this.assign_key = assign_key;
   }
   public String getAssign_coursename() {
      return assign_coursename;
   }

   public void setAssign_coursename(String assign_coursename) {
      this.assign_coursename = assign_coursename;
   }

   public String getAssign_title() {
      return assign_title;
   }

   public void setAssign_title(String assign_title) {
      this.assign_title = assign_title;
   }

   public String getAssign_duedate() {
      return assign_duedate;
   }

   public void setAssign_duedate(String assign_duedate) {
      this.assign_duedate = assign_duedate;
   }

   public String getAssign_duetime() {
      return assign_duetime;
   }

   public void setAssign_duetime(String assign_duetime) {
      this.assign_duetime = assign_duetime;
   }





}
