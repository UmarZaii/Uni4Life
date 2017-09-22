package com.umarzaii.uni4life.Activity.DeptAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblDepartment;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblTimeFrame;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Mapper.LecturerMapper;
import com.umarzaii.uni4life.Mapper.TimeTableMapper;
import com.umarzaii.uni4life.Mapper.UserMapper;
import com.umarzaii.uni4life.Model.LecturerModel;
import com.umarzaii.uni4life.Model.UserModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class DptAdmFrgLectAdd extends Fragment implements View.OnClickListener {

    private FragmentController fragmentController;
    private FirebaseController firebaseController;

    private TblUser tblUser;
    private TblLecturer tblLecturer;
    private TblDepartment tblDepartment;
    private TblTimeFrame tblTimeFrame;

    private EditText edtLecturerID, edtLecturerName;
    private TextView txtUserID;
    private Button btnScanLect, btnAddLect;

    private String userID;
    private String lecturerID;
    private String lecturerName;
    private String departmentID;
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
        firebaseController = new FirebaseController();

        tblTimeFrame = new TblTimeFrame();
        tblDepartment = new TblDepartment();
        tblLecturer = new TblLecturer();
        tblUser = new TblUser();

        edtLecturerID = (EditText)v.findViewById(R.id.edtLecturerID);
        edtLecturerName = (EditText)v.findViewById(R.id.edtLecturerName);
        txtUserID = (TextView)v.findViewById(R.id.txtUserID);
        btnScanLect = (Button)v.findViewById(R.id.btnScanLect);
        btnAddLect = (Button)v.findViewById(R.id.btnAddLect);

        getLecturerID(firebaseController.getUserID());

        btnScanLect.setOnClickListener(this);
        btnAddLect.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null){
                Toast.makeText(getActivity(), "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                checkUserDetails(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkUserDetails(final String scanID) {

        tblUser.getTable().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(scanID)) {
                    txtUserID.setText(scanID);
                    userID = scanID;
                } else {
                    Toast.makeText(getActivity(), "User Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getLecturerID(String userID) {

        tblUser.getLecturerID(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lecturerID = dataSnapshot.getValue().toString();
                getDepartmentID(lecturerID);
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
                departmentID = dataSnapshot.getValue().toString();
                getFacultyID(departmentID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getFacultyID(String deptID) {

        tblDepartment.getFacultyID(deptID).addValueEventListener(new ValueEventListener() {
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
        if (userID == null) {
            Toast.makeText(getActivity(), "Please scan user first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(lecturerName)) {
            Toast.makeText(getActivity(), "Please input user lecturer name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(lecturerID)) {
            Toast.makeText(getActivity(), "Please input user lecturer ID", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void addLecturer() {
        UserModel userModel = new UserModel();
        userModel.setUserRole(DBConstants.lecturer);
        userModel.setLecturerID(lecturerID);
        UserMapper userMapper = new UserMapper(userModel);
        tblUser.getTable(userID).updateChildren(userMapper.credentialsToMap());

        LecturerModel lecturerModel = new LecturerModel();
        lecturerModel.setFacultyID(facultyID);
        lecturerModel.setDeptID(departmentID);
        lecturerModel.setLecturerID(lecturerID);
        lecturerModel.setLecturerID(lecturerName);
        lecturerModel.setDeptAdmin(false);
        lecturerModel.setDeptHead(false);
        LecturerMapper lecturerMapper = new LecturerMapper(lecturerModel);
        tblLecturer.getTable(lecturerID).updateChildren(lecturerMapper.detailsToMap());

        TimeTableMapper ttMapper = new TimeTableMapper(getActivity());
        final Map<String, Object> dataMapTt = new HashMap<String, Object>();
        dataMapTt.put(lecturerID, ttMapper.timeTableInit(DBConstants.tblLecturer));
        tblTimeFrame.getTblLecturer().updateChildren(dataMapTt);

        fragmentController.popBackStack("AddLect");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnScanLect:
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, integrator.REQUEST_CODE);
                break;
            case R.id.btnAddLect:
                lecturerID = edtLecturerID.getText().toString().trim();
                lecturerName = edtLecturerName.getText().toString().trim();
                if (inputCheck())
                    addLecturer();
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}