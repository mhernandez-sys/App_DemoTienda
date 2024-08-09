package com.example.demo.ReciclerView.Movimientos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ReciclerView.Productos.ListAdapterProductos;
import com.example.demo.ReciclerView.Productos.ListProductos;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterMovimiento extends RecyclerView.Adapter<ListAdapterMovimiento.ViewHolder> {
    private List<ListMovimientos> mData;
    private List<ListMovimientos> mDataFiltered; // Lista para mantener los datos filtrados
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListeners listeners;
    private int selectedPosition = -1; // Índice del elemento seleccionado
    private int lastPosition = -1;


    public interface OnItemClickListeners{
        void onItemClick(ListMovimientos item);
        void onItemLongClick(ListMovimientos item);
    }

    public ListAdapterMovimiento(Context context, List<ListMovimientos> itemList, OnItemClickListeners listeners) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataFiltered = new ArrayList<>(itemList);
        this.mData = itemList;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_movimientos, parent, false);
        return new ListAdapterMovimiento.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterMovimiento.ViewHolder holder, int position) {
        holder.bindData(mDataFiltered.get(position), position);

// Añadir la animación aquí
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // Solo animar los elementos que no han sido mostrados todavía
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion_uno);
            viewToAnimate.startAnimation(animation);

    }
    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }
    public void setItems(List<ListMovimientos> items) {
        this.mData = items;
        this.mDataFiltered = new ArrayList<>(items);
    }

    public void filter(String text) {
        mDataFiltered.clear();
        if (text.isEmpty()) {
            mDataFiltered.addAll(mData);
        } else {
            text = text.toLowerCase();
            for (ListMovimientos item : mData) {
                if (item.getDesc_Producto().toLowerCase().contains(text) ||
                        item.getDesc_Movimiento().toLowerCase().contains(text) ||
                        item.getFecha().toLowerCase().contains(text) ||
                        item.getSKU_NumLote().toLowerCase().contains(text) ||
                        item.getExistencia().toLowerCase().contains(text)) {
                    mDataFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Icon_Movimientos;
        TextView TV_DescripcionProducto, TV_DescMovimineto, TV_SKU_NumLote, TV_Existencia, TV_Fecha, TV_DescConcepto;


        ViewHolder(View itemView) {
            super(itemView);
            Icon_Movimientos = itemView.findViewById(R.id.Icon_Movimientos);
            TV_DescripcionProducto = itemView.findViewById(R.id.TV_DescripcionProducto);
            TV_DescMovimineto = itemView.findViewById(R.id.TV_DescMovimineto);
            TV_DescConcepto = itemView.findViewById(R.id.TV_DescConcepto);
            TV_SKU_NumLote = itemView.findViewById(R.id.TV_SKU_NumLote);
            TV_Existencia = itemView.findViewById(R.id.TV_Existencia);
            TV_Fecha = itemView.findViewById(R.id.TV_Fecha);
        }

        void bindData(final ListMovimientos item, final int position) {
            TV_DescripcionProducto.setText(item.getDesc_Producto());
            TV_DescMovimineto.setText(item.getDesc_Movimiento());
            TV_DescConcepto.setText(item.getDesc_Concepto());
            TV_SKU_NumLote.setText(item.getSKU_NumLote());
            TV_Existencia.setText(item.getExistencia());
            TV_Fecha.setText(item.getFecha());

            // Cambiar el icono dependiendo del tipo de movimiento
            if ("Entrada".equalsIgnoreCase(item.getDesc_Movimiento())) {
                Icon_Movimientos.setImageResource(R.drawable.verde);
            } else if ("Salida".equalsIgnoreCase(item.getDesc_Movimiento())) {
                Icon_Movimientos.setImageResource(R.drawable.rojo);
            }

            // Resaltar el fondo si el elemento está seleccionado
            itemView.setBackgroundColor(selectedPosition == position ? Color.LTGRAY : Color.TRANSPARENT);

            itemView.setOnClickListener(v -> {
                listeners.onItemClick(item);
                selectedPosition = position;
                notifyDataSetChanged(); // Notificar cambios al adapter para actualizar la vista

                // Añadir animación al hacer clic
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion_uno);
                itemView.startAnimation(animation);
            });

            itemView.setOnLongClickListener(v -> {
                listeners.onItemLongClick(item);
                return true;
            });
        }
    }
}
