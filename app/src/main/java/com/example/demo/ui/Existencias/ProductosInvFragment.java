package com.example.demo.ui.Existencias;

import android.media.RouteListingPreference;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.ReciclerView.Productos.ListAdapterProductos;
import com.example.demo.ReciclerView.Productos.ListProductos;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;

import org.apache.xmlbeans.impl.xb.xsdschema.BlockSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProductosInvFragment extends Fragment {

    private List<ListProductos> elements;
    private RecyclerView recyclerView;
    private ListAdapterProductos listAdapterProductos;
    private WebServiceManager webServiceManager;
    private SearchView SV_BusquedaProductos;
    private ListProductos selectedItem; // Para almacenar el producto seleccionado
    public String SKU;
    private boolean isLocked = false;
    private String Ban_leido = "";



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

//        builder.setPositiveButton("Impirmir", (dialog, which) -> {
//            // Acción para editar
//            String codigo = item.getClave() + "+" + item.getDesProducto();
//            Bundle bundle = new Bundle();
//            bundle.putString("barcode", codigo);
//
//            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
//
//            // Navegar al fragmento BarcodeFragment
//            navController.navigate(R.id.action_nav_gallery_to_nav_barcode, bundle);
//        });

        builder.setNegativeButton("Inventario", (dialog, which) -> {
            showCustomAlertInventary(selectedItem);
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

    private void showCustomAlertInventary(ListProductos item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salidasve, null);
        builder.setView(dialogView).setCancelable(false);
        android.app.AlertDialog alertDialog = builder.create();

        TextView TV_CanEsperada = dialogView.findViewById(R.id.TV_CanEsperada);
        TextView TV_ArtLeidos = dialogView.findViewById(R.id.TV_ArtLeidos);
        LinearLayout llPorCajas = dialogView.findViewById(R.id.LL_PorCajas);
        EditText ET_NumserieAutomatico = dialogView.findViewById(R.id.ET_NumserieAutomatico);
        EditText ET_NumserieManual = dialogView.findViewById(R.id.ET_NumserieManual);
        Button btnCompletar = dialogView.findViewById(R.id.btn_completar);
        Button BT_Siguiente = dialogView.findViewById(R.id.BT_Siguiente);
        Switch SW_Modo = dialogView.findViewById(R.id.SW_Modo);
        TextView TV_SWEstatus = dialogView.findViewById(R.id.TV_SWEstatus);
        Button BT_Cancelar = dialogView.findViewById(R.id.BT_Cancelar);
        AtomicReference<String> numSerieAutomatico = new AtomicReference<>("");
        AtomicReference<String> numSerieManual = new AtomicReference<>("");
        SKU = "";
        String Art_Esperados = selectedItem.getExistencia();


        TV_CanEsperada.setText(Art_Esperados);
        ET_NumserieAutomatico.requestFocus();  // Coloca el foco en el EditText automatico
        ET_NumserieManual.setHint("Codigo");

        BT_Siguiente.setVisibility(View.GONE);
        btnCompletar.setVisibility(View.VISIBLE);
        llPorCajas.setVisibility(View.GONE);


        SW_Modo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TV_SWEstatus.setText("Manual");
                    ET_NumserieManual.setEnabled(true); // Habilitar el EditText
                    ET_NumserieManual.setVisibility(View.VISIBLE);
                    ET_NumserieAutomatico.setVisibility(View.GONE);
                    BT_Siguiente.setVisibility(View.VISIBLE);
                    ET_NumserieManual.requestFocus();  // Coloca el foco en el EditText manual
                    ET_NumserieManual.setHint("Codigo");

                } else {
                    TV_SWEstatus.setText("Automático");
                    ET_NumserieAutomatico.setEnabled(true);
                    ET_NumserieManual.setVisibility(View.GONE);
                    ET_NumserieAutomatico.setVisibility(View.VISIBLE);
                    BT_Siguiente.setVisibility(View.GONE);
                    ET_NumserieAutomatico.requestFocus();  // Coloca el foco en el EditText automatico
                    ET_NumserieAutomatico.setHint("Codigo");
                    Ban_leido = "0";
                    isLocked = false; // Asegúrate de marcar el procesamiento como terminado
                }
            }
        });

        BT_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset();
                alertDialog.dismiss();
            }
        });

        ET_NumserieAutomatico.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Si el EditText está bloqueado, no permitir entradas adicionales
                if (isLocked) {
                    return ""; // No permitir la entrada de texto
                }
                return null; // Permitir el texto si no está bloqueado
            }
        }});

        ET_NumserieAutomatico.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isLocked) {
                    // Si ya se está procesando, no hacemos nada
                    return;
                }
                isLocked = true; // Marca que estamos procesando un código
                try {
                    if (!Ban_leido.equals("1")) {
                        Ban_leido = "1";
                        // Verifica si el código introducido es válido (puedes agregar una lógica de validación aquí si es necesario)
                        if (s.length() > 0) {
                            // Suma de los artículos leídos
                            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
                            int Artesperados = Integer.parseInt(Art_Esperados);
                            datoleidos++;
                            TV_ArtLeidos.setText(String.valueOf(datoleidos));
                            // Comprueba si se ha alcanzado el número esperado de artículos
                            if (datoleidos == Artesperados ){
                                BT_Siguiente.setVisibility(View.GONE);
                                btnCompletar.setVisibility(View.VISIBLE);
                                ET_NumserieAutomatico.setVisibility(View.GONE);
                                ET_NumserieAutomatico.clearFocus(); // Quita el foco del EditText
                                ET_NumserieAutomatico.setEnabled(false); // Bloquea el EditText
                                // Obtener el texto de los EditText
                                //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
                            }
                            // Muestra el código leído por 2 segundos antes de limpiar el EditText
                            new Handler().postDelayed(() -> {
                               numSerieAutomatico.set(ET_NumserieAutomatico.getText().toString().trim());
                                SKU += numSerieAutomatico+ ",";
                                ET_NumserieAutomatico.setText("");
                                Ban_leido = "0";
                                isLocked = false; // Marca que el procesamiento ha terminado
                            }, 500); // 2000 milisegundos = 2 segundos
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor, ingrese números válidos.", Toast.LENGTH_SHORT).show();
                    isLocked = false; // Asegúrate de marcar el procesamiento como terminado

                }
            }
        });

        btnCompletar.setOnClickListener(v -> {
            ///En esta parte se agrega
            String Leidos = TV_ArtLeidos.getText().toString();
            String Esperados = selectedItem.getExistencia();
            String Descripcion = selectedItem.getDesProducto();
            String Clave = selectedItem.getClave();
            Bundle bundle = new Bundle();
            //bundle.putSerializable("selected_product", selectedItem);
            bundle.putSerializable("ART_Leidos", Leidos);
            bundle.putSerializable("ART_Esperados", Esperados);
            bundle.putSerializable("Descripcion", Descripcion);
            bundle.putSerializable("Clave", Clave);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_productos_inventario_to_nav_inventario, bundle);
            alertDialog.dismiss();
        });

        BT_Siguiente.setOnClickListener(v -> {
            // Suma de los artículos leídos
            int datoleidos = Integer.parseInt(TV_ArtLeidos.getText().toString());
            int Artesperados = Integer.parseInt(Art_Esperados);
            datoleidos++;
            TV_ArtLeidos.setText(String.valueOf(datoleidos));
            if (datoleidos == Artesperados ){
                ET_NumserieManual.setVisibility(View.GONE);
                ET_NumserieManual.clearFocus(); // Quita el foco del EditText
                ET_NumserieManual.setEnabled(false); // Bloquea el EditText
                BT_Siguiente.setVisibility(View.GONE);
                btnCompletar.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "Todos los artículos han sido escaneados.", Toast.LENGTH_LONG).show();
            }
            numSerieManual.set(ET_NumserieManual.getText().toString().trim());
            SKU += numSerieManual+ ",";
            ET_NumserieAutomatico.setText("");
            ET_NumserieManual.setText("");
        });
        alertDialog.show();
    }


}