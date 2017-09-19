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
import com.umarzaii.uni4life.Model.LecturerModel;
import com.umarzaii.uni4life.R;

public class DptAdmFrgLectList extends Fragment implements View.OnClickListener {

    private TblLecturer tblLecturer;

    private FragmentController frgController;
    private RecyclerViewController rvController;

    private RecyclerView rvLecturerList;
    private Button btnGoToAddLect;

    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_lectlist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Lecturer List");
        View v = getView();

        tblLecturer = new TblLecturer();
        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        rvController = new RecyclerViewController(getActivity());

        btnGoToAddLect = (Button)v.findViewById(R.id.btnGoToAddLect);
        rvLecturerList = (RecyclerView)v.findViewById(R.id.rvLecturerList);

        facultyID = getArguments().getString("facultyID");

        rvController.init(rvLecturerList);
        btnGoToAddLect.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<LecturerModel,LecturerViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LecturerModel, LecturerViewHolder>(

                LecturerModel.class,
                R.layout.rvitem_lectlist,
                LecturerViewHolder.class,
                tblLecturer.getTable().orderByChild(DBConstants.facultyID).equalTo(facultyID)

        ) {
            @Override
            protected void populateViewHolder(LecturerViewHolder viewHolder, LecturerModel model, int position) {

                final String lecturerID = model.getLecturerID();
                final String lecturerName = model.getLecturerName();
                viewHolder.setLecturerID(lecturerID);
                viewHolder.setLecturerName(lecturerName);

                viewHolder.fView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("lecturerID", lecturerID);
//                        frgController.stackFragment(new DeptAdminTimeFrameDayFragment(),bundle,"TimeFrameDay");
                    }
                });

            }
        };

        rvLecturerList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnGoToAddLect:
                frgController.stackFragment(new DptAdmFrgLectAdd(), R.id.dptAdContentMain, "AddLect");
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class LecturerViewHolder extends RecyclerView.ViewHolder {
        View fView;

        public LecturerViewHolder(View itemView) {
            super(itemView);
            fView = itemView;
        }

        public void setLecturerID(String lecturerID) {
            TextView txtLecturerID = (TextView)fView.findViewById(R.id.txtLecturerID);
            txtLecturerID.setText(lecturerID);
        }

        public void setLecturerName(String lecturerName) {
            TextView txtLecturerName = (TextView)fView.findViewById(R.id.txtLecturerName);
            txtLecturerName.setText(lecturerName);
        }
    }

}
