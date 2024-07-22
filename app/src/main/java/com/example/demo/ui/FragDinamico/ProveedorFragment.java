package com.example.demo.ui.FragDinamico;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.main.KeyDwonFragment;

public class ProveedorFragment extends KeyDwonFragment {

    private ProveedorViewModel mViewModel;

    public static ProveedorFragment newInstance() {
        return new ProveedorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proveedor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProveedorViewModel.class);
        // TODO: Use the ViewModel
    }

}