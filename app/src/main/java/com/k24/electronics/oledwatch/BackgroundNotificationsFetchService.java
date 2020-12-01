package com.k24.electronics.oledwatch;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;


import java.util.Arrays;
import java.util.UUID;


public class BackgroundNotificationsFetchService extends NotificationListenerService implements BlutoothRequester {
    private String title;
    private String text;
    private String package_name;

    private String mac = "90:9A:77:2B:08:F5";
    private String TAG = "OLW1 - NOTIFYSERV";
    //private UUID uuid_service = UUID.fromString("0000ffe0-0000-1000-8000-00805F9B34FB");
    private UUID uuid_characteristics = UUID.fromString("0000ffe1-0000-1000-8000-00805F9B34FB");
    private BluetoothGattCharacteristic characteristic_global;
    private BLEAdapter bleAdapter = new BLEAdapter(this);
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothGatt bluetoothGatt_global;

    public BackgroundNotificationsFetchService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        package_name = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        if (! package_name.equalsIgnoreCase("com.digibites.accubattery")) {
           title = notification.extras.getString("android.title");
            if (notification.extras.getCharSequenceArray("android.textLines") != null) {
                text = Arrays.toString(notification.extras.getCharSequenceArray("android.textLines"));
                text = text.substring(0, Math.min(text.length(), 180));
            }
        } else {
           title = "";
           text = "";
           return;
        }
        //Log.d("OLED","\t"+title+"\t"+text+"\t"+package_name);
        bleAdapter.setMessage("\\\\\\\\\\\\\\\\"+package_name+"\\"+text+"\\"+package_name);
        connect(mac);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean connect(String address) {
        Log.i(TAG, "Connecting to " + address);
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac);

        Log.d(TAG, device.getAddress());
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        BluetoothGatt bluetoothGatt = device.connectGatt(this.getBaseContext(), false, bleAdapter);
        bluetoothGatt_global = bluetoothGatt;
        Log.d(TAG, "Trying to create a new connection.");
        return true;
    }

    @Override
    public BluetoothGattCharacteristic findCharacteristic(BluetoothGatt bluetoothGatt, UUID characteristicUUID) {
        if (bluetoothGatt == null) {
            Log.d(TAG, "FAIL: findcharacteristics -> GATT = NULL");
            return null;
        }
        for (BluetoothGattService service : bluetoothGatt.getServices()) {

            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (characteristic != null) {
                Log.d(TAG, "OK: findcharacteristics -> characteristic not null" + characteristic.getUuid().toString());
                return characteristic;
            }
        }

        return null;
    }

    @Override
    public boolean writeCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, String value) {
        BluetoothGatt bluetoothGatt = bluetoothGatt_global;
        Log.d(TAG, "WriteCharacteristic:"+bluetoothGatt.toString());
        characteristic.setValue(value);
        if (bluetoothGatt != null) {
            return bluetoothGatt.writeCharacteristic(characteristic);
        }

        //gatt.disconnect();
        return false;
    }

    @Override
    public void disconnect(BluetoothGatt gatt) {

        if (mBluetoothAdapter == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }

        if (gatt != null) {
            //gatt.disconnect();
            Log.w(TAG, "can be safely disconnected");
        }
    }

    public UUID getUuid_characteristics() {
        return uuid_characteristics;
    }

    public BluetoothGattCharacteristic getCharacteristic_global() {
        return characteristic_global;
    }

    public void setCharacteristic_global(BluetoothGattCharacteristic characteristic_global) {
        this.characteristic_global = characteristic_global;
    }


    @Override
    public Context passContext() {
        return getBaseContext();
    }
}
