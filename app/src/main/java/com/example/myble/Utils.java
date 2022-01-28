package com.example.myble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Utils {

    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter){
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static void requestBluetooth(Activity activity){
        Intent enableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableIntent,MainActivity.REQUEST_ENABLE_BT);
    }

    public static void toast(Context context,String message){
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }
}
