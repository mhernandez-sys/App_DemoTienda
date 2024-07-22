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

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;
public class EntradasFragment extends KeyDwonFragment {

    private EntradasViewModel mViewModel;
    private String cantidadIngresada;

    public static EntradasFragment newInstance() {
        return new EntradasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entradas, container, false);

        // Obtén los Spinners del layout
        Spinner spinner1 = root.findViewById(R.id.spinner_entrada_1);
        Spinner spinner2 = root.findViewById(R.id.spinner_entrada_2);

        // Datos para los Spinners
        String[] datosSpinner1 = {"Opción 1", "Opción 2", "Opción 3"};
        String[] datosSpinner2 = {"Elemento A", "Elemento B", "Elemento C"};

        // Adaptador para el primer Spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        // Adaptador para el segundo Spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // Obtén los CheckBoxes y el EditText del layout
        CheckBox checkboxEntrada1 = root.findViewById(R.id.checkbox_entrada_1);
        CheckBox checkboxEntrada2 = root.findViewById(R.id.checkbox_entrada_2);
        EditText editTextCantidad = root.findViewById(R.id.edit_text_entrada_1);

        checkboxEntrada1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = editTextCantidad.getText().toString();
                    showCustomAlertDialog("Número de lote");
                }
            }
        });

        checkboxEntrada2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cantidadIngresada = editTextCantidad.getText().toString();
                    showCustomAlertDialog("Número de serie");
                }
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
