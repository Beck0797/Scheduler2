package com.scheduler.beck.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.scheduler.beck.R;

public class adapterNotification extends  RecyclerView.Adapter<adapterNotification.ViewHolder> {
    private final String string;
    private final Context context;

    public adapterNotification(Context context, String string) {
        this.string = string;
        this.context = context;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.assignment.setText(string);


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView assignment;
        public ViewHolder(View itemView) {
            super(itemView);
            assignment = itemView.findViewById(R.id.txtCourseName);

        }
    }
}

