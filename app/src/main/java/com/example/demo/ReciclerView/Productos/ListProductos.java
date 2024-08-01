package com.example.demo.ReciclerView.Productos;

import java.io.Serializable;

public class ListProductos implements Serializable {
    public String Id;
    public String DesProducto;
    public String TipoProducto;
    public String ClasProducto;
    public String Existencia;
    public String Clave;

    public ListProductos(String id, String desProducto, String tipoProducto, String clasProducto, String existencia, String clave) {
        Id = id;
        DesProducto = desProducto;
        TipoProducto = tipoProducto;
        ClasProducto = clasProducto;
        Existencia = existencia;
        Clave = clave;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDesProducto() {
        return DesProducto;
    }

    public void setDesProducto(String desProducto) {
        DesProducto = desProducto;
    }

    public String getTipoProducto() {
        return TipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        TipoProducto = tipoProducto;
    }

    public String getClasProducto() {
        return ClasProducto;
    }

    public void setClasProducto(String clasProducto) {
        ClasProducto = clasProducto;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }
}
