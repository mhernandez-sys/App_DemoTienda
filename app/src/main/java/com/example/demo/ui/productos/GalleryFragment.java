package com.example.demo.ui.productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.databinding.FragmentGalleryBinding;
import com.example.demo.main.KeyDwonFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends KeyDwonFragment {

    private Spinner SP_TipoProducto, SP_ClasProd;
    private EditText ET_DescProducto, ET_ClaveProducto;
    public Button BT_Guardar, BT_Imprimir;
    private WebServiceManager webServiceManager;
    private FragmentGalleryBinding binding;
    private List<TipoItem> datosProductos = new ArrayList<>();
    private List<String> datosClasificacionP = new ArrayList<>();
    private int spinnersLoadedCount = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa WebServiceManager
        webServiceManager = new WebServiceManager(requireContext());
        //Spiners que son llenados en WS
        SP_TipoProducto = root.findViewById(R.id.SP_TipoProducto);
        SP_ClasProd = root.findViewById(R.id.SP_ClasProd);
        //Edit_text  para introcucir descripcion y gernerar la clave
        ET_DescProducto = root.findViewById(R.id.ET_DescProducto);
        ET_ClaveProducto = root.findViewById(R.id.ET_ClaveProducto<)>;
        //Botones para imprimir y llamar a WS
        BT_Guardar = root.findViewById(R.id.BT_Guardar);
        BT_Imprimir = root.findViewById(R.id.BT_Imprimir);
        // Llama al WebService para obtener los datos
        DialogoAnimaciones.showLoadingDialog(getContext());
        llenarSpinners(SP_TipoProducto, datosProductos, "Tipo_Producto", "id_Tipo");
       llenarSpinners(SP_ClasProd, datosClasificacionP, "Clasifica_producto", "id_ClasiP");
        //obtenerDatosParaSpinners(SP_TipoProducto, SP_ClasProd, spinner3, spinner4);

        // Añadir listeners a los spinners
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //generarNumeroSerie(SP_TipoProducto, SP_ClasProd, spinner3, spinner4, etNumeroSerie);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        };

        BT_Imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLayout();
            }
        });

        BT_Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoItem selected_tipoProducto = (TipoItem) SP_TipoProducto.getSelectedItem();
                TipoItem selected_clasProducto = (TipoItem) SP_ClasProd.getSelectedItem();
                // Obtener el id_Tipo del elemento seleccionado
                String idTipo = selected_tipoProducto.getIdTipo();
                String idClasificacion = selected_clasProducto.getIdTipo();
                if(idTipo.equals("Seleccionar")||idClasificacion.equals("Seleccionar")||ET_ClaveProducto.getText().toString().isEmpty()||ET_DescProducto.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Campos incompletos, favor de completar todos los campos", Toast.LENGTH_LONG).show();
                }else {
                    insertar_producto();
                }
            }
        });

        return root;
    }

    private void llenarSpinners(Spinner provedores, List datos, String metodo, String id) {
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
                            String descripcion = jsonObject.getString("Descripcion");
                            datos.add(new TipoItem(idTipo, descripcion));
                        }

                        ArrayAdapter<TipoItem> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        provedores.setAdapter(adapter);
                        spinnersLoadedCount++;
                        checkAllSpinnersLoaded();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarSpinners(provedores,datos,metodo,id));

                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarSpinners(provedores,datos,metodo,id));
                }
            }
        });
    }


    private void cambiarLayout() {
        String codigo = ET_ClaveProducto.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("barcode", codigo);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        // Navegar al fragmento BarcodeFragment
        navController.navigate(R.id.action_nav_gallery_to_nav_barcode, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

        TipoItem selected_tipoProducto = (TipoItem) SP_TipoProducto.getSelectedItem();
        TipoItem selected_clasProducto = (TipoItem) SP_ClasProd.getSelectedItem();
        // Obtener el id_Tipo del elemento seleccionado
        String idTipo = selected_tipoProducto.getIdTipo();
        String idClasificacion = selected_clasProducto.getIdTipo();
        String Descripcion = ET_DescProducto.getText().toString();
        String Clave = ET_ClaveProducto.getText().toString();

        Map<String, String> propeties = new HashMap<>();
        propeties.put("Descripcion",Descripcion);
        propeties.put("existencia","0");
        propeties.put("tipo",idTipo);
        propeties.put("clasificacionp",idClasificacion);
        propeties.put("claveprod",Clave);

        webServiceManager.callWebService("GuardarProductos", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        Toast.makeText(getContext(), "Se inserto con exito", Toast.LENGTH_LONG).show();
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

    private void checkAllSpinnersLoaded() {
        if (spinnersLoadedCount == 2) { // Cambiar a 2 si tienes dos Spinners
            DialogoAnimaciones.hideLoadingDialog();
        }
    }
}