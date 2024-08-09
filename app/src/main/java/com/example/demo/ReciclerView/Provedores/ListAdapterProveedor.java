package com.example.demo.ReciclerView.Provedores;

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

public class ListAdapterProveedor extends RecyclerView.Adapter<ListAdapterProveedor.ViewHolder> {

    private List<ListProveedor> mData;
    private List<ListProveedor> mDataFiltered;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListeners onItemClickListeners;
    private int selectedPosition=-1;  // Índice del elemento seleccionado
    private int lastPosition = -1;

    public interface OnItemClickListeners {
        void onItemClick(ListProveedor item);
        void onItemLongClick(ListProveedor item);
    }

    public ListAdapterProveedor(List<ListProveedor> itemList, Context context, OnItemClickListeners onItemClickListeners) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.mDataFiltered = new ArrayList<>(itemList); // Inicializa mDataFiltered con todos los datos
        this.onItemClickListeners = onItemClickListeners;
    }

    @NonNull
    @Override
    public ListAdapterProveedor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_proveedor, parent, false);
        return new ListAdapterProveedor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterProveedor.ViewHolder holder, int position) {
        holder.bindData(mDataFiltered.get(position), position);
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // Solo animar los elementos que no han sido mostrados todavía
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion_uno);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mDataFiltered.size(); // Usa la lista filtrada
    }

    public void filter(String text) {
        mDataFiltered.clear();
        if (text.isEmpty()) {
            mDataFiltered.addAll(mData);
        } else {
            text = text.toLowerCase();
            for (ListProveedor item : mData) {
                if (item.getClaveProv().toLowerCase().contains(text) ||
                        item.getNombre().toLowerCase().contains(text) ||
                        item.getRFC().toLowerCase().contains(text)) {
                    mDataFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProveedor, tvRFCProveedor, tvClaveProveedor;
        ImageView iconProveedor;

        ViewHolder(View itemView) {
            super(itemView);
            tvNombreProveedor = itemView.findViewById(R.id.TV_NombreProveedor);
            tvRFCProveedor = itemView.findViewById(R.id.TV_RFCProveedor);
            tvClaveProveedor = itemView.findViewById(R.id.TV_ClaveProveedor);
            iconProveedor = itemView.findViewById(R.id.Icon_Proveedor);
        }

        void bindData(final ListProveedor item, final int position) {
            tvNombreProveedor.setText(item.getNombre());
            tvRFCProveedor.setText(item.getRFC());
            tvClaveProveedor.setText(item.getClaveProv());

            // Resaltar el fondo si el elemento está seleccionado
            itemView.setBackgroundColor(selectedPosition == position ? Color.LTGRAY : Color.TRANSPARENT);

            itemView.setOnClickListener(v -> {
                onItemClickListeners.onItemClick(item);
                selectedPosition = position;
                notifyDataSetChanged(); // Notificar cambios al adapter para actualizar la vista

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacion_uno);
                itemView.startAnimation(animation);
            });

            itemView.setOnLongClickListener(v -> {
                onItemClickListeners.onItemLongClick(item);
                return true;
            });
        }
    }
}
