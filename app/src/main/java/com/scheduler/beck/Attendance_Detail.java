package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scheduler.beck.Adapters.GridViewAdapter;
import com.scheduler.beck.Models.SatuesDates;
import com.scheduler.beck.Models.check_attendance_cons;
import com.scheduler.beck.Utils.ThemeUtils;

import java.util.ArrayList;

public class Attendance_Detail extends AppCompatActivity {
    public String class_name, attendance_statues, absence_statues;
    public int statues = 1;
    GridViewAdapter adapter;
    ArrayList<SatuesDates> names;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_attendance__detail);
        names = new ArrayList<>();
        Intent intent = getIntent();
        class_name = intent.getStringExtra("class_name");
        absence_statues  = intent.getStringExtra("absence_statues");
        attendance_statues = intent.getStringExtra("attendance_statues");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        databaseReference.child("attendance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    check_attendance_cons ch = data.getValue(check_attendance_cons.class);
                    if(class_name.equals(ch.getClass_name())) {
                        names.add(new SatuesDates(ch.getDate(), ch.getStatus()));
                    }
                }

                if(names.isEmpty()){
                    findViewById(R.id.NoAttendGrid).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.NoAttendGrid).setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView recyclerView = findViewById(R.id.grid_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(new ListSpacingDecoration(this,  6));


        adapter = new GridViewAdapter(this, names);
        recyclerView.setAdapter(adapter);


    }

   class ListSpacingDecoration extends RecyclerView.ItemDecoration {

        private static final int VERTICAL = OrientationHelper.VERTICAL;

        private int orientation = -1;
        private int spanCount = -1;
        private final int spacing;
        private final int halfSpacing;


  public ListSpacingDecoration(Context context,int spacingDimen) {

            spacing = spacingDimen;
            halfSpacing = spacing / 2;
        }

  public ListSpacingDecoration(int spacingPx) {

            spacing = spacingPx;
            halfSpacing = spacing / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            super.getItemOffsets(outRect, view, parent, state);

            if (orientation == -1) {
                orientation = getOrientation(parent);
            }

            if (spanCount == -1) {
                spanCount = getTotalSpan(parent);
            }

            int childCount = parent.getLayoutManager().getItemCount();
            int childIndex = parent.getChildAdapterPosition(view);

            int itemSpanSize = getItemSpanSize(parent, childIndex);
            int spanIndex = getItemSpanIndex(parent, childIndex);

            /* INVALID SPAN */
            if (spanCount < 1) return;

            setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex);
        }

        protected void setSpacings(Rect outRect, RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

            outRect.top = halfSpacing;
            outRect.bottom = halfSpacing;
            outRect.left = halfSpacing;
            outRect.right = halfSpacing;

            if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
                outRect.top = spacing;
            }

            if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
                outRect.left = spacing;
            }

            if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
                outRect.right = spacing;
            }

            if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
                outRect.bottom = spacing;
            }
        }

        @SuppressWarnings("all")
        protected int getTotalSpan(RecyclerView parent) {

            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof GridLayoutManager) {
                return ((GridLayoutManager) mgr).getSpanCount();
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) mgr).getSpanCount();
            } else if (mgr instanceof LinearLayoutManager) {
                return 1;
            }

            return -1;
        }

        @SuppressWarnings("all")
        protected int getItemSpanSize(RecyclerView parent, int childIndex) {

            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof GridLayoutManager) {
                return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanSize(childIndex);
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                return 1;
            } else if (mgr instanceof LinearLayoutManager) {
                return 1;
            }

            return -1;
        }

        @SuppressWarnings("all")
        protected int getItemSpanIndex(RecyclerView parent, int childIndex) {

            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof GridLayoutManager) {
                return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanIndex(childIndex, spanCount);
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                return childIndex % spanCount;
            } else if (mgr instanceof LinearLayoutManager) {
                return 0;
            }

            return -1;
        }

        @SuppressWarnings("all")
        protected int getOrientation(RecyclerView parent) {

            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) mgr).getOrientation();
            } else if (mgr instanceof GridLayoutManager) {
                return ((GridLayoutManager) mgr).getOrientation();
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) mgr).getOrientation();
            }

            return VERTICAL;
        }

        protected boolean isLeftEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

            if (orientation == VERTICAL) {

                return spanIndex == 0;

            } else {

                return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
            }
        }

        protected boolean isRightEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

            if (orientation == VERTICAL) {

                return (spanIndex + itemSpanSize) == spanCount;

            } else {

                return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
            }
        }

        protected boolean isTopEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

            if (orientation == VERTICAL) {

                return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);

            } else {

                return spanIndex == 0;
            }
        }

        protected boolean isBottomEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

            if (orientation == VERTICAL) {

                return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);

            } else {

                return (spanIndex + itemSpanSize) == spanCount;
            }
        }

        protected boolean isFirstItemEdgeValid(boolean isOneOfFirstItems, RecyclerView parent, int childIndex) {

            int totalSpanArea = 0;
            if (isOneOfFirstItems) {
                for (int i = childIndex; i >= 0; i--) {
                    totalSpanArea = totalSpanArea + getItemSpanSize(parent, i);
                }
            }

            return isOneOfFirstItems && totalSpanArea <= spanCount;
        }

        protected boolean isLastItemEdgeValid(boolean isOneOfLastItems, RecyclerView parent, int childCount, int childIndex, int spanIndex) {

            int totalSpanRemaining = 0;
            if (isOneOfLastItems) {
                for (int i = childIndex; i < childCount; i++) {
                    totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i);
                }
            }

            return isOneOfLastItems && (totalSpanRemaining <= spanCount - spanIndex);
        }
    }

}