package com.example.demo.ui.clientes;

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
import com.example.demo.ReciclerView.Clientes.ListAdapterClientes;
import com.example.demo.ReciclerView.Clientes.ListClientes;
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

public class MenuClientesFragment extends Fragment {

    private List<ListClientes> listClientes;
    private RecyclerView recyclerView;
    private ListAdapterClientes listAdapterClientes;
    private WebServiceManager webServiceManager;
    private ImageButton AddCliente, BuscarClientes, EliminarClientes;
    private SearchView SV_BusquedaClientes;
    private ListClientes selectedItem; // Para almacenar el producto seleccionado

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu_clientes, container, false);
        webServiceManager = new WebServiceManager(getContext());
        recyclerView = root.findViewById(R.id.ListRecyclerViewClientes);
        AddCliente = root.findViewById(R.id.IB_AgregarClientes);
        BuscarClientes = root.findViewById(R.id.IB_BuscarClientes);
        SV_BusquedaClientes = root.findViewById(R.id.SV_BusquedaClientes);
        EliminarClientes = root.findViewById(R.id.IB_EliminarClientes);
        inicilizar();
        llenarListaClientes();
        //Se programa boton para insertar un nuevo cliente
        AddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_product", selectedItem);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_agregarcliente, bundle);
            }
        });
        // Configurar el SearchView para filtrar los datos
        SV_BusquedaClientes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapterClientes.filter(newText);
                return false;
            }
        });

        //Se programa el boton para mostrar el mensaje de confirmaacion
        EliminarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem != null) {
                    showDeleteConfirmationDialog(selectedItem);
                } else {
                    Toast.makeText(getContext(), "Por favor, seleccione un producto.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Configurar el FAB para mostrar el SearchView
        BuscarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SV_BusquedaClientes.getVisibility() == View.GONE) {
                    SV_BusquedaClientes.setVisibility(View.VISIBLE);
                    SV_BusquedaClientes.requestFocus();
                } else {
                    SV_BusquedaClientes.setVisibility(View.GONE);
                }
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    /* En esta seccion se muestran las llamadas a webservice */
    //Llamada  para llenar la lista
    private void llenarListaClientes() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService("ObtenerClientes", null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String idCliente = jsonObject.getString("id_Cliente");
                            String nombre = jsonObject.getString("Nombre");
                            String RFC = jsonObject.getString("RFC");
                            String clave= jsonObject.getString("ClaveClient");
                            listClientes.add(new ListClientes(idCliente,nombre,RFC, clave));
                        }
                        //Cuando se seleciona un producto
                        listAdapterClientes = new ListAdapterClientes(listClientes, getContext(), new ListAdapterClientes.OnItemClickListeners() {
                            @Override
                            public void onItemClick(ListClientes item) {
                                selectedItem = item;
                            }
                            @Override
                            public void onItemLongClick(ListClientes item) {
                                selectedItem = item;
                                showOptionsDialog(item);
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(listAdapterClientes);
                        DialogoAnimaciones.hideLoadingDialog();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogoAnimaciones.hideLoadingDialog();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaClientes());

                    }
                } else {
                    DialogoAnimaciones.hideLoadingDialog();
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaClientes( ));
                }
            }
        });
    }

    private void eliminar_producto() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        // Obtener el id_Tipo del elemento seleccionado
        String C_eliminado = selectedItem.getId();
        String clave = selectedItem.getClave();

        Map<String, String> propeties = new HashMap<>();
        propeties.put("ClaveCliente",clave);
        propeties.put("id_cliente",C_eliminado);


        webServiceManager.callWebService("EliminarCliente", propeties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                DialogoAnimaciones.hideLoadingDialog();
                if (result != null) {
                    try {
                        if (result.equals("Se realizó el delete correctamente.")) {
                            Toast.makeText(getContext(), "Se elimino con exito", Toast.LENGTH_LONG).show();
                            inicilizar();
                            llenarListaClientes();
                            selectedItem = null;
                        } else if (result.equals("No se pudo realizar el delete.")) {
                            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                            inicilizar();
                            llenarListaClientes();
                        }else {}

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


    /* En esta seccion se muestran los mensajes que se deplegaran */

    //Mensaje que muestra la informacion seleccionada y la opcion de eliminar
    private void showOptionsDialog(ListClientes item) {
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
        message.setText("Producto: " + item.getDesClientes() + "\nClave: " + item.getClave());
        message.setPadding(10, 10, 10, 10);

        builder.setView(message);

        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            showDeleteConfirmationDialog(item);
        });

        builder.show();
    }
    //Mensaje que muestra un mensaje de alerta
    private void showDeleteConfirmationDialog(ListClientes item) {
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

    public void inicilizar(){
        // Inicializar la lista y el adaptador
        listClientes = new ArrayList<>();
        listAdapterClientes =new ListAdapterClientes(listClientes, getContext(),new ListAdapterClientes.OnItemClickListeners(){

            @Override
            public void onItemClick(ListClientes item) {

            }

            @Override
            public void onItemLongClick(ListClientes item) {

            }
        });
    }
}