package com.umarzaii.uni4life.Activity.DeptAdmin;

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
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Controller.RecyclerViewController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Model.ClassLocationModel;
import com.umarzaii.uni4life.R;

public class DptAdmFrgCLList extends Fragment implements View.OnClickListener {

    private TblLecturer tblLecturer;

    private FragmentController frgController;
    private RecyclerViewController rvController;

    private RecyclerView rvCLList;
    private Button btnGoToAddCL;

    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_cllist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Class Location List");
        View v = getView();

        tblLecturer = new TblLecturer();
        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        rvController = new RecyclerViewController(getActivity());

        btnGoToAddCL = (Button)v.findViewById(R.id.btnGoToAddCL);
        rvCLList = (RecyclerView)v.findViewById(R.id.rvCLList);

        facultyID = getArguments().getString("facultyID");

        rvController.init(rvCLList);
        btnGoToAddCL.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ClassLocationModel,ClassLocactionViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClassLocationModel, ClassLocactionViewHolder>(

                ClassLocationModel.class,
                R.layout.rvitem_cllist,
                ClassLocactionViewHolder.class,
                tblLecturer.getTable().orderByChild(DBConstants.facultyID).equalTo(facultyID)

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
//                        frgController.stackFragment(new DeptAdminTimeFrameDayFragment(),bundle,"TimeFrameDay");
                    }
                });

            }
        };

        rvCLList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnGoToAddCL:
                frgController.stackFragment(new DptAdmFrgCLAdd(), R.id.dptAdContentMain, "AddCL");
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
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
