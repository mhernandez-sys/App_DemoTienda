package com.example.demo.ui.eliminar;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.WebServiceManager;

public class  EliminarFragment extends Fragment {

    private EliminarViewModel mViewModel;
    private Button BT_Eliminar;
    private EditText ET_Descripcion_elim, ET_Clave_elim, ET_Existencia_elim;
    private WebServiceManager webServiceManager; // Asegúrate de tener una instancia de WebServiceManager
    private RecyclerView recyclerView;
    //    private EliminarAdapter adapter;
//    private List<Eliminar> eliminarList;
    private TextView TV_Descripcion_elim, TV_Clave_elim, TV_Existencia_elim;

    public static EliminarFragment newInstance() {
        return new EliminarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eliminar, container, false);

        // Inicializar los EditTexts y el botón

        ET_Descripcion_elim = view.findViewById(R.id.ET_Descripcion_elim);
        ET_Clave_elim = view.findViewById(R.id.ET_Clave_elim);
        ET_Existencia_elim = view.findViewById(R.id.ET_Existencia_elim);
        BT_Eliminar = view.findViewById(R.id.BT_Eliminar);


        // Inicializar WebServiceManager
        webServiceManager = new WebServiceManager(getContext());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarViewModel.class);
        // TODO: Use the ViewModel
    }

}