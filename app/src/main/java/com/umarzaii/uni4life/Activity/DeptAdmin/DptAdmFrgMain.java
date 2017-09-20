package com.umarzaii.uni4life.Activity.DeptAdmin;

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
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.R;

public class DptAdmFrgMain extends Fragment implements View.OnClickListener{

    private FragmentController fragmentController;
    private FirebaseController firebaseController;

    private TblUser tblUser;
    private TblLecturer tblLecturer;

    private Button btnDeptGoToLectList;
    private Button btnDeptGoToUCList;
    private Button btnDeptGoToCLList;
    private Button btnMyTimeTable;
    private Button btnLogOut;

    private String lecturerID;
    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_main,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Dept Admin Main Fragment");
        View v = getView();

        fragmentController = new FragmentController(getActivity().getSupportFragmentManager());
        firebaseController = new FirebaseController();

        tblUser = new TblUser();
        tblLecturer = new TblLecturer();

        btnDeptGoToLectList = (Button)v.findViewById(R.id.btnDeptGoToLectList);
        btnDeptGoToUCList = (Button)v.findViewById(R.id.btnDeptGoToUCList);
        btnDeptGoToCLList = (Button)v.findViewById(R.id.btnDeptGoToCLList);
        btnMyTimeTable = (Button)v.findViewById(R.id.btnMyTimeTable);
        btnLogOut = (Button)v.findViewById(R.id.btnLogOut);

        btnDeptGoToLectList.setOnClickListener(this);
        btnDeptGoToUCList.setOnClickListener(this);
        btnDeptGoToCLList.setOnClickListener(this);
        btnMyTimeTable.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        getLecturerID();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnDeptGoToLectList:
                Bundle lectBundle = new Bundle();
                lectBundle.putString("facultyID", facultyID);
                fragmentController.stackFragment(new DptAdmFrgLectList(), R.id.dptAdContentMain, lectBundle, "LectList");
                break;
            case R.id.btnDeptGoToUCList:
                Bundle ucBundle = new Bundle();
                ucBundle.putString("facultyID", facultyID);
                fragmentController.stackFragment(new DptAdmFrgUCList(), R.id.dptAdContentMain, ucBundle, "UCList");
                break;
            case R.id.btnDeptGoToCLList:
                Bundle clBundle = new Bundle();
                clBundle.putString("facultyID", facultyID);
                fragmentController.stackFragment(new DptAdmFrgCLList(), R.id.dptAdContentMain, clBundle, "CLList");
                break;
            case R.id.btnMyTimeTable:
                Bundle ttBundle = new Bundle();
                ttBundle.putString("lecturerID", lecturerID);
                ttBundle.putString("title", "My TimeTable");
                ttBundle.putString("status", "MyTimeTable");
                fragmentController.stackFragment(new DptAdmFrgTTList(), R.id.dptAdContentMain, ttBundle, "MyTimeTable");
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

    public void getLecturerID() {
        tblUser.getLecturerID(firebaseController.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lecturerID = dataSnapshot.getValue().toString();
                getFacultyID();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getFacultyID() {
        tblLecturer.getFacultyID(lecturerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                facultyID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
