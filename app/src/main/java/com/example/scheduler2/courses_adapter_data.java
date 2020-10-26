package com.example.scheduler2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class courses_adapter_data extends  RecyclerView.Adapter<courses_adapter_data.ViewHolder> {

    private Context context;
   private ArrayList<Course_display> list;
    public courses_adapter_data(Context context, ArrayList<Course_display> list) {
        this.context = context;
        this.list = list;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

                Course_display display = list.get(position);
                holder.course_name.setText(display.getCourse_display_name());
                holder.course_day.setText(display.getCourse_day_name());
                holder.course_room_number.setText(display.getCourse_display_classroom() + " room");
                holder.course_professor_name.setText(display.getCourse_display_professor_name());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView course_name, course_day, course_room_number, course_professor_name;
        public ViewHolder(View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.course_name);
            course_day = itemView.findViewById(R.id.course_day);
            course_room_number = itemView.findViewById(R.id.course_room_number);
            course_professor_name = itemView.findViewById(R.id.course_professor_name);


        }
    }
}

