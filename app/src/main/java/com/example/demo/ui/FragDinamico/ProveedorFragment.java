package com.example.demo.ui.FragDinamico;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.main.KeyDwonFragment;

import java.util.HashMap;
import java.util.Map;

public class ProveedorFragment extends KeyDwonFragment {

    private ClientesViewModel mViewModel;
    private EditText editTextproveedor1, editTextproveedor2, editTextproveedor3;
    private Button buttonSaveCliente;
    private WebServiceManager webServiceManager; // Asegúrate de tener una instancia de WebServiceManager



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proveedor, container, false);

        // Inicializar los EditTexts y el botón
        editTextproveedor1 = view.findViewById(R.id.edit_text_1);
        editTextproveedor2 = view.findViewById(R.id.edit_text_2);
        editTextproveedor3 = view.findViewById(R.id.edit_text_3);
        buttonSaveCliente = view.findViewById(R.id.button_save);

        // Inicializar WebServiceManager
        webServiceManager = new WebServiceManager(getContext());

        // Configurar el listener para el botón
        buttonSaveCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClient();
                limpiar();
                FragmentManager fragmentManager = getParentFragmentManager();

                // Comprueba que haya tenido un fragmento anteriormente
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Regresa al fragmento anterior
                    fragmentManager.popBackStack();
                }

            }

            private void limpiar() {
                // Limpiar los EditTexts
                editTextproveedor1.setText("");
                editTextproveedor2.setText("");
                editTextproveedor3.setText("");

                // Restablecer los Spinners a su valor predeterminado (generalmente la posición 0)
//                spinner1.setSelection(0);
//                spinner2.setSelection(0);
            }
        });

        // Configurar los EditTexts para cambiar el foco al presionar Enter
        editTextproveedor1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    editTextproveedor2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextproveedor2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    editTextproveedor3.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextproveedor3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonSaveCliente.requestFocus();
                    return true;
                }
                return false;
            }
        });

        // Establecer el foco inicial en el EditText de nombre
        editTextproveedor1.requestFocus();


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
        String nombreCliente = "";
        nombreCliente = editTextproveedor1.getText().toString();
        String rfc = editTextproveedor2.getText().toString();
        String claveCliente = editTextproveedor3.getText().toString();

        // Validar los datos (opcional)
        if (nombreCliente.isEmpty() || rfc.isEmpty() || claveCliente.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            Map<String, String> properties = new HashMap<>();
            properties.put("nuevoNombre", nombreCliente);
            properties.put("nuevoRFC", rfc);
            properties.put("nuevaClave", claveCliente);

            webServiceManager.callWebService("GuadarProveedor", properties, new WebServiceManager.WebServiceCallback() {
                @Override
                public void onWebServiceCallComplete(String result) {
                    Log.d("ClientesFragment", "WebService Result: " + result);
                    handleSaveClientResult(result);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al llamar al web service: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void handleSaveClientResult(String result) {
        // Manejar el resultado del web service
        // Puedes agregar lógica para manejar el resultado aquí, por ejemplo:
        if (result.equals("Success")) {
            Toast.makeText(getContext(), "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getContext(), "Error al guardar el cliente", Toast.LENGTH_SHORT).show();

        }
    }
}
