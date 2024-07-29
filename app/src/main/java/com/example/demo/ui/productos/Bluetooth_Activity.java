package com.example.demo.ui.productos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo.R;
import com.sewoo.port.android.BluetoothPort;

import java.util.Iterator;
import java.util.Vector;

public class Bluetooth_Activity extends Activity {

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int BT_PRINTER = 1536;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private Button button_search;
    private ListView list_address;

    private Vector<BluetoothDevice> remoteDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BluetoothPort bluetoothPort;
    private CheckTypesTask BTtask;

    boolean searchflags;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluetooth_list);

        button_search = (Button)findViewById(R.id.ButtonSearchBT);
        list_address = (ListView)findViewById(R.id.ListView02);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list_address.setAdapter(adapter);

        Init_BluetoothSet();

        bluetoothPort = BluetoothPort.getInstance();
        bluetoothPort.SetMacFilter(false);   //not using mac address filtering

        addPairedDevices();

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTtask = new CheckTypesTask();
                BTtask.execute();
            }
        });

        list_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                String address = parsed_address(info);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private String parsed_address(String mac_address)
    {
        String str_parsed = "";

        str_parsed = mac_address.substring(mac_address.indexOf("\n")+2);
        str_parsed = str_parsed.substring(0, str_parsed.indexOf("]"));

        return str_parsed;
    }

    private void addPairedDevices()
    {
        BluetoothDevice pairedDevice;
        Iterator<BluetoothDevice> iter = (mBluetoothAdapter.getBondedDevices()).iterator();

        String key = "";

        while(iter.hasNext())
        {
            pairedDevice = iter.next();
            if(bluetoothPort.isValidAddress(pairedDevice.getAddress()))
            {
                int deviceNum = pairedDevice.getBluetoothClass().getMajorDeviceClass();

                if(deviceNum == BT_PRINTER)
                {
                    remoteDevices.add(pairedDevice);

                    key = pairedDevice.getName() +"\n["+pairedDevice.getAddress()+"] [Paired]";
                    adapter.add(key);
                }
            }
        }
    }

    private void clearBtDevData()
    {
        remoteDevices = new Vector<BluetoothDevice>();
    }

    private void bluetoothSetup()
    {
        // Initialize
        clearBtDevData();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
            return;
        }
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void Init_BluetoothSet()
    {
        bluetoothSetup();

        discoveryResult = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String key;
                boolean bFlag = true;
                BluetoothDevice btDev;
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(remoteDevice != null)
                {
                    int devNum = remoteDevice.getBluetoothClass().getMajorDeviceClass();

                    if(devNum != BT_PRINTER)
                        return;

                    if(remoteDevice.getBondState() != BluetoothDevice.BOND_BONDED)
                    {
                        key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"]";
                    }
                    else
                    {
                        key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"] [Paired]";
                    }
                    if(bluetoothPort.isValidAddress(remoteDevice.getAddress()))
                    {
                        for(int i = 0; i < remoteDevices.size(); i++)
                        {
                            btDev = remoteDevices.elementAt(i);
                            if(remoteDevice.getAddress().equals(btDev.getAddress()))
                            {
                                bFlag = false;
                                break;
                            }
                        }
                        if(bFlag)
                        {
                            remoteDevices.add(remoteDevice);
                            adapter.add(key);
                        }
                    }
                }
            }
        };

        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        searchStart = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                //Toast.makeText(mainView, "Search Bluetooth Device", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(searchStart, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));

        searchFinish = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                searchflags = true;
            }
        };
        registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }

    private void SearchingBTDevice()
    {
        adapter.clear();
        adapter.notifyDataSetChanged();

        clearBtDevData();
        mBluetoothAdapter.startDiscovery();
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(Bluetooth_Activity.this);

        @Override
        protected void onPreExecute(){
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Searching the Printer...");
            asyncDialog.setCancelable(false);
            asyncDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            searchflags = true;
                            mBluetoothAdapter.cancelDiscovery();
                        }
                    });
            asyncDialog.show();
            SearchingBTDevice();
            super.onPreExecute();
        };

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                while(true)
                {
                    if(searchflags)
                        break;

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(asyncDialog.isShowing())
                asyncDialog.dismiss();

            searchflags = false;
            super.onPostExecute(result);
        };
    }
}
