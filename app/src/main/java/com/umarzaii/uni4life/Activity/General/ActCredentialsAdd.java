package com.umarzaii.uni4life.Activity.General;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Activity.Student.StuActMain;
import com.umarzaii.uni4life.Controller.DropdownController;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblCourse;
import com.umarzaii.uni4life.Database.TblStudent;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Mapper.StudentMapper;
import com.umarzaii.uni4life.Mapper.UserMapper;
import com.umarzaii.uni4life.Model.StudentModel;
import com.umarzaii.uni4life.Model.UserModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class ActCredentialsAdd extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseController fbController;
    private DropdownController dpController;

    private TblUser tblUser;
    private TblStudent tblStudent;
    private TblCourse tblCourse;
    private TblUserClass tblUserClass;

    private EditText edtStudentID;
    private Spinner spnCourseID, spnSemesterID, spnUserClassID;
    private Button btnAddStudentCredentials;

    private String studentID;
    private String courseIDSelection;
    private String semesterIDSelection;
    private String userClassIDSelection;
    private String facultyID;
    private String deptID;

    private Boolean boolCourse = true;
    private Boolean boolUserClass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_activity_credentialsadd);

        fbController = new FirebaseController();
        dpController = new DropdownController(this);

        tblUser = new TblUser();
        tblStudent = new TblStudent();
        tblCourse = new TblCourse();
        tblUserClass = new TblUserClass();

        edtStudentID = (EditText)findViewById(R.id.edtStudentID);
        spnCourseID = (Spinner)findViewById(R.id.spnCourseID);
        spnSemesterID = (Spinner)findViewById(R.id.spnSemesterID);
        spnUserClassID = (Spinner)findViewById(R.id.spnUserClassID);
        btnAddStudentCredentials = (Button) findViewById(R.id.btnAddStudentCredentials);

        spnCourseID.setOnItemSelectedListener(this);
        spnSemesterID.setOnItemSelectedListener(this);
        spnUserClassID.setOnItemSelectedListener(this);
        btnAddStudentCredentials.setOnClickListener(this);

        getCourseIDList();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAddStudentCredentials:
                studentID = edtStudentID.getText().toString().trim();
                if (inputCheck()) {
                    addCredentials();
                }
                break;
            default:
                Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(view.getId()){
            case R.id.spnCourseID:
                courseIDSelection = parent.getItemAtPosition(position).toString();
                getUserClassIDList(courseIDSelection);
                getFacultyAndDeptID(courseIDSelection);
                break;
            case R.id.spnSemesterID:
                semesterIDSelection = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spnUserClassID:
                userClassIDSelection = parent.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getCourseIDList() {
        tblCourse.getTable().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (boolCourse) {
                    ArrayAdapter adapter = dpController.getAdapter(dataSnapshot);
                    spnCourseID.setAdapter(adapter);
                    boolCourse = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFacultyAndDeptID(String courseID) {
        tblCourse.getTable(courseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facultyID = dataSnapshot.child(DBConstants.facultyID).getValue().toString();
                deptID = dataSnapshot.child(DBConstants.deptID).getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserClassIDList(String courseID) {
        tblUserClass.getTable().orderByChild(DBConstants.courseID).equalTo(courseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (boolUserClass) {
                    ArrayAdapter adapter = dpController.getAdapter(dataSnapshot);
                    spnUserClassID.setAdapter(adapter);
                    boolUserClass = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean inputCheck() {
        if(TextUtils.isEmpty(studentID) || studentID == null) {
            Toast.makeText(this, "Please input your studentID", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(courseIDSelection) || courseIDSelection == null) {
            Toast.makeText(this, "Please select your course", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(semesterIDSelection) || semesterIDSelection == null) {
            Toast.makeText(this, "Please select semester", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(userClassIDSelection) || userClassIDSelection == null) {
            Toast.makeText(this, "Please select your class", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void addCredentials() {

        UserModel userModel = new UserModel();
        userModel.setStudentID(studentID);
        userModel.setUserRole(DBConstants.student);
        UserMapper userMapper = new UserMapper(userModel);

        final Map<String, Object> mapUser = new HashMap<String, Object>();
        mapUser.put(fbController.getUserID(), userMapper.credentialsToMap());
        tblUser.getTable().updateChildren(mapUser);

        StudentModel studentModel = new StudentModel();
        studentModel.setCourseID(courseIDSelection);
        studentModel.setSemesterID(semesterIDSelection);
        studentModel.setUserClassID(userClassIDSelection);
        studentModel.setFacultyID(facultyID);
        studentModel.setDeptID(deptID);
        StudentMapper studentMapper = new StudentMapper(studentModel);

        final Map<String, Object> mapStudent = new HashMap<String, Object>();
        mapStudent.put(studentID, studentMapper.detailsToMap());
        tblStudent.getTable().updateChildren(mapStudent);

        Intent intent = new Intent(getApplicationContext(), StuActMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
