package com.umarzaii.uni4life.Activity.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.R;

public class ActivationActivity extends AppCompatActivity {

    private FirebaseController controller;

    private TextView txtUserEmail;
    private Button btnResendEmail, btnContinue;
    private ProgressDialog progressDialog;

    private String userEmail;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_activation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        controller = new FirebaseController();

        txtUserEmail = (TextView)findViewById(R.id.txtUserEmail);
        btnResendEmail = (Button)findViewById(R.id.btnResendEmail);
        btnContinue = (Button)findViewById(R.id.btnContinue);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userPass = intent.getStringExtra("userPass");

        final String strUserEmail = controller.getCurrentUser().getEmail();
        txtUserEmail.setText(strUserEmail);

        btnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail(strUserEmail);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Checking, Please Wait...");
                progressDialog.show();

                controller.getFirebaseAuth().signInWithEmailAndPassword(userEmail,userPass).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        checkUserLogin();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        controller.getFirebaseAuth().signOut();
        super.onBackPressed();
    }

    private void verifyEmail(final String userEmail) {

        controller.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ActivationActivity.this, "Verification email sent to " + userEmail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkUserLogin() {

        if (controller.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(ActivationActivity.this, CredentialsCheckActivity.class);
            startActivity(intent);
            finish();
            progressDialog.dismiss();
        } else {
            controller.getFirebaseAuth().signOut();
            progressDialog.dismiss();
            Toast.makeText(ActivationActivity.this, "Please verify your e-mail first", Toast.LENGTH_SHORT).show();
        }

    }

}
