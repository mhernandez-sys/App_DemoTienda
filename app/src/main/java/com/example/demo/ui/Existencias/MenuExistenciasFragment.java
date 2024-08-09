package com.example.demo.ui.Existencias;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.demo.R;
import com.example.demo.ReciclerView.Existencias.ListAdapterExistencias;
import com.example.demo.ReciclerView.Existencias.ListExistencias;
import com.example.demo.WebServiceManager;

import java.util.List;

public class MenuExistenciasFragment extends Fragment {

    private List<ListExistencias> listExistencias;
    private RecyclerView recyclerView;
    private ListAdapterExistencias listAdapterExistencias;
    private WebServiceManager webServiceManager;
    private ImageButton AgregarInventario;
    private ListExistencias selectedItem; // Para almacenar el producto seleccionado

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu_existencias, container, false);
        webServiceManager = new WebServiceManager(getContext());
        AgregarInventario = root.findViewById(R.id.IB_AgregarInventario);
        
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