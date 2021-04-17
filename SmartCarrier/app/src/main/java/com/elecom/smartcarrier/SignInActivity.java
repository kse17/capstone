package com.elecom.smartcarrier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    Button btn_login, btn_register;
    EditText et_id, et_password;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText)findViewById(R.id.et_password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please wating");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //db에 유저 id가 존재하는 경우
                        if(dataSnapshot.child(et_id.getText().toString()).exists()){
                          mDialog.dismiss();
                          //입력한 id로 database에서 일치하는 id를 가진 유저의 정보를 가져옴
                          UserDTO user = dataSnapshot.child(et_id.getText().toString()).getValue(UserDTO.class);
                          //비밀번호 일치하는 경우 -> 로그인 완료
                          if(user.getpassword().equals(et_password.getText().toString())){
                              Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                              startActivity(intent);
                              //finish();
                          }
                          //비밀번호가 일치하지않는 경우
                          else{
                              Toast.makeText(SignInActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                          }
                        }
                        //id가 database에 존재하지않는 경우
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "This id does not exitst", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void onLogin(View v){
        //Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        //startActivity(intent);
    }
    public void onRegister(View v){
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}
