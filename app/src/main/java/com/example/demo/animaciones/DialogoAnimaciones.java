package com.example.demo.animaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.R;

public class DialogoAnimaciones {
    private static androidx.appcompat.app.AlertDialog dialog;
    //Esta clase muestra las pantallas de carga y de no conexion a internet
    //La funcion showNoInternetDialog muestra la pantalla de que fallo la conexion a internet
    public static void showNoInternetDialog(Context context, String errorMessage, Runnable retryAction) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.carga, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        TextView errorTextView = dialogView.findViewById(R.id.TV_MsgCarga);
        if (errorTextView != null) {
            errorTextView.setText(errorMessage);
        }

        Button closeButton = dialogView.findViewById(R.id.BT_voverint);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ///Carga de nuevo los datos y vuelve a llamar a la funcion que invoca a web service
                if (retryAction != null) {
                    retryAction.run(); // Call the web service again when the button is clicked
                }
            }
        });
    }
    //Muestra la pantalla de carga que hace que el usuario se espere hasta que haya una respuesta del webservice
    public static void showLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.cargaws, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(dialogView);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    //Cierra la pantalla del
    public static void hideLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
