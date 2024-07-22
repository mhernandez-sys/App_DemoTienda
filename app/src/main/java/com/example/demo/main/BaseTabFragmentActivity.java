package com.example.demo.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.utility.StringUtility;

public class BaseTabFragmentActivity extends AppCompatActivity {
    public RFIDWithUHFUART mReader;
    public KeyDwonFragment currentFragment = null;
    public int TidLen = 6;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Comprobar si se han concedido los permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Solicitar permiso al usuario si aún no se ha otorgado el permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Se han concedido los permisos y se pueden realizar las operaciones correspondientes.
            // Lea la tarjeta SD aquí
        }
    }

    //Permisos del programa
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se ha otorgado permiso para realizar la operación correspondiente.
                Log.e("TEST", "Ya solicité permiso para tarjeta SD");
                // Lea la tarjeta SD aquí
            } else {
                // Permiso denegado, incapaz de realizar la operación correspondiente
                Log.e("TEST", "No se pudo solicitar el permiso de la tarjeta SD");
            }
        }
    }

    //Esta parte infla el menu que se encuentra en basetabfragment activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //En esta parte se seleciona la respuesta y la funcion que se manda a llamar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //Verifica si se selecciono la opcion"Exportardatos"
        if(id == R.id.action_exportar){
            //LLamar al metodo para exportar datos
            Toast.makeText(this, "Exportar datos seleccionado", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> { //Iniciar tarea
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub

            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mypDialog.cancel();
            if (!result) {
                Toast.makeText(BaseTabFragmentActivity.this, "Fallo al comenzar", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mypDialog = new ProgressDialog(BaseTabFragmentActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("Comenzando...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    public boolean vailHexInput(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        if (str.length() % 2 == 0) {
            return StringUtility.isHexNumberRex(str);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(keyCode == 280 || keyCode == 293){
            if(event.getRepeatCount() == 0){
                if(currentFragment != null){
                    currentFragment.myOnKeyDwon();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initUHF() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {

            toastMessage(ex.getMessage());

            return;
        }

        if (mReader != null) {
            new InitTask().execute();

        }
    }

    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
