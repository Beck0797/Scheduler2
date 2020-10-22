package com.example.scheduler2;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder>{

        private static final String TAG = "RecyclerViewAdapter";


        private ArrayList<AtttendanceInfo> mImageNames = new ArrayList<>();
        private Context mContext;

        public AdapterData(Context context, ArrayList<AtttendanceInfo> imageNames){
            mImageNames = imageNames;

            mContext = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder: called.");

           final AtttendanceInfo item = mImageNames.get(position);
            holder.class_name.setText(item.getClass_name());
            holder.absence_statues.setText(item.getAbsence());
            holder.attendance_statues.setText(item.getAttendance());

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, Attendance_Detail.class);
                    intent.putExtra("class_name", item.getClass_name());
                    intent.putExtra("absence_statues",item.getAbsence());
                    intent.putExtra("attendance_statues", item.getAttendance());


                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mImageNames.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView class_name, attendance_statues, absence_statues;
            LinearLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                class_name = itemView.findViewById(R.id.counter);
                attendance_statues = itemView.findViewById(R.id.attendance);
                absence_statues = itemView.findViewById(R.id.absence);
                parentLayout = itemView.findViewById(R.id.parentLayout);
            }
        }
    }