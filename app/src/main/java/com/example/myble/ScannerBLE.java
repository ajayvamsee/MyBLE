package com.example.myble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class ScannerBLE {

    private MainActivity ma;
    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning = true;
    private Handler mHandler;

    private long scanPeriod;
    private int signalStrength;

    public ScannerBLE(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        ma = mainActivity;
        mHandler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager = (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean ismScanning() {
        return mScanning;
    }

    public void start() {
        if (!Utils.checkBluetooth(bluetoothAdapter)) {
            Utils.requestBluetooth(ma);
            ma.stopScan();
        } else {
            scanLeDevices(true);
        }
    }

    public void stop() {
        scanLeDevices(false);
    }

    private void scanLeDevices(boolean enable) {
        if (enable && !mScanning) {
            Utils.toast(ma.getApplicationContext(), "Start BLE Scan");

            mHandler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @RequiresApi(api = Build.VERSION_CODES.S)
                @Override
                public void run() {
                    Utils.toast(ma.getApplicationContext(), "Stopped BLE SCan");
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    ma.stopScan();
                }
            },scanPeriod);
        }
        mScanning=true;
        bluetoothAdapter.startLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d("XXX",""+device.getAddress()+""+rssi);
             final int new_rssi=rssi;
             if(rssi>signalStrength){
                 mHandler.post(new Runnable() {
                     @SuppressLint("NewApi")
                     @Override
                     public void run() {
                         ma.addDevice(device,new_rssi);
                     }
                 });

             }
        }
    };

    // Device scan callback.
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Log.d("XXX",result.toString());
//                    leDeviceListAdapter.addDevice(result.getDevice());
//                    leDeviceListAdapter.notifyDataSetChanged();
                }
            };
}
