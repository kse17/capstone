package com.elecom.smartcarrier.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.ui.group.GroupAddActivity;
import com.elecom.smartcarrier.ui.group.GroupFragment;
import com.elecom.smartcarrier.ui.my.MyAddActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeLockPassword extends AppCompatActivity {
    ToggleButton btn_lock;
    Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lock_password);
        Button btn_lockOn = (Button)findViewById(R.id.btn_lockOn);
        Button btn_lockOff = (Button)findViewById(R.id.btn_lockOff);
        Button btn_ok = (Button)findViewById(R.id.btn_ok);

        //in it firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database의 Carrier라는 인스턴스 갖기
        final DatabaseReference table_carrier = database.getReference("Carrier");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //@Override
    //public void onBackPressed() {
        //안드로이드 백버튼 막기
    //    return;
    //}

}
