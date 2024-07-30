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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.ListAdapterProductos;
import com.example.demo.ReciclerView.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuProductosFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private String[] Id, Descripvion, Clave, Existencia, TipoProducto, ClasificacionProducto;
    private ImageButton AddProducto, ImprimirProducto, BuscarProductos, EliminarProducto;
    private SearchView SV_BusquedaProductos;
    private FloatingActionButton FB_Buscar;
    private ListProductos selectedItem; // Para almacenar el producto seleccionado


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
        ImprimirProducto = root.findViewById(R.id.IB_ImprimirProducto);
        BuscarProductos = root.findViewById(R.id.IB_EliminarProducto);
        FB_Buscar = root.findViewById(R.id.FB_Buscar);
        //lleanrlista();
        llenarListaProductos();

        AddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_product", selectedItem);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_agregarproducto, bundle);

            }
        });

        ImprimirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedItem != null) {
                    String codigo = selectedItem.getClave();
                    Bundle bundle = new Bundle();
                    bundle.putString("barcode", codigo);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

                    // Navegar al fragmento BarcodeFragment
                    navController.navigate(R.id.action_nav_gallery_to_nav_barcode, bundle);
                } else {
                    Toast.makeText(getContext(), "Por favor, seleccione un producto.", Toast.LENGTH_SHORT).show();
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

        // Configurar el FAB para mostrar el SearchView
        BuscarProductos.setOnClickListener(new View.OnClickListener() {
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

    private void llenarListaProductos() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService("ObtenerProductosConDetalles", null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        DialogoAnimaciones.hideLoadingDialog();
                        JSONArray jsonArray = new JSONArray(result);
                        elements = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String idproducto = jsonObject.getString("id_Prod");
                            String descripcion = jsonObject.getString("Descripcion");
                            String clave = jsonObject.getString("ClaveProds");
                            String existencia = jsonObject.getString("Existencia");
                            String tipoDescripcion = jsonObject.getString("Descripcion_Tipo");
                            String clasificacionDescripcion = jsonObject.getString("Descripcion_Clasificacion");
                            elements.add(new ListProductos(idproducto,descripcion,tipoDescripcion,clasificacionDescripcion,existencia,clave));
                        }
                        //Cuando se seleciona un producto
                        listAdapterProductos = new ListAdapterProductos(elements, getContext(), new ListAdapterProductos.OnItemClickListeners() {
                            @Override
                            public void onItemClick(ListProductos item) {
                                selectedItem = item;
                            }
                            @Override
                            public void onItemLongClick(ListProductos item) {
                                showOptionsDialog(item);
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(listAdapterProductos);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.hideLoadingDialog();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaProductos());

                    }
                } else {
                    DialogoAnimaciones.hideLoadingDialog();
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaProductos( ));
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

        @Override
        public String toString() {
            return descripcion; // Esto es lo que se mostrará en el Spinner
        }
    }


}