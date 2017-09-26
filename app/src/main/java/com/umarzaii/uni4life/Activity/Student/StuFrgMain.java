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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.umarzaii.uni4life.Activity.General.ActSignUp;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Database.TblStudent;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.R;

public class StuFrgMain extends Fragment implements View.OnClickListener {

    private FragmentController fragmentController;
    private FirebaseController firebaseController;

    private TblUser tblUser;
    private TblStudent tblStudent;

    private Button btnStudTimeTable;
    private Button btnLogOut;

    private String studentID;
    private String userClassID;

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

        tblUser = new TblUser();
        tblStudent = new TblStudent();

        btnStudTimeTable = (Button)v.findViewById(R.id.btnStudTimeTable);
        btnLogOut = (Button)v.findViewById(R.id.btnLogOut);

        btnStudTimeTable.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        getStudentID();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnStudTimeTable:
                Bundle ttBundle = new Bundle();
                ttBundle.putString("userClassID", userClassID);
                ttBundle.putString("title", "My TimeTable");
                fragmentController.stackFragment(new StuFrgTTList(), R.id.studContentMain, ttBundle, "TTList");
                break;
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

    public void getStudentID() {
        tblUser.getStudentID(firebaseController.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentID = dataSnapshot.getValue().toString();
                getUserClassID();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserClassID() {
        tblStudent.getUserClassID(studentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userClassID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
