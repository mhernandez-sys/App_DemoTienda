package com.example.demo.ui.Existencias;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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

import com.example.demo.R;
import com.example.demo.ReciclerView.Productos.ListAdapterProductos;
import com.example.demo.ReciclerView.Productos.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductosInvFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private SearchView SV_BusquedaProductos;
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_productos_inv, container, false);
        webServiceManager = new WebServiceManager(getContext());
        recyclerView = root.findViewById(R.id.ListRecyclerViewproductosInv);
        SV_BusquedaProductos = root.findViewById(R.id.SV_BusquedaProductosInv);

        inicilizar_lista();
        llenarListaProductos();
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

        return root;
    }

    private void llenarListaProductos() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService("ObtenerProductosConDetalles", null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
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
                                selectedItem = item;
                                showOptionsDialog(item);
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(listAdapterProductos);
                        DialogoAnimaciones.hideLoadingDialog();

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

    //Mensaje que muestra un mensaje con las dos opciones
    private void showOptionsDialog(ListProductos item) {
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
        message.setText("Producto: " + item.getDesProducto() + "\nClave: " + item.getClave());
        message.setPadding(10, 10, 10, 10);

        builder.setView(message);

        builder.setPositiveButton("Impirmir", (dialog, which) -> {
            // Acción para editar
            String codigo = item.getClave() + "+" + item.getDesProducto();
            Bundle bundle = new Bundle();
            bundle.putString("barcode", codigo);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

            // Navegar al fragmento BarcodeFragment
            navController.navigate(R.id.action_nav_gallery_to_nav_barcode, bundle);
        });

        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            //showDeleteConfirmationDialog(item);
        });

        builder.show();
    }

    public void inicilizar_lista(){
        //         Inicializar la lista y el adaptador
        elements = new ArrayList<>();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterProductos);
    }

}