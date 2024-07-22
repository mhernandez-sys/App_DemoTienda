package com.example.demo.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.demo.R;
import com.example.demo.assist.AddressRepo;
import com.example.demo.main.KeyDwonFragment;
import com.sewoo.jpos.command.ESCPOSConst;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;

public class BarcodeFragment extends KeyDwonFragment {

    public static Vector<String> ipAddrVector;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName = dir + "//WFPrinter";
    private static final String TAG = "WiFiConnectMenu";
    private final int sendRequestCode = 2345;
    public String Id_identi, Descripcion_identi;

    private EditText edit_addressBT;
    private TextView Et_datosLKP;
    private Button button_select, button_gencode, button_clear, button_cancelar;

    private ImageView barcodeImageView;
    private BluetoothTask bluetoothTask;
    public String codigobarras;

    // Define el ActivityResultLauncher
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            String arr = data.getExtras().getString(Bluetooth_Activity.EXTRA_DEVICE_ADDRESS);
                            // TODO Address saved.
                            AddressRepo.getInstance().setBluetoothAddress(arr);
                            edit_addressBT.setText(arr);
                        }
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barcode, container, false);
        barcodeImageView = view.findViewById(R.id.barcodeImageView);
        edit_addressBT = view.findViewById(R.id.EditTextAddressBT);
        button_select = view.findViewById(R.id.ButtonSelectBT);
        button_clear = view.findViewById(R.id.ButtonClearBT);
        button_gencode = view.findViewById(R.id.btngencode);
        Et_datosLKP = view.findViewById(R.id.Et_datosLKP);
        button_select = view.findViewById(R.id.ButtonSelectBT);
        button_cancelar = view.findViewById(R.id.btncancelar);

        Bundle args = getArguments();
        if (args != null) {
            codigobarras = args.getString("barcode");
            // Usa el valor de barcode como necesites

            Et_datosLKP.setText(codigobarras);
        }

        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Bluetooth_Activity.class);
                activityResultLauncher.launch(intent);
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addressBT.setText("");
                AddressRepo.getInstance().setBluetoothAddress("");
            }
        });

        button_gencode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor1 = edit_addressBT.getText().toString();
                String valor2 = AddressRepo.getInstance().getBluetoothAddress();

                if (valor1.isEmpty() || valor2.isEmpty()){
                    Toast.makeText(getContext(), "Impresora no conectada", Toast.LENGTH_SHORT).show();
                }else {
                    //Instruccion que manda a imprimir
                    bluetoothTask = new BluetoothTask();
                    bluetoothTask.execute(AddressRepo.getInstance().getBluetoothAddress());
                    // Esto te llevará al fragmento anterior en la pila de back stack
                    requireActivity().getSupportFragmentManager().popBackStack();
                    //Con esto se desconecta de la impresora
                    edit_addressBT.setText("");
                    AddressRepo.getInstance().setBluetoothAddress("");
                }
            }
        });
        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto te llevará al fragmento anterior en la pila de back stack
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No es necesario generar el código de barras aquí
    }



    private void saveSettingFile()
    {
        try
        {
            File tempDir = new File(dir);
            if(!tempDir.exists())
            {
                tempDir.mkdir();
            }
            BufferedWriter fWriter = new BufferedWriter(new FileWriter(fileName));
            Iterator<String> iter = ipAddrVector.iterator();
            while(iter.hasNext())
            {
                fWriter.write(iter.next());
                fWriter.newLine();
            }
            fWriter.close();
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    class BluetoothTask extends AsyncTask<String, Void, Integer> {
        private BluetoothPort bluetoothPort;
        private Thread rThread;

        public void closeConnection() throws InterruptedException, IOException {
            if ((bluetoothPort != null) && (bluetoothPort.isConnected())) {
                bluetoothPort.disconnect();
            }
            if ((rThread != null) && (rThread.isAlive())) {
                rThread.interrupt();
                rThread = null;
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = Integer.valueOf(0);
            bluetoothPort = BluetoothPort.getInstance();
            if (bluetoothPort.isValidAddress(params[0])) {
                try {
                    // Connection
                    bluetoothPort.connect(params[0]);
                    rThread = new Thread(new RequestHandler());
                    rThread.start();
                    // Printing
                    Print_Bar_2D(new ESCPOSPrinter());
                    Thread.sleep(1000);
                    // Disconnection
                    bluetoothPort.disconnect();
                    if ((rThread != null) && (rThread.isAlive())) {
                        rThread.interrupt();
                        rThread = null;
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    result = Integer.valueOf(-1);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                    result = Integer.valueOf(-2);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            int retVal = result.intValue();
            if (retVal < 0) {
                Toast.makeText(getContext(), "Bluetooth Print Failed.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public void Print_Bar_2D(ESCPOSPrinter escposPrinter) throws UnsupportedEncodingException {
        String data = Et_datosLKP.getText().toString();
        String data1 = ("\t\t      ")+ data + ("\r\n");
        escposPrinter.printString(data1);
        escposPrinter.printPDF417(data, data.length(), 0, 10, ESCPOSConst.LK_ALIGNMENT_LEFT);
        escposPrinter.printString("\r\n");
        //escposPrinter.printString("QRCode\r\n");
        escposPrinter.printQRCode(data, data.length(), 6, ESCPOSConst.LK_QRCODE_EC_LEVEL_L, ESCPOSConst.LK_ALIGNMENT_CENTER);
        escposPrinter.lineFeed(4);
        escposPrinter.cutPaper();
    }

}