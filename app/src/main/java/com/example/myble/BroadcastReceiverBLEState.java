package com.example.myble;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverBLEState extends BroadcastReceiver {

    private final Context mContext;

    public BroadcastReceiverBLEState(Context context) {
        this.mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Utils.toast(mContext, "Bluetooth is off");
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Utils.toast(mContext, "Bluetooth is Turing off");
                case BluetoothAdapter.STATE_ON:
                    Utils.toast(mContext, "Bluetooth is on");
                case BluetoothAdapter.STATE_TURNING_ON:
                    Utils.toast(mContext, "Bluetooth is  Turing on");
            }
        }
    }
}
