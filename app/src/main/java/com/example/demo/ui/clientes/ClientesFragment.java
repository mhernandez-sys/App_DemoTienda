package com.example.demo.ui.clientes;

import androidx.lifecycle.ViewModelProvider;

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

public class ClientesFragment extends KeyDwonFragment {

    private EditText editTextCliente1, editTextCliente2, editTextCliente3;
    private Button buttonSaveCliente;
    private WebServiceManager webServiceManager; // Asegúrate de tener una instancia de WebServiceManager

    public static ClientesFragment newInstance() {
        return new ClientesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        // Inicializar los EditTexts y el botón
        editTextCliente1 = view.findViewById(R.id.edit_text_cliente_1);
        editTextCliente2 = view.findViewById(R.id.edit_text_cliente_2);
        editTextCliente3 = view.findViewById(R.id.edit_text_cliente_3);
        buttonSaveCliente = view.findViewById(R.id.button_save_cliente);

        // Inicializar WebServiceManager
        webServiceManager = new WebServiceManager(getContext());

        // Configurar el listener para el botón
        buttonSaveCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClient();
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
        String nombreCliente = editTextCliente1.getText().toString();
        String rfc = editTextCliente2.getText().toString();
        String claveCliente = editTextCliente3.getText().toString();

        // Validar los datos (opcional)
        if (nombreCliente.isEmpty() || rfc.isEmpty() || claveCliente.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            DialogoAnimaciones.hideLoadingDialog();
            return;
        }
        // Crear propiedades para el web service
        Map<String, String> properties = new HashMap<>();
        properties.put("nuevoNombre", nombreCliente);
        properties.put("nuevoRFC", rfc);
        properties.put("nuevaClave", claveCliente);

        // Llamar al web service
        webServiceManager.callWebService("GuardarClientes", properties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                try{
                    DialogoAnimaciones.hideLoadingDialog();
                    handleSaveClientResult(result);
                }catch (Exception e) {
                    Toast.makeText(getContext(), "Error al llamar al web service: " + e.getMessage(), Toast.LENGTH_LONG).show();

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
                DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: CF-123", () -> saveClient());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogoAnimaciones.showNoInternetDialog(getContext(), "Error de conexion: CF-127", () -> saveClient());
        }
    }
    private void limpiar() {
        editTextCliente1.setText("");
        editTextCliente2.setText("");
        editTextCliente3.setText("");
    }
}
