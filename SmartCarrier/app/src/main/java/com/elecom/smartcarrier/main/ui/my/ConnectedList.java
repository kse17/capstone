package com.elecom.smartcarrier.main.ui.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.dto.CarrierControlDTO;
import com.elecom.smartcarrier.dto.CarrierDTO;
import com.elecom.smartcarrier.dto.CarrierOperationDTO;
import com.elecom.smartcarrier.dto.CarrierSettingDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.elecom.smartcarrier.common.DefineValue.REQ_BLUETOOTH;

public class ConnectedList extends AppCompatActivity {

    private static final String TAG = "ConnectedList";

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = firebaseDatabase.getReference("users");
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");
    final DatabaseReference table_carrierUsers = firebaseDatabase.getReference("carrierUsers");
    final DatabaseReference table_userCarriers = firebaseDatabase.getReference("userCarriers");

    //BluetoothAdapter
    private BluetoothAdapter mBluetoothAdapter;

    FirebaseUser user;

    //UI
    Button btnSearch;
    CheckBox chkFindme;
//    //페어링된 리스트 주석처리 ***
//    ListView listPaired;
    ListView listDevice;

    //Adapter
    SimpleAdapter adapterPaired;
    SimpleAdapter adapterDevice;

    //list - Device 목록 저장
    List<Map<String, String>> dataPaired;
    List<Map<String, String>> dataDevice;
    List<BluetoothDevice> bluetoothDevices;
    List<BluetoothDevice> pairedDevices;
    int selectDevice;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_list);
        context = this;

        user = FirebaseAuth.getInstance().getCurrentUser();

        //UI
        chkFindme = findViewById(R.id.chkFindme);
        btnSearch = findViewById(R.id.btnSearch);
//        //페어링된 리스트 주석처리 ***
//        listPaired = findViewById(R.id.listPaired);
        listDevice = findViewById(R.id.listDevice);

        //Adapter1
        dataPaired = new ArrayList<>();
        adapterPaired = new SimpleAdapter(this, dataPaired, android.R.layout.simple_list_item_2, new String[]{"cname", "mac"}, new int[]{android.R.id.text1, android.R.id.text2});
