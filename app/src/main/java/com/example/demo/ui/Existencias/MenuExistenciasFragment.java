package com.example.demo.ui.Existencias;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.Existencias.ListAdapterExistencias;
import com.example.demo.ReciclerView.Existencias.ListExistencias;
import com.example.demo.ReciclerView.Productos.ListProductos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuExistenciasFragment extends Fragment {

    private ImageButton AgregarInventario;
    private RecyclerView recyclerView;
    private ListAdapterExistencias adapter;
    private List<ListExistencias> productoList = new ArrayList<>(); //alamcena la lista de productos

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

            // Añadir un nuevo producto a la lista del recyclerView
            ListExistencias producto = new ListExistencias("1", Descripcion, esperados, encontrados, Clave, getCurrentDate());
            productoList.add(producto);

            Toast.makeText(getContext(), "Articulos escaneados" + encontrados, Toast.LENGTH_LONG).show();

            // Ahora puedes usar 'selectedItem' y 'articulos' como necesites en este fragmento
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu_existencias, container, false);

        recyclerView = root.findViewById(R.id.ListRecyclerViewExistencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador con la lista de productos
        adapter = new ListAdapterExistencias(productoList, getContext(), new ListAdapterExistencias.OnItemClickListeners() {
            @Override
            public void onItemClick(ListExistencias item) {
                // Manejar el clic en el elemento
                Toast.makeText(getContext(), "Item seleccionado: " + item.getDesc_Producto(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ListExistencias item) {
                // Manejar el clic largo en el elemento
                Toast.makeText(getContext(), "Long click en: " + item.getDesc_Producto(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        AgregarInventario = root.findViewById(R.id.IB_AgregarInventario);
        AgregarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_inventario_to_nav_productos);
            }
        });

        return root;
    }

    private String getCurrentDate() {
        // Obtener la fecha actual en formato deseado (puedes personalizarlo)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
