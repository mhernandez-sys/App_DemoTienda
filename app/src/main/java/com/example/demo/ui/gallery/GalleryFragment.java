package com.example.demo.ui.gallery;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.databinding.FragmentGalleryBinding;
import com.example.demo.main.KeyDwonFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GalleryFragment extends KeyDwonFragment {

    private Spinner SP_TipoProducto, SP_ClasProd;
    private EditText ET_DescProducto, ET_ClaveProducto;
    public Button BT_Guardar, BT_Imprimir;
    private WebServiceManager webServiceManager;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa WebServiceManager
        webServiceManager = new WebServiceManager(requireContext());

        //Spiners que son llenados en WS
        SP_TipoProducto = root.findViewById(R.id.SP_TipoProducto);
        SP_ClasProd = root.findViewById(R.id.SP_ClasProd);
        //Edit_text  para introcucir descripcion y gernerar la clave
        ET_DescProducto = root.findViewById(R.id.ET_DescProducto);
        ET_ClaveProducto = root.findViewById(R.id.ET_ClaveProducto);
        //Botones para imprimir y llamar a WS
        BT_Guardar = root.findViewById(R.id.BT_Guardar);
        BT_Imprimir = root.findViewById(R.id.BT_Imprimir);

        // Llama al WebService para obtener los datos
        tipoProducto(SP_TipoProducto);
        ClasProd(SP_ClasProd);
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

|


        BT_Imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLayout();
            }
        });

        return root;
    }

    private void tipoProducto(Spinner provedores) {
        webServiceManager.callWebService("Tipo_Producto", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
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
    private void ClasProd(Spinner productos) {
        webServiceManager.callWebService("Clasifica_producto", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
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
                        productos.setAdapter(adapter1);

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
}