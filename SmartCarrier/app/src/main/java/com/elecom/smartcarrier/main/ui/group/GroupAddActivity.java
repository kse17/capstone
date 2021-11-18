package com.elecom.smartcarrier.main.ui.group;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.dto.CarrierControlDTO;
import com.elecom.smartcarrier.dto.CarrierDTO;
import com.elecom.smartcarrier.dto.CarrierOperationDTO;
import com.elecom.smartcarrier.dto.CarrierSettingDTO;
import com.elecom.smartcarrier.dto.UserDTO;
import com.elecom.smartcarrier.dto.UserSettingDTO;
import com.elecom.smartcarrier.main.ui.my.ConnectedList;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;
import static com.elecom.smartcarrier.common.PreferenceManager.getStringPreference;

public class GroupAddActivity extends AppCompatActivity {

    private static final String TAG = "GroupAddActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = firebaseDatabase.getReference("users");
    final DatabaseReference table_carrierUsers = firebaseDatabase.getReference("carrierUsers");
    final DatabaseReference table_userCarriers = firebaseDatabase.getReference("userCarriers");

    private EditText etEmail;
    private Button btnAdd;
    private String mac;

    //private String email;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_info);

        Intent intent = getIntent();
        mac = intent.getStringExtra("mac");
        Log.d(TAG, "mac = " + getStringPreference(this, DefineValue.PREFERENCE_DEFAULT_CARRIER,"default"));

        etEmail = (EditText)findViewById(R.id.et_email);
        //email = etEmail.getText().toString();

        btnAdd = (Button)findViewById(R.id.btn_add_member);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, etEmail.getText().toString());

                Query emailQuery = table_user.orderByChild("email").equalTo(etEmail.getText().toString());

                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child: snapshot.getChildren()) {
                            Log.d(TAG, "email Exists: " + etEmail.getText().toString() + ": " + child.getKey());
                            writeNewMember(child.getKey());


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void writeNewMember(String uid) {
        // DB에 그룹 정보 저장
        table_carrierUsers.child(getStringPreference(this, DefineValue.PREFERENCE_DEFAULT_CARRIER,"default")).child(uid).setValue(false);
        table_userCarriers.child(uid).child(getStringPreference(this, DefineValue.PREFERENCE_DEFAULT_CARRIER,"default")).setValue(false);

        Toast.makeText(this,"good~",Toast.LENGTH_SHORT).show();
    }
}
