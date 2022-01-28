package com.example.myble;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static int REQUEST_ENABLE_BT=1;

    private HashMap<String, BLEDevice> mBLEDevicesHashMap;
    private ArrayList<BLEDevice> mBLEDevicesArrayList;
    private ListAdapter adapter;

    private Button btnScan;
    private BroadcastReceiverBLEState broadcastReceiverBLEState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not Supported");
            finish();
        }

        broadcastReceiverBLEState = new BroadcastReceiverBLEState(getApplicationContext());
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                Utils.toast(getApplicationContext(), "Scan Button is Pressed");
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
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiverBLEState);
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

}