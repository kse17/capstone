package com.elecom.smartcarrier.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.ui.group.GroupAddActivity;
import com.elecom.smartcarrier.ui.group.GroupFragment;

public class MySetActivity extends AppCompatActivity {
    Button btn_delete;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        btn_delete = (Button)findViewById(R.id.btn_delete);
    }

    public void onAdd(View v){
        Intent intent = new Intent(MySetActivity.this, MyFragment.class);
        startActivity(intent);
    }

    public void onClick(Button btn_delete, int witch){

    }
}
