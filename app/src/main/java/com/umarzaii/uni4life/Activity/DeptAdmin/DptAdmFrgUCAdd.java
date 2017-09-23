package com.umarzaii.uni4life.Activity.DeptAdmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.DropdownController;
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

public class DptAdmFrgUCAdd extends Fragment implements
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentController fragmentController;
    private FirebaseController controller;
    private DropdownController dpController;

    private TblUser tblUser;
    private TblLecturer tblLecturer;
    private TblUserClass tblUserClass;
    private TblTimeFrame tblTimeFrame;

    private Spinner spnSemesterID;
    private EditText edtUCID;
    private Button btnAddUC;

    private String userClassID;
    private String facultyID;
    private String deptID;
    private String semesterID;

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
        dpController = new DropdownController(getActivity());

        tblTimeFrame = new TblTimeFrame();
        tblUserClass = new TblUserClass();
        tblLecturer = new TblLecturer();
        tblUser = new TblUser();

        spnSemesterID = (Spinner)v.findViewById(R.id.spnUCAddSemesterID);
        edtUCID = (EditText)v.findViewById(R.id.edtUCID);
        btnAddUC = (Button)v.findViewById(R.id.btnAddUC);

        getLecturerID();
        spinnerInit();

        spnSemesterID.setOnItemSelectedListener(this);
        btnAddUC.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddUC:
                userClassID = edtUCID.getText().toString().trim();
                if (inputCheck())
                    addUserClass();
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        semesterID = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void spinnerInit() {
        ArrayAdapter adapter = dpController.getAdapter(dpController.semester());
        spnSemesterID.setAdapter(adapter);
    }

    private boolean inputCheck() {
        if (TextUtils.isEmpty(userClassID)) {
            Toast.makeText(getActivity(), "Please input user class ID", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(semesterID)) {
            Toast.makeText(getActivity(), "Please choose semester", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addUserClass() {
        UserClassModel ucModel = new UserClassModel();
        ucModel.setUserClassID(userClassID);
        ucModel.setFacultyID(facultyID);
        ucModel.setDeptID(deptID);
        ucModel.setSemesterID(semesterID);
//        ucModel.setCourseID();
        UserClassMapper ucMapper = new UserClassMapper(ucModel);

        final Map<String, Object> dataMapCl = new HashMap<String, Object>();
        dataMapCl.put(userClassID, ucMapper.detailsToMap());
        tblUserClass.getTable().updateChildren(dataMapCl);

        TimeTableMapper ttMapper = new TimeTableMapper(getActivity());
        final Map<String, Object> dataMapTt = new HashMap<String, Object>();
        dataMapTt.put(userClassID, ttMapper.timeTableInit(DBConstants.tblUserClass));
        tblTimeFrame.getTblUserClass().updateChildren(dataMapTt);

        fragmentController.popBackStack("Add UC");
    }
}