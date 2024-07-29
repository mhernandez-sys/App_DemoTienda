package com.example.demo.ReciclerView;

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

import java.util.List;

public class ListAdapterProductos extends RecyclerView.Adapter<ListAdapterProductos.ViewHolder> {
    private List<ListProductos> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapterProductos.OnItemClickListeners listeners;
    private int selectedPosition = -1; // Índice del elemento seleccionado



    public interface OnItemClickListeners{
        void onItemClick(ListProductos item);
        void onItemLongClick(ListProductos item);
    }

    public ListAdapterProductos(List<ListProductos> itemList, Context context, ListAdapterProductos.OnItemClickListeners listeners){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
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
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size(); //Nos da el tamaños de elementos que hay en esa lista
    }

    public void setItem (List<ListProductos> items){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Icon_Inventario;
        TextView TV_DescripcionProducto, TV_TipoProducto, TV_ClasificacionProducto, TV_Existencia, TV_Clave;

        ViewHolder(View itemView){
            super(itemView);
            Icon_Inventario = itemView.findViewById(R.id.Icon_Inventario);
            TV_DescripcionProducto = itemView.findViewById(R.id.TV_DescripcionProducto);
            TV_TipoProducto = itemView.findViewById(R.id.TV_TipoProducto);
            TV_ClasificacionProducto = itemView.findViewById(R.id.TV_ClasificacionProducto);
            TV_Existencia = itemView.findViewById(R.id.TV_Existencia);
            TV_Clave = itemView.findViewById(R.id.TV_Clave);
        }

        void bindData (final ListProductos item){
            TV_DescripcionProducto.setText(item.getDesProducto());
            TV_TipoProducto.setText(item.getTipoProducto());
            TV_ClasificacionProducto.setText(item.getClasProducto());
            TV_Existencia.setText(item.getExistencia());
            TV_Clave.setText(item.getClave());
            // Resaltar el fondo si el elemento está seleccionado
            itemView.setBackgroundColor(selectedPosition == getPosition() ? Color.LTGRAY : Color.TRANSPARENT);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.onItemClick(item);
                    listeners.onItemClick(item);
                    // Actualizar el estado de selección
                    selectedPosition = getPosition();
                    notifyDataSetChanged(); // Notificar cambios al adapter
                }
            });
            itemView.setOnLongClickListener(v -> {
                listeners.onItemLongClick(item);
                return true;
            });
        }

    }

}
