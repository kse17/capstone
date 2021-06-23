package com.elecom.smartcarrier.main.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.dto.CarrierDTO;
import com.elecom.smartcarrier.dto.UserDTO;
import com.elecom.smartcarrier.init.InitStepHandler;
import com.elecom.smartcarrier.server.firebase.SharedFirebasePreferences;
import com.elecom.smartcarrier.server.firebase.SharedFirebasePreferencesContextWrapper;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MySetActivity extends AppCompatActivity {

    private static final String TAG = "MySetActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    private TextView txtManager;
    private TextView txtMac;
    private String mac;
    private Button btnAdd;
    private Switch switchLock;
    private Switch switchBuzzer;
    private Switch switchLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        Intent intent = getIntent();
        mac = intent.getStringExtra("mac");

        txtManager = (TextView)findViewById(R.id.manager_title);
        txtMac = (TextView)findViewById(R.id.mac_title);
        btnAdd = (Button) findViewById(R.id.btn_add_finger);
        switchLock = (Switch) findViewById(R.id.lock_title);
        switchBuzzer = (Switch) findViewById(R.id.buzzer_title);
        switchLed = (Switch) findViewById(R.id.led_title);

        //Toast.makeText(getApplicationContext(),"MAC: " + mac, Toast.LENGTH_SHORT).show();

        //init();
    }

    protected void onStart() {

        super.onStart();

        table_carrier.child(mac).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use The Model Class To Get The Data
                final CarrierDTO carrierDTO = dataSnapshot.getValue(CarrierDTO.class);

                String slock = carrierDTO.getSlock();
                Toast.makeText(getApplicationContext(),"Lock: " + slock, Toast.LENGTH_SHORT).show();

                if (slock.equals("0")) {
                    switchLock.setText("Smart Lock: OFF");
                    switchLock.setChecked(false);
                    switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("lock").setValue("1");
                            Toast.makeText(getApplicationContext(),"Lock: " + slock, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (slock.equals("1")) {
                    switchLock.setText("Smart Lock: ON");
                    switchLock.setChecked(true);
                    switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("lock").setValue("0");
                            Toast.makeText(getApplicationContext(),"Lock: " + slock, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String sbuzzer = carrierDTO.getSbuzzer();
                Toast.makeText(getApplicationContext(),"Buzzer: " + sbuzzer, Toast.LENGTH_SHORT).show();

                if (sbuzzer.equals("0")) {
                    switchBuzzer.setText("Buzzer: OFF");
                    switchBuzzer.setChecked(false);
                    switchBuzzer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("buzzer").setValue("1");
                            Toast.makeText(getApplicationContext(),"Buzzer: " + sbuzzer, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (sbuzzer.equals("1")) {
                    switchBuzzer.setText("Buzzer: ON");
                    switchBuzzer.setChecked(true);
                    switchBuzzer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("buzzer").setValue("0");
                            Toast.makeText(getApplicationContext(),"Buzzer: " + sbuzzer, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String sled = carrierDTO.getSled();
                Toast.makeText(getApplicationContext(),"Lock: " + sled, Toast.LENGTH_SHORT).show();

                if (sled.equals("0")) {
                    switchLed.setText("Led: OFF");
                    switchLed.setChecked(false);
                    switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("led").setValue("1");
                            Toast.makeText(getApplicationContext(),"Led: " + sled, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (sled.equals("1")) {
                    switchLed.setText("Led: ON");
                    switchLed.setChecked(true);
                    switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("led").setValue("0");
                            Toast.makeText(getApplicationContext(),"Led: " + sled, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                txtManager.setText(carrierDTO.getManager());
                txtMac.setText(mac);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table_carrier.child(mac).child("operation").setValue("1");

                //Intent intent = new Intent(getApplicationContext(), ConnectedList.class);
                //startActivity(intent);
                //show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
                init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // 인증이 되어 있는 상태
            setContentView(R.layout.activity_my_settings);
            populateCarrier();
            //populateSetting();

            //updateMainView();
        } else {
            // 인증이 되어 있지 않은 상태
        }

    }

    private void populateCarrier() {
        TextView txtManager = (TextView)findViewById(R.id.manager_title);
        TextView txtMac = (TextView)findViewById(R.id.mac_title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String mac = intent.getStringExtra("mac");

        //txtManager.setText();
        txtMac.setText(mac);


    }

    private void populateSetting() {

        switchLock.setChecked(true);

        TextView txtManager = (TextView) findViewById(R.id.manager_title);
        TextView txtMac = (TextView) findViewById(R.id.mac_title);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String mac = intent.getStringExtra("mac");
        //txtManager.setText();

        txtMac.setText(mac);


    }

    private void updateCarrierData(FirebaseUser user){
        Map<String, Object> carrierUpdates = new HashMap<>();
        carrierUpdates.put("mac/lock", Boolean.FALSE);
        table_carrier.updateChildren(carrierUpdates);
    }
}