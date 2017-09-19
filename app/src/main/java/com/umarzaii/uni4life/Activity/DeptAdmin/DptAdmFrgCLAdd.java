package com.umarzaii.uni4life.Activity.DeptAdmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblClassLocation;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Mapper.ClassLocationMapper;
import com.umarzaii.uni4life.Mapper.TimeTableMapper;
import com.umarzaii.uni4life.Model.ClassLocationModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class DptAdmFrgCLAdd extends Fragment implements View.OnClickListener {

    private FragmentController fragmentController;
    private FirebaseController controller;

    private TblUser tblUser;
    private TblLecturer tblLecturer;
    private TblClassLocation tblClassLocation;
    private TblTimeFrame tblTimeFrame;

    private EditText edtCLID;
    private EditText edtCLName;
    private Button btnAddCL;

    private String classLocationID;
    private String classLocationName;
    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_lectadd,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Add Lecturer");
        View v = getView();

        fragmentController = new FragmentController(getActivity().getSupportFragmentManager());
        controller = new FirebaseController();

        tblTimeFrame = new TblTimeFrame();
        tblClassLocation = new TblClassLocation();
        tblLecturer = new TblLecturer();
        tblUser = new TblUser();

        edtCLID = (EditText)v.findViewById(R.id.edtCLID);
        edtCLName = (EditText)v.findViewById(R.id.edtCLName);
        btnAddCL = (Button)v.findViewById(R.id.btnAddCL);

        btnAddCL.setOnClickListener(this);

        getLecturerID();
    }

    private void getLecturerID() {
        tblUser.getLecturerID(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lecturerID = dataSnapshot.getValue().toString();
                getFacultyID(lecturerID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFacultyID(String lecturerID) {
        tblLecturer.getFacultyID(lecturerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facultyID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean inputCheck() {
        if (TextUtils.isEmpty(classLocationID)) {
            Toast.makeText(getActivity(), "Please input user class ID", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(classLocationName)) {
            Toast.makeText(getActivity(), "Please input user class name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addClassLocation() {
        ClassLocationModel clModel = new ClassLocationModel();
        clModel.setClassLocationID(classLocationID);
        clModel.setClassLocationName(classLocationName);
        clModel.setFacultyID(facultyID);
        ClassLocationMapper clMapper = new ClassLocationMapper(clModel);

        final Map<String, Object> dataMapCl = new HashMap<String, Object>();
        dataMapCl.put(classLocationID, clMapper.checkToMap());
        tblClassLocation.getTable().updateChildren(dataMapCl);

        TimeTableMapper ttMapper = new TimeTableMapper();
        final Map<String, Object> dataMapTt = new HashMap<String, Object>();
        dataMapTt.put(classLocationID, ttMapper.timeTableInit(DBConstants.tblClassLocation));
        tblTimeFrame.getTable().updateChildren(dataMapTt);

        fragmentController.popBackStack("ScanLect");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddLect:
                classLocationID = edtCLID.getText().toString().trim();
                classLocationName = edtCLName.getText().toString().trim();
                if (inputCheck())
                    addClassLocation();
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}