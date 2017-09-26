package com.umarzaii.uni4life.Activity.Lecturer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.DropdownController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblClassLocation;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblSubject;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Mapper.TimeTableMapper;
import com.umarzaii.uni4life.Model.TimeTableModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class LectFrgTTEdit extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentController frgController;
    private DropdownController dpController;

    private TblTimeFrame tblTimeFrame;
    private TblSubject tblSubject;
    private TblLecturer tblLecturer;
    private TblClassLocation tblClassLocation;
    private TblUserClass tblUserClass;

    private Spinner spnSubjectID, spnRandomID2, spnRandomID3;
    private TextView lblRandomID2, lblRandomID3;
    private TextView txtRandomID, txtTTDayID, txtTTHourID;
    private Button btnTTSave;

    private String status;
    private String dayID, timeID, subjectID;
    private String lecturerID, userClassID, classLocationID;

    private Boolean boolSubject = false;
    private Boolean boolRandom2 = false;
    private Boolean boolRandom3 = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lect_frg_ttedit,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("TimeTable Edit");

        getBundle();
        initClass();
        setTTDetails();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTTSave:
                saveClass();
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spnSubjectID:
                subjectID = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spnRandomID2:
                if(getArguments().getString("status") == DBConstants.lecturer) {
                    userClassID = parent.getItemAtPosition(position).toString();
                } else if(getArguments().getString("status") == DBConstants.userClass) {
                    lecturerID = parent.getItemAtPosition(position).toString();
                } else if(getArguments().getString("status") == DBConstants.classLocation) {
                    lecturerID = parent.getItemAtPosition(position).toString();
                }
                break;
            case R.id.spnRandomID3:
                if(getArguments().getString("status") == DBConstants.lecturer) {
                    classLocationID = parent.getItemAtPosition(position).toString();
                } else if(getArguments().getString("status") == DBConstants.userClass) {
                    classLocationID = parent.getItemAtPosition(position).toString();
                } else if(getArguments().getString("status") == DBConstants.classLocation) {
                    userClassID = parent.getItemAtPosition(position).toString();
                }
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getBundle() {
        dayID = getArguments().getString("dayID");
        timeID = getArguments().getString("timeID");
        status = getArguments().getString("status");
        subjectID = getArguments().getString("subjectID");
        lecturerID = getArguments().getString("lecturerID");
        userClassID = getArguments().getString("userClassID");
        classLocationID = getArguments().getString("classLocationID");
    }

    private void initClass() {
        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        dpController = new DropdownController(getActivity());

        tblTimeFrame = new TblTimeFrame();
        tblSubject = new TblSubject();
        tblLecturer = new TblLecturer();
        tblClassLocation = new TblClassLocation();
        tblUserClass = new TblUserClass();

        View v = getView();
        txtRandomID = (TextView)v.findViewById(R.id.txtRandomID);
        txtTTDayID = (TextView)v.findViewById(R.id.txtTTDayID);
        txtTTHourID = (TextView)v.findViewById(R.id.txtTTHourID);
        lblRandomID2 = (TextView)v.findViewById(R.id.lblRandomID2);
        lblRandomID3 = (TextView)v.findViewById(R.id.lblRandomID3);
        spnSubjectID = (Spinner)v.findViewById(R.id.spnSubjectID);
        spnRandomID2 = (Spinner)v.findViewById(R.id.spnRandomID2);
        spnRandomID3 = (Spinner)v.findViewById(R.id.spnRandomID3);
        btnTTSave = (Button)v.findViewById(R.id.btnTTSave);

        if(status == DBConstants.lecturer) {
            txtRandomID.setText(lecturerID);
        } else if(status == DBConstants.userClass) {
            txtRandomID.setText(userClassID);
        } else if(status == DBConstants.classLocation) {
            txtRandomID.setText(classLocationID);
        }
        String startTime = timeID.substring(0,2)+":00";
        String endTime = timeID.substring(5,7)+":00";
        txtTTHourID.setText(startTime+" - "+endTime);
        txtTTDayID.setText(dayID.substring(1));

        spnSubjectID.setOnItemSelectedListener(this);
        spnRandomID2.setOnItemSelectedListener(this);
        spnRandomID3.setOnItemSelectedListener(this);
        btnTTSave.setOnClickListener(this);

        initSpinner(spnSubjectID,"subject");
        initSpinner(spnRandomID2,"random2");
        initSpinner(spnRandomID3,"random3");
    }

    private void initSpinner(final Spinner spinnerID, String boolType) {
        if (!boolSubject && boolType == "subject") {
            Query query = tblSubject.getTable();
            getSpinnerList(spinnerID,query);
            boolSubject = true;
        } else if (!boolRandom2 && boolType == "random2") {
            if(status == DBConstants.lecturer) {
                Query query = tblUserClass.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom2 = true;
            } else if(status == DBConstants.classLocation) {
                Query query = tblLecturer.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom2 = true;
            } else if(status == DBConstants.userClass) {
                Query query = tblLecturer.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom2 = true;
            }
        } else if (!boolRandom3 && boolType == "random3") {
            if(status == DBConstants.lecturer) {
                Query query = tblClassLocation.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom3 = true;
            } else if(status == DBConstants.classLocation) {
                Query query = tblUserClass.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom3 = true;
            } else if(status == DBConstants.userClass) {
                Query query = tblClassLocation.getTable();
                getSpinnerList(spinnerID,query);
                boolRandom3 = true;
            }
        }
    }

    private void getSpinnerList(final Spinner spnRandomID, Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayAdapter adapter = dpController.getAdapter(dataSnapshot);
                spnRandomID.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTTDetails() {
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

    private void saveClass() {
        TimeTableModel ttModel = new TimeTableModel();
        ttModel.setSubjectID(subjectID);
        ttModel.setLecturerID(lecturerID);
        ttModel.setUserClassID(userClassID);
        ttModel.setClassLocationID(classLocationID);
        TimeTableMapper ttMapper = new TimeTableMapper(getActivity(),ttModel);

        final Map<String, Object> dataMapLC = new HashMap<String, Object>();
        dataMapLC.put(timeID, ttMapper.lecturerToMap());
        tblTimeFrame.getLCDay(lecturerID,dayID).updateChildren(dataMapLC);

        final Map<String, Object> dataMapUC = new HashMap<String, Object>();
        dataMapUC.put(timeID, ttMapper.userClassToMap());
        tblTimeFrame.getUCDay(userClassID,dayID).updateChildren(dataMapUC);

        final Map<String, Object> dataMapCL = new HashMap<String, Object>();
        dataMapCL.put(timeID, ttMapper.classLocationToMap());
        tblTimeFrame.getLCDay(classLocationID,dayID).updateChildren(dataMapCL);

        frgController.popBackStack("TTEdit");
    }
}