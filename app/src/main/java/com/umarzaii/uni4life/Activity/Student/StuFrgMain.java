package com.umarzaii.uni4life.Activity.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.umarzaii.uni4life.Activity.General.ActSignUp;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.R;

public class StuFrgMain extends Fragment implements View.OnClickListener {

    private FragmentController fragmentController;
    private FirebaseController firebaseController;

    private Button btnStudTimeTable;
    private Button btnLogOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stud_frg_main,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Student Main Fragment");
        View v = getView();

        fragmentController = new FragmentController(getActivity().getSupportFragmentManager());
        firebaseController = new FirebaseController();

        btnStudTimeTable = (Button)v.findViewById(R.id.btnStudTimeTable);
        btnLogOut = (Button)v.findViewById(R.id.btnLogOut);

        btnStudTimeTable.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogOut:
                firebaseController.getFirebaseAuth().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), ActSignUp.class));
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
