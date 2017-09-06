package com.umarzaii.uni4life.Activity.General;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.R;

public class CredentialsCheckActivity extends AppCompatActivity {

    private FirebaseController controller;

    private Button btnContAsLect, btnContAsStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_credentialscheck);

        controller = new FirebaseController();

        btnContAsLect = (Button)findViewById(R.id.btnContAsLect);
        btnContAsStudent = (Button)findViewById(R.id.btnContAsStudent);

        btnContAsLect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CredentialsCheckActivity.this, QRCodeActivity.class));
            }
        });

        btnContAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CredentialsCheckActivity.this, CredentialsAddActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        controller.getFirebaseAuth().signOut();
        super.onBackPressed();
    }

}
