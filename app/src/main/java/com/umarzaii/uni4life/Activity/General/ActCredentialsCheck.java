package com.umarzaii.uni4life.Activity.General;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.R;

public class ActCredentialsCheck extends AppCompatActivity implements View.OnClickListener {

    private FirebaseController controller;

    private Button btnContAsLect, btnContAsStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_activity_credentialscheck);

        controller = new FirebaseController();

        btnContAsLect = (Button)findViewById(R.id.btnContAsLect);
        btnContAsStudent = (Button)findViewById(R.id.btnContAsStudent);

        btnContAsLect.setOnClickListener(this);
        btnContAsStudent.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        controller.getFirebaseAuth().signOut();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnContAsLect:
                startActivity(new Intent(ActCredentialsCheck.this, ActQRCode.class));
                break;
            case R.id.btnContAsStudent:
                startActivity(new Intent(ActCredentialsCheck.this, ActCredentialsAdd.class));
                break;
            default:
                Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
