package com.elecom.smartcarrier.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.ActivityHandler;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.elecom.smartcarrier.dto.UserDTO;
import com.elecom.smartcarrier.dto.UserSettingDTO;
import com.elecom.smartcarrier.init.InitStepHandler;
import com.elecom.smartcarrier.init.PermissionActivity;
import com.elecom.smartcarrier.server.analytics.AnalyticsHelper;
import com.elecom.smartcarrier.main.ui.set.SettingsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.elecom.smartcarrier.common.PreferenceManager.getStringPreference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = firebaseDatabase.getReference("users");

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityHandler.OpenActivity(this, InitStepHandler.getCurrentInitState(this));
        if(InitStepHandler.getCurrentInitState(this) == DefineValue.INIT_STATE_FINISH) {
            //Analytics 수집
            AnalyticsHelper analyticsHelper = new AnalyticsHelper();
            analyticsHelper.initFirebaseAnalytics(this);

            if(!PermissionActivity.isLocationServicesStatus(this) ||
                    !PermissionActivity.isLocationPermissins(this)) {
                ActivityHandler.OpenActivity(this, DefineValue.INIT_STATE_PERMISSION);
                analyticsHelper.setUserProperty(DefineValue.FA_USER_PROPERTY_PERMISSION,
                        DefineValue.FA_USER_PROPERTY_PERMISSION_NO);
                return;
            }
            analyticsHelper.setUserProperty(DefineValue.FA_USER_PROPERTY_PERMISSION,
                    DefineValue.FA_USER_PROPERTY_PERMISSION_OK);

            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DefineValue.REQ_GUIDE:
                InitStepHandler.nextInitState(this, DefineValue.INIT_STATE_PERMISSION);
                break;
            case DefineValue.REQ_PERMISSION:
                if(resultCode == Activity.RESULT_CANCELED) {
                    finish();
                } else {
                    init();
                    InitStepHandler.nextInitState(this, DefineValue.INIT_STATE_FINISH);
                }
                break;
            case DefineValue.REQ_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    // Successfully signed in.
                    Log.d(TAG, "Successfully signed in.");
                    Toast.makeText(this,"Successfully signed in.",Toast.LENGTH_SHORT).show();
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_DEFAULT_USER, user.getUid());
                    PreferenceManager.setStringPreference(this, DefineValue.PREFERENCE_DEFAULT_CARRIER, "default_carrier");
                    Toast.makeText(this,getStringPreference(this, DefineValue.PREFERENCE_DEFAULT_USER, "default_user"),Toast.LENGTH_SHORT).show();

                    signUp();
                } else {
                    // Sign in failed.
                    Log.d(TAG, "Sign in failed. " + response.getError().getErrorCode());
                    Toast.makeText(this,"Sign in failed.",Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actSetting = menu.findItem(R.id.action_settings);
        MenuItem actSignIn = menu.findItem(R.id.action_sign_in);
        MenuItem actSignOut = menu.findItem(R.id.action_sign_out);
        MenuItem actDeleteAccount = menu.findItem(R.id.action_delete_account);
        MenuItem actClearData = menu.findItem(R.id.action_clear_data);

        if (user != null) {
            // 인증이 되어 있는 상태
            actSetting.setEnabled(true);
            actSignIn.setEnabled(false);
            actSignOut.setEnabled(true);
            actDeleteAccount.setEnabled(true);
            actClearData.setEnabled(true);
        } else {
            // 인증이 되어 있지 않은 상태
            actSetting.setEnabled(false);
            actSignIn.setEnabled(true);
            actSignOut.setEnabled(false);
            actDeleteAccount.setEnabled(false);
            actClearData.setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("uid", user.getUid());
                startActivity(intent);
                return true;
            case R.id.action_sign_in:
                Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
                signIn();
                return true;
            case R.id.action_sign_out:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            case R.id.action_delete_account:
                Toast.makeText(this, "Delete User", Toast.LENGTH_SHORT).show();
                deleteAccountSelected();
                return true;
            case R.id.action_clear_data:
                Toast.makeText(this, "Clear Data", Toast.LENGTH_SHORT).show();
                clearApplicationData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        updateMainView();
    }

    private void updateMainView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my, R.id.nav_log, R.id.nav_group)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

   // 인증 요청
    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())                           // Theme 설정
                        .setLogo(getSelectedLogo())                             // 로고 설정
                        .setAvailableProviders(getSelectedProviders())          // Providers 설정
                        .setTosAndPrivacyPolicyUrls("https://naver.com",
                                "https://google.com")
                        .setIsSmartLockEnabled(true)                            // SmartLock 설정
                        .build(),
                DefineValue.REQ_SIGN_IN);
    }

    // FirebaseUI에 표시할 테마 정보
    private int getSelectedTheme() {
        // 기본 테마
        return AuthUI.getDefaultTheme();
    }

    //Firebase UI에 표시할 로고 이미지
    private int getSelectedLogo() {
        // 로고를 표시하지 않은 경우
        return AuthUI.NO_LOGO;
    }

    // FirebaseUI를 통해 제공 받을 인증 서비스 설정
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder().build());
        return selectedProviders;
    }

    // 정보 확인 후 firebase에 데이터 저장
    public void signUp() {
        Log.d(TAG, "register");

        //showProgressDialog();
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please Wait");
        mDialog.show();

        table_user.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                } else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                }

                if (task.getResult().getValue() == null) {
                    // 회원 정보 db 저장
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),user.getUid() + " go go~",Toast.LENGTH_SHORT).show();
                    writeNewUser(user);
                    // 액티비티 종료
                    //finish();
                } else {
                    // 이미 등록된 아이디일 경우
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"This id(" + user.getUid() +") is already registered",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeNewUser(FirebaseUser user){
        // UserDTO 입력한 정보 저장
        UserDTO userDTO = new UserDTO(user.getUid(), user.getEmail(), user.getDisplayName());
        UserSettingDTO userSettingDTO = new UserSettingDTO("1", "1", "1");

        // DB에 유저 정보 저장
        table_user.child(user.getUid()).setValue(userDTO);
        table_user.child(user.getUid()).child("userSetting").setValue(userSettingDTO);
        Toast.makeText(this,"Sign up is complete",Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                        }
                    }
                });
    }

    private void deleteAccountSelected() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"OK Click",Toast.LENGTH_SHORT).show();
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .create();

        dialog.show();
    }

    private void deleteAccount() {
        // Prompt the user to enter their email and password
        String password = "@@@@@@@";

        // Get auth credentials from the user for re-authentication.
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "User re-authenticated.");
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        Toast.makeText(getApplicationContext(),"Delete account is success",Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        //startActivity(intent);
                                        deleteUserData(user);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Delete account is failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
        });


    }

    // 정보 확인 후 firebase의 데이터 삭제
    public void deleteUserData(FirebaseUser user) {
        Log.d(TAG, "Delete User Data");

        table_user.child(user.getUid()).removeValue();
        Toast.makeText(this,"Delete user data is complete",Toast.LENGTH_SHORT).show();
    }

    // 데이터 삭제
    public void clearApplicationData() {
        File cache = getCacheDir();
        try {
        } catch (Exception e) {
        }
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib") && !(s.equals("shared_prefs"))) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir.delete();
    }

}