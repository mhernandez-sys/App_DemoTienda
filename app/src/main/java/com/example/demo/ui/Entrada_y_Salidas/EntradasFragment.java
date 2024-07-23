package com.example.demo.ui.Entrada_y_Salidas;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;
public class EntradasFragment extends KeyDwonFragment {

    private EntradasViewModel mViewModel;
    private String cantidadIngresada;
    private CheckBox CB_Lotes, CB_Unidad;
    private Spinner Sp_Provedor, SP_Producto;
    private TextView TV_Cantidad, TV_Cajas;
    private EditText ET_Can_Lotes, ET_ArtEsperados, Et_CanCajas;
    private Button BT_Añadir;

    public static EntradasFragment newInstance() {
        return new EntradasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entradas, container, false);

        // Obtén los Spinners del layout
        Sp_Provedor = root.findViewById(R.id.spinner_entrada_1);
        SP_Producto = root.findViewById(R.id.spinner_entrada_2);

        // Obtén los CheckBoxes y el EditText del layout
        CB_Lotes = root.findViewById(R.id.CB_Lotes);
        CB_Unidad = root.findViewById(R.id.CB_Unidad);

        BT_Añadir = root.findViewById(R.id.BT_Añadir);

        TV_Cajas = root.findViewById(R.id.TV_Cajas);
        TV_Cantidad = root.findViewById(R.id.TV_Cantidad);
        ET_Can_Lotes = root.findViewById(R.id.et_piezascaja);
        ET_ArtEsperados = root.findViewById(R.id.ET_ArtEsperados);
        Et_CanCajas = root.findViewById(R.id.Et_CanCajas);

        CB_Lotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // Mostrar TextView y EditText
                    ET_Can_Lotes.setVisibility(View.VISIBLE);
                    TV_Cantidad.setVisibility(View.VISIBLE);
                    Et_CanCajas.setVisibility(View.VISIBLE);
                    TV_Cajas.setVisibility(View.VISIBLE);
                    BT_Añadir.setVisibility(View.VISIBLE);
                }else{
                    // Ocultar TextView y EditText
                    ET_Can_Lotes.setVisibility(View.GONE);
                    TV_Cantidad.setVisibility(View.GONE);
                    BT_Añadir.setVisibility(View.GONE);
                    Et_CanCajas.setVisibility(View.GONE);
                    TV_Cajas.setVisibility(View.GONE);
                }
            }
        });

        CB_Unidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = ET_ArtEsperados.getText().toString();
                    showCustomAlertDialog("Número de serie");
                }
            }
        });


        BT_Añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidadIngresada = ET_ArtEsperados.getText().toString();
                showCustomAlertDialog("Número de lote");
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntradasViewModel.class);
        // TODO: Use the ViewModel
    }

    private void showCustomAlertDialog(String hintText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView);

        // Hacer el diálogo no cancelable
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        TextView tvCantidadLabel = dialogView.findViewById(R.id.tv_cantidad_label);
        TextView tvCantidadValue = dialogView.findViewById(R.id.tv_cantidad_value);
        EditText etLoteSerie = dialogView.findViewById(R.id.et_lote_serie);
        TextView tvEntradasLeidas = dialogView.findViewById(R.id.tv_entradas_leidas);
        TextView tvEntradas = dialogView.findViewById(R.id.tv_entradas);
        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);

        tvCantidadLabel.setText("Cantidad Esp");
        tvCantidadValue.setText(cantidadIngresada);
        etLoteSerie.setHint(hintText);

        // Aquí se pueden agregar más funcionalidades, por ejemplo, al ingresar datos en etLoteSerie
        etLoteSerie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Actualiza el TextView de entradas leídas
                tvEntradas.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
