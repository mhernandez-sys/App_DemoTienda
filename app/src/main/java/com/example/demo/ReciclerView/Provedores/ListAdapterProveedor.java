package com.example.demo.ReciclerView.Provedores;

import android.content.Context;
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

public class ListAdapterProveedor extends RecyclerView.Adapter<ListAdapterProveedor.ProveedorViewHolder> {

    private List<ListProveedor> proveedorList;
    private List<ListProveedor> proveedorListFull;
    private OnItemClickListeners onItemClickListeners;

    public interface OnItemClickListeners {
        void onItemClick(ListProveedor item);
        void onItemLongClick(ListProveedor item);
    }

    public ListAdapterProveedor(List<ListProveedor> proveedorList, Context context, OnItemClickListeners onItemClickListeners) {
        this.proveedorList = proveedorList;
        this.proveedorListFull = new ArrayList<>(proveedorList);
        this.onItemClickListeners = onItemClickListeners;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proveedor, parent, false);
        return new ProveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        ListProveedor proveedor = proveedorList.get(position);

        holder.tvNombreProveedor.setText(proveedor.getNombre());
        holder.tvRFCProveedor.setText(proveedor.getRFC());
        holder.tvClaveProveedor.setText(proveedor.getClaveProv());
        holder.itemView.setOnClickListener(v -> onItemClickListeners.onItemClick(proveedor));
        holder.itemView.setOnLongClickListener(v -> {
            onItemClickListeners.onItemLongClick(proveedor);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return proveedorList.size();
    }

    public void filter(String text) {
        proveedorList.clear();
        if (text.isEmpty()) {
            proveedorList.addAll(proveedorListFull);
        } else {
            text = text.toLowerCase();
            for (ListProveedor item : proveedorListFull) {
                if (item.getNombre().toLowerCase().contains(text) || item.getRFC().toLowerCase().contains(text) || item.getClaveProv().toLowerCase().contains(text)) {
                    proveedorList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProveedor, tvRFCProveedor, tvClaveProveedor;
        ImageView iconProveedor;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProveedor = itemView.findViewById(R.id.TV_NombreProveedor);
            tvRFCProveedor = itemView.findViewById(R.id.TV_RFCProveedor);
            tvClaveProveedor = itemView.findViewById(R.id.TV_ClaveProveedor);
            iconProveedor = itemView.findViewById(R.id.Icon_Proveedor);
        }
    }
}
