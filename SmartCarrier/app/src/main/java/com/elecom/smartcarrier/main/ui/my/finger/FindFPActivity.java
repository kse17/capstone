package com.elecom.smartcarrier.main.ui.my.finger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.dto.CarrierDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindFPActivity extends AppCompatActivity {

    private static final String TAG = "FindFPActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    private String mac;

    private TextView txtFpLoc;
    private TextView txtFpConf;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_finger);

        Intent intent = getIntent();
        mac = intent.getStringExtra("mac");

        txtFpLoc = (TextView)findViewById(R.id.text_fp_location);
        txtFpConf = (TextView)findViewById(R.id.text_fp_confidence);
    }


    protected void onStart() {
        super.onStart();

        table_carrier.child(mac).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use The Model Class To Get The Data
                final CarrierDTO carrierDTO = dataSnapshot.getValue(CarrierDTO.class);

                String fpLoc = carrierDTO.getFpLocation().toString();
                String fpConf = carrierDTO.getFpConfidence().toString();

                txtFpLoc.setText("지문 등록 번호: " + fpLoc);
                txtFpConf.setText("지문 데이터 정확도: " +fpConf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}
