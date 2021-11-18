package com.elecom.smartcarrier.main.ui.group;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.DefineValue;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.elecom.smartcarrier.main.ui.my.finger.AddFPActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;
import static com.elecom.smartcarrier.common.PreferenceManager.getStringPreference;

public class GroupFragment extends Fragment {

    private static final String TAG = "GroupFragment";

    private Context context;
    //ArrayList<GroupData> groupDataList = new ArrayList<GroupData>();

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = firebaseDatabase.getReference("users");
    final DatabaseReference table_carrierUsers = firebaseDatabase.getReference("carrierUsers");

    List<Map<String, String>> userList;
    SimpleAdapter adapter;

    public GroupFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        View root = inflater.inflate(R.layout.fragment_group, container, false);

        ListView listGroup = (ListView) root.findViewById(R.id.list_group);
        registerForContextMenu(listGroup);

        userList = new ArrayList<>();
        adapter = new SimpleAdapter(getActivity(), userList, android.R.layout.simple_list_item_2, new String[]{"uname", "email"}, new int[]{android.R.id.text1, android.R.id.text2});
        listGroup.setAdapter(adapter);

        getListUser();

        // ADD 화면 전환
        Button btnAdd = (Button) root.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GroupAddActivity.class);
                startActivity(intent);
                //show();
            }
        });

        return root;
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("AlertDialog Title");
        builder.setMessage("AlertDialog Content");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_group, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menu_delete:
                show();
                //삭제
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void getListUser() {

        userList.clear();
        table_carrierUsers.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default_carrier"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Toast.makeText(context, userSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                            // 데이터 저장
                            Map<String, String> map = new HashMap();

                            table_user.child(userSnapshot.getKey()).child("uname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "Error getting data", task.getException());
                                    } else {
                                        Log.d(TAG, String.valueOf(task.getResult().getValue()));
                                        map.put("uname", String.valueOf(task.getResult().getValue()));
                                    }
                                }
                            });

                            table_user.child(userSnapshot.getKey()).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "Error getting data", task.getException());
                                    } else {
                                        Log.d(TAG, String.valueOf(task.getResult().getValue()));
                                        map.put("email", String.valueOf(task.getResult().getValue()));

                                        userList.add(map);

                                        // 리스트 목록갱신
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }

}