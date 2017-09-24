package com.umarzaii.uni4life.Activity.DeptAdmin;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.DropdownController;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Controller.RecyclerViewController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Model.TimeTableModel;
import com.umarzaii.uni4life.Model.UserClassModel;
import com.umarzaii.uni4life.R;
import com.umarzaii.uni4life.UIDesign.MyTextView;

import java.util.ArrayList;

public class DptAdmFrgTTList extends Fragment {

    private TblTimeFrame tblTimeFrame;
    private Query query;

    private FragmentController frgController;
    private RecyclerViewController rvController;

    private RecyclerView rvTTList;
    private TextView txtTTTitle;

    private String lecturerID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_ttlist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("TimeTable List");
        View v = getView();

        tblTimeFrame = new TblTimeFrame();

        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        rvController = new RecyclerViewController(getActivity());

        txtTTTitle = (TextView) v.findViewById(R.id.txtTTTitle);
        rvTTList = (RecyclerView)v.findViewById(R.id.rvTTList);

        lecturerID = getArguments().getString("lecturerID");

        rvController.init(rvTTList);
        txtTTTitle.setText(getArguments().getString("title"));
    }

    @Override
    public void onStart() {
        super.onStart();
        onLoad();

        FirebaseRecyclerAdapter<TimeTableModel,TimeTableViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TimeTableModel, TimeTableViewHolder>(
                TimeTableModel.class,
                R.layout.rvitem_ttlist,
                TimeTableViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(TimeTableViewHolder viewHolder, TimeTableModel model, int position) {
                populate(viewHolder,model);
            }
        };

        rvTTList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class TimeTableViewHolder extends RecyclerView.ViewHolder {
        View fView;
        TextView txt0809;
        TextView txt0910;
        TextView txt1011;
        TextView txt1112;
        TextView txt1213;
        TextView txt1314;
        TextView txt1415;
        TextView txt1516;
        TextView txt1617;

        public TimeTableViewHolder(View itemView) {
            super(itemView);
            fView = itemView;
        }

        public void setDay(String day) {
            MyTextView txtDay = (MyTextView)fView.findViewById(R.id.txtDay);
            txtDay.setText(day);
        }

        public void setTime0809(String time0809) {
            txt0809 = (TextView)fView.findViewById(R.id.txt0809);
            txt0809.setText(time0809);
        }

        public void setTime0910(String time0910) {
            txt0910 = (TextView)fView.findViewById(R.id.txt0910);
            txt0910.setText(time0910);
        }

        public void setTime1011(String time1011) {
            txt1011 = (TextView)fView.findViewById(R.id.txt1011);
            txt1011.setText(time1011);
        }

        public void setTime1112(String time1112) {
            txt1112 = (TextView)fView.findViewById(R.id.txt1112);
            txt1112.setText(time1112);
        }

        public void setTime1213(String time1213) {
            txt1213 = (TextView)fView.findViewById(R.id.txt1213);
            txt1213.setText(time1213);
        }

        public void setTime1314(String time1314) {
            txt1314 = (TextView)fView.findViewById(R.id.txt1314);
            txt1314.setText(time1314);
        }

        public void setTime1415(String time1415) {
            txt1415 = (TextView)fView.findViewById(R.id.txt1415);
            txt1415.setText(time1415);
        }

        public void setTime1516(String time1516) {
            txt1516 = (TextView)fView.findViewById(R.id.txt1516);
            txt1516.setText(time1516);
        }

        public void setTime1617(String time1617) {
            txt1617 = (TextView)fView.findViewById(R.id.txt1617);
            txt1617.setText(time1617);
        }
    }

    public void onLoad() {
        if (getArguments().getString("status") == "MyTimeTable") {
            query = tblTimeFrame.getTblLecturer(lecturerID);
        } else if(getArguments().getString("status") == DBConstants.classLocation) {
            query = tblTimeFrame.getTblClassLocation(getArguments().getString("classLocationID"));
        } else if(getArguments().getString("status") == DBConstants.userClass) {
            query = tblTimeFrame.getTblUserClass(getArguments().getString("userClassID"));
        } else if(getArguments().getString("status") == DBConstants.lecturer) {
            query = tblTimeFrame.getTblLecturer(getArguments().getString("lecturerID"));
        }
    }

    public void populate(TimeTableViewHolder viewHolder, TimeTableModel model) {
        final String dayID = model.getDayID();
        viewHolder.setDay(dayID.substring(1,4).toUpperCase());
        if (getArguments().getString("status") == "MyTimeTable") {
            getLectTimeTable(viewHolder,lecturerID,dayID);
        } else if(getArguments().getString("status") == DBConstants.classLocation) {
            getCLTimeTable(viewHolder,getArguments().getString("classLocationID"),dayID);
        } else if(getArguments().getString("status") == DBConstants.userClass) {
            getUCTimeTable(viewHolder,getArguments().getString("userClassID"),dayID);
        } else if(getArguments().getString("status") == DBConstants.lecturer) {
            getLectTimeTable(viewHolder,getArguments().getString("lecturerID"),dayID);
        }
    }

    public void getLectTimeTable(final TimeTableViewHolder viewHolder, String userID, final String dayID) {
        tblTimeFrame.getLCDay(userID,dayID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setViewHolder(viewHolder,dataSnapshot);
                onClick(viewHolder,dayID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCLTimeTable(final TimeTableViewHolder viewHolder, String classLocationID, final String dayID) {
        tblTimeFrame.getCLDay(classLocationID,dayID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setViewHolder(viewHolder,dataSnapshot);
                onClick(viewHolder,dayID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUCTimeTable(final TimeTableViewHolder viewHolder, String userClassID, final String dayID) {
        tblTimeFrame.getUCDay(userClassID,dayID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setViewHolder(viewHolder,dataSnapshot);
                onClick(viewHolder,dayID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setViewHolder(TimeTableViewHolder viewHolder, DataSnapshot dataSnapshot) {
        viewHolder.setTime0809(dataSnapshot.child("0800-0900").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime0910(dataSnapshot.child("0900-1000").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1011(dataSnapshot.child("1000-1100").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1112(dataSnapshot.child("1100-1200").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1213(dataSnapshot.child("1200-1300").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1314(dataSnapshot.child("1300-1400").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1415(dataSnapshot.child("1400-1500").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1516(dataSnapshot.child("1500-1600").child(DBConstants.subjectID).getValue().toString());
        viewHolder.setTime1617(dataSnapshot.child("1600-1700").child(DBConstants.subjectID).getValue().toString());
    }

    public void onClick(TimeTableViewHolder viewHolder, final String dayID) {
        viewHolder.txt0809.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"0800-0900");
            }
        });
        viewHolder.txt0910.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"0900-1000");
            }
        });
        viewHolder.txt1011.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1000-1100");
            }
        });
        viewHolder.txt1112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1100-1200");
            }
        });
        viewHolder.txt1213.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1200-1300");
            }
        });
        viewHolder.txt1314.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1300-1400");
            }
        });
        viewHolder.txt1415.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1400-1500");
            }
        });
        viewHolder.txt1516.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1500-1600");
            }
        });
        viewHolder.txt1617.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent(dayID,"1600-1700");
            }
        });
    }

    public void onClickEvent(String dayID, String timeID) {

        if(getArguments().getString("status") == DBConstants.lecturer ||
                getArguments().getString("status") == "MyTimeTable") {

            String lecturerID = getArguments().getString("lecturerID");
            Bundle bundle = new Bundle();
            bundle.putString("dayID", dayID);
            bundle.putString("timeID", timeID);
            bundle.putString("lecturerID", lecturerID);
            bundle.putString("status", DBConstants.lecturer);
            frgController.stackFragment(new DptAdmFrgTTView(), R.id.dptAdContentMain, bundle, "TTView");

        } else if(getArguments().getString("status") == DBConstants.classLocation) {

            String classLocationID = getArguments().getString("classLocationID");
            Bundle bundle = new Bundle();
            bundle.putString("dayID", dayID);
            bundle.putString("timeID", timeID);
            bundle.putString("classLocationID", classLocationID);
            bundle.putString("status", DBConstants.classLocation);
            frgController.stackFragment(new DptAdmFrgTTView(), R.id.dptAdContentMain, bundle, "TTView");

        } else if(getArguments().getString("status") == DBConstants.userClass) {

            final String userClassID = getArguments().getString("userClassID");
            Bundle bundle = new Bundle();
            bundle.putString("dayID", dayID);
            bundle.putString("timeID", timeID);
            bundle.putString("userClassID", userClassID);
            bundle.putString("status", DBConstants.userClass);
            frgController.stackFragment(new DptAdmFrgTTView(), R.id.dptAdContentMain, bundle, "TTView");

        }
    }

}
