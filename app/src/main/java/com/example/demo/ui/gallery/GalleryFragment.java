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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.demo.MainActivity;
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

    private MainActivity mContext;
    private TextView producto;
    private Spinner spTipoProducto, spClasProd, DescripcionProducto, spClaveProducto;
    private EditText numserie;
    public Button btn_imprimir;
    private WebServiceManager webServiceManager;
    private FragmentGalleryBinding binding;
    private boolean isLayout1 = true; // Variable para controlar el layout actual

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa WebServiceManager
        webServiceManager = new WebServiceManager(requireContext());

        // Obtén los Spinners y el EditText del binding
        Spinner spinner1 = binding.spTipoProducto;
        Spinner spinner2 = binding.spClasProd;
        Spinner spinner3 = binding.DescripcionProducto;
        Spinner spinner4 = binding.spClaveProducto;
        EditText etNumeroSerie = binding.etNumeroser;
        Button buttonChangeLayout = binding.btncancelar;

        // Llama al WebService para obtener los datos
        obtenerDatosParaSpinners(spinner1, spinner2, spinner3, spinner4);

        // Añadir listeners a los spinners
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generarNumeroSerie(spinner1, spinner2, spinner3, spinner4, etNumeroSerie);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        };

        spinner1.setOnItemSelectedListener(listener);
        spinner2.setOnItemSelectedListener(listener);
        spinner3.setOnItemSelectedListener(listener);
        spinner4.setOnItemSelectedListener(listener);

        btn_imprimir = root.findViewById(R.id.btn_imprimir);
        numserie = root.findViewById(R.id.et_numeroser);

        btn_imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLayout();
            }
        });

        return root;
    }

    private void obtenerDatosParaSpinners(Spinner spinner1, Spinner spinner2, Spinner spinner3, Spinner spinner4) {
        webServiceManager.callWebService("ObtenerProductosConDetalles", new HashMap<>(), new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        List<String> datosSpinner1 = new ArrayList<>();
                        List<String> datosSpinner2 = new ArrayList<>();
                        List<String> datosSpinner3 = new ArrayList<>();
                        List<String> datosSpinner4 = new ArrayList<>();

                        // Agrega la opción "Seleccionar" como primera opción
                        datosSpinner1.add("Seleccionar");
                        datosSpinner2.add("Seleccionar");
                        datosSpinner3.add("Seleccionar");
                        datosSpinner4.add("Seleccionar");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            datosSpinner1.add(jsonObject.getString("Descripcion_Tipo"));
                            datosSpinner2.add(jsonObject.getString("Descripcion_Clasificacion"));
                            datosSpinner3.add(jsonObject.getString("Descripcion"));
                            datosSpinner4.add(jsonObject.getString("ClaveProds"));
                        }

                        // Adaptador para el primer Spinner
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner1.setAdapter(adapter1);

                        // Adaptador para el segundo Spinner
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner2);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);

                        // Adaptador para el tercer Spinner
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner3);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner3.setAdapter(adapter3);

                        // Adaptador para el cuarto Spinner
                        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, datosSpinner4);
                        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner4.setAdapter(adapter4);

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

    private void generarNumeroSerie(Spinner spinner1, Spinner spinner2, Spinner spinner3, Spinner spinner4, EditText etNumeroSerie) {
        String tipoProducto = spinner1.getSelectedItem().toString();
        String clasProd = spinner2.getSelectedItem().toString();
        String descripcionProducto = spinner3.getSelectedItem().toString();
        String claveProducto = spinner4.getSelectedItem().toString();

        if (!tipoProducto.equals("Seleccionar") && !clasProd.equals("Seleccionar") &&
                !descripcionProducto.equals("Seleccionar") && !claveProducto.equals("Seleccionar")) {
            String numeroSerie = tipoProducto.substring(0, 2) + clasProd.substring(0, 2) +
                    descripcionProducto.substring(0, 2) + claveProducto.substring(0, 2) +
                    System.currentTimeMillis();
            etNumeroSerie.setText(numeroSerie);
        } else {
            etNumeroSerie.setText("");
        }
    }

    private void cambiarLayout() {
        String codigo = numserie.getText().toString();
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