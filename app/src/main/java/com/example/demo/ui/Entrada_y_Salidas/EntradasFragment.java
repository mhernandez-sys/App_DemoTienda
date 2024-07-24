package com.example.demo.ui.Entrada_y_Salidas;

import androidx.fragment.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.main.KeyDwonFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntradasFragment extends KeyDwonFragment {

    private EntradasViewModel mViewModel;
    private String cantidadIngresada;
    private CheckBox CB_Lotes, CB_Unidad;
    private Spinner Sp_Provedor, SP_Producto;
    private TextView TV_Cantidad, TV_Cajas;
    private EditText ET_PiezasCaja, ET_ArtEsperados, Et_CanCajas;
    private Button BT_Añadir;
    private String Ban_leido = "";
    private WebServiceManager webServiceManager;

    private static final String QR_CAJA = "QR Caja";
    private static final String NUMERO_SERIE = "Número de serie";

    public static EntradasFragment newInstance() {
        return new EntradasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_entradas, container, false);
        webServiceManager = new WebServiceManager(requireContext());

        // Obtén los Spinners del layout
        Sp_Provedor = root.findViewById(R.id.sp_proveedor);
        SP_Producto = root.findViewById(R.id.sp_producto);

        // Obtén los CheckBoxes y el EditText del layout
        CB_Lotes = root.findViewById(R.id.CB_Lotes);
        CB_Unidad = root.findViewById(R.id.CB_Unidad);

        BT_Añadir = root.findViewById(R.id.BT_Añadir);

        TV_Cajas = root.findViewById(R.id.TV_Cajas);
        TV_Cantidad = root.findViewById(R.id.TV_Cantidad);
        ET_PiezasCaja = root.findViewById(R.id.ET_PiezasCaja);
        ET_ArtEsperados = root.findViewById(R.id.ET_ArtEsperados);
        Et_CanCajas = root.findViewById(R.id.Et_CanCajas);

        // Llama al WebService para obtener los datos
        obtenerDatosParaSpinners(Sp_Provedor);
        obtenerDatosParaSpinners2(SP_Producto);

        // Añadir listeners a los spinners
        Sp_Provedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        });

        // Añadir listeners a los spinners
        SP_Producto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        });

        CB_Lotes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                CB_Unidad.setChecked(false);
                setLotesVisibility(View.VISIBLE);
            } else {
                setLotesVisibility(View.GONE);
            }
        });

        CB_Unidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                CB_Lotes.setChecked(false);
                cantidadIngresada = ET_ArtEsperados.getText().toString();
                showCustomAlertDialog(NUMERO_SERIE);
            }
        });

        BT_Añadir.setOnClickListener(v -> {
            cantidadIngresada = ET_ArtEsperados.getText().toString();
            showCustomAlertDialog(QR_CAJA);
        });

        return root;
    }

    private void setLotesVisibility(int visibility) {
        ET_PiezasCaja.setVisibility(visibility);
        TV_Cantidad.setVisibility(visibility);
        Et_CanCajas.setVisibility(visibility);
        TV_Cajas.setVisibility(visibility);
        BT_Añadir.setVisibility(visibility);
    }

    private void obtenerDatosParaSpinners(Spinner provedores) {
        webServiceManager.callWebService("Proveedoressp", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        List<String> datosSpinner1 = new ArrayList<>();
                        datosSpinner1.add("Seleccionar");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            datosSpinner1.add(jsonObject.getString("Descripcion_Tipo"));
                        }

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        provedores.setAdapter(adapter1);

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
                            datosSpinner1.add(jsonObject.getString("Descripcion_Tipo"));
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntradasViewModel.class);
    }

    private void showCustomAlertDialog(String hintText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView).setCancelable(false);
        AlertDialog alertDialog = builder.create();

        TextView TV_CanEsperada = dialogView.findViewById(R.id.TV_CanEsperada);
        TextView TV_CajasLeidas = dialogView.findViewById(R.id.TV_CajasLeidas);
        TextView TV_ArtLeidos = dialogView.findViewById(R.id.TV_ArtLeidos);
        LinearLayout llPorCajas = dialogView.findViewById(R.id.LL_PorCajas);
        EditText ET_Numserie = dialogView.findViewById(R.id.ET_Numserie);
        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);
        Button BT_Siguiente = dialogView.findViewById(R.id.BT_Siguiente);

        TV_CanEsperada.setText(cantidadIngresada);
        ET_Numserie.setHint(hintText);
        ET_Numserie.requestFocus();

        if (QR_CAJA.equals(hintText)) {
            llPorCajas.setVisibility(View.VISIBLE);
            BT_Siguiente.setVisibility(View.VISIBLE);
            btnCompletar.setVisibility(View.GONE);
        } else {
            llPorCajas.setVisibility(View.GONE);
            BT_Siguiente.setVisibility(View.VISIBLE);
            btnCompletar.setVisibility(View.GONE);
        }

        ET_Numserie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!Ban_leido.equals("1")) {
                        Ban_leido = "1";
                        if (QR_CAJA.equals(hintText)) {
                            int datopz = Integer.parseInt(ET_PiezasCaja.getText().toString());
                            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                            TV_ArtLeidos.setText(String.valueOf(datopz + datoleidos));

                            int numcajas = Integer.parseInt(TV_CajasLeidas.getText().toString());
                            TV_CajasLeidas.setText(String.valueOf(numcajas + 1));
                        } else {
                            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                            TV_ArtLeidos.setText(String.valueOf(datoleidos + 1));
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor, ingrese números válidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCompletar.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            alertDialog.dismiss();
        });

        BT_Siguiente.setOnClickListener(v -> {
            ET_Numserie.setText("");
            Ban_leido = "0";

            String articulos = TV_ArtLeidos.getText().toString();
            if (articulos.equals(ET_ArtEsperados.getText().toString())) {
                BT_Siguiente.setVisibility(View.GONE);
                btnCompletar.setVisibility(View.VISIBLE);
            }
        });

        alertDialog.show();
    }
}
