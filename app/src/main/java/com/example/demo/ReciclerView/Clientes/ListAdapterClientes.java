package com.example.demo.ReciclerView.Clientes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterClientes extends RecyclerView.Adapter<ListAdapterClientes.ViewHolder> {
    private List<ListClientes> mData;
    private List<ListClientes> mDataFiltered; // Lista para mantener los datos filtrados
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListeners listeners;
    private int selectedPosition = -1; // Índice del elemento seleccionado

    public interface OnItemClickListeners {
        void onItemClick(ListClientes item);
        void onItemLongClick(ListClientes item);
    }

    public ListAdapterClientes(List<ListClientes> itemList, Context context, OnItemClickListeners listeners) {
        this.mData = itemList;
        this.mDataFiltered = new ArrayList<>(itemList);
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public ListAdapterClientes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_clientes, parent, false);
        return new ListAdapterClientes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterClientes.ViewHolder holder, int position) {
        holder.bindData(mDataFiltered.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size(); // Usa la lista filtrada
    }

    public void setItems(List<ListClientes> items) {
        this.mData = items;
        this.mDataFiltered =  new ArrayList<>(items);
    }

    public void filter(String text) {
        mDataFiltered.clear();
        if (text.isEmpty()) {
            mDataFiltered.addAll(mData);
        } else {
            text = text.toLowerCase();
            for (ListClientes item : mData) {
                if (item.getDesClientes().toLowerCase().contains(text) ||
                        item.getRFC().toLowerCase().contains(text) ||
                        item.getClave().toLowerCase().contains(text)) {
                    mDataFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Icon_Inventario;
        TextView TV_Nombre_Cliente, TV_RFC, TV_ClaveCliente;

        ViewHolder(View itemView) {
            super(itemView);
            Icon_Inventario = itemView.findViewById(R.id.Icon_Inventario);
            TV_Nombre_Cliente = itemView.findViewById(R.id.TV_Nombre_Cliente);
            TV_RFC = itemView.findViewById(R.id.TV_RFC);
            TV_ClaveCliente = itemView.findViewById(R.id.TV_ClaveCliente);
        }

        void bindData(final ListClientes item, final int position) {
            TV_Nombre_Cliente.setText(item.getDesClientes());
            TV_RFC.setText(item.getRFC());
            TV_ClaveCliente.setText(item.getClave());
            // Resaltar el fondo si el elemento está seleccionado
            itemView.setBackgroundColor(selectedPosition == position ? Color.LTGRAY : Color.TRANSPARENT);

            itemView.setOnClickListener(v -> {
                listeners.onItemClick(item);
                selectedPosition = position;
                notifyDataSetChanged(); // Notificar cambios al adapter para actualizar la vista
            });

            itemView.setOnLongClickListener(v -> {
                listeners.onItemLongClick(item);
                return true;
            });
        }
    }
}
