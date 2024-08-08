package com.example.demo.ui.Movimientos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.demo.R;
import com.example.demo.ReciclerView.Movimientos.ListAdapterMovimiento;
import com.example.demo.ReciclerView.Movimientos.ListMovimientos;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuMovimientosFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ListMovimientos> elements;
    private ListAdapterMovimiento listAdapterMovimiento;
    private WebServiceManager webServiceManager;
    private ListMovimientos selectedItem;

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
        View root = inflater.inflate(R.layout.fragment_menu_movimientos, container, false);
        SearchView SV_BusquedaMoviminetos = root.findViewById(R.id.SV_BusquedaMoviminetos);
        recyclerView = root.findViewById(R.id.ListRecyclerViewMovimientos);
        webServiceManager = new WebServiceManager(getContext());
        inicilizar_lista();
        llenarListaMoviminetos();

        SV_BusquedaMoviminetos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapterMovimiento.filter(newText);
                return false;
            }
        });

        return root;
    }

    public void llenarListaMoviminetos(){
        DialogoAnimaciones.showLoadingDialog(getContext());
        webServiceManager.callWebService("DetallesDeMovimiento", null, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                if(result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i < jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Lote_SKU = jsonObject.getString("CombinedValue");
                            String Desc_Movimiento = jsonObject.getString("TipoMovDescripcion");
                            String id_tipoMov = jsonObject.getString("id_TipoMov");
                            String Desc_Producto = jsonObject.getString("ProdDescripcion");
                            String id_Prod = jsonObject.getString("id_Prod");
                            String Cantidad = jsonObject.getString("Cantidad");
                            String Fecha = jsonObject.getString("Fecha_Mov");
                            elements.add(new ListMovimientos(id_tipoMov, id_Prod, Desc_Movimiento, Desc_Producto,Lote_SKU, Cantidad, Fecha));
                        }
                        //Cuando se seleciona un producto
                        listAdapterMovimiento = new ListAdapterMovimiento(getContext(), elements, new ListAdapterMovimiento.OnItemClickListeners() {
                            @Override
                            public void onItemClick(ListMovimientos item) {
                                selectedItem = item;
                            }

                            @Override
                            public void onItemLongClick(ListMovimientos item) {
                                selectedItem = item;
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(listAdapterMovimiento);
                        DialogoAnimaciones.hideLoadingDialog();

                    }catch (JSONException e){
                        DialogoAnimaciones.hideLoadingDialog();
                        DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaMoviminetos());
                    }
                }else {
                    DialogoAnimaciones.hideLoadingDialog();
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion", () -> llenarListaMoviminetos());
                }
            }
        });


    }

    public void inicilizar_lista(){
        //         Inicializar la lista y el adaptador
        elements = new ArrayList<>();
        listAdapterMovimiento = new ListAdapterMovimiento(getContext(), elements, new ListAdapterMovimiento.OnItemClickListeners() {
            @Override
            public void onItemClick(ListMovimientos item) {
                selectedItem = item;
            }

            @Override
            public void onItemLongClick(ListMovimientos item) {
                selectedItem=item;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterMovimiento);
    }

    public void llenar_listaManual(){
        elements = new ArrayList<>();
        elements.add(new ListMovimientos("1","22", "Entrada", "CPU 40-24", "1234/789", "45", "24/04/2024"));
        elements.add(new ListMovimientos("2","22", "Salidas", "Monitores", "148655/48", "20", "2/08/2024"));
        elements.add(new ListMovimientos("2","098", "Salidas", "Compresores", "KJ45/89", "150", "15/12/2023"));
        elements.add(new ListMovimientos("1","5012", "Entradas", "C5 Chainway", "156878/82", "203", "12/08/2024"));
        elements.add(new ListMovimientos("1","22", "Entrada", "CPU 40-24", "1234/789", "45", "24/04/2024"));

        //Cuando se seleciona un producto
        listAdapterMovimiento = new ListAdapterMovimiento(getContext(), elements, new ListAdapterMovimiento.OnItemClickListeners() {
            @Override
            public void onItemClick(ListMovimientos item) {
                selectedItem = item;
            }

            @Override
            public void onItemLongClick(ListMovimientos item) {
                selectedItem = item;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterMovimiento);
    }
}