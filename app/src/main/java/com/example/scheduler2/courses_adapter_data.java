package com.example.scheduler2;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.scheduler2.courseList.TAG;

public class courses_adapter_data extends  RecyclerView.Adapter<courses_adapter_data.ViewHolder> {

    private Context context;
    private ArrayList<Course_display> list;

    private static OnItemClickListener listener;
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView course_name, course_day, course_room_number, course_professor_name;
        private CardView linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.course_name);
            course_day = itemView.findViewById(R.id.course_day);
            course_room_number = itemView.findViewById(R.id.course_room_number);
            course_professor_name = itemView.findViewById(R.id.course_professor_name);
            linearLayout  = itemView.findViewById(R.id.class_click_view);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            


        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "item clicked");
            if (listener != null) {
                Log.d(TAG, "internal item clicked");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "update");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            listener.onWhatEverClick(position);
                            return true;
                        case 2:
                            listener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listenerm) {
        listener = listenerm;
    }
}