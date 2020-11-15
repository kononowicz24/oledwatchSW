package com.k24.electronics.oledwatch;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

public class BLEAdapter extends BluetoothGattCallback {
    private String TAG = "OLEDWATCH-BT";
    private BlutoothRequester requester;
    public BLEAdapter(BlutoothRequester requester1) {
        super();
        requester = requester1;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        String address = gatt.getDevice().getAddress();
        //gatt.discoverServices();
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(TAG, "Attempting to start service discovery:" + gatt.discoverServices());
        }
        else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.i(TAG, "Disconnected from GATT server.");
            gatt.close();
            Toast.makeText(requester.passContext(), "Restart your Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "onservicesdiscovered - success");
            BluetoothGattCharacteristic characteristic = requester.findCharacteristic(gatt,requester.getUuid_characteristics());
            requester.setCharacteristic_global(characteristic);
            Log.d(TAG, "characreristic null? = " + Boolean.toString(requester.getCharacteristic_global() == null));
            requester.writeCharacteristic(gatt,characteristic, "TEST6969696969");
            // success, we can communicate with the device
        } else {
            // failure
            Log.d(TAG, "onservicesdiscovered - failure");
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
    }
}
