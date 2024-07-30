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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.ReciclerView.ListAdapterProductos;
import com.example.demo.ReciclerView.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MenuProductosFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private String[] Id, Descripvion, Clave, Existencia, TipoProducto, ClasificacionProducto;
    private ImageButton AddProducto;
    private SearchView SV_BusquedaProductos;
    private FloatingActionButton FB_Buscar;



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
        AddProducto = root.findViewById(R.id.IB_AgregarProducto);
        SV_BusquedaProductos = root.findViewById(R.id.SV_BusquedaProductos);
        FB_Buscar = root.findViewById(R.id.FB_Buscar);
        lleanrlista();

        AddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

                // Navegar al fragmento BarcodeFragment
                navController.navigate(R.id.nav_agregarproducto, bundle);
            }
        });
        // Configurar el SearchView para filtrar los datos
        SV_BusquedaProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapterProductos.filter(newText);
                return false;
            }
        });

        // Configurar el FAB para mostrar el SearchView
        FB_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SV_BusquedaProductos.getVisibility() == View.GONE) {
                    SV_BusquedaProductos.setVisibility(View.VISIBLE);
                    SV_BusquedaProductos.requestFocus();
                } else {
                    SV_BusquedaProductos.setVisibility(View.GONE);
                }
            }
        });

        // Configurar el SearchView para filtrar los datos
        SV_BusquedaProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapterProductos.filter(newText);
                return false;
            }
        });

        // Inflate the layout for this fragment
        return root;
    }


    public void lleanrlista(){
        // Create a list of elements
        elements = new ArrayList<>();
        elements.add(new ListProductos("1","Mesa de billar","Muebles de oficina","Roto","2","CDF5460"));
        elements.add(new ListProductos("1","Escaleras de madera","Muebles","Bueno","9","CDF58765"));
        elements.add(new ListProductos("1","Juguetes","Plsticos","Descompuesto","20","HFDJ455"));
        elements.add(new ListProductos("1","Mesa de pocker","Cernamica","Defectuoso","8","IODRUE54"));

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