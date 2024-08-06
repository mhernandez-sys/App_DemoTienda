package com.example.demo.ui.Entrada_y_Salidas;

import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import android.widget.LinearLayout;
import android.widget.Switch;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalidasFragment extends KeyDwonFragment {

    private String cantidadIngresada;
    private CheckBox CB_SalLote, CB_SalUnidad;
    private Spinner sp_cliente, sp_producto;

    private WebServiceManager webServiceManager;

    private LinearLayout LL_SalidasLote;
    private EditText Et_SalNumlote, ET_SalidasArtEsperados;
    private TextView ET_FechaSalidas, ET_SalPiezasCaja;
    private Button BT_Añadir;
    private String Ban_leido = "";
    private final List<String> DatosClientes = new ArrayList<>();
    private List<String> DatosProducto = new ArrayList<>();
    private int spinnersLoadedCount = 0;
    private static final String QR_CAJA = "QR Caja";
    private boolean isLocked = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salidas, container, false);

        //Checkbox
        CB_SalLote = view.findViewById(R.id.CB_SalLote);
        CB_SalUnidad = view.findViewById(R.id.CB_SalUnidad);
        // Obtén los Spinners del layout
        sp_cliente = view.findViewById(R.id.sp_cliente);
        sp_producto = view.findViewById(R.id.sp_producto);

        webServiceManager = new WebServiceManager(requireContext());

        //Linear layout para lotes
        LL_SalidasLote = view.findViewById(R.id.LL_SalidasLote);
        //Edit text para lotes y unidad
        Et_SalNumlote = view.findViewById(R.id.Et_SalNumlote);
        ET_SalPiezasCaja = view.findViewById(R.id.ET_SalPiezasCaja);
        ET_SalidasArtEsperados =view.findViewById(R.id.ET_SalidasArtEsperados);
        ET_FechaSalidas = view.findViewById(R.id.ET_FechaSalidas);
        //Boton para llamar a recicler view
        BT_Añadir = view.findViewById(R.id.BT_SalAñadir);

        // Obtener la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Formato de fecha deseado
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        ///Establece la fecha en el ET+
        ET_FechaSalidas.setText(currentDate);

        DialogoAnimaciones.showLoadingDialog(getContext());
        // Llama al WebService para obtener los datos
        llenarSpinners(sp_cliente, DatosClientes, "clientessp", "id_Cliente", "Nombre");
        llenarSpinners(sp_producto, DatosProducto, "productossp", "id_Prod", "Descripcion");

        CB_SalLote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ET_SalidasArtEsperados.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
                }else {
                    if (isChecked) {
                        CB_SalUnidad.setChecked(false);
                        cantidadIngresada = ET_SalidasArtEsperados.getText().toString();
                        ET_SalPiezasCaja.setText(cantidadIngresada);
                        LL_SalidasLote.setVisibility(View.VISIBLE);
                    } else {
                        LL_SalidasLote.setVisibility(View.GONE);
                    }
                }
            }
        });

        CB_SalUnidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ET_SalidasArtEsperados.getText().toString().isEmpty()){
                    CB_SalUnidad.setChecked(false);
                    Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
                }else {
                    if (isChecked) {
                        CB_SalLote.setChecked(false);
                        cantidadIngresada = ET_SalidasArtEsperados.getText().toString();
                        showCustomAlertDialog("Número de serie");
                    }
                }
            }
        });

        BT_Añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar_producto();
            }
        });
        return view;
    }

    private void showCustomAlertDialog(String hintText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView).setCancelable(false);
        AlertDialog alertDialog = builder.create();


        TextView TV_CanEsperada = dialogView.findViewById(R.id.TV_CanEsperada);
        TextView TV_ArtLeidos = dialogView.findViewById(R.id.TV_ArtLeidos);
        LinearLayout llPorCajas = dialogView.findViewById(R.id.LL_PorCajas);
        EditText ET_NumserieAutomatico = dialogView.findViewById(R.id.ET_NumserieAutomatico);
        EditText ET_NumserieManual = dialogView.findViewById(R.id.ET_NumserieManual);
        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);
        Button BT_Siguiente = dialogView.findViewById(R.id.BT_Siguiente);
        Switch SW_Modo = dialogView.findViewById(R.id.SW_Modo);
        TextView TV_SWEstatus = dialogView.findViewById(R.id.TV_SWEstatus);
        Button BT_Cancelar = dialogView.findViewById(R.id.BT_Cancelar);

        TV_CanEsperada.setText(cantidadIngresada);
        ET_NumserieAutomatico.requestFocus();  // Coloca el foco en el EditText automatico
        ET_NumserieAutomatico.setHint(hintText);

        if (QR_CAJA.equals(hintText)) {
            llPorCajas.setVisibility(View.VISIBLE);
        } else {
            llPorCajas.setVisibility(View.GONE);
        }
        BT_Siguiente.setVisibility(View.GONE);
        btnCompletar.setVisibility(View.GONE);

        SW_Modo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TV_SWEstatus.setText("Manual");
                    ET_NumserieManual.setEnabled(true); // Habilitar el EditText
                    ET_NumserieManual.setVisibility(View.VISIBLE);
                    ET_NumserieAutomatico.setVisibility(View.GONE);
                    BT_Siguiente.setVisibility(View.VISIBLE);
                    ET_NumserieManual.requestFocus();  // Coloca el foco en el EditText manual
                    ET_NumserieManual.setHint(hintText);

                } else {
                    TV_SWEstatus.setText("Automático");
                    ET_NumserieAutomatico.setEnabled(true);
                    ET_NumserieManual.setVisibility(View.GONE);
                    ET_NumserieAutomatico.setVisibility(View.VISIBLE);
                    BT_Siguiente.setVisibility(View.GONE);
                    ET_NumserieAutomatico.requestFocus();  // Coloca el foco en el EditText automatico
                    ET_NumserieAutomatico.setHint(hintText);
                    Ban_leido = "0";
                    isLocked = false; // Asegúrate de marcar el procesamiento como terminado
                }
            }
        });

        BT_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                alertDialog.dismiss();
            }
        });

        ET_NumserieAutomatico.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Si el EditText está bloqueado, no permitir entradas adicionales
                if (isLocked) {
                    return ""; // No permitir la entrada de texto
                }
                return null; // Permitir el texto si no está bloqueado
            }
        }});

        ET_NumserieAutomatico.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isLocked) {
                    // Si ya se está procesando, no hacemos nada
                    return;
                }
                isLocked = true; // Marca que estamos procesando un código
                try {
                    if (!Ban_leido.equals("1")) {
                        Ban_leido = "1";
                        // Verifica si el código introducido es válido (puedes agregar una lógica de validación aquí si es necesario)
                        if (s.length() > 0) {
                            // Suma de los artículos leídos
                            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                            int Artesperados = Integer.parseInt(cantidadIngresada);
                            datoleidos++;
                            TV_ArtLeidos.setText(String.valueOf(datoleidos));
                            if (datoleidos == Artesperados ){
                                ET_NumserieAutomatico.setVisibility(View.GONE);
                                ET_NumserieAutomatico.clearFocus(); // Quita el foco del EditText
                                ET_NumserieAutomatico.setEnabled(false); // Bloquea el EditText
                                //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
                            }
                            // Muestra el código leído por 2 segundos antes de limpiar el EditText
                            new Handler().postDelayed(() -> {
                                ET_NumserieAutomatico.setText("");
                                Ban_leido = "0";
                                isLocked = false; // Marca que el procesamiento ha terminado
                            }, 500); // 2000 milisegundos = 2 segundos

                            // Comprueba si se ha alcanzado el número esperado de artículos
                            String articulos = TV_ArtLeidos.getText().toString();
                            if (articulos.equals(ET_SalidasArtEsperados.getText().toString())) {
                                BT_Siguiente.setVisibility(View.GONE);
                                btnCompletar.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor, ingrese números válidos.", Toast.LENGTH_SHORT).show();
                    isLocked = false; // Asegúrate de marcar el procesamiento como terminado

                }
            }
        });

        btnCompletar.setOnClickListener(v -> {
            eliminar_producto();
            alertDialog.dismiss();

        });

        BT_Siguiente.setOnClickListener(v -> {
            // Suma de los artículos leídos
            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
            int Artesperados = Integer.parseInt(cantidadIngresada);
            datoleidos++;
            TV_ArtLeidos.setText(String.valueOf(datoleidos));
            if (datoleidos == Artesperados ){
                ET_NumserieManual.setVisibility(View.GONE);
                ET_NumserieManual.clearFocus(); // Quita el foco del EditText
                ET_NumserieManual.setEnabled(false); // Bloquea el EditText
                //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
            }
            // Comprueba si se ha alcanzado el número esperado de artículos
            String articulos = TV_ArtLeidos.getText().toString();
            if (articulos.equals(ET_SalidasArtEsperados.getText().toString())) {
                BT_Siguiente.setVisibility(View.GONE);
                btnCompletar.setVisibility(View.VISIBLE);
            }
            ET_NumserieAutomatico.setText("");
            ET_NumserieManual.setText("");

        });
        alertDialog.show();
    }
    private void llenarSpinners(Spinner provedores, List datos, String metodo, String id, String Descripcion) {
        webServiceManager.callWebService(metodo, null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
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
                        spinnersLoadedCount++;
                        checkAllSpinnersLoaded();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: SF-285", () -> llenarSpinners(provedores,datos,metodo,id,Descripcion));
                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: SF-288", () -> llenarSpinners(provedores,datos,metodo,id,Descripcion));

                }
            }
        });
    }

    public class TipoItem {
        private String idTipo;
        private final String  descripcion;

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

        @NonNull
        @Override
        public String toString() {
            return descripcion; // Esto es lo que se mostrará en el Spinner
        }
    }

    private void eliminar_producto() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        TipoItem selected_cliente = (TipoItem) sp_cliente.getSelectedItem();
        TipoItem selected_producto = (TipoItem) sp_producto.getSelectedItem();
        // Obtener el id_Tipo del elemento seleccionado
        String Cliente = selected_cliente.getIdTipo();
        String Producto = selected_producto.getIdTipo();
        String Cantidad = cantidadIngresada;
        String Fecha = ET_FechaSalidas.getText().toString();

        Map<String, String> propeties = new HashMap<>();
        propeties.put("nuevoTM","2");
        propeties.put("nuevoproducto",Producto);
        propeties.put("nuevacant",Cantidad);
        propeties.put("nuevafecha",Fecha);
        propeties.put("nuevocliente",Cliente);

        webServiceManager.callWebService("GuardarSalidas ", propeties, result -> {
            if (result != null) {
                try {
                    if(result.equals("Compra aprobada.")){
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        DialogoAnimaciones.hideLoadingDialog();
                        salir();
                    } else if (result.contains("Stock insuficiente.")) {
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        DialogoAnimaciones.hideLoadingDialog();
                        reset();
                    }else {
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        DialogoAnimaciones.hideLoadingDialog();
                        reset();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    DialogoAnimaciones.hideLoadingDialog();
                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void salir(){
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    private void checkAllSpinnersLoaded() {
        if (spinnersLoadedCount == 2) { // Cambiar a 2 si tienes dos Spinners
            DialogoAnimaciones.hideLoadingDialog();
        }
    }

    public void reset() {
        CB_SalLote.setChecked(false);
        CB_SalUnidad.setChecked(false);
        ET_SalidasArtEsperados.setText("");
        ET_FechaSalidas.setText("");
        Et_SalNumlote.setText("");
        ET_SalPiezasCaja.setText("");
        sp_cliente.setSelection(0);
        sp_producto.setSelection(0);
    }

}
