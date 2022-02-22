package com.example.myble;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter_BTLE_Services extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<BluetoothGattService> serviceArrayList;
    private HashMap<String,ArrayList<BluetoothGattCharacteristic>> characteristic_HashMap;

    public ListAdapter_BTLE_Services(Activity activity,
                                     ArrayList<BluetoothGattService> serviceArrayList,
                                     HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristic_HashMap) {
        this.activity = activity;
        this.serviceArrayList = serviceArrayList;
        this.characteristic_HashMap = characteristic_HashMap;
    }

    @Override
    public int getGroupCount() {
        return serviceArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return characteristic_HashMap.get(serviceArrayList.get(groupPosition).getUuid().toString()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return serviceArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return characteristic_HashMap.get(serviceArrayList.get(groupPosition).getUuid().toString()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        BluetoothGattService bluetoothGattService= (BluetoothGattService) getGroup(groupPosition);

        String serviceUUID=bluetoothGattService.getUuid().toString();
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.ble_device_list_item,null);
        }
        TextView tv_Service=convertView.findViewById(R.id.tv_service_uuid);
        tv_Service.setText("S: "+serviceUUID);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) getChild(groupPosition, childPosition);

        String characteristicUUID =  bluetoothGattCharacteristic.getUuid().toString();
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.btle_characteristics_list_item, null);
        }

        TextView tv_service = convertView.findViewById(R.id.tv_characteristic_uuid);
        tv_service.setText("C: " + characteristicUUID);

        int properties = bluetoothGattCharacteristic.getProperties();

        TextView tv_property = convertView.findViewById(R.id.tv_properties);
        StringBuilder sb = new StringBuilder();

        if (Utils.hasReadProperty(properties) != 0) {
            sb.append("R");
        }

        if (Utils.hasWriteProperty(properties) != 0) {
            sb.append("W");
        }

        if (Utils.hasNotifyProperty(properties) != 0) {
            sb.append("N");
        }

        tv_property.setText(sb.toString());

        TextView tv_value = (TextView) convertView.findViewById(R.id.tv_value);

        byte[] data = bluetoothGattCharacteristic.getValue();
        if (data != null) {
            tv_value.setText("Value: " + Utils.hexToString(data));
        }
        else {
            tv_value.setText("Value: ---");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
