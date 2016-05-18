package com.test.tdslogger.bluetoothtest;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Set;
import java.util.logging.Handler;

public class BTPairingActivity extends AppCompatActivity{
    private static final int REQUEST_ENABLE_BT = 10;
    private static final String LOG_TAG = BTPairingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluethoot_pairing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Register the BroadcastReceiver
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(LOG_TAG + "Intent", intent.getAction());
            }
        };
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Snackbar.make(view, "Bluetooth not supported", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else{
                            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                            // If there are paired devices
                            if (pairedDevices.size() > 0) {
                                // Loop through paired devices
                                for (BluetoothDevice device : pairedDevices) {
                                    // Add the name and address to an array adapter to show in a ListView
                                    Log.v(LOG_TAG, device.getName());
                                    //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                }
                            }
                            if(mBluetoothAdapter.startDiscovery()){
                                Toast.makeText(fab.getContext(),"Finding Devices",
                                                Toast.LENGTH_LONG).show();
                                IntentFilter filter = new IntentFilter();
                                filter.addAction(BluetoothDevice.ACTION_FOUND);
                                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                                registerReceiver(mReceiver, filter);
                            }
                        }
                    }
                }
            });
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
