package com.umarzaii.uni4life.Activity.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Controller.DateTimeController;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.R;

import org.joda.time.DateTimeConstants;

public class LoginActivity extends AppCompatActivity {

    private FirebaseController controller;

    private TblUser tblUser;
    private TblLecturer tblLecturer;

    private EditText edtUserEmailLogin, edtUserPassLogin;
    private Button btnLogin, btnGoToSignUp;
    private ProgressDialog progressDialog;

    private String strUserEmail;
    private String strUserPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        controller = new FirebaseController();

        tblUser = new TblUser();
        tblLecturer = new TblLecturer();

        edtUserEmailLogin = (EditText)findViewById(R.id.edtUserEmailLogin);
        edtUserPassLogin = (EditText)findViewById(R.id.edtUserPassLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnGoToSignUp = (Button)findViewById(R.id.btnGoToSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strUserEmail = edtUserEmailLogin.getText().toString().trim();
                strUserPass = edtUserPassLogin.getText().toString().trim();

                if (inputCheck()) {
                    userLogin();
                }
                DateTimeController controller1 = new DateTimeController(LoginActivity.this);
                controller1.getTimeTableDate(DateTimeConstants.MONDAY);
            }
        });

        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    private boolean inputCheck() {

        if(TextUtils.isEmpty(strUserEmail) || strUserEmail == null) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(strUserPass) || strUserPass == null) {
            Toast.makeText(this, "Please input your password", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private void userLogin() {

        progressDialog.setMessage("LogIn, Please Wait...");
        progressDialog.show();

        controller.getFirebaseAuth().signInWithEmailAndPassword(strUserEmail,strUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", task.getException().toString());
                } else if (!controller.getCurrentUser().isEmailVerified()) {
                    Intent intent = new Intent(LoginActivity.this, ActivationActivity.class);
                    intent.putExtra("userEmail", strUserEmail);
                    intent.putExtra("userPass", strUserPass);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {
                    checkUserCredentials();
                }
            }
        });

    }

    private void checkUserCredentials() {
        tblUser.getTable(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(DBConstants.userRole)) {
                    getUserRole();
                } else {
                    startActivity(new Intent(LoginActivity.this, CredentialsCheckActivity.class));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserRole() {

        tblUser.getUserRole(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.equals(DBConstants.lecturer)) {
                    String lecturerID = dataSnapshot.child(DBConstants.lecturerID).getValue().toString();
                    getLecturerStatus(lecturerID);
                } else if (dataSnapshot.equals(DBConstants.student)) {
//                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
//                    finish();
                    progressDialog.dismiss();
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
//                    startActivity(new Intent(LoginActivity.this, DeptAdminMainActivity.class));
//                    finish();
                    progressDialog.dismiss();
                } else if (deptHead) {
//                    startActivity(new Intent(LoginActivity.this, DeptHeadMainActivity.class));
//                    finish();
                    progressDialog.dismiss();
                } else {
//                    startActivity(new Intent(LoginActivity.this, LecturerMainActivity.class));
//                    finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}