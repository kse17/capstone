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
import com.google.firebase.firestore.auth.User;

import java.util.Iterator;


public class SignUpActivity extends AppCompatActivity {
    EditText et_id;
    EditText et_password;
    EditText et_phone;
    EditText et_password_ck;
    Button btn_register;
    private DatabaseReference table_user;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_id = (EditText)findViewById(R.id.et_id);
        et_password = (EditText)findViewById(R.id.et_password);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_password_ck = (EditText)findViewById(R.id.et_password_ck);
        btn_register = (Button)findViewById(R.id.btn_register);

        //in it firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //데이터 베이스 User라는 인스턴스 갖기
        final DatabaseReference table_user = database.getReference("User");

        //register button click -> 입력한 정보 확인 후 firebase에 데이터 저장
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                 mDialog.setMessage("Please Waiting");
                 mDialog.show();

                 String LenID = et_id.getText().toString();
                 String LenPassword = et_password.getText().toString();
                 String LenPasswordCK = et_password_ck.getText().toString();
                 String LenPhone = et_phone.getText().toString();

                 //공백칸이 존재하는데 register 버튼을 클릭했을 때
                 if (LenID.getBytes().length <= 0 || LenPassword.getBytes().length <= 0 || LenPasswordCK.getBytes().length <= 0 || LenPhone.getBytes().length <=0)
                 {
                     mDialog.dismiss();
                     Toast.makeText(SignUpActivity.this,"빈 칸을 입력하세요",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     table_user.addValueEventListener(new ValueEventListener() {

                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             //이미 등록된 아이디일 경우
                             if (dataSnapshot.child(et_id.getText().toString()).exists()) {
                                 mDialog.dismiss();
                                 Toast.makeText(SignUpActivity.this, "This id is already registered", Toast.LENGTH_SHORT).show();
                             }
                             //회원 정보 db 저장
                             else {
                                 if ((et_password.getText().toString()).equals(et_password_ck.getText().toString())) {
                                     mDialog.dismiss();
                                     //userDTO 입력한 정보 저장
                                     UserDTO user = new UserDTO(et_id.getText().toString(), et_password.getText().toString(), et_phone.getText().toString());
                                     //db에 유저 정보 저장
                                     table_user.child(et_id.getText().toString()).setValue(user);
                                     Toast.makeText(SignUpActivity.this, "Sign up is complete", Toast.LENGTH_SHORT).show();
                                     finish();
                                 }
                                 //비밀번호와 비밀번호 확인이 일치하지않는 경우
                                 else {
                                     mDialog.dismiss();
                                     Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {
                         }
                     });
                 }
            }
        });
    }

    public void onRegister(View v){
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}
