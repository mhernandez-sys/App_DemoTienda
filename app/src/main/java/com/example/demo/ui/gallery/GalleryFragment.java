package com.example.demo.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.databinding.FragmentGalleryBinding;
import com.example.demo.main.KeyDwonFragment;

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

        SP_TipoProducto.setOnItemSelectedListener(listener);
        SP_ClasProd.setOnItemSelectedListener(listener);

        BT_Imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLayout();
            }
        });

        return root;
    }

    private void generarNumeroSerie(Spinner SP_TipoProducto, Spinner SP_ClasProd, Spinner spinner3, Spinner spinner4, EditText etNumeroSerie) {
        String tipoProducto = SP_TipoProducto.getSelectedItem().toString();
        String clasProd = SP_ClasProd.getSelectedItem().toString();
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