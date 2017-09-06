package com.umarzaii.uni4life.Activity.General;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.umarzaii.uni4life.Controller.FirebaseController;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Database.TblLecturer;
import com.umarzaii.uni4life.Database.TblUser;
import com.umarzaii.uni4life.R;

public class QRCodeActivity extends AppCompatActivity {

    private FirebaseController controller;

    private TblUser tblUser;
    private TblLecturer tblLecturer;

    private ImageView imgQRCode;
    private TextView txtUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_qrcode);

        controller = new FirebaseController();

        tblUser = new TblUser();
        tblLecturer = new TblLecturer();

        imgQRCode = (ImageView)findViewById(R.id.imgQrCode);
        txtUserID = (TextView)findViewById(R.id.txtUserID);

        txtUserID.setText(controller.getUserID());

        getMyQRCode();
        getLecturerID();
    }

    private void getMyQRCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(controller.getUserID(), BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void getLecturerID() {

        tblUser.getUserRole(controller.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lecturerID = dataSnapshot.child(DBConstants.lecturerID).getValue().toString();
                getLecturerStatus(lecturerID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getLecturerStatus(String lecturerID) {

        tblLecturer.getTable(lecturerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean deptAdmin = Boolean.valueOf(dataSnapshot.child(DBConstants.deptAdmin).getValue().toString());
                Boolean deptHead = Boolean.valueOf(dataSnapshot.child(DBConstants.deptHead).getValue().toString());
                if (deptAdmin) {
//                    Intent intent = new Intent(getApplicationContext(), DeptAdminMainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
                } else if (deptHead) {
//                    Intent intent = new Intent(getApplicationContext(), DeptHeadMainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
                } else {
//                    Intent intent = new Intent(getApplicationContext(), LecturerMainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
