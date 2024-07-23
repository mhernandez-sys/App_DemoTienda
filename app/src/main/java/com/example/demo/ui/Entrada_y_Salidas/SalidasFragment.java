package com.example.demo.ui.Entrada_y_Salidas;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;
public class SalidasFragment extends KeyDwonFragment {

    private String cantidadIngresada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salidas, container, false);

        CheckBox checkboxSalida1 = view.findViewById(R.id.checkbox_salida_1);
        CheckBox checkboxSalida2 = view.findViewById(R.id.checkbox_salida_2);
        EditText editTextCantidad = view.findViewById(R.id.edit_text_salida_1);

        checkboxSalida1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = editTextCantidad.getText().toString();
                    showCustomAlertDialog("Número de lote");
                }
            }
        });

        checkboxSalida2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = editTextCantidad.getText().toString();
                    showCustomAlertDialog("Número de serie");
                }
            }
        });

        return view;
    }

    private void showCustomAlertDialog(String hintText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView);

        // Hacer el diálogo no cancelable
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

//        TextView tvCantidadLabel = dialogView.findViewById(R.id.tv_cantidad_label);
//        TextView tvCantidadValue = dialogView.findViewById(R.id.tv_cantidad_value);
//        EditText etLoteSerie = dialogView.findViewById(R.id.et_lote_serie);
//        TextView tvEntradasLeidas = dialogView.findViewById(R.id.tv_entradas_leidas);
//        TextView tvEntradas = dialogView.findViewById(R.id.tv_entradas);
//        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);

//        tvCantidadLabel.setText("Cantidad Esp");
//        tvCantidadValue.setText(cantidadIngresada);
//        etLoteSerie.setHint(hintText);

//        // Aquí se pueden agregar más funcionalidades, por ejemplo, al ingresar datos en etLoteSerie
//        etLoteSerie.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Actualiza el TextView de entradas leídas
//                tvEntradas.setText(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        btnCompletar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });

        alertDialog.show();
    }
}
