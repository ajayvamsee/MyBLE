package com.example.myble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.widget.Toast;

public class Utils {

    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static void requestBluetooth(Activity activity) {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableIntent, MainActivity.REQUEST_ENABLE_BT);
    }

    public static void toast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static String hexToString(byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length);

        for (byte byteChar : data) {
            sb.append(String.format("%02X", byteChar));
        }

        return sb.toString();
    }

    public static int hasWriteProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_WRITE;
    }

    public static int hasReadProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_WRITE;
    }

    public static int hasNotifyProperty(int property){
        return property & BluetoothGattCharacteristic.PROPERTY_NOTIFY;
    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Service_BTLE_GATT.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Service_BTLE_GATT.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Service_BTLE_GATT.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Service_BTLE_GATT.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }
}
