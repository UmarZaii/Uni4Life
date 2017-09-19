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
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.Mapper.UserMapper;
import com.umarzaii.uni4life.Model.UserModel;
import com.umarzaii.uni4life.R;

import java.util.HashMap;
import java.util.Map;

public class ActSignUp extends AppCompatActivity implements
        View.OnClickListener, OnCompleteListener<AuthResult> {

    private FirebaseController controller;

    private TblUser tblUser;

    private EditText edtUserEmailReg, edtUserPassReg;
    private Button btnSignUp;
    private ProgressDialog progressDialog;

    private String userEmail;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_act_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        controller = new FirebaseController();

        tblUser = new TblUser();

        edtUserEmailReg = (EditText)findViewById(R.id.edtUserEmailReg);
        edtUserPassReg = (EditText)findViewById(R.id.edtUserPassReg);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
    }

    private boolean inputCheck() {
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(ActSignUp.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(userPass)){
            Toast.makeText(ActSignUp.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSignUp:
                userEmail = edtUserEmailReg.getText().toString().trim();
                userPass = edtUserPassReg.getText().toString().trim();
                if (inputCheck()) {
                    progressDialog.setMessage("Signing Up, Please Wait...");
                    progressDialog.show();
                    controller.getFirebaseAuth().createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(this);
                }
                break;
            default:
                Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (!task.isSuccessful()){

            progressDialog.dismiss();
            Log.d("Unsuccessfull", task.getException().toString());
            Toast.makeText(ActSignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

        } else {

            UserModel userModel = new UserModel();
            userModel.setUserEmail(userEmail);
            UserMapper mapper = new UserMapper(userModel);

            final Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put(controller.getUserID(), mapper.detailsToMap());
            tblUser.getTable().updateChildren(dataMap);

            controller.getCurrentUser().sendEmailVerification();

            Intent intent = new Intent(ActSignUp.this, ActActivation.class);
            intent.putExtra("userPass", userPass);
            startActivity(intent);

            finish();
            progressDialog.dismiss();

        }
    }
}
