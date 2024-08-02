package com.example.demo.ui.Provedores;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.main.KeyDwonFragment;

import java.util.HashMap;
import java.util.Map;
public class ProveedorFragment extends KeyDwonFragment {


    private EditText ET_Nom_Provedor, ET_FRC, ET_ClaveProvedor;
    private Button BT_GuardarProvedor, BT_CancelarProvedor;
    private WebServiceManager webServiceManager; // Asegúrate de tener una instancia de WebServiceManager



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proveedor, container, false);

        // Inicializar los EditTexts y el botón
        ET_Nom_Provedor = view.findViewById(R.id.ET_Nom_Provedor);
        ET_FRC = view.findViewById(R.id.ET_FRC);
        ET_ClaveProvedor = view.findViewById(R.id.ET_ClaveProvedor);
        BT_GuardarProvedor = view.findViewById(R.id.BT_GuardarProvedor);
        BT_CancelarProvedor = view.findViewById(R.id.BT_CancelarProvedor);


        // Inicializar WebServiceManager
        webServiceManager = new WebServiceManager(getContext());

        // Configurar el listener para el botón
        BT_GuardarProvedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClient();
            }

        });

        BT_CancelarProvedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto te llevará al fragmento anterior en la pila de back stack
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    private void saveClient() {
        DialogoAnimaciones.hideLoadingDialog();
        DialogoAnimaciones.showLoadingDialog(getContext());
        // Obtener los valores de los EditTexts
        String nombreCliente = "";
        nombreCliente = ET_Nom_Provedor.getText().toString();
        String rfc = ET_FRC.getText().toString();
        String claveCliente = ET_ClaveProvedor.getText().toString();

        // Validar los datos (opcional)
        if (nombreCliente.isEmpty() || rfc.isEmpty() || claveCliente.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            DialogoAnimaciones.hideLoadingDialog();
            return;
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("nuevoNombre", nombreCliente);
        properties.put("nuevoRFC", rfc);
        properties.put("nuevaClave", claveCliente);


        webServiceManager.callWebService("GuadarProveedor", properties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                DialogoAnimaciones.hideLoadingDialog();
                try {
                    DialogoAnimaciones.hideLoadingDialog();
                    handleSaveClientResult(result);

                } catch (Exception e) {
                    DialogoAnimaciones.showNoInternetDialog(getContext(), "Fallo de conexion", () -> saveClient());

                }
            }
        });
    }

    private void handleSaveClientResult(String result) {
        // Manejar el resultado del web service
        if (result == null || result.isEmpty()) {
            Toast.makeText(getContext(), "Error al guardar el cliente: Respuesta vacía del servidor", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí asumimos que el resultado contiene una respuesta JSON
        try {

            if (result.equals("Se realizó el insert correctamente.")) {
                limpiar();
                Toast.makeText(getContext(), "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();

            } else {
                DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: PF-127", () -> saveClient());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: PF-129", () -> saveClient());
        }
    }
    private void limpiar() {
        ET_Nom_Provedor.setText("");
        ET_FRC.setText("");
        ET_ClaveProvedor.setText("");
    }
}
