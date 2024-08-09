package com.example.demo.ui.Existencias;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.Productos.ListProductos;

public class MenuExistenciasFragment extends Fragment {

    private ImageButton AgregarInventario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Recuperar el objeto "selected_product"
            //String selectedItem = (String) getArguments().getSerializable("selected_product");

            // Recuperar la cadena "articulos"
            String encontrados = (String) getArguments().getSerializable("ART_Leidos");
            String esperados = (String) getArguments().getSerializable("ART_Esperados");
            String Descripcion = (String) getArguments().getSerializable("Descripcion");
            String Clave = (String) getArguments().getSerializable("Clave");
            Toast.makeText(getContext(), "Articulos escaneados" + encontrados, Toast.LENGTH_LONG).show();

            // Ahora puedes usar 'selectedItem' y 'articulos' como necesites en este fragmento
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu_existencias, container, false);
        AgregarInventario = root.findViewById(R.id.IB_AgregarInventario);
        // Obtener los datos del Bundle

        AgregarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_inventario_to_nav_productos, bundle);
            }
        });

        return root;
    }
}