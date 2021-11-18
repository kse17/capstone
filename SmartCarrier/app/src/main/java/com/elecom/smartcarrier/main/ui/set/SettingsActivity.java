package com.elecom.smartcarrier.main.ui.set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.dto.UserDTO;
import com.elecom.smartcarrier.dto.UserSettingDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = firebaseDatabase.getReference("users");

    private TextView txtEmail;
    private TextView txtUName;

    private String uid;

    private Switch switchOpen;
    private Switch switchDistance;
    private Switch switchGroup;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        txtEmail = (TextView) findViewById(R.id.email_title);
        txtUName = (TextView) findViewById(R.id.uname_title);

        switchOpen = (Switch) findViewById(R.id.open_title);
        switchDistance = (Switch) findViewById(R.id.distance_title);
        switchGroup = (Switch) findViewById(R.id.group_title);
    }


    protected void onStart() {
        super.onStart();

        table_user.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use The Model Class To Get The Data
                final UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);

                txtEmail.setText(userDTO.getEmail());
                txtUName.setText(userDTO.getUname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        table_user.child(uid).child("userSetting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Use The Model Class To Get The Data
                //final UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                final UserSettingDTO userSettingDTO = dataSnapshot.getValue(UserSettingDTO.class);

                Log.d(TAG, "changed: " + dataSnapshot.getValue());

                String sopen = userSettingDTO.getSopen();
                Toast.makeText(getApplicationContext(), "Open: " + sopen, Toast.LENGTH_SHORT).show();

                if (sopen.equals("0")) {
                    switchOpen.setText("Open and Close: OFF");
                    switchOpen.setChecked(false);
                    switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sopen").setValue("1");
                            //Toast.makeText(getApplicationContext(),"Open and Close: " + sopen, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (sopen.equals("1")) {
                    switchOpen.setText("Open and Close: ON");
                    switchOpen.setChecked(true);
                    switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sopen").setValue("0");
                            //Toast.makeText(getApplicationContext(),"Open and Close: " + sopen, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String sdist = userSettingDTO.getSdist();
                Toast.makeText(getApplicationContext(), "Distance: " + sdist, Toast.LENGTH_SHORT).show();

                if (sdist.equals("0")) {
                    switchDistance.setText("Distance: OFF");
                    switchDistance.setChecked(false);
                    switchDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sdist").setValue("1");
                            //Toast.makeText(getApplicationContext(),"Distance: " + sdist, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (sdist.equals("1")) {
                    switchDistance.setText("Distance: ON");
                    switchDistance.setChecked(true);
                    switchDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sdist").setValue("0");
                            //Toast.makeText(getApplicationContext(),"Distance: " + sdist, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String sgroup = userSettingDTO.getSgroup();
                Toast.makeText(getApplicationContext(), "Open: " + sgroup, Toast.LENGTH_SHORT).show();

                if (sgroup.equals("0")) {
                    switchGroup.setText("Group: OFF");
                    switchGroup.setChecked(false);
                    switchGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sgroup").setValue("1");
                            //Toast.makeText(getApplicationContext(),"Group: " + sgroup, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (sgroup.equals("1")) {
                    switchGroup.setText("Group: ON");
                    switchGroup.setChecked(true);
                    switchGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            table_user.child(uid).child("userSetting").child("sgroup").setValue("0");
                            //Toast.makeText(getApplicationContext(),"Group: " + sgroup, Toast.LENGTH_SHORT).show();
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
        switch (item.getItemId()) {
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
