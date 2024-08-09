package com.example.demo.ui.productos;

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
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.Productos.ListAdapterProductos;
import com.example.demo.ReciclerView.Productos.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuProductosFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private ImageButton AddProducto, ImprimirProducto, BuscarProductos, EliminarProducto;
    private SearchView SV_BusquedaProductos;
    private ListProductos selectedItem; // Para almacenar el producto seleccionado


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu_productos, container, false);
        webServiceManager = new WebServiceManager(getContext());
        recyclerView = root.findViewById(R.id.ListRecyclerViewproductos);
        AddProducto = root.findViewById(R.id.IB_AgregarProducto);
        ImprimirProducto = root.findViewById(R.id.IB_ImprimirProducto);
        BuscarProductos = root.findViewById(R.id.IB_BuscarProductos);
        SV_BusquedaProductos = root.findViewById(R.id.SV_BusquedaProductos);
        EliminarProducto = root.findViewById(R.id.IB_EliminarProducto);
        inicilizar_lista();
        //lleanrlista();
        llenarListaProductos();

        AddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selected_product", selectedItem);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.action_nav_inventario_to_nav_productos, bundle);
            }
        });

        ImprimirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedItem != null) {
                    String codigo = selectedItem.getClave() + "+" + selectedItem.getDesProducto();
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
                listAdapterProductos.filter(newText);
                return false;
            }
        });

        // Inflate the layout for this fragment
        return root;
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
            showDeleteConfirmationDialog(item);
        });

        builder.show();
    }

    //Mensaje que muestra una alerta
    private void showDeleteConfirmationDialog(ListProductos item) {
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
            eliminar_producto(); // Llamada a la función para eliminar el producto
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss(); // Cierra el diálogo sin hacer nada
        });

        builder.show();
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

    private void eliminar_producto() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        // Obtener el id_Tipo del elemento seleccionado
        String P_eliminado = selectedItem.getId();
        String Existencia = selectedItem.getExistencia();


        Map<String, String> propeties = new HashMap<>();
        propeties.put("existencia",Existencia);
        propeties.put("id_Prod",P_eliminado);


        webServiceManager.callWebService("EliminarProductos", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                DialogoAnimaciones.hideLoadingDialog();
                if (result != null) {
                    try {
                        if (result.equals("Se realizó el delete correctamente.")) {
                            Toast.makeText(getContext(), "Se elimino con exito", Toast.LENGTH_LONG).show();
                            inicilizar_lista();
                            llenarListaProductos();
                        } else if (result.equals("No se pudo realizar el delete.")) {
                            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                            inicilizar_lista();
                            llenarListaProductos();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> eliminar_producto());
                    }
                } else {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> eliminar_producto());
                }
                }
        });
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