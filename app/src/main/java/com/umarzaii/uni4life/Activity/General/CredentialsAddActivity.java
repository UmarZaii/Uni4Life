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
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblCourse;
import com.umarzaii.uni4life.Database.TblFaculty;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Mapper.UserMapper;
import com.umarzaii.uni4life.Model.CourseModel;
import com.umarzaii.uni4life.Model.UserClassModel;
import com.umarzaii.uni4life.Model.UserModel;
import com.umarzaii.uni4life.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CredentialsAddActivity extends AppCompatActivity {

    private FirebaseController controller;

    private TblUser tblUser;
    private TblFaculty tblFaculty;
    private TblCourse tblCourse;
    private TblUserClass tblUserClass;

    private ArrayAdapter adpCourserID;
    private ArrayAdapter adpUserClassID;
    private ArrayList<String> courseIDList;
    private ArrayList<String> userClassIDList;

    private EditText edtStudentID;
    private Spinner spnCourseID, spnUserClassID;
    private Button btnAddStudentCredentials;

    private String strStudentID;
    private String strCourseIDSelection;
    private String strUserClassIDSelection;
    private String strFacultyID;
    private String strDeptID;

    private Boolean boolCourseID = true;
    private Boolean boolUserClassID = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_credentialsadd);

        controller = new FirebaseController();

        tblUser = new TblUser();
        tblFaculty = new TblFaculty();
        tblCourse = new TblCourse();
        tblUserClass = new TblUserClass();

        edtStudentID = (EditText)findViewById(R.id.edtStudentID);
        spnCourseID = (Spinner)findViewById(R.id.spnCourseID);
        spnUserClassID = (Spinner)findViewById(R.id.spnUserClassID);
        btnAddStudentCredentials = (Button) findViewById(R.id.btnAddStudentCredentials);

        getCourseIDList();
        getUserClassIDList();

        btnAddStudentCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strStudentID = edtStudentID.getText().toString().trim();

                if (inputCheck()) {
                    addCredentials();
                }
            }
        });
    }

    private void getCourseIDList(String facultyID) {
        tblFaculty.getCourseList(facultyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (boolCourseID) {
                    getSpinnerCourseID(dataSnapshot);
                    boolCourseID = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spnCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCourseIDSelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSpinnerCourseID(DataSnapshot dataSnapshot) {
        courseIDList = new ArrayList<String>();
        userClassIDList = new ArrayList<String>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            CourseModel courseModel = postSnapshot.getValue(CourseModel.class);
            String courseID = courseModel.courseID;
            courseIDList.add(courseID);
        }

        adpCourserID = new ArrayAdapter<String>(CredentialsAddActivity.this, android.R.layout.simple_spinner_item, courseIDList);
        adpCourserID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCourseID.setAdapter(adpCourserID);
    }

    private void getUserClassIDList() {
        tblCourse.getUserClassList(courseID,semesterID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (boolUserClassID) {
                    getSpinnerUserClassID(dataSnapshot);
                    boolUserClassID = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spnUserClassID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strUserClassIDSelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSpinnerUserClassID(DataSnapshot dataSnapshot) {
        userClassIDList = new ArrayList<String>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            UserClassModel model = postSnapshot.getValue(UserClassModel.class);
            String userClassID = model.getUserClassID();
            userClassIDList.add(userClassID);
        }

        adpUserClassID = new ArrayAdapter<String>(CredentialsAddActivity.this, android.R.layout.simple_spinner_item, userClassIDList);
        adpUserClassID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUserClassID.setAdapter(adpUserClassID);
    }

    private boolean inputCheck() {
        if(TextUtils.isEmpty(strStudentID) || strStudentID == null) {
            Toast.makeText(this, "Please input your studentID", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(strCourseIDSelection) || strCourseIDSelection == null) {
            Toast.makeText(this, "Please select your course", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(strUserClassIDSelection) || strUserClassIDSelection == null) {
            Toast.makeText(this, "Please select your class", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void addCredentials() {

        UserModel model = new UserModel();
        model.setStudentID(strStudentID);
        model.setCourseID(strCourseIDSelection);
        model.setUserClassID(strUserClassIDSelection);
        model.setFacultyID();
        model.setDeptID();
        model.setUserRole(DBConstants.student);
        UserMapper mapper = new UserMapper(model);

        final Map<String, Object> mapUser = new HashMap<String, Object>();
        mapUser.put(DBConstants.credentials, mapper.credentialsToMap());
        tblUser.getTable(controller.getUserID()).updateChildren(mapUser);

        final Map<String, Object> mapCourse = new HashMap<String, Object>();
        mapCourse.put(controller.getUserID(), mapper.userIDToMap());
        tblUserClass.getTblStudent(strUserClassIDSelection).updateChildren(mapCourse);

        Intent intent = new Intent(getApplicationContext(), StudentMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
