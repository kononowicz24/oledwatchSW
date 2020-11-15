package com.k24.electronics.oledwatch;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BackgroundNotificationsFetchService extends NotificationListenerService {
    private BluetoothManager bluetoothManager;
    private String title;
    private String text;
    private String package_name;
    private String mac = "90:9A:77:2B:08:F5";
    public BackgroundNotificationsFetchService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothManager = BluetoothManager.getInstance();
        if (bluetoothManager == null) {
            // Bluetooth unavailable on this device :( tell the user
            Toast.makeText(this, "Bluetooth not available.", Toast.LENGTH_LONG).show(); // Replace context with your context instance.
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Toast.makeText(this,"TEST", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        package_name = sbn.getPackageName();
        if (! package_name.equalsIgnoreCase("com.digibites.accubattery")) {
            title = "" + sbn.getNotification().extras.getString("android.title");
            text = "" + sbn.getNotification().extras.getString("android.text");
        } else {
            title = "";
            text = "";
        }

        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //mac = settings.getString(getString(R.string.ble_mac_string_key), "");

        connectDevice(mac);
        //Toast.makeText(this, "NEW NOTIFICATION", Toast.LENGTH_LONG).show();
        Log.d("OLED","0001"+title+"0002"+text+"0003"+package_name);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void connectDevice(String mac) {
        final Disposable subscribe = bluetoothManager.openSerialDevice(mac).subscribe(this::onConnected, this::onError);
    }

    private void onConnected(BluetoothSerialDevice connectedDevice) {
        // You are now connected to this device!
        // Here you may want to retain an instance to your device:
        Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show();
        SimpleBluetoothDeviceInterface deviceInterface = connectedDevice.toSimpleDeviceInterface();

        // Listen to bluetooth events
        deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, this::onError);

        // Let's send a message:
        deviceInterface.sendMessage("0001"+title+"0002"+text+"0003"+package_name);
    }

    private void onMessageSent(String message) {
        // We sent a message! Handle it here.
        //Toast.makeText(this, "Sent a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
        bluetoothManager.close();
    }

    private void onMessageReceived(String message) {
        // We received a message! Handle it here.
        //Toast.makeText(context, "Received a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onError(Throwable error) {
        // Handle the error
        bluetoothManager.close();
        Log.d("OLEDWATCH","HUJ",error);
    }
}
