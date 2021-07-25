package com.elecom.smartcarrier.main.ui.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.elecom.smartcarrier.dto.CarrierDTO;
import com.elecom.smartcarrier.dto.CarrierSettingDTO;
import com.elecom.smartcarrier.main.ui.my.finger.AddFPActivity;
import com.elecom.smartcarrier.main.ui.my.finger.DeleteFPActivity;
import com.elecom.smartcarrier.main.ui.my.finger.FindFPActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;

public class MySetActivity extends AppCompatActivity {

    private static final String TAG = "MySetActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    private TextView txtManager;
    private TextView txtMac;
    private String mac;
    private Button btnAdd;
    private Button btnFind;
    private Button btnDelete;
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
        btnFind = (Button)findViewById(R.id.btn_find_finger);
        btnDelete = (Button)findViewById(R.id.btn_delete_finger);

        switchLock = (Switch) findViewById(R.id.lock_title);
        switchBuzzer = (Switch) findViewById(R.id.buzzer_title);
        switchLed = (Switch) findViewById(R.id.led_title);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_carrier.child(mac).child("fpValue").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Error getting data", task.getException());
                        } else {
                            Log.d(TAG, String.valueOf(task.getResult().getValue()));
                        }

                        if (String.valueOf(task.getResult().getValue()).equals("0")) {
                            table_carrier.child(mac).child("carrierOperation").child("addOperation").setValue("1");
                            Intent intent = new Intent(getApplicationContext(), AddFPActivity.class);
                            intent.putExtra("mac", mac);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Fingerprint Data is already resisted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_carrier.child(mac).child("fpValue").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e(TAG, "Error getting data", task.getException());
                        } else {
                            Log.d(TAG, String.valueOf(task.getResult().getValue()));
                        }

                        if (String.valueOf(task.getResult().getValue()).equals("0")) {
                            Toast.makeText(getApplicationContext(), "No Fingerprint Data", Toast.LENGTH_SHORT).show();
                        } else {
                            table_carrier.child(mac).child("carrierOperation").child("deleteOperation").setValue("1");
                            Intent intent = new Intent(getApplicationContext(), DeleteFPActivity.class);
                            intent.putExtra("mac", mac);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_carrier.child(mac).child("fpValue").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e(TAG, "Error getting data", task.getException());
                        } else {
                            Log.d(TAG, String.valueOf(task.getResult().getValue()));
                        }

                        if (String.valueOf(task.getResult().getValue()).equals("0")) {
                            Toast.makeText(getApplicationContext(), "No Fingerprint Data", Toast.LENGTH_SHORT).show();
                        } else {
                            table_carrier.child(mac).child("carrierOperation").child("findOperation").setValue("1");
                            Intent intent = new Intent(getApplicationContext(), FindFPActivity.class);
                            intent.putExtra("mac", mac);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        //Toast.makeText(getApplicationContext(),"MAC: " + mac, Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        super.onStart();

        table_carrier.child(mac).child("manager").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                } else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    txtManager.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        table_carrier.child(mac).child("mac").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                } else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    txtMac.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        table_carrier.child(mac).child("carrierSetting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use The Model Class To Get The Data
                //final CarrierDTO carrierDTO = dataSnapshot.getValue(CarrierDTO.class);
                final CarrierSettingDTO carrierSettingDTO = dataSnapshot.getValue(CarrierSettingDTO.class);

                String slock = carrierSettingDTO.getSlock();
                //Toast.makeText(getApplicationContext(),"Lock: " + slock, Toast.LENGTH_SHORT).show();

                if (slock.equals("0")) {
                    switchLock.setText("Smart Lock: OFF");
                    switchLock.setChecked(false);
                    switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("slock").setValue("1");
                        }
                    });
                }
                if (slock.equals("1")) {
                    switchLock.setText("Smart Lock: ON");
                    switchLock.setChecked(true);
                    switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("slock").setValue("0");
                        }
                    });
                }

                String sbuzzer = carrierSettingDTO.getSbuzzer();
                //Toast.makeText(getApplicationContext(),"Buzzer: " + sbuzzer, Toast.LENGTH_SHORT).show();

                if (sbuzzer.equals("0")) {
                    switchBuzzer.setText("Buzzer: OFF");
                    switchBuzzer.setChecked(false);
                    switchBuzzer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("sbuzzer").setValue("1");
                        }
                    });
                }
                if (sbuzzer.equals("1")) {
                    switchBuzzer.setText("Buzzer: ON");
                    switchBuzzer.setChecked(true);
                    switchBuzzer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("sbuzzer").setValue("0");
                        }
                    });
                }

                String sled = carrierSettingDTO.getSled();
                //Toast.makeText(getApplicationContext(),"Lock: " + sled, Toast.LENGTH_SHORT).show();

                if (sled.equals("0")) {
                    switchLed.setText("Led: OFF");
                    switchLed.setChecked(false);
                    switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("sled").setValue("1");
                        }
                    });
                }
                if (sled.equals("1")) {
                    switchLed.setText("Led: ON");
                    switchLed.setChecked(true);
                    switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_carrier.child(mac).child("carrierSetting").child("sled").setValue("0");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                //init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}