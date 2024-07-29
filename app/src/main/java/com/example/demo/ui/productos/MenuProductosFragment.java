package com.example.demo.ui.productos;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.ReciclerView.ListAdapterProductos;
import com.example.demo.ReciclerView.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuProductosFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private String[] Id, Descripvion, Clave, Existencia, TipoProducto, ClasificacionProducto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu_productos, container, false);
        webServiceManager = new WebServiceManager(getContext());
        recyclerView = root.findViewById(R.id.ListRecyclerViewproductos);
        lleanrlista();
        // Inflate the layout for this fragment
        return root;
    }


    public void lleanrlista(){
        // Create a list of elements
        elements = new ArrayList<>();
        elements.add(new ListProductos("1","Mesa de billar","Muebles","Bueno","4","CDF5465"));
        elements.add(new ListProductos("1","Mesa de billar","Muebles","Bueno","4","CDF5465"));
        elements.add(new ListProductos("1","Mesa de billar","Muebles","Bueno","4","CDF5465"));
        elements.add(new ListProductos("1","Mesa de billar","Muebles","Bueno","4","CDF5465"));

        //Cuando se seleciona un producto
        listAdapterProductos = new ListAdapterProductos(elements, getContext(), new ListAdapterProductos.OnItemClickListeners() {
            @Override
            public void onItemClick(ListProductos item) {

            }
            @Override
            public void onItemLongClick(ListProductos item) {
                showOptionsDialog(item);
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterProductos);

    }

    private void showOptionsDialog(ListProductos item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Opciones")
                .setMessage("Producto: " + item.getDesProducto() + "\nClave: " + item.getClave())
                .setPositiveButton("Editar", (dialog, which) -> {
                    // Acción para editar
                })
                .setNegativeButton("Eliminar", (dialog, which) -> {
                    // Acción para eliminar
                })
                .show();
    }
}