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
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Mapper.TimeTableMapper;
import com.umarzaii.uni4life.Mapper.UserClassMapper;
import com.umarzaii.uni4life.Model.UserClassModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class DptAdmFrgUCAdd extends Fragment implements View.OnClickListener {

    private FragmentController fragmentController;
    private FirebaseController controller;

    private TblUser tblUser;
    private TblLecturer tblLecturer;
    private TblUserClass tblUserClass;
    private TblTimeFrame tblTimeFrame;

    private EditText edtUCID;
    private EditText edtUCName;
    private Button btnAddUC;

    private String userClassID;
    private String userClassName;
    private String facultyID;
    private String deptID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_ucadd,container,false);
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
        tblUserClass = new TblUserClass();
        tblLecturer = new TblLecturer();
        tblUser = new TblUser();

        edtUCID = (EditText)v.findViewById(R.id.edtUCID);
        edtUCName = (EditText)v.findViewById(R.id.edtUCName);
        btnAddUC = (Button)v.findViewById(R.id.btnAddUC);

        btnAddUC.setOnClickListener(this);

        getLecturerID();
    }

    private void getLecturerID() {
        tblUser.getLecturerID(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lecturerID = dataSnapshot.getValue().toString();
                getFacultyID(lecturerID);
                getDepartmentID(lecturerID);
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

    private void getDepartmentID(String lecturerID) {
        tblLecturer.getDepartmentID(lecturerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deptID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean inputCheck() {
        if (TextUtils.isEmpty(userClassID)) {
            Toast.makeText(getActivity(), "Please input user class ID", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(userClassName)) {
            Toast.makeText(getActivity(), "Please input user class name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addUserClass() {
        UserClassModel ucModel = new UserClassModel();
        ucModel.setUserClassID(userClassID);
        ucModel.setUserClassName(userClassName);
        ucModel.setFacultyID(facultyID);
        ucModel.setDeptID(deptID);
//        ucModel.setCourseID();
//        ucModel.setSemesterID();
        UserClassMapper ucMapper = new UserClassMapper(ucModel);

        final Map<String, Object> dataMapCl = new HashMap<String, Object>();
        dataMapCl.put(userClassID, ucMapper.detailsToMap());
        tblUserClass.getTable().updateChildren(dataMapCl);

        TimeTableMapper ttMapper = new TimeTableMapper();
        final Map<String, Object> dataMapTt = new HashMap<String, Object>();
        dataMapTt.put(userClassID, ttMapper.timeTableInit(DBConstants.tblUserClass));
        tblTimeFrame.getTable().updateChildren(dataMapTt);

        fragmentController.popBackStack("ScanLect");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddLect:
                userClassID = edtUCID.getText().toString().trim();
                userClassName = edtUCName.getText().toString().trim();
                if (inputCheck())
                    addUserClass();
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}