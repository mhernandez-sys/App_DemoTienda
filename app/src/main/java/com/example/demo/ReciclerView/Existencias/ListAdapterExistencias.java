package com.example.demo.ReciclerView.Existencias;

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
import com.example.demo.ReciclerView.Movimientos.ListAdapterMovimiento;
import com.example.demo.ReciclerView.Movimientos.ListMovimientos;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterExistencias extends RecyclerView.Adapter<ListAdapterExistencias.ViewHolder> {
    private List<ListExistencias> mData;
    private List<ListExistencias> mDataFiltered;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListeners listeners;
    private int selectedPosition = -1; // Índice del elemento seleccionado

    public interface OnItemClickListeners{
        void onItemClick(ListExistencias item);
        void onItemLongClick(ListExistencias item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_existencias, parent, false);
        return new ListAdapterExistencias.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mDataFiltered.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    public void setItems(List<ListExistencias> items) {
        this.mData = items;
        this.mDataFiltered = new ArrayList<>(items);
    }

    public void filter(String text) {
        mDataFiltered.clear();
        if (text.isEmpty()) {
            mDataFiltered.addAll(mData);
        } else {
            text = text.toLowerCase();
            for (ListExistencias item : mData) {
                if (item.getDesc_Producto().toLowerCase().contains(text) ||
                        item.getClave().toLowerCase().contains(text) ||
                        item.getFecha().toLowerCase().contains(text) ||
                        item.getArtEncontrados().toLowerCase().contains(text) ||
                        item.getArtEsperados().toLowerCase().contains(text)) {
                    mDataFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public ListAdapterExistencias(List<ListExistencias> itemList, Context context, OnItemClickListeners listeners) {
        this.mData = itemList;
        this.mDataFiltered = new ArrayList<>(itemList);
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listeners = listeners;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TV_DescripcionProducto, TV_CanEsperada, TV_Can_Encontrados, TV_Clave, TV_Fecha;

        ViewHolder(View itemView) {
            super(itemView);
            TV_DescripcionProducto = itemView.findViewById(R.id.TV_DescProducto);
            TV_CanEsperada = itemView.findViewById(R.id.TV_CanEsperada);
            TV_Can_Encontrados = itemView.findViewById(R.id.TV_Can_Encontrados);
            TV_Clave = itemView.findViewById(R.id.TV_Clave);
            TV_Fecha = itemView.findViewById(R.id.TV_Fecha);
        }

        void bindData(final ListExistencias item, final int position) {
            TV_DescripcionProducto.setText(item.getDesc_Producto());
            TV_CanEsperada.setText(item.getArtEsperados());
            TV_Can_Encontrados.setText(item.getArtEncontrados());
            TV_Clave.setText(item.getClave());
            TV_Fecha.setText(item.getFecha());

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
