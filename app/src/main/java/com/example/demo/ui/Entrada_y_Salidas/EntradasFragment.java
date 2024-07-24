package com.example.demo.ui.Entrada_y_Salidas;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;
public class EntradasFragment extends KeyDwonFragment {

    private EntradasViewModel mViewModel;
    private String cantidadIngresada;
    private CheckBox CB_Lotes, CB_Unidad;
    private Spinner Sp_Provedor, SP_Producto;
    private TextView TV_Cantidad, TV_Cajas;
    private EditText ET_PiezasCaja, ET_ArtEsperados, Et_CanCajas;
    private Button BT_Añadir;
    private String Ban_leido = "";

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
        ET_PiezasCaja = root.findViewById(R.id.ET_PiezasCaja);
        ET_ArtEsperados = root.findViewById(R.id.ET_ArtEsperados);
        Et_CanCajas = root.findViewById(R.id.Et_CanCajas);


        CB_Lotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ET_ArtEsperados.getText().toString().isEmpty()){
                    CB_Lotes.setChecked(false);
                    Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
                }else {
                    if (isChecked) {
                        CB_Unidad.setChecked(false);
                        // Mostrar TextView y EditText
                        ET_PiezasCaja.setVisibility(View.VISIBLE);
                        TV_Cantidad.setVisibility(View.VISIBLE);
                        Et_CanCajas.setVisibility(View.VISIBLE);
                        TV_Cajas.setVisibility(View.VISIBLE);
                        BT_Añadir.setVisibility(View.VISIBLE);
                    } else {
                        // Ocultar TextView y EditText
                        ET_PiezasCaja.setVisibility(View.GONE);
                        TV_Cantidad.setVisibility(View.GONE);
                        BT_Añadir.setVisibility(View.GONE);
                        Et_CanCajas.setVisibility(View.GONE);
                        TV_Cajas.setVisibility(View.GONE);
                    }
                }
            }
        });

        CB_Unidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ET_ArtEsperados.getText().toString().isEmpty()){
                    CB_Unidad.setChecked(false);
                    Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
                }else {
                    if (isChecked) {
                        CB_Lotes.setChecked(false);
                        cantidadIngresada = ET_ArtEsperados.getText().toString();
                        showCustomAlertDialog("Número de serie");
                    }
                }
            }
        });


        BT_Añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidadIngresada = ET_ArtEsperados.getText().toString();
                showCustomAlertDialog("QR Caja");
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

        TextView TV_CanEsperada = dialogView.findViewById(R.id.TV_CanEsperada);
        TextView TV_CajasLeidas = dialogView.findViewById(R.id.TV_CajasLeidas);
        TextView TV_ArtLeidos = dialogView.findViewById(R.id.TV_ArtLeidos);
        LinearLayout llPorCajas = dialogView.findViewById(R.id.LL_PorCajas);
        EditText ET_Numserie = dialogView.findViewById(R.id.ET_Numserie);
        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);
        Button BT_Siguiente = dialogView.findViewById(R.id.BT_Siguiente);
        String seleccionado = hintText;


        TV_CanEsperada.setText(cantidadIngresada);
        ET_Numserie.setHint(hintText);
        // Establecer el foco en el EditText
        ET_Numserie.requestFocus();

        if (hintText.equals("QR Caja")){
            llPorCajas.setVisibility(View.VISIBLE);
            BT_Siguiente.setVisibility(View.GONE);
            btnCompletar.setVisibility(View.GONE);

        }else {
            llPorCajas.setVisibility(View.GONE);
            BT_Siguiente.setVisibility(View.GONE);
            btnCompletar.setVisibility(View.GONE);
        }


        // Aquí se pueden agregar más funcionalidades, por ejemplo, al ingresar datos en etLoteSerie
        ET_Numserie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Aquí puedes agregar cualquier lógica que necesites ejecutar cuando el texto cambia
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(!Ban_leido.equals("1")) {
                        Ban_leido = "1";

                        // Verifica si el código introducido es válido (puedes agregar una lógica de validación aquí si es necesario)
                        if (s.length() > 0) {
                            if (seleccionado.equals("QR Caja")) {
                                // Suma de los artículos leídos
                                int datopz = Integer.parseInt(ET_PiezasCaja.getText().toString());
                                int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                                int suma = datopz + datoleidos;
                                TV_ArtLeidos.setText(String.valueOf(suma));

                                // Suma de las cajas leídas
                                int numcajas = Integer.parseInt(TV_CajasLeidas.getText().toString());
                                numcajas++;
                                TV_CajasLeidas.setText(String.valueOf(numcajas));
                            } else {
                                // Suma de los artículos leídos
                                int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                                datoleidos++;
                                TV_ArtLeidos.setText(String.valueOf(datoleidos));
                            }

                            // Muestra el código leído por 2 segundos antes de limpiar el EditText
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ET_Numserie.setText("");
                                    Ban_leido = "0";
                                }
                            }, 500); // 2000 milisegundos = 2 segundos

                            // Comprueba si se ha alcanzado el número esperado de artículos
                            String articulos = TV_ArtLeidos.getText().toString();
                            if (articulos.equals(ET_ArtEsperados.getText().toString())) {
                                BT_Siguiente.setVisibility(View.GONE);
                                btnCompletar.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Manejar la excepción si los valores no son números válidos
                    Toast.makeText(getContext(), "Por favor, ingrese números válidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();

                // Comprueba que haya tenido un fragmento anteriormente
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Regresa al fragmento anterior
                    fragmentManager.popBackStack();
                }
                alertDialog.dismiss();
            }
        });

        BT_Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ET_Numserie.setText("");
                Ban_leido="0";

                String articulos = TV_ArtLeidos.getText().toString();
                if (articulos.equals(ET_ArtEsperados.getText().toString())) {
                    BT_Siguiente.setVisibility(View.GONE);
                    btnCompletar.setVisibility(View.VISIBLE);
                }

            }
        });

        alertDialog.show();
    }
}
