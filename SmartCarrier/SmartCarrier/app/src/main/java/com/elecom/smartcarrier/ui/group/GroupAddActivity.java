package com.elecom.smartcarrier.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;

public class GroupAddActivity extends AppCompatActivity {
    Button btn_add;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_info);
        btn_add = (Button)findViewById(R.id.btn_add);
    }

    public void onAdd(View v){
        Intent intent = new Intent(GroupAddActivity.this, GroupFragment.class);
        startActivity(intent);
    }

}
