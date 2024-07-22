package com.example.demo.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

import java.util.List;

public class ListAdapterInventario extends RecyclerView.Adapter<ListAdapterInventario.ViewHolder> {
    private List<ListInventario> mData;
    private LayoutInflater mInflater;
    private Context context;
    final OnItemClickListeners listeners;

    public interface OnItemClickListeners {
        void onItemClick(ListInventario item);
    }

    public ListAdapterInventario(List<ListInventario> itemList, Context context, OnItemClickListeners listeners) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listinventario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ListInventario> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage, statusCircle;
        TextView descripcion, encontrados, esperados, sucursales;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            statusCircle = itemView.findViewById(R.id.statusCircle);
            descripcion = itemView.findViewById(R.id.TV_Descripcion);
            encontrados = itemView.findViewById(R.id.TV_Encontrados);
            esperados = itemView.findViewById(R.id.TV_Esperados);
            sucursales = itemView.findViewById(R.id.TV_Ciudades);
        }

        void bindData(final ListInventario item) {
            descripcion.setText(item.getDescripcion());
            encontrados.setText(item.getEncontrados());
            esperados.setText(item.getEsperados());
            sucursales.setText(item.getSucursales());

            // Comparación de encontrados y esperados para cambiar el color del círculo
            if (Integer.parseInt(item.getEncontrados()) >= Integer.parseInt(item.getEsperados())) {
                statusCircle.setImageResource(R.drawable.circle_green); // Verde si encontrados >= esperados
            } else {
                statusCircle.setImageResource(R.drawable.circle_gray); // Gris si encontrados < esperados
            }

            // Manejo del click en el item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.onItemClick(item);
                }
            });

        }
    }
}
