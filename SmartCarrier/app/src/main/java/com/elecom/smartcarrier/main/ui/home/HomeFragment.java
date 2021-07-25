package com.elecom.smartcarrier.main.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.elecom.smartcarrier.dto.CarrierControlDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    private Context context;

    public  Button btnMap;

    private Button btnOn;
    private Button btnOff;

    private Button btnOpen;
    private Button btnClose;

    private ImageView imgState;
    private ImageView imgLock;

    private TextView txtState;
    private TextView txtLock;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnMap = (Button)root.findViewById(R.id.btn_map);

        btnOn = (Button)root.findViewById(R.id.btn_buzzer_on);
        btnOff = (Button)root.findViewById(R.id.btn_buzzer_off);

        btnOpen = (Button)root.findViewById(R.id.btn_lock_open);
        btnClose = (Button)root.findViewById(R.id.btn_lock_close);

        imgState = (ImageView)root.findViewById(R.id.img_state);
        imgLock = (ImageView)root.findViewById(R.id.img_lock);

        txtState = (TextView)root.findViewById(R.id.text_state);
        txtLock = (TextView)root.findViewById(R.id.text_lock);


        table_carrier.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default"))
                .child("carrierControl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Use The Model Class To Get The Data
                    final CarrierControlDTO carrierControlDTO = dataSnapshot.getValue(CarrierControlDTO.class);

                    String lock = carrierControlDTO.getLock();

                    if (lock.equals("0")) {
                        txtLock.setText(R.string.lock_closed);
                        imgLock.setImageResource(R.drawable.ic_outline_lock_24);
                    }
                    if (lock.equals("1")) {
                        txtLock.setText(R.string.lock_opened);
                        imgLock.setImageResource(R.drawable.ic_outline_lock_open_24);
                    }

                    String open = carrierControlDTO.getOpen();

                    if (open.equals("0")) {
                        txtState.setText(R.string.state_closed);
                        imgState.setImageResource(R.drawable.ic_carrier_green);
                    }
                    if (open.equals("1")) {
                        txtState.setText(R.string.state_opened);
                        imgState.setImageResource(R.drawable.ic_carrier_red);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // GPS 클릭
        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        // ON 클릭
        btnOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default"))
                        .child("carrierControl").child("buzzer").setValue("1");
            }
        });

        // OFF 클릭
        btnOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default"))
                        .child("carrierControl").child("buzzer").setValue("0");
            }
        });

        // OPEN 클릭
        btnOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default"))
                        .child("carrierControl").child("lock").setValue("1");
            }
        });

        // CLOSE 클릭
        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default"))
                        .child("carrierControl").child("lock").setValue("0");
            }
        });



    }
}