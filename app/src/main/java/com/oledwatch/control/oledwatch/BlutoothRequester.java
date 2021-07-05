package com.oledwatch.control.oledwatch;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.UUID;

public interface BlutoothRequester {
    boolean connect(String address);
    BluetoothGattCharacteristic findCharacteristic(BluetoothGatt gatt, UUID characteristicUUID);
    boolean writeCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, String value);
    void disconnect(BluetoothGatt gatt);
    UUID getUuid_characteristics() ;
    BluetoothGattCharacteristic getCharacteristic_global();
    void setCharacteristic_global(BluetoothGattCharacteristic characteristic_global);
    Context passContext();
}