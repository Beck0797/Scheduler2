package com.example.scheduler2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    Attendance_Detail obj = new Attendance_Detail();
    private String[] mData;
    private LayoutInflater mInflater;
    private ArrayList<SatuesDates> list;

    // data is passed into the constructor
    public GridViewAdapter(Context context, ArrayList<SatuesDates> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_layout, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list.size() > 0) {
            SatuesDates taken_data = list.get(position);

            holder.class_date.setText(taken_data.getDate());
            if (taken_data.getStatues().equals("attendance")) {
                holder.class_statues.setText(taken_data.getStatues());
                holder.gridLayout.setBackgroundResource(R.drawable.green_color);
            } else {
                holder.gridLayout.setBackgroundResource(R.drawable.red_color);
                holder.class_statues.setText(taken_data.getStatues());
            }
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return list.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView class_statues, class_date;
        public LinearLayout gridLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            class_statues = itemView.findViewById(R.id.statues);
            class_date = itemView.findViewById(R.id.class_date);
            gridLayout = itemView.findViewById(R.id.gridLayout);
        }


    }


    }




