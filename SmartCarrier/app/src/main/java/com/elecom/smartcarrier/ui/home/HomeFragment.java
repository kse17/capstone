package com.elecom.smartcarrier.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.ui.group.GroupAddActivity;

public class HomeFragment extends Fragment {

    private Context context;
    private HomeViewModel homeViewModel;
    public HomeFragment(){

    }
    ToggleButton btn_lock;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_lockOn = (Button)root.findViewById(R.id.btn_lockOn);
        Button btn_lockOff = (Button)root.findViewById(R.id.btn_lockOff);

        //잠금 on 클릭 시 비밀번호 입력 창 열기
        btn_lockOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeLockPassword.class);
                startActivity(intent);
            }
        });
        //잠금 off 클릭 시 비밀번호 입력 창 열기
        btn_lockOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeLockPassword.class);
                startActivity(intent);
            }
        });
        return root;
    }


}