package com.example.scheduler2;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class adapter_data extends  RecyclerView.Adapter<adapter_data.ViewHolder> {
    public ArrayList<AssignmentCons> assign_list;
    private Context context;
    boolean check = false;
    private String take_key;
    private static  final String TAG = "adapter_data";
    private DatabaseReference databaseReference;
    public adapter_data(Context context, ArrayList<AssignmentCons> assignmentCons, DatabaseReference databaseReference) {
        this.assign_list = assignmentCons;
        this.context = context;
        this.databaseReference = databaseReference;

    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setHandler();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_recycler_view_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AssignmentCons assignCons = assign_list.get(position);
        holder.assignment_title.setText(assignCons.getAssign_title());
        holder.assignment_course_name.setText(assignCons.getAssign_coursename());

        if(assignCons.getTime_left() == null) {
            holder.assignment_deadline.setText("calculating...");
        } else {
            if (assignCons.getTime_left().substring(0, 1) == "-") {
                holder.assignment_deadline.setText("missing");
            } else {
                holder.assignment_deadline.setText(Integer.parseInt(assignCons.getTime_left().substring(0, 1)) > 0 ?
                        assignCons.getTime_left() : "missing");
                }
        }




        if(assignCons.getTime_left() != null && Integer.parseInt(assignCons.getTime_left().substring(0,1)) == 0) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //DataSnapshot getdata = dataSnapshot.child("assignments");
                        Course_Info course_info = dataSnapshot.getValue(Course_Info.class);
                        if (course_info.getCourse_name().equals(assignCons.getAssign_coursename())) {
                            Log.d(TAG, "key pringing: " + dataSnapshot.child("assignments").child(assignCons.getAssign_key()).getValue());
                            take_key = dataSnapshot.getKey();
                            databaseReference.child(take_key).child("assignments").child(assignCons.getAssign_key()).removeValue();
                            notifyDataSetChanged();
                        }
                        /*for (DataSnapshot innerdata : getdata.getChildren()) {
                               //Log.d(TAG, "assignment printing: " + innerdata.child(assignCons.getAssign_key()).getValue());
                        }*/
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void setHandler(){
        final Handler handler = new Handler();
        final int delay = 1000 ; //1000 milliseconds = 1 sec

        handler.postDelayed(new Runnable(){
            public void run(){

                String getDate = "", getTime="";
                Point[] point = new Point[assign_list.size()];
                Log.d(TAG, "size:" + assign_list.size());
                String curr_date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                Calendar cal = Calendar.getInstance();
                int curr_hour = cal.get(Calendar.HOUR_OF_DAY);
                int curr_min = cal.get(Calendar.MINUTE);
                for(int i = 0; i < assign_list.size(); i++) {
                    //change data depending on time

                    //set new data to the deadline field
                    try {
                        // 2020-30-04
                        // 2020/6

                            Log.d(TAG, "getting in");
                            getDate = assign_list.get(i).getAssign_duedate();
                            getTime  = assign_list.get(i).getAssign_duetime();

                        StringBuilder changeDate = new StringBuilder(getDate);
                        changeDate.setCharAt(4, '/');
                        changeDate.setCharAt(7, '/');
                        String format = "yyyy/MM/dd hh:mm";
                        Log.d(TAG, "printing value: " + changeDate + " and " + getTime);
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        Date dateObj1 = sdf.parse(changeDate + " " + getTime);
                        Date dateObj2 = sdf.parse(curr_date + " " + (curr_hour < 10 ? "0" + curr_hour : curr_hour) + ":" + (curr_min < 10 ? "0" + curr_min : curr_min));


                        DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

                        // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
                        long diff = dateObj1.getTime() - dateObj2.getTime();

                        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                        System.out.println("difference between days: " + diffDays);

                        int diffhours = (int) (diff / (60 * 60 * 1000));
                        System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

                        int diffmin = (int) (diff / (60 * 1000));
                        System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

                        int diffsec = (int) (diff / (1000));
                        System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

                        System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));
                        diffhours = (int)(diffhours) % (int)(24);
                        diffmin = (int)(diffmin) % (int)(60);
                        String time_left;
                        if(diffDays != 0) {
                           time_left = Integer.toString(diffDays) + "d " + Integer.toString(diffhours) + "h" + Integer.toString(diffmin) + "m";
                        } else if(diffhours != 0) {
                            time_left = Integer.toString(diffhours) + "h" + Integer.toString(diffmin) + "m";
                        } else {
                             time_left = Integer.toString(diffmin) + "m";
                        }

                         assign_list.get(i).setTime_left(time_left);

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public int getItemCount() {
        return assign_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assignment_title, assignment_course_name, assignment_deadline;
        public ViewHolder(View itemView) {
            super(itemView);
            assignment_title = itemView.findViewById(R.id.assignment_title);
            assignment_course_name = itemView.findViewById(R.id.assignment_course_name);
            assignment_deadline = itemView.findViewById(R.id.assignment_deadline);

        }
    }
      class Point {
        String date;
        String time;
      }

}

