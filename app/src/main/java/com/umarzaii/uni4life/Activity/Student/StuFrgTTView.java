package com.umarzaii.uni4life.Activity.Student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Activity.DeptAdmin.DptAdmFrgTTEdit;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Mapper.TimeTableMapper;
import com.umarzaii.uni4life.Model.TimeTableModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class StuFrgTTView extends Fragment  {

    FragmentController frgController;

    private TblTimeFrame tblTimeFrame;

    private TextView lblRandomID2, lblRandomID3;
    private TextView txtSubjectID, txtRandomID2, txtRandomID3;
    private TextView txtRandomID, txtTTDayID, txtTTHourID;

    private String status;
    private String dayID, timeID, subjectID;
    private String lecturerID, userClassID, classLocationID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stu_frg_ttview,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("TimeTable View");

        getBundle();
        initClass();
        getTTDetails();
    }

    private void initClass() {
        frgController = new FragmentController(getActivity().getSupportFragmentManager());

        tblTimeFrame = new TblTimeFrame();

        View v = getView();
        txtRandomID = (TextView)v.findViewById(R.id.txtRandomID);
        txtTTDayID = (TextView)v.findViewById(R.id.txtTTDayID);
        txtTTHourID = (TextView)v.findViewById(R.id.txtTTHourID);
        txtSubjectID = (TextView)v.findViewById(R.id.txtSubjectID);
        lblRandomID2 = (TextView)v.findViewById(R.id.lblRandomID2);
        txtRandomID2 = (TextView)v.findViewById(R.id.txtRandomID2);
        lblRandomID3 = (TextView)v.findViewById(R.id.lblRandomID3);
        txtRandomID3 = (TextView)v.findViewById(R.id.txtRandomID3);

        if(getArguments().getString("status") == DBConstants.lecturer) {
            txtRandomID.setText(lecturerID);
        } else if(getArguments().getString("status") == DBConstants.userClass) {
            txtRandomID.setText(userClassID);
        } else if(getArguments().getString("status") == DBConstants.classLocation) {
            txtRandomID.setText(classLocationID);
        }
        String startTime = timeID.substring(0,2)+":00";
        String endTime = timeID.substring(5,7)+":00";
        txtTTHourID.setText(startTime+" - "+endTime);
        txtTTDayID.setText(dayID.substring(1));

    }



    private void getBundle() {
        dayID = getArguments().getString("dayID");
        timeID = getArguments().getString("timeID");
        status = getArguments().getString("status");
        if(status == DBConstants.lecturer) {
            lecturerID = getArguments().getString("lecturerID");
        } else if(status == DBConstants.userClass) {
            userClassID = getArguments().getString("userClassID");
        } else if(status == DBConstants.classLocation) {
            classLocationID = getArguments().getString("classLocationID");
        }
    }

    private void getTTDetails() {
        if(status == DBConstants.lecturer) {
            tblTimeFrame.getLCTime(lecturerID,dayID,timeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subjectID = dataSnapshot.child(DBConstants.subjectID).getValue().toString();
                    userClassID = dataSnapshot.child(DBConstants.userClassID).getValue().toString();
                    classLocationID = dataSnapshot.child(DBConstants.classLocationID).getValue().toString();
                    setTTDetails(subjectID,userClassID,classLocationID);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(status == DBConstants.classLocation) {
            tblTimeFrame.getCLTime(classLocationID,dayID,timeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subjectID = dataSnapshot.child(DBConstants.subjectID).getValue().toString();
                    lecturerID = dataSnapshot.child(DBConstants.lecturerID).getValue().toString();
                    userClassID = dataSnapshot.child(DBConstants.userClassID).getValue().toString();
                    setTTDetails(subjectID,lecturerID,userClassID);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(status == DBConstants.userClass) {
            tblTimeFrame.getUCTime(userClassID,dayID,timeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    subjectID = dataSnapshot.child(DBConstants.subjectID).getValue().toString();
                    lecturerID = dataSnapshot.child(DBConstants.lecturerID).getValue().toString();
                    classLocationID = dataSnapshot.child(DBConstants.classLocationID).getValue().toString();
                    setTTDetails(subjectID,lecturerID,classLocationID);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setTTDetails(String subjectID, String randomID2, String randomID3) {
        txtSubjectID.setText(subjectID);
        txtRandomID2.setText(randomID2);
        txtRandomID3.setText(randomID3);

        if(status == DBConstants.lecturer) {
            lblRandomID2.setText("UserClass ID");
            lblRandomID3.setText("ClassLocation ID");
        } else if(status == DBConstants.classLocation) {
            lblRandomID2.setText("Lecturer ID");
            lblRandomID3.setText("UserClass ID");
        } else if(status == DBConstants.userClass) {
            lblRandomID2.setText("Lecturer ID");
            lblRandomID3.setText("ClassLocation ID");
        }
    }
}