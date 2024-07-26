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
import com.example.demo.animaciones.DialogoAnimaciones;
import com.example.demo.main.KeyDwonFragment;

import java.util.HashMap;
import java.util.Map;

public class ProveedorFragment extends KeyDwonFragment {

    private ClientesViewModel mViewModel;
    private EditText ET_Nom_Provedor, ET_FRC, ET_ClaveProvedor;
    private Button BT_GuardarProvedor;
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

        // Inicializar WebServiceManager
        webServiceManager = new WebServiceManager(getContext());

        // Configurar el listener para el botón
        BT_GuardarProvedor.setOnClickListener(new View.OnClickListener() {
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
                ET_Nom_Provedor.setText("");
                ET_FRC.setText("");
                ET_ClaveProvedor.setText("");

            }
        });

        // Configurar los EditTexts para cambiar el foco al presionar Enter
        ET_Nom_Provedor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    ET_FRC.requestFocus();
                    return true;
                }
                return false;
            }
        });

        ET_FRC.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    ET_ClaveProvedor.requestFocus();
                    return true;
                }
                return false;
            }
        });

        ET_ClaveProvedor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    BT_GuardarProvedor.requestFocus();
                    return true;
                }
                return false;
            }
        });

        // Establecer el foco inicial en el EditText de nombre
        ET_Nom_Provedor.requestFocus();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClientesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void saveClient() {
        DialogoAnimaciones.showLoadingDialog(getContext());
        // Obtener los valores de los EditTexts
        String nombreCliente = "";
        nombreCliente = ET_Nom_Provedor.getText().toString();
        String rfc = ET_FRC.getText().toString();
        String claveCliente = ET_ClaveProvedor.getText().toString();

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
                //DialogoAnimaciones.hideLoadingDialog();
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
        if (result.equals("Se realizó el insert correctamente.")) {
            Toast.makeText(getContext(), "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al guardar el cliente", Toast.LENGTH_SHORT).show();

        }
    }
}
