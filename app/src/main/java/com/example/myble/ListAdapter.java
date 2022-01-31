package com.example.myble;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<BLEDevice> {

    private Activity activity;
    int layoutResourceID;
    ArrayList<BLEDevice> devices;


    public ListAdapter(@NonNull Activity activity, int resource, @NonNull ArrayList<BLEDevice> objects) {
        super(activity.getApplicationContext(), resource, objects);

        this.activity=activity;
        layoutResourceID=resource;
        devices=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)  activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layoutResourceID,parent,false);
        }
        BLEDevice device=devices.get(position);
        String name=device.getName();
        String address=device.getAddress();
        int rssi=device.getRssi();

        TextView tv=null;
        tv=convertView.findViewById(R.id.tv_name);
        if(name!=null && name.length()>0){
            tv.setText(device.getName());
        }else {
            tv.setText("No Name");
        }

        tv=convertView.findViewById(R.id.tv_rssi);
        tv.setText("RSSI:"+ Integer.toString(rssi));

        tv=convertView.findViewById(R.id.tv_macaddr);
        if(address!=null && address.length()>0){
            tv.setText(device.getAddress());
        }
        else {
            tv.setText("No Address");
        }

        return convertView;
    }
}
