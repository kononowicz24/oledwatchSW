package com.k24.electronics.oledwatch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.UUID;

public class FirstFragment extends Fragment implements BlutoothRequester {
    private EditText macAddrEditText;
    private String mac = "90:9A:77:2B:08:F5";
    private String TAG = "OLEDWATCH";
    private UUID uuid_service = UUID.fromString("0000ffe0-0000-1000-8000-00805F9B34FB");
    private UUID uuid_characteristics = UUID.fromString("0000ffe1-0000-1000-8000-00805F9B34FB");
    private BluetoothGattCharacteristic characteristic_global;
    private BLEAdapter bleAdapter = new BLEAdapter(this);
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothGatt bluetoothGatt_global;
    private HashMap<String,BluetoothGatt> bluetoothGatts;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        macAddrEditText = view.findViewById(R.id.editTextMACAddr);
        view.findViewById(R.id.buttonDiscover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Context context = getActivity();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.ble_mac_string_key), macAddrEditText.getText().toString());
                editor.apply();
                Toast.makeText(context, macAddrEditText.getText().toString() , Toast.LENGTH_LONG).show();
                //getActivity().startService(new Intent(getActivity(), BackgroundNotificationsFetchService.class));
                //todo
                //tutaj połączenie z BT i testowa transmisja
                connect(mac);

            }
        });
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
        BluetoothGatt bluetoothGatt = device.connectGatt(this.getContext(), false, bleAdapter);
        bluetoothGatt_global = bluetoothGatt;
        Log.d(TAG, "Trying to create a new connection.");
        //bluetoothGatts.put(address, bluetoothGatt);
        return true;
    }

    @Override
    public BluetoothGattCharacteristic findCharacteristic(BluetoothGatt bluetoothGatt, UUID characteristicUUID) {

        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        //BluetoothGatt bluetoothGatt = device.connectGatt(this.getContext(), false, bleAdapter);

        if (bluetoothGatt == null) {
            Log.d(TAG, "findcharacteristics -> GATT = NULL");
            return null;
        }

        for (BluetoothGattService service : bluetoothGatt.getServices()) {

            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (characteristic != null) {
                Log.d(TAG, "findcharacteristics -> characteristic nie null" + characteristic.getUuid().toString());
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

        gatt.disconnect();
        return false;
    }

    @Override
    public void disconnect(BluetoothGatt gatt) {

        if (mBluetoothAdapter == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }

        //BluetoothGatt bluetoothGatt = bluetoothGatt_global;

        if (gatt != null) {

            gatt.disconnect();
        }
    }

    public String getMac() {
        return mac;
    }

    public UUID getUuid_service() {
        return uuid_service;
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

    public Context passContext() {
        return getContext();
    }
}