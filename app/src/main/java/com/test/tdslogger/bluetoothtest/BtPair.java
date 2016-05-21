package com.test.tdslogger.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BtPair extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private final static String LOG_TAG = BtPair.class.getSimpleName();
    private LinearLayout myLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_pair);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myLayout= (LinearLayout) findViewById(R.id.btPair_linLayout);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentOpenBluetoothSettings = new Intent();
                    intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intentOpenBluetoothSettings);
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCards();
    }

    @Override
    protected void onPause() {
        myLayout.removeAllViewsInLayout();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myLayout.removeAllViewsInLayout();
        super.onDestroy();
    }

    private void setCards ( ){
        int response = 0;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,"Device does not support bluetooth",Toast.LENGTH_LONG)
                    .show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            final int numberOfDevices = pairedDevices.size();
            BluetoothDevice[] arrayDevices = new BluetoothDevice[numberOfDevices];
            int tempIndex = 0;
            for (BluetoothDevice device : pairedDevices) {
                arrayDevices[tempIndex] = device;
                tempIndex++;
            }
            View[] btDevices = new View[numberOfDevices];
            for(int i =0; i<numberOfDevices;i++){
                btDevices[i] = getLayoutInflater().inflate(R.layout.card_layout,myLayout,false);
                btDevices[i].setTag(i);
                btDevices[i].setOnClickListener(new MyOnClickListener(i));
                TextView textViewName = (TextView)btDevices[i].findViewById(R.id.bt_device_name);
                textViewName.setText(arrayDevices[i].getName());
                TextView textViewAdd = (TextView)btDevices[i].findViewById(R.id.bt_device_mac);
                textViewAdd.setText(arrayDevices[i].getAddress());
                myLayout.addView(btDevices[i]);

            }
        }
    }
    public class MyOnClickListener implements View.OnClickListener {

        int index;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View arg0) {
            TextView mac_id = (TextView)arg0.findViewById(R.id.bt_device_mac);
            Log.i("Device", (String) mac_id.getText());
        }
    }

}
