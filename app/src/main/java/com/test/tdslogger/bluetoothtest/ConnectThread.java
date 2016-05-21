package com.test.tdslogger.bluetoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;


public class ConnectThread extends Thread{

    private final BluetoothDevice bTDevice;
    private final BluetoothSocket bTSocket;
    private static final String LOG_TAG = ConnectThread.class.getSimpleName();
    public ConnectThread(BluetoothDevice bTDevice, UUID UUID) {

        BluetoothSocket tmp = null;
        this.bTDevice = bTDevice;

        try {
            tmp = this.bTDevice.createRfcommSocketToServiceRecord(UUID);
        }
        catch (IOException e) {
            Log.d(LOG_TAG, "Could not start listening for RFCOMM");
        }
        bTSocket = tmp;
    }

    public boolean connect() {

        try {
            bTSocket.connect();
        } catch(IOException e) {
            Log.d(LOG_TAG,"Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch(IOException close) {
                Log.d(LOG_TAG, "Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

}
