package com.example.myble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver_BTLE_GATT extends BroadcastReceiver {
    private boolean mConnected = false;

    private ActivityBLEServices activityBLEServices;

    public BroadcastReceiver_BTLE_GATT(ActivityBLEServices activityBLEServices) {
        this.activityBLEServices = activityBLEServices;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (Service_BTLE_GATT.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
        } else if (Service_BTLE_GATT.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            Utils.toast(activityBLEServices.getApplicationContext(), "Disconnected From Device");
            activityBLEServices.finish();
        } else if (Service_BTLE_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            activityBLEServices.updateServices();
        } else if (Service_BTLE_GATT.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

            activityBLEServices.updateCharacteristic();
        }

        return;

    }
}
