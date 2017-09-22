package com.umarzaii.uni4life.Activity.DeptAdmin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.umarzaii.uni4life.Controller.FragmentController;
import com.umarzaii.uni4life.Controller.RecyclerViewController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblUserClass;
import com.umarzaii.uni4life.Model.UserClassModel;
import com.umarzaii.uni4life.R;

public class DptAdmFrgUCList extends Fragment implements View.OnClickListener {

    private TblUserClass tblUserClass;

    private FragmentController frgController;
    private RecyclerViewController rvController;

    private RecyclerView rvUCList;
    private Button btnGoToAddUC;

    private String facultyID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dptad_frg_uclist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("UserClass List");
        View v = getView();

        tblUserClass = new TblUserClass();
        frgController = new FragmentController(getActivity().getSupportFragmentManager());
        rvController = new RecyclerViewController(getActivity());

        btnGoToAddUC = (Button)v.findViewById(R.id.btnGoToAddUC);
        rvUCList = (RecyclerView)v.findViewById(R.id.rvUCList);

        facultyID = getArguments().getString("facultyID");

        rvController.init(rvUCList);
        btnGoToAddUC.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<UserClassModel,UserClassViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserClassModel, UserClassViewHolder>(

                UserClassModel.class,
                R.layout.rvitem_uclist,
                UserClassViewHolder.class,
                tblUserClass.getTable().orderByChild(DBConstants.facultyID).equalTo(facultyID)

        ) {
            @Override
            protected void populateViewHolder(UserClassViewHolder viewHolder, UserClassModel model, int position) {

                final String userClassID = model.getUserClassID();
                viewHolder.setUserClassID(userClassID);

                viewHolder.fView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("userClassID", userClassID);
                        bundle.putString("title", userClassID + " TimeTable");
                        bundle.putString("status", DBConstants.userClass);
                        frgController.stackFragment(new DptAdmFrgTTList(), R.id.dptAdContentMain, bundle,"TimeFrameDay");
                    }
                });

            }
        };

        rvUCList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnGoToAddUC:
                frgController.stackFragment(new DptAdmFrgUCAdd(), R.id.dptAdContentMain, "Add UC");
                break;
            default:
                Toast.makeText(getActivity(), "This feature is in development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class UserClassViewHolder extends RecyclerView.ViewHolder {
        View fView;

        public UserClassViewHolder(View itemView) {
            super(itemView);
            fView = itemView;
        }

        public void setUserClassID(String userClassID) {
            TextView txtUserClassID = (TextView)fView.findViewById(R.id.txtUserClassID);
            txtUserClassID.setText(userClassID);
        }
    }

}
