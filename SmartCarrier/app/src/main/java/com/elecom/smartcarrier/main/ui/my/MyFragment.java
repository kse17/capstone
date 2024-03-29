package com.elecom.smartcarrier.main.ui.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;
import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_USER;
import static com.elecom.smartcarrier.common.DefineValue.REQ_BLUETOOTH;
import static com.elecom.smartcarrier.common.PreferenceManager.getStringPreference;

public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    private Context context;
    //ArrayList<MyData> myDataList = new ArrayList<MyData>();

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");
    final DatabaseReference table_user = firebaseDatabase.getReference("users");
    final DatabaseReference table_carrierUsers = firebaseDatabase.getReference("carrierUsers");
    final DatabaseReference table_userCarriers = firebaseDatabase.getReference("userCarriers");


    //BluetoothAdapter
    BluetoothAdapter mBluetoothAdapter;

    //UI
    int selectDevice;

    List<BluetoothDevice> bluetoothDevices;
    List<BluetoothDevice> pairedDevices;
    List<Map<String, String>> dataPaired;
    List<Map<String, String>> dataDevice;
    List<Map<String, String>> carrierList;

    SimpleAdapter adapterPaired;
    SimpleAdapter adapterDevice;

    public MyFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        View root = inflater.inflate(R.layout.fragment_my, container, false);

        // 컨텍스트 메뉴
        ListView listMy = (ListView) root.findViewById(R.id.list_my);
        // ListView 를 Context 메뉴로 등록
        registerForContextMenu(listMy);


        //검색된 블루투스 디바이스 데이터
        bluetoothDevices = new ArrayList<>();
        pairedDevices = new ArrayList<>();
        //선택한 디바이스 없음
        selectDevice = -1;

        //블루투스 지원 유무 확인
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        dataPaired = new ArrayList<>();
        carrierList = new ArrayList<>();
        adapterPaired = new SimpleAdapter(getActivity(), carrierList, android.R.layout.simple_list_item_2, new String[]{"cname", "mac"}, new int[]{android.R.id.text1, android.R.id.text2});
        //    페어링된 리스트 주석처리 ***
        listMy.setAdapter(adapterPaired);

        // 블루투스를 지원하지 않으면 null을 리턴한다
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            //return;
        }

        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        context.registerReceiver(mBluetoothStateReceiver, stateFilter);

        //2. 블루투스가 꺼져있으면 사용자에게 활성화 요청하기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_BLUETOOTH);
        } else {
            getListCarrier();
        }

        // ADD 화면 전환
        Button btnAdd = (Button) root.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ConnectedList.class);
                startActivity(intent);
                //show();
            }
        });

        return root;
    }

    // Context 메뉴로 등록한 View가 처음 클릭되어 만들어질 때 호출되는 메소드
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_my, menu);
    }

    // Context 메뉴로 등록한 View가 클릭되었을 때 자동으로 호출되는 메소드
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        switch (item.getItemId()){
            case R.id.menu_default:
                PreferenceManager.setStringPreference(context, DefineValue.PREFERENCE_DEFAULT_CARRIER, carrierList.get(index).get("mac"));
                Toast.makeText(context,getStringPreference(context, DefineValue.PREFERENCE_DEFAULT_CARRIER, "default_carrier"),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_set:
                Toast.makeText(context,"Carrier Set: " + carrierList.get(index).get("mac"),Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,pairedDevices.get(index) + "Carrier Set",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MySetActivity.class);
                intent.putExtra("mac", carrierList.get(index).get("mac"));
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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


    public void getListCarrier() {

        carrierList.clear();
        table_userCarriers.child(PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_USER, "default_user"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot carrierSnapshot : snapshot.getChildren()) {
                            Toast.makeText(context, carrierSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                            // 데이터 저장
                            Map<String, String> map = new HashMap();

                            table_carrier.child(carrierSnapshot.getKey()).child("cname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "Error getting data", task.getException());
                                    } else {
                                        Log.d(TAG, String.valueOf(task.getResult().getValue()));
                                        map.put("cname", String.valueOf(task.getResult().getValue()));
                                    }
                                }
                            });

                            table_carrier.child(carrierSnapshot.getKey()).child("mac").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e(TAG, "Error getting data", task.getException());
                                    } else {
                                        Log.d(TAG, String.valueOf(task.getResult().getValue()));
                                        map.put("mac", String.valueOf(task.getResult().getValue()));

                                        carrierList.add(map);

                                        // 리스트 목록갱신
                                        adapterPaired.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_BLUETOOTH:
                //블루투스 활성화 승인
                if (resultCode == Activity.RESULT_OK) {
                    getListCarrier();
                }
                //블루투스 활성화 거절
                else {
                    Toast.makeText(context, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                break;
        }
    }
    //블루투스 상태변화 BroadcastReceiver
    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            //블루투스 활성화
            if (state == BluetoothAdapter.STATE_ON) {
                Log.i("bt", "블루투스 활성화");
            }
            //블루투스 활성화 중
            else if (state == BluetoothAdapter.STATE_TURNING_ON) {
                Log.i("bt", "블루투스 활성화중...");
            }
            //블루투스 비활성화
            else if (state == BluetoothAdapter.STATE_OFF) {
                Log.i("bt", "블루투스 활성화");
            }
            //블루투스 비활성화 중
            else if (state == BluetoothAdapter.STATE_TURNING_OFF) {
                Log.i("bt", "블루투스 활성화중...");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBluetoothStateReceiver);
    }


}