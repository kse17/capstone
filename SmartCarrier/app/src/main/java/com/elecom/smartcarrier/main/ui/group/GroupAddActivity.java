package com.elecom.smartcarrier.main.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.main.ui.my.ConnectedList;

public class GroupAddActivity extends AppCompatActivity {
    EditText emailEt;
    Button addBtn;
    private String emailTxt;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_info);

        emailEt = (EditText)findViewById(R.id.et_email);
        addBtn = (Button)findViewById(R.id.btn_add_member);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailTxt = emailEt.getText().toString();
                Toast.makeText(getApplicationContext(), emailTxt, Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(GroupAddActivity.this, GroupFragment.class);
                startActivity(intent);
            }
        });
    }

}
