package com.elecom.smartcarrier.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;

public class MyAddActivity extends AppCompatActivity {

    Button btn_add;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carrier_info);
        btn_add = (Button)findViewById(R.id.btn_add);
    }

    public void onAdd(View v){
        Intent intent = new Intent(MyAddActivity.this, MyFragment.class);
        startActivity(intent);
    }
}
