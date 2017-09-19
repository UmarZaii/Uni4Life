package com.umarzaii.uni4life.Activity.Student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.R;

public class StuActMain extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_act_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);

        fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.startFragment(new StuFrgMain(), R.id.studContentMain);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
