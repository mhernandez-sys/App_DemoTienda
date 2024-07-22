package com.example.demo.ui.Inventario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.recyclerview.ListInventario;

import java.util.ArrayList;
import java.util.List;

public class InventarioViewModel extends ViewModel {

    private MutableLiveData<List<ListInventario>> dataListLiveData = new MutableLiveData<>();

    public LiveData<List<ListInventario>> getDataList() {
        return dataListLiveData;
    }

    public void setDataList(List<ListInventario> dataList) {
        dataListLiveData.setValue(dataList);
    }

    public void updateItem(ListInventario updatedItem) {
        if (dataListLiveData.getValue() != null) {
            List<ListInventario> currentDataList = new ArrayList<>(dataListLiveData.getValue());
            for (int i = 0; i < currentDataList.size(); i++) {
                if (currentDataList.get(i).getDescripcion().equals(updatedItem.getDescripcion())) {
                    currentDataList.set(i, updatedItem);
                    break;
                }
            }
            dataListLiveData.setValue(currentDataList);
        }
    }
    public void limpiarDatos() {
        dataListLiveData.setValue(new ArrayList<>()); // Limpiar dataListLiveData
    }
}