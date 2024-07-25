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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.main.KeyDwonFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalidasFragment extends KeyDwonFragment {

    private String cantidadIngresada;
    private CheckBox CB_SalLote, CB_SalUnidad;
    private Spinner sp_cliente, sp_producto;

    private WebServiceManager webServiceManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salidas, container, false);

        CB_SalLote = view.findViewById(R.id.CB_SalLote);
        CB_SalUnidad = view.findViewById(R.id.CB_SalUnidad);
        EditText editTextCantidad = view.findViewById(R.id.edit_text_salida_1);
        // Obtén los Spinners del layout
        sp_cliente = view.findViewById(R.id.sp_cliente);
        sp_producto = view.findViewById(R.id.sp_producto);

        webServiceManager = new WebServiceManager(requireContext());

        obtenerDatosParaSpinners(sp_cliente);
        obtenerDatosParaSpinners2(sp_producto);


        CB_SalLote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = editTextCantidad.getText().toString();
                    showCustomAlertDialog("Número de lote");
                }
            }
        });

        CB_SalUnidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


    private void obtenerDatosParaSpinners(Spinner clientes) {
        webServiceManager.callWebService("clientessp", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        List<String> datosSpinner1 = new ArrayList<>();
                        datosSpinner1.add("Seleccionar");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            datosSpinner1.add(jsonObject.getString("Nombre"));
                        }

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clientes.setAdapter(adapter1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void obtenerDatosParaSpinners2(Spinner prosuctos) {
        webServiceManager.callWebService("productossp", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        List<String> datosSpinner1 = new ArrayList<>();
                        datosSpinner1.add("Seleccionar");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            datosSpinner1.add(jsonObject.getString("Descripcion"));
                        }

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        prosuctos.setAdapter(adapter1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
                }
            }
        });
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
