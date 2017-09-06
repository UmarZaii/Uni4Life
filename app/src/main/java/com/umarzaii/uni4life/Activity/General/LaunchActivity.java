package com.umarzaii.uni4life.Activity.General;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.R;

public class LaunchActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener fAuthListener;
    private FirebaseController controller;

    private TblUser tblUser;
    private TblLecturer tblLecturer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_launch);

        controller = new FirebaseController();

        tblUser = new TblUser();
        tblLecturer = new TblLecturer();

        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    checkUserLogin();
                } else {
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.getFirebaseAuth().addAuthStateListener(fAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.getFirebaseAuth().removeAuthStateListener(fAuthListener);
    }

    private void checkUserCredentials() {

        tblUser.getTable(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(DBConstants.userRole)) {
                    getUserRole();
                } else {
                    controller.getFirebaseAuth().signOut();
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkUserLogin() {

        if (controller.getCurrentUser().isEmailVerified()) {
            checkUserCredentials();
        } else {
            controller.getFirebaseAuth().signOut();
            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
            finish();
        }

    }

    private void getUserRole() {

        tblUser.getUserRole(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.equals(DBConstants.lecturer)) {
                    String lecturerID = dataSnapshot.child(DBConstants.lecturerID).getValue().toString();
                    getLecturerStatus(lecturerID);
                } else if (dataSnapshot.equals(DBConstants.student)) {
//                    startActivity(new Intent(LaunchActivity.this, StudentMainActivity.class));
//                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getLecturerStatus(String lecturerID) {

        tblLecturer.getTable(lecturerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean deptAdmin = Boolean.valueOf(dataSnapshot.child(DBConstants.deptAdmin).getValue().toString());
                Boolean deptHead = Boolean.valueOf(dataSnapshot.child(DBConstants.deptHead).getValue().toString());
                if (deptAdmin) {
//                    startActivity(new Intent(LaunchActivity.this, DeptAdminMainActivity.class));
//                    finish();
                } else if (deptHead) {
//                    startActivity(new Intent(LaunchActivity.this, DeptHeadMainActivity.class));
//                    finish();
                } else {
//                    startActivity(new Intent(LaunchActivity.this, LecturerMainActivity.class));
//                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