//        //페어링된 리스트 주석처리 ***
//        listPaired.setAdapter(adapterPaired);
        //Adapter2
        dataDevice = new ArrayList<>();
        adapterDevice = new SimpleAdapter(this, dataDevice, android.R.layout.simple_list_item_2, new String[]{"cname", "mac"}, new int[]{android.R.id.text1, android.R.id.text2});
        listDevice.setAdapter(adapterDevice);

        //검색된 블루투스 디바이스 데이터
        bluetoothDevices = new ArrayList<>();
        pairedDevices = new ArrayList<>();
        //선택한 디바이스 없음
        selectDevice = -1;

        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStateReceiver, stateFilter);

        //리시버2
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //리시버3
        IntentFilter scanmodeFilter = new IntentFilter();
        scanmodeFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBluetoothScanmodeReceiver, scanmodeFilter);

        //블루투스 지원 유무 확인
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //2. 블루투스가 꺼져있으면 사용자에게 활성화 요청하기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_BLUETOOTH);
        } else {
            GetListPairedDevice();
        }

        //검색된 디바이스목록 클릭시 페어링 요청
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bluetoothDevices.get(position);
                try {
                    //선택한 디바이스 페어링 요청
                    Method method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                    selectDevice = position;

                    registerCarrier(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //블루투스를 지원하지 않으면 null을 리턴한다
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    //블루투스 검색 버튼 클릭
    public void mOnBluetoothSearch(View v) {
        if (true) Log.d("data", "doDiscovery()");
        Toast.makeText(ConnectedList.this, "블루투스 기기 검색 중", Toast.LENGTH_LONG).show();
        //검색버튼 비활성화
        btnSearch.setEnabled(false);
        //mBluetoothAdapter.isDiscovering() : 블루투스 검색중인지 여부 확인
        //mBluetoothAdapter.cancelDiscovery() : 블루투스 검색 취소
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //mBluetoothAdapter.startDiscovery() : 블루투스 검색 시작
        mBluetoothAdapter.startDiscovery();
    }

    //블루투스 상태변화 BroadcastReceiver
    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            //블루투스 활성화
            if (state == BluetoothAdapter.STATE_ON) {
                Toast.makeText(ConnectedList.this, "블루투스 활성화", Toast.LENGTH_SHORT).show();
            }
            //블루투스 비활성화
            else if (state == BluetoothAdapter.STATE_OFF) {
                Toast.makeText(ConnectedList.this, "블루투스 비활성화", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //블루투스 검색결과 BroadcastReceiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.i("data", "확인");
                    Map map = new HashMap();
                    map.put("cname", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                    map.put("mac", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                    dataDevice.add(map);
                    bluetoothDevices.add(device);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                btnSearch.setEnabled(true);
            }

            //리스트 목록갱신
            adapterDevice.notifyDataSetChanged();
        }
    };

    //블루투스 검색응답 모드 BroadcastReceiver
    BroadcastReceiver mBluetoothScanmodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            switch (state) {
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                case BluetoothAdapter.SCAN_MODE_NONE:
                    chkFindme.setChecked(false);
                    chkFindme.setEnabled(true);
                    Toast.makeText(ConnectedList.this, "검색응답 모드 종료", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Toast.makeText(ConnectedList.this, "다른 블루투스 기기에서 내 휴대폰을 찾을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //검색응답 모드 - 블루투스가 외부 블루투스의 요청에 답변하는 슬레이브 상태
    //BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE : 검색응답 모드 활성화 + 페이지 모드 활성화
    //BluetoothAdapter.SCAN_MODE_CONNECTABLE : 검색응답 모드 비활성화 + 페이지 모드 활성화
    //BluetoothAdapter.SCAN_MODE_NONE : 검색응답 모드 비활성화 + 페이지 모드 비활성화
    //검색응답 체크박스 클릭
    public void mOnChkFindme(View v) {
        //검색응답 체크
        if (chkFindme.isChecked()) {
            //검색응답 모드가 활성화이면 하지 않음
            if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                //60초 동안 상대방이 나를 검색할 수 있도록한다
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                startActivity(intent);
            }
        }
    }

    //이미 페어링된 목록 가져오기
    public void GetListPairedDevice() {
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        dataPaired.clear();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                //데이터 저장
                Map map = new HashMap();
                map.put("cname", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                map.put("mac", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                dataPaired.add(map);
                pairedDevices.add(device);
            }
        }
        //리스트 목록갱신
        adapterPaired.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_BLUETOOTH:
                //블루투스 활성화 승인
                if (resultCode == Activity.RESULT_OK) {
                    GetListPairedDevice();
                }
                //블루투스 활성화 거절
                else {
                    Toast.makeText(this, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
        }
    }

    // 정보 확인 후 firebase에 데이터 저장
    public void registerCarrier(BluetoothDevice device) {
        Log.d(TAG, "register");

        //showProgressDialog();
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please Wait");
        mDialog.show();

        table_carrier.child(device.getAddress()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e(TAG, "Error getting data", task.getException());
                } else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                }

                if (task.getResult().getValue() == null) {
                    // 캐리어 정보 db 저장
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),device.getAddress() + " go go~",Toast.LENGTH_SHORT).show();
                    writeNewCarrier(user, device);
                } else {
                    // 이미 등록된 맥주소일 경우
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"This mac(" + device.getAddress() +") is already registered",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeNewCarrier(FirebaseUser user, BluetoothDevice device){
        // CarrierDTO 입력한 정보 저장
        CarrierDTO carrierDTO = new CarrierDTO(device.getAddress(), device.getName(), user.getUid(),"0","0",0,0);
        CarrierControlDTO carrierControlDTO = new CarrierControlDTO("0", "0", "0");
        CarrierSettingDTO carrierSettingDTO = new CarrierSettingDTO("0","0","0");
        CarrierOperationDTO carrierOperationDTO = new CarrierOperationDTO("0","0", "0");

        // DB에 캐리어 정보 저장
        table_carrier.child(device.getAddress()).setValue(carrierDTO);
        table_carrier.child(device.getAddress()).child("carrierSetting").setValue(carrierSettingDTO);
        table_carrier.child(device.getAddress()).child("carrierOperation").setValue(carrierOperationDTO);
        table_carrier.child(device.getAddress()).child("carrierControl").setValue(carrierControlDTO);

        // 그룹 테스트
        // 트랜잭션 이용해야되나?
        // https://forest71.tistory.com/177?category=673494 참고

        Toast.makeText(getApplicationContext(),"Carrier registration is complete: " + carrierDTO.toString(),Toast.LENGTH_SHORT).show();

    }

}