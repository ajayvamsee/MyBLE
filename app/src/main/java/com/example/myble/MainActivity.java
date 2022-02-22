package com.example.myble;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    private Context context;

    private HashMap<String, BLEDevice> mBLEDevicesHashMap;
    private ArrayList<BLEDevice> mBLEDevicesArrayList;
    private ListAdapter adapter;

    private Button btnScan;
    private BroadcastReceiverBLEState broadcastReceiverBLEState;
    private ScannerBLE mScannerBLE;

    private static final String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bleCheck();

        if (!permissionCheck()) {
            requestPermissions(permissions,1);
        }


        broadcastReceiverBLEState = new BroadcastReceiverBLEState(getApplicationContext());
        mScannerBLE = new ScannerBLE(this, 5000, -260);
        mBLEDevicesHashMap = new HashMap<>();
        mBLEDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter(this, R.layout.ble_device_list_item, mBLEDevicesArrayList);

        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);

        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
    }

    private boolean permissionCheck() {
       if(permissions!=null){
           for (String permission: permissions){
               if(checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED){
                   return false;
               }
           }
       }
       return true;
    }

    private void bleCheck() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not Supported");
            finish();
        }
    }


    /**
     * On ItemCLick will call when user clicked on ListView
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Context context=view.getContext();

        stopScan();

        String name =mBLEDevicesArrayList.get(position).getName();
        String address=mBLEDevicesArrayList.get(position).getAddress();

        Intent intent=new Intent(this,ActivityBLEServices.class);
        intent.putExtra(ActivityBLEServices.EXTRA_NAME,name);
        intent.putExtra(ActivityBLEServices.EXTRA_ADDRESS,address);
        startActivityForResult(intent,BTLE_SERVICES);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                if (!mScannerBLE.ismScanning()) {
                    startScan();
                } else {
                    stopScan();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiverBLEState, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiverBLEState);
        stopScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Utils.toast(getApplicationContext(), "Bluetooth Turned on");
            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please Turn on Bluetooth");
            }
        }
    }

    public void addDevice(BluetoothDevice device, int new_rssi) {
        String address = device.getAddress();
        if (!mBLEDevicesHashMap.containsKey(address)) {
            BLEDevice bleDevice = new BLEDevice(device);
            bleDevice.setRssi(new_rssi);
            mBLEDevicesHashMap.put(address, bleDevice);
            mBLEDevicesArrayList.add(bleDevice);
        } else {
            mBLEDevicesHashMap.get(address).setRssi(new_rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan() {
        btnScan.setText("Scanning...");
        mBLEDevicesArrayList.clear();
        mBLEDevicesHashMap.clear();
        adapter.notifyDataSetChanged();
        mScannerBLE.start();
    }

    public void stopScan() {

        btnScan.setText("Scan Again");
        mScannerBLE.stop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}