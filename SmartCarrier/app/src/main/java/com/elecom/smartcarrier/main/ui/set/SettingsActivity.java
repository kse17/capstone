package com.elecom.smartcarrier.main.ui.set;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    //firebaseDatabase = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
                init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // 인증이 되어 있는 상태
            setContentView(R.layout.activity_settings);
            populateAccount();
            //populateIdpToken();

            //updateMainView();
        } else {
            // 인증이 되어 있지 않은 상태
        }

    }

    private void populateAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView txtEmail = (TextView)findViewById(R.id.email_title);
        txtEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());

        TextView txtUname = (TextView)findViewById(R.id.uname_title);
        txtUname.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());
    }
}
