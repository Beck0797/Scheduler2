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
            //tariddness_statues, attend_class_day, atten_class_room, atten_prof_name;
            holder.tariddness_statues.setText(item.getTardiness());
            holder.attend_class_day.setText(item.getClass_day());
            holder.atten_class_room.setText(item.getRoom_number());
            holder.atten_prof_name.setText(item.getProfessor_name());

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

            TextView class_name, attendance_statues, absence_statues, tariddness_statues, attend_class_day, atten_class_room, atten_prof_name;
            LinearLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                class_name = itemView.findViewById(R.id.attendance_class_name);
                attendance_statues = itemView.findViewById(R.id.attendance_percentage);
                absence_statues = itemView.findViewById(R.id.absence_percentage);
                tariddness_statues = itemView.findViewById(R.id.tardiness_percentage);
                attend_class_day = itemView.findViewById(R.id.attendance_class_day);
                atten_class_room = itemView.findViewById(R.id.attendance_room_number);
                atten_prof_name = itemView.findViewById(R.id.attendance_professor_name);

                parentLayout = itemView.findViewById(R.id.parentLayout);
            }
        }
    }