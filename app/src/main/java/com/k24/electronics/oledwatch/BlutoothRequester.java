package com.k24.electronics.oledwatch;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.UUID;

public interface BlutoothRequester {
    boolean connect(String address);
    BluetoothGattCharacteristic findCharacteristic(BluetoothGatt gatt, UUID characteristicUUID);
    boolean writeCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, String value);
    void disconnect(BluetoothGatt gatt);
    String getMac();
    UUID getUuid_service();
    UUID getUuid_characteristics() ;
    BluetoothGattCharacteristic getCharacteristic_global();
    void setCharacteristic_global(BluetoothGattCharacteristic characteristic_global);
    Context passContext();
}
