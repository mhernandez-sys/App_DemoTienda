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

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.main.KeyDwonFragment;
import com.example.demo.ui.productos.GalleryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class EntradasFragment extends KeyDwonFragment {

    private String cantidadIngresada;
    private CheckBox CB_Lotes, CB_Unidad;
    private Spinner Sp_Provedor, SP_Producto;
    private TextView TV_Cantidad, TV_Cajas;
    private EditText ET_PiezasCaja, ET_ArtEsperados, Et_CanCajas, ET_FechaEntrada;
    private Button BT_Añadir;
    private String Ban_leido = "";
    private WebServiceManager webServiceManager;
    private String seleccionado = "";
    private List<String> DatosProvedor = new ArrayList<>();
    private List<String> DatosProducto = new ArrayList<>();


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
        ET_FechaEntrada = root.findViewById(R.id.ET_FechaEntrada);

        // Llama al WebService para obtener los datos
        llenarSpinners(Sp_Provedor, DatosProvedor, "Proveedoressp", "id_Proveedores", "Nombre");
        llenarSpinners(SP_Producto, DatosProducto, "productossp", "id_Prod", "Descripcion");


        CB_Lotes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(ET_ArtEsperados.getText().toString().isEmpty()){
                CB_Lotes.setChecked(false);
                Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
            }else {
                if (isChecked) {
                    CB_Unidad.setChecked(false);
                    // Mostrar TextView y EditText
                    setLotesVisibility(View.VISIBLE);
                } else {
                    // Ocultar TextView y EditText
                    setLotesVisibility(View.GONE);
                }
            }
        });

        CB_Unidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showCustomAlertDialog(String hintText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        seleccionado = hintText;

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
        } else {
            llPorCajas.setVisibility(View.GONE);
        }
        BT_Siguiente.setVisibility(View.GONE);
        btnCompletar.setVisibility(View.GONE);

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
                            new Handler().postDelayed(() -> {
                                ET_Numserie.setText("");
                                Ban_leido = "0";
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
                    Toast.makeText(getContext(), "Por favor, ingrese números válidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCompletar.setOnClickListener(v -> {
            insertar_producto();
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

    private void llenarSpinners(Spinner provedores, List datos, String metodo, String id, String Descripcion) {
        DialogoAnimaciones.hideLoadingDialog();
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService(metodo, null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                DialogoAnimaciones.hideLoadingDialog();
                if (result != null||result.contains("Error")) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        datos.add(new TipoItem("0", "Seleccionar")); // Opción predeterminada
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String idTipo = jsonObject.getString(id);
                            String descripcion = jsonObject.getString(Descripcion);
                            datos.add(new TipoItem(idTipo, descripcion));
                        }

                        ArrayAdapter<GalleryFragment.TipoItem> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        provedores.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: EF-273", () -> llenarSpinners(provedores,datos,metodo,id,Descripcion));
                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: EF-276", new Runnable() {
                        @Override
                        public void run() {
                            llenarSpinners(provedores,datos,metodo,id,Descripcion);
                        }
                    });
                }
            }
        });
    }

    public class TipoItem {
        private String idTipo;
        private String descripcion;

        public TipoItem(String idTipo, String descripcion) {
            this.idTipo = idTipo;
            this.descripcion = descripcion;
        }

        public String getIdTipo() {
            return idTipo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion; // Esto es lo que se mostrará en el Spinner
        }
    }

    private void insertar_producto() {

        TipoItem selected_provedor = (TipoItem) Sp_Provedor.getSelectedItem();
        TipoItem selected_producto = (TipoItem) SP_Producto.getSelectedItem();
        // Obtener el id_Tipo del elemento seleccionado
        String Provedor = selected_provedor.getIdTipo();
        String Producto = selected_producto.getIdTipo();
        String Cantidad = ET_ArtEsperados.getText().toString();
        String Fecha = ET_FechaEntrada.getText().toString();

        Map<String, String> propeties = new HashMap<>();
        propeties.put("nuevoTM","1");
        propeties.put("nuevoproducto",Producto);
        propeties.put("nuevacant",Cantidad);
        propeties.put("nuevafecha",Fecha);
        propeties.put("nuevoprovedor",Provedor);

        webServiceManager.callWebService("GuardarEntradas", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        salir();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void salir(){
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }


}
