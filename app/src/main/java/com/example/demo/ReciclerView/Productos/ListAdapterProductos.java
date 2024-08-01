package com.example.demo.ReciclerView.Productos;

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

public class ListAdapterProductos extends RecyclerView.Adapter<ListAdapterProductos.ViewHolder> {
    private List<ListProductos> mData;
    private List<ListProductos> mDataFiltered; // Lista para mantener los datos filtrados
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListeners listeners;
    private int selectedPosition = -1; // Índice del elemento seleccionado

    public interface OnItemClickListeners {
        void onItemClick(ListProductos item);
        void onItemLongClick(ListProductos item);
    }

    public ListAdapterProductos(List<ListProductos> itemList, Context context, OnItemClickListeners listeners) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.mDataFiltered = new ArrayList<>(itemList); // Inicializa mDataFiltered con todos los datos
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public ListAdapterProductos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_productos, parent, false);
        return new ListAdapterProductos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterProductos.ViewHolder holder, final int position) {
        holder.bindData(mDataFiltered.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size(); // Usa la lista filtrada
    }

    public void setItems(List<ListProductos> items) {
        this.mData = items;
        this.mDataFiltered = new ArrayList<>(items);
    }

    public void filter(String text) {
        mDataFiltered.clear();
        if (text.isEmpty()) {
            mDataFiltered.addAll(mData);
        } else {
            text = text.toLowerCase();
            for (ListProductos item : mData) {
                if (item.getDesProducto().toLowerCase().contains(text) ||
                        item.getTipoProducto().toLowerCase().contains(text) ||
                        item.getClasProducto().toLowerCase().contains(text) ||
                        item.getClave().toLowerCase().contains(text)) {
                    mDataFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Icon_Inventario;
        TextView TV_DescripcionProducto, TV_TipoProducto, TV_ClasificacionProducto, TV_Existencia, TV_Clave;

        ViewHolder(View itemView) {
            super(itemView);
            Icon_Inventario = itemView.findViewById(R.id.Icon_Inventario);
            TV_DescripcionProducto = itemView.findViewById(R.id.TV_DescripcionProducto);
            TV_TipoProducto = itemView.findViewById(R.id.TV_TipoProducto);
            TV_ClasificacionProducto = itemView.findViewById(R.id.TV_ClasificacionProducto);
            TV_Existencia = itemView.findViewById(R.id.TV_Existencia);
            TV_Clave = itemView.findViewById(R.id.TV_Clave);
        }

        void bindData(final ListProductos item, final int position) {
            TV_DescripcionProducto.setText(item.getDesProducto());
            TV_TipoProducto.setText(item.getTipoProducto());
            TV_ClasificacionProducto.setText(item.getClasProducto());
            TV_Existencia.setText(item.getExistencia());
            TV_Clave.setText(item.getClave());

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