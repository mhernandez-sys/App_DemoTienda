package com.example.demo.ui.Entrada_y_Salidas;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class EntradasFragment extends KeyDwonFragment {

    private String cantidadIngresada;
    private CheckBox CB_Lotes, CB_Unidad;
    private Spinner Sp_Provedor, SP_Producto, SP_concepto;
    private TextView TV_Cantidad, TV_Cajas, ET_FechaEntrada,ET_PiezasCaja;
    private EditText  ET_ArtEsperados, Et_NumLotes;
    private Button BT_Añadir;
    private String Ban_leido = "";
    private WebServiceManager webServiceManager;
    private String seleccionado = "";
    private List<String> DatosProvedor = new ArrayList<>();
    private List<String> DatosProducto = new ArrayList<>();
    private List<String> DatosConcepto = new ArrayList<>();
    private int spinnersLoadedCount = 0;
    public String SKU;

    private static final String QR_CAJA = "QR Caja";
    private boolean isLocked = false;

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
        SP_concepto = root.findViewById(R.id.sp_concepto);

        // Obtén los CheckBoxes y el EditText del layout
        CB_Lotes = root.findViewById(R.id.CB_Lotes);
        CB_Unidad = root.findViewById(R.id.CB_Unidad);

        BT_Añadir = root.findViewById(R.id.BT_Añadir);

        TV_Cajas = root.findViewById(R.id.TV_Cajas);
        TV_Cantidad = root.findViewById(R.id.TV_Cantidad);
        ET_PiezasCaja = root.findViewById(R.id.ET_PiezasCaja);
        ET_ArtEsperados = root.findViewById(R.id.ET_ArtEsperados);
        Et_NumLotes = root.findViewById(R.id.Et_NumLotes);
        ET_FechaEntrada = root.findViewById(R.id.ET_FechaEntrada);

        DialogoAnimaciones.showLoadingDialog(getContext());
        // Llama al WebService para obtener los datos
        llenarSpinners(Sp_Provedor, DatosProvedor, "Proveedoressp", "id_Proveedores", "Nombre", null);
        llenarSpinners(SP_Producto, DatosProducto, "productossp", "id_Prod", "Descripcion", null);
        llenarSpinners(SP_concepto, DatosConcepto,"Conceptosp","Id_concepto", "Descripcion","1");

        // Obtener la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Formato de fecha deseado
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        ///Establece la fecha en el ET+
        ET_FechaEntrada.setText(currentDate);

        CB_Lotes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(ET_ArtEsperados.getText().toString().isEmpty()){
                CB_Lotes.setChecked(false);
                Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
            }else {
                if (isChecked) {
                    CB_Unidad.setChecked(false);
                    // Mostrar TextView y EditText
                    setLotesVisibility(View.VISIBLE);
                    cantidadIngresada = ET_ArtEsperados.getText().toString();
                    ET_PiezasCaja.setText(cantidadIngresada);
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
            if (validarEntradas()) {
                insertar_producto();
            } else {
                Toast.makeText(getContext(), "Por favor, asegúrese de que todos los campos estén llenos.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean validarEntradas() {
        // Comprueba que se haya seleccionado un elemento de los spinners que no sea "Seleccionar"
        boolean spinnersLlenos = !((TipoItem) Sp_Provedor.getSelectedItem()).getIdTipo().equals("0") &&
                !((TipoItem) SP_Producto.getSelectedItem()).getIdTipo().equals("0") &&
                !((TipoItem) SP_concepto.getSelectedItem()).getIdTipo().equals("0");

        // Comprueba que el EditText no esté vacío
        boolean editTextLleno = !ET_ArtEsperados.getText().toString().trim().isEmpty();

        return spinnersLlenos && editTextLleno;
    }

    private void setLotesVisibility(int visibility) {
        ET_PiezasCaja.setVisibility(visibility);
        TV_Cantidad.setVisibility(visibility);
        Et_NumLotes.setVisibility(visibility);
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
        AtomicReference<String> numSerieAutomatico = new AtomicReference<>("");
        AtomicReference<String> numSerieManual = new AtomicReference<>("");
        SKU = "";


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
                                    // Obtener el texto de los EditText
                                    //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
                                }
                            // Muestra el código leído por 2 segundos antes de limpiar el EditText
                            new Handler().postDelayed(() -> {
                                numSerieAutomatico.set(ET_NumserieAutomatico.getText().toString().trim());
                                SKU += numSerieAutomatico+ ",";
                                ET_NumserieAutomatico.setText("");
                                Ban_leido = "0";
                                isLocked = false; // Marca que el procesamiento ha terminado
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
                    isLocked = false; // Asegúrate de marcar el procesamiento como terminado

                }
            }
        });

        btnCompletar.setOnClickListener(v -> {
            insertar_producto();
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
                BT_Siguiente.setVisibility(View.GONE);
                btnCompletar.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
            }
            numSerieManual.set(ET_NumserieManual.getText().toString().trim());
            SKU += numSerieManual+ ",";
            ET_NumserieAutomatico.setText("");
            ET_NumserieManual.setText("");
        });
        alertDialog.show();
    }

    private void llenarSpinners(Spinner provedores, List datos, String metodo, String id, String Descripcion, String Entradas) {
        Map<String, String> propeties = new HashMap<>();
        // Solo agregar 'Entradas' si no es nulo y no está vacío
        if (Entradas != null && !Entradas.isEmpty()) {
        propeties.put("entrada",Entradas);
        }

        webServiceManager.callWebService(metodo, propeties, new WebServiceManager.WebServiceCallback() {
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
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: EF-273", () -> llenarSpinners(provedores,datos,metodo,id,Descripcion, Entradas));
                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: EF-276", new Runnable() {
                        @Override
                        public void run() {
                            llenarSpinners(provedores,datos,metodo,id,Descripcion, Entradas);
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
        DialogoAnimaciones.showLoadingDialog(getContext());
        TipoItem selected_provedor = (TipoItem) Sp_Provedor.getSelectedItem();
        TipoItem selected_producto = (TipoItem) SP_Producto.getSelectedItem();
        TipoItem selected_concepto = (TipoItem) SP_concepto.getSelectedItem();
        // Obtener el id_Tipo del elemento seleccionado
        String Provedor = selected_provedor.getIdTipo();
        String Producto = selected_producto.getIdTipo();
        String Concepto = selected_concepto.getIdTipo();
        String Cantidad = cantidadIngresada;
        String Fecha = ET_FechaEntrada.getText().toString();
        String NumLote = Et_NumLotes.getText().toString();

        if(NumLote.isEmpty()){
            Cantidad = "1";
        }

        Map<String, String> propeties = new HashMap<>();
        propeties.put("nuevoTM","1");
        propeties.put("nuevoproducto",Producto);
        propeties.put("concepto",Concepto);
        propeties.put("nuevacant",Cantidad);
        propeties.put("sku",SKU);
        propeties.put("NumLote",NumLote);
        propeties.put("nuevafecha",Fecha);
        propeties.put("nuevoprovedor",Provedor);

        webServiceManager.callWebService("EntradasManAut", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        DialogoAnimaciones.hideLoadingDialog();
                        salir();

                    } catch (Exception e) {
                        DialogoAnimaciones.hideLoadingDialog();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
                    DialogoAnimaciones.hideLoadingDialog();
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

    private void checkAllSpinnersLoaded() {
        if (spinnersLoadedCount == 3) { // Cambiar a 2 si tienes dos Spinners
            DialogoAnimaciones.hideLoadingDialog();
        }
    }

    public void reset() {
        CB_Lotes.setChecked(false);
        CB_Unidad.setChecked(false);
        ET_ArtEsperados.setText("");
        ET_FechaEntrada.setText("");
        Et_NumLotes.setText("");
        ET_PiezasCaja.setText("");
        Sp_Provedor.setSelection(0);
        SP_Producto.setSelection(0);
    }

}
