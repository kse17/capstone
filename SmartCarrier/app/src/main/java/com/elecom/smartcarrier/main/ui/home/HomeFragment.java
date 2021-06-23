package com.elecom.smartcarrier.main.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    private Context context;

    private Button btnOpen;
    private Button btnClose;
    //ToggleButton btn_lock;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnOpen = (Button)root.findViewById(R.id.btn_lock_open);
        btnClose = (Button)root.findViewById(R.id.btn_lock_close);

        init();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // OPEN 클릭 시 비밀번호 입력 창 열기
        btnOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 테스트를 위해 바로 열리도록 수정
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), DefineValue.PREFERENCE_DEFAULT_CARRIER, "default")).child("lock").setValue("1");

                //Intent intent = new Intent(getActivity().getApplicationContext(), HomeLockPassword.class);
                //startActivity(intent);
            }
        });

        // CLOSE 클릭 시 비밀번호 입력 창 열기
        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 테스트를 위해 바로 닫히도록 수정
                table_carrier.child(PreferenceManager.getStringPreference(getContext(), DefineValue.PREFERENCE_DEFAULT_CARRIER, "default")).child("lock").setValue("0");

                //Intent intent = new Intent(getActivity().getApplicationContext(), HomeLockPassword.class);
                //startActivity(intent);
            }
        });

    }

    private void init() {
        getCarrierData();

        updateHomeView();
    }

    private void getCarrierData() {

    }

    private void updateHomeView() {

    }


}