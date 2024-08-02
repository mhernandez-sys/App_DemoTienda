package com.example.demo.ui.Provedores;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.Provedores.ListAdapterProveedor;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.main.KeyDwonFragment;
import com.example.demo.ReciclerView.Provedores.ListProveedor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuProveedorFragment extends KeyDwonFragment {

    private List<ListProveedor> elements;
    private RecyclerView recyclerView;
    private ListAdapterProveedor ListAdapterProveedor;
    private WebServiceManager webServiceManager;
    private String[] Id, Descripvion, Clave, Existencia, TipoProducto, ClasificacionProducto;
    private ImageButton AddProveedor, BuscarProveedor, EliminarProducto;
    private SearchView SV_BusquedaProductos;
    private FloatingActionButton FB_Buscar;
    private ListProveedor selectedItem; // Para almacenar el producto seleccionado


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu_proveedor, container, false);
        webServiceManager = new WebServiceManager(getContext());
        recyclerView = root.findViewById(R.id.ListRecyclerViewProveedor);
        AddProveedor = root.findViewById(R.id.IB_AgregarProveedor);
        BuscarProveedor = root.findViewById(R.id.IB_BuscarProveedors);
        SV_BusquedaProductos = root.findViewById(R.id.SV_BusquedaProductos);
        EliminarProducto = root.findViewById(R.id.IB_EliminarProveedor);
        FB_Buscar = root.findViewById(R.id.FB_Buscar);

        // Inicializar la lista y el adaptador
        elements = new ArrayList<>();
        ListAdapterProveedor = new ListAdapterProveedor(elements, getContext(), new ListAdapterProveedor.OnItemClickListeners() {
            @Override
            public void onItemClick(ListProveedor item) {
                selectedItem = item;
            }

            @Override
            public void onItemLongClick(ListProveedor item) {
                showOptionsDialog(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ListAdapterProveedor);

        //lleanrlista();
        llenarListaProductos();

        AddProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_product", selectedItem);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.menuProvedor_to_addProvedor, bundle);

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
                ListAdapterProveedor.filter(newText);
                return false;
            }
        });

//        // Configurar el FAB para mostrar el SearchView
        BuscarProveedor.setOnClickListener(new View.OnClickListener() {
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

        EliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem != null) {
                    showDeleteConfirmationDialog(selectedItem);
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
                ListAdapterProveedor.filter(newText);
                return false;
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void showOptionsDialog(ListProveedor item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Personalizar el título
        TextView title = new TextView(getContext());
        title.setText("Opciones");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        builder.setCustomTitle(title);

        // Personalizar el mensaje
        TextView message = new TextView(getContext());
        message.setText("Producto: " + item.getNombre() + "\nClave: " + item.getClaveProv());
        message.setPadding(10, 10, 10, 10);

        builder.setView(message);


        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            showDeleteConfirmationDialog(item);
        });

        builder.show();
    }

    //Mensaje que muestra un mensaje de alerta
    private void showDeleteConfirmationDialog(ListProveedor item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Personalizar el título
        TextView title = new TextView(getContext());
        title.setText("Confirmación");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        builder.setCustomTitle(title);

        // Personalizar el mensaje
        TextView message = new TextView(getContext());
        message.setText("¿Estás seguro de eliminar este producto?");
        message.setPadding(10, 20, 10, 10);
        message.setGravity(Gravity.CENTER);
        builder.setView(message);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            eliminar_proveedor(); // Llamada a la función para eliminar el producto
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss(); // Cierra el diálogo sin hacer nada
        });

        builder.show();
    }


    private void llenarListaProductos() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService("ObtenerProveedores", null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        DialogoAnimaciones.hideLoadingDialog();
                        JSONArray jsonArray = new JSONArray(result);
                        elements = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String idProveedor = jsonObject.getString("id_Proveedores");
                            String nombre = jsonObject.getString("Nombre");
                            String RFC = jsonObject.getString("RFC");
                            String claveProv = jsonObject.getString("ClaveProv");
                            elements.add(new ListProveedor(idProveedor,nombre, RFC, claveProv));
                        }
                        //Cuando se seleciona un producto
                        ListAdapterProveedor = new ListAdapterProveedor(elements, getContext(), new ListAdapterProveedor.OnItemClickListeners() {
                            @Override
                            public void onItemClick(ListProveedor item) {
                                selectedItem = item;
                            }
                            @Override
                            public void onItemLongClick(ListProveedor item) {
                                showOptionsDialog(item);
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(ListAdapterProveedor);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.hideLoadingDialog();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaProductos());

                    }
                } else {
                    DialogoAnimaciones.hideLoadingDialog();
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de analisis", () -> llenarListaProductos( ));
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        limpiarDatos();
    }

    private void limpiarDatos() {
        if (elements != null) {
            elements.clear();
        }
        if (ListAdapterProveedor != null) {
            ListAdapterProveedor.notifyDataSetChanged();
        }
        selectedItem = null;
    }

    private void eliminar_proveedor() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        // Obtener el id_Tipo del elemento seleccionado
        String P_eliminado = selectedItem.getClaveProv();


        Map<String, String> propeties = new HashMap<>();
        propeties.put("ClaveProveedor",P_eliminado);


        webServiceManager.callWebService("EliminarProveedor", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                DialogoAnimaciones.hideLoadingDialog();
                if (result != null) {
                    try {
                        if (result.equals("Se realizó el delete correctamente.")) {
                            Toast.makeText(getContext(), "Se elimino con exito", Toast.LENGTH_LONG).show();
                        } else if (result.equals("Proveedor Existente.")) {
                            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> eliminar_proveedor());
                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> eliminar_proveedor());
                }
            }
        });
}


}