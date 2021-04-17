package com.elecom.smartcarrier.ui.my;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elecom.smartcarrier.BluetoothUtils;
import com.elecom.smartcarrier.MainActivity;
import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.SignUpActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.elecom.smartcarrier.BluetoothUtils.REQUEST_ENABLE_BT;
import static com.elecom.smartcarrier.BluetoothUtils.REQUEST_FINE_LOCATION;
import static com.elecom.smartcarrier.BluetoothUtils.SCAN_PERIOD;
import static com.elecom.smartcarrier.BluetoothUtils.UUID_DKDK_SERVICE;

import java.util.ArrayList;
import java.util.List;

public class MyAddActivity extends AppCompatActivity {

    private final static String TAG = MyAddActivity.class.toString();

    private BluetoothLeScanner bleScanner_;
    private BluetoothGatt bleGatt_;
    private BluetoothAdapter bleAdapter_;

    private Handler scanHandler_;
    private ArrayList<BluetoothDevice> deviceList_;
    private ScanCallback scanCb_;

    private boolean bIsConnected_ = false;
    private boolean bIsScanning_ = false;

    Button btn_add;
    Button btn_add_carrier;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carrier_info);
        //블루투스
        BluetoothManager bleManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter_ = bleManager.getAdapter();

        btn_add = (Button)findViewById(R.id.btn_add);
        btn_add_carrier = (Button)findViewById(R.id.btn_add_carrier);

        //in it firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //데이터 베이스 User라는 인스턴스 갖기
        final DatabaseReference table_user = database.getReference("User");

        btn_add_carrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Toast.makeText(MyAddActivity.this, "블루투스 미지원 기기입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                //블루투스가 위치정보를 확인하기 때문에 위치정보권한확인 & 권한 요청
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestLocationPermission();
                        return;
                    }
                }
                //블루투스 설정이 켜져있는지 확인, 안켜져있으면 enableBLE()함수를 통해 블루투스 설정화면으로 이동
                if (bleAdapter_ == null || !bleAdapter_.isEnabled()) {
                    enableBLE();
                    return;
                }
                startScan();
            }
        });

    }

    //블루투스 연결 기기 스캔
    private  void startScan(){
        Toast.makeText(MyAddActivity.this, "Start Scan", Toast.LENGTH_SHORT).show();
        List<ScanFilter> filters= new ArrayList<>();
        ScanFilter scan_filter= new ScanFilter.Builder()
                .setServiceUuid( new ParcelUuid( UUID_DKDK_SERVICE ) ) //필터 값으로 고유 UUID값을 넣고
                .build();
        filters.add( scan_filter );

        ScanSettings settings= new ScanSettings.Builder()
                .setScanMode( ScanSettings.SCAN_MODE_LOW_POWER )
                .build();

        deviceList_ = new ArrayList<>();
        scanCb_ = new BLEScanCallback(deviceList_);
        bleScanner_ = bleAdapter_.getBluetoothLeScanner();
        bleScanner_.startScan( filters, settings, scanCb_);

        scanHandler_ = new Handler();
        scanHandler_.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
            }
        }, SCAN_PERIOD ); //약 20초간 스캔을 하면 자동으로 스캔 활동을 종료 하게끔 설정
    }

    private void stopScan() {
        Toast.makeText(MyAddActivity.this, "Stop Scan", Toast.LENGTH_SHORT).show();
        if( bIsScanning_ && bleAdapter_ != null && bleAdapter_.isEnabled() && bleScanner_ != null ) {
            bleScanner_.stopScan(scanCb_);
        }

        bIsScanning_ = false;
        scanCb_ = null;
        scanHandler_ = null;

        Log.d(TAG, "Scan is stopped.");
    }


    //블루투스 기기 스캔 종료
    private void scanFinished() {
        Toast.makeText(MyAddActivity.this, "Scan Finish", Toast.LENGTH_SHORT).show();

        for( BluetoothDevice _device : deviceList_) {
            ParcelUuid[] uuids = _device.getUuids();

            if (uuids != null)
                for (ParcelUuid uuid : uuids) {
                    Log.d( TAG, "device uuid: " + uuid.toString() );
                }

            if (_device.getAddress() != null)
                Log.d( TAG, "device address: " + _device.getAddress() );

            if (_device.getName() != null)
                Log.d( TAG, "device address: " + _device.getName() );

            connectDevice(_device);
            return;
        }
    }

    private void connectDevice( BluetoothDevice _device ) {
        GattClientCallback gattClientCb = new GattClientCallback();
        Log.d( TAG, "Try to connect " + _device.getName() );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bleGatt_= _device.connectGatt( this, false, gattClientCb, BluetoothDevice.TRANSPORT_LE);
        }
        else {
            bleGatt_= _device.connectGatt( this, false, gattClientCb);
        }

    }

    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange( BluetoothGatt _gatt, int _status, int _new_state ) {
            super.onConnectionStateChange( _gatt, _status, _new_state );
            if( _new_state == BluetoothProfile.STATE_CONNECTED ) {
                bIsConnected_ = true;
                Log.d( TAG, "Connected to the GATT server" );
                _gatt.discoverServices();
            } else if ( _new_state == BluetoothProfile.STATE_DISCONNECTED ) {
                Log.d( TAG, "status is STATE_DISCONNECTED" );
                disconnectGattServer();
            }
        }

        @Override
        public void onServicesDiscovered( BluetoothGatt _gatt, int _status ) {
            super.onServicesDiscovered( _gatt, _status );
            if( _status != BluetoothGatt.GATT_SUCCESS ) {
                Log.e( TAG, "Discovery failed, status: " + _status );
                return;
            }

            List<BluetoothGattCharacteristic> matching_characteristics = BluetoothUtils.findBLECharacteristics( _gatt );
            if( matching_characteristics.isEmpty() ) {
                Log.e( TAG, "failed to find characteristic" );
                disconnectGattServer();
                return;
            }
            Log.d( TAG, "Services discovery : success" );
        }

        @Override
        public void onCharacteristicChanged( BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic ) {
            super.onCharacteristicChanged( _gatt, _characteristic );

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic, int _status ) {
            super.onCharacteristicWrite( _gatt, _characteristic, _status );
            if( _status == BluetoothGatt.GATT_SUCCESS ) {
                Log.d( TAG, "onCharacteristicWrite : SUCCESS" );
            } else {
                Log.d( TAG, "onCharacteristicWrite : FAILED" );
                //disconnectGattServer();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic, int _status) {
            super.onCharacteristicRead(_gatt, _characteristic, _status);
            if (_status == BluetoothGatt.GATT_SUCCESS) {
                Log.d( TAG, "onCharacteristicRead : SUCCESS" );
                //readCharacteristic(characteristic);
            } else {
                Log.e( TAG, "Characteristic read unsuccessful, status: " + _status);

            }
        }

        private void readCharacteristic( BluetoothGattCharacteristic _characteristic ) {
            byte[] msg= _characteristic.getValue();
            Log.d( TAG, "read: " + msg.toString() );
        }
    }

    public void disconnectGattServer() {
        bIsConnected_ = false;

        if( bleGatt_ != null ) {
            bleGatt_.disconnect();
            bleGatt_.close();
        }
    }

    //스캔을 하다가 장치가 발견되면 BLEScanCallback  클래스의 콜백 함수가 호출.
    private class BLEScanCallback extends ScanCallback {

        private ArrayList<BluetoothDevice> _foundDevices;

        BLEScanCallback( ArrayList<BluetoothDevice>  _scanDeviceList ) {
            _foundDevices = _scanDeviceList;
        }

        @Override
        public void onScanResult( int _callback_type, ScanResult _result ) {
            addScanResult( _result );

            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    scanFinished();
                }
            }, 100 );
        }

        @Override
        public void onBatchScanResults( List<ScanResult> _results ) {
            for( ScanResult result: _results ) {
                addScanResult( result );
            }

            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    scanFinished();
                }
            }, 100 );
        }

        @Override
        public void onScanFailed( int _error ) {
            Log.e( TAG, "Scan failed : " +_error );
        }

        private void addScanResult( ScanResult _result ) {
            BluetoothDevice device= _result.getDevice();
            _foundDevices.add(device );
        }
    }

    //블루투스 설정 화면으로 이동
    private void enableBLE() {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );
    }

    //위치 권한 요청
    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }
    public void onAdd(View v){
        Intent intent = new Intent(MyAddActivity.this, MyFragment.class);
        startActivity(intent);
    }
}
