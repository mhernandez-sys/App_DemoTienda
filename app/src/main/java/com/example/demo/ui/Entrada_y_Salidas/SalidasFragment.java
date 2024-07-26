package com.example.demo.ui.Entrada_y_Salidas;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.main.KeyDwonFragment;
import com.example.demo.ui.gallery.GalleryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalidasFragment extends KeyDwonFragment {

    private String cantidadIngresada;
    private CheckBox CB_SalLote, CB_SalUnidad;
    private Spinner sp_cliente, sp_producto;

    private WebServiceManager webServiceManager;

    private LinearLayout LL_SalidasLote;
    private EditText Et_SalCajasCan, ET_SalPiezasCaja, ET_SalidasArtEsperados, ET_FechaSalidas;
    private Button BT_Añadir;
    private String Ban_leido = "";
    private List<String> DatosClientes = new ArrayList<>();
    private List<String> DatosProducto = new ArrayList<>();


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

        // Llama al WebService para obtener los datos
        llenarSpinners(sp_cliente, DatosClientes, "clientessp", "id_Cliente", "Nombre");
        llenarSpinners(sp_producto, DatosProducto, "productossp", "id_Prod", "Descripcion");

//        obtenerDatosParaSpinners(sp_cliente);
//        obtenerDatosParaSpinners2(sp_producto);

        //Linear layout para lotes
        LL_SalidasLote = view.findViewById(R.id.LL_SalidasLote);
        //Edit text para lotes y unidad
        Et_SalCajasCan = view.findViewById(R.id.Et_SalCajasCan);
        ET_SalPiezasCaja = view.findViewById(R.id.ET_SalPiezasCaja);
        ET_SalidasArtEsperados =view.findViewById(R.id.ET_SalidasArtEsperados);
        ET_FechaSalidas = view.findViewById(R.id.ET_FechaSalidas);
        //Boton para llamar a recicler view
        BT_Añadir = view.findViewById(R.id.BT_SalAñadir);

        CB_SalLote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ET_SalidasArtEsperados.getText().toString().isEmpty()){
                    CB_SalLote.setChecked(false);
                    Toast.makeText(getContext(), "Por favor, Introdusca la cantidad total que va a salir.", Toast.LENGTH_SHORT).show();
                }else {
                    if (isChecked) {
                        CB_SalUnidad.setChecked(false);
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
                cantidadIngresada = ET_SalidasArtEsperados.getText().toString();
                showCustomAlertDialog("QR Caja");
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
                                int datopz = Integer.parseInt(ET_SalPiezasCaja.getText().toString());
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
                            if (articulos.equals(ET_SalidasArtEsperados.getText().toString())) {
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
                if (articulos.equals(ET_SalidasArtEsperados.getText().toString())) {
                    BT_Siguiente.setVisibility(View.GONE);
                    btnCompletar.setVisibility(View.VISIBLE);
                }

            }
        });

        alertDialog.show();
    }

    private void llenarSpinners(Spinner provedores, List datos, String metodo, String id, String Descripcion) {
        webServiceManager.callWebService(metodo, null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
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
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data from server", Toast.LENGTH_LONG).show();
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

        TipoItem selected_cliente = (TipoItem) sp_cliente.getSelectedItem();
        TipoItem selected_producto = (TipoItem) sp_producto.getSelectedItem();
        // Obtener el id_Tipo del elemento seleccionado
        String Cliente = selected_cliente.getIdTipo();
        String Producto = selected_producto.getIdTipo();
        String Cantidad = ET_SalidasArtEsperados.getText().toString();
        String Fecha = ET_FechaSalidas.getText().toString();

        Map<String, String> propeties = new HashMap<>();
        propeties.put("nuevoTM","2");
        propeties.put("nuevoproducto",Producto);
        propeties.put("nuevacant",Cantidad);
        propeties.put("nuevafecha",Fecha);
        propeties.put("nuevocliente",Cliente);

        webServiceManager.callWebService("GuardarSalidas ", propeties, new WebServiceManager.WebServiceCallback() {
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
