package com.example.myble;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        return convertView;
    }
}
