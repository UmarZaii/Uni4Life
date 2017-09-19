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

public class ActActivation extends AppCompatActivity implements View.OnClickListener {

    private FirebaseController controller;

    private TextView txtUserEmail;
    private Button btnResendEmail, btnContinue;
    private ProgressDialog progressDialog;

    private String userEmail;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_act_activation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        controller = new FirebaseController();

        txtUserEmail = (TextView)findViewById(R.id.txtUserEmail);
        btnResendEmail = (Button)findViewById(R.id.btnResendEmail);
        btnContinue = (Button)findViewById(R.id.btnContinue);

        userPass = getIntent().getStringExtra("userPass");
        userEmail = controller.getCurrentUser().getEmail();

        txtUserEmail.setText(userEmail);

        btnResendEmail.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        progressDialog.setMessage("Checking, Please Wait...");
        progressDialog.show();

        switch(v.getId()){

            case R.id.btnResendEmail:
                controller.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ActActivation.this, "Verification email sent to " + userEmail, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActActivation.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;

            case R.id.btnContinue:
                controller.getFirebaseAuth().signInWithEmailAndPassword(userEmail,userPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (controller.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(ActActivation.this, ActCredentialsCheck.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            controller.getFirebaseAuth().signOut();
                            progressDialog.dismiss();
                            Toast.makeText(ActActivation.this, "Please verify your e-mail first", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            default:
                Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        controller.getFirebaseAuth().signOut();
        super.onBackPressed();
    }
}
