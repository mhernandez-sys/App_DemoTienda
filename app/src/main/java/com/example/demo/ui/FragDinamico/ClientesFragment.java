package com.example.demo.ui.FragDinamico;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.main.KeyDwonFragment;

import java.util.HashMap;
import java.util.Map;

public class ClientesFragment extends KeyDwonFragment {

    private ClientesViewModel mViewModel;
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
        mViewModel = new ViewModelProvider(this).get(ClientesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void saveClient() {
        // Obtener los valores de los EditTexts
        String nombreCliente = editTextCliente1.getText().toString().trim();
        String rfc = editTextCliente2.getText().toString().trim();
        String claveCliente = editTextCliente3.getText().toString().trim();

        // Validar los datos (opcional)
        if (nombreCliente.isEmpty() || rfc.isEmpty() || claveCliente.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear propiedades para el web service
        Map<String, String> properties = new HashMap<>();
        properties.put("nombreCliente", nombreCliente);
        properties.put("rfc", rfc);
        properties.put("claveCliente", claveCliente);

        // Llamar al web service
        webServiceManager.callWebService("GuardarCliente", properties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                handleSaveClientResult(result);
            }
        });
    }

    private void handleSaveClientResult(String result) {
        // Manejar el resultado del web service
        // Puedes agregar lógica para manejar el resultado aquí, por ejemplo:
        if (result.equals("success")) {
            Toast.makeText(getContext(), "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al guardar el cliente", Toast.LENGTH_SHORT).show();
        }
    }
}
