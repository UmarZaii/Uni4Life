package com.umarzaii.uni4life.Activity.Lecturer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.umarzaii.uni4life.Activity.DeptAdmin.DptAdmFrgCLAdd;
import com.umarzaii.uni4life.Activity.DeptAdmin.DptAdmFrgTTList;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Controller.RecyclerViewController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblClassLocation;
import com.umarzaii.uni4life.Model.ClassLocationModel;
import com.umarzaii.uni4life.R;

public class LectFrgCLList extends Fragment {

    private TblClassLocation tblClassLocation;

    private FragmentController frgController;
    private RecyclerViewController rvController;

    private RecyclerView rvCLList;

    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lect_frg_cllist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Class Location List");
        View v = getView();

        tblClassLocation = new TblClassLocation();
        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        rvController = new RecyclerViewController(getActivity());

        rvCLList = (RecyclerView)v.findViewById(R.id.rvCLList);

        facultyID = getArguments().getString("facultyID");

        rvController.init(rvCLList);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClassLocationModel,ClassLocactionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClassLocationModel, ClassLocactionViewHolder>(

                ClassLocationModel.class,
                R.layout.rvitem_cllist,
                ClassLocactionViewHolder.class,
                tblClassLocation.getTable().orderByChild(DBConstants.facultyID).equalTo(facultyID)

        ) {
            @Override
            protected void populateViewHolder(ClassLocactionViewHolder viewHolder, ClassLocationModel model, int position) {

                final String classLocationID = model.getClassLocationID();
                final String classLocationName = model.getClassLocationName();
                viewHolder.setLecturerID(classLocationID);
                viewHolder.setLecturerName(classLocationName);

                viewHolder.fView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("classLocationID", classLocationID);
                        bundle.putString("title", classLocationID + " TimeTable");
                        bundle.putString("status", DBConstants.classLocation);
                        frgController.stackFragment(new LectFrgTTList(), R.id.lectContentMain, bundle, "TimeFrameDay");
                    }
                });

            }
        };

        rvCLList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ClassLocactionViewHolder extends RecyclerView.ViewHolder {
        View fView;

        public ClassLocactionViewHolder(View itemView) {
            super(itemView);
            fView = itemView;
        }

        public void setLecturerID(String classLocationID) {
            TextView txtClassLocationID = (TextView)fView.findViewById(R.id.txtClassLocationID);
            txtClassLocationID.setText(classLocationID);
        }

        public void setLecturerName(String classLocationName) {
            TextView txtClassLocationName = (TextView)fView.findViewById(R.id.txtClassLocationName);
            txtClassLocationName.setText(classLocationName);
        }
    }

}
