package com.example.scheduler2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class courses_adapter_data extends  RecyclerView.Adapter<courses_adapter_data.ViewHolder> {
    private String string;
    private Context context;

    public courses_adapter_data(Context context, String string) {
        this.string = string;
        this.context = context;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.assignment.setText("Python");


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assignment;
        public ViewHolder(View itemView) {
            super(itemView);
            assignment = itemView.findViewById(R.id.txtCourseNameCourses);

        }
    }
}

