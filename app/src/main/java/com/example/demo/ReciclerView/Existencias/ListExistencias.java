package com.example.demo.ReciclerView.Existencias;

public class ListExistencias {
    public String id_Producto;
    public String Desc_Producto;
    public String ArtEsperados;
    public String ArtEncontrados;
    public String Fecha;
    public String Clave;

    public ListExistencias(String id_Producto, String desc_Producto, String artEsperados, String artEncontrados, String Clave, String Fecha) {
        this.id_Producto = id_Producto;
        Desc_Producto = desc_Producto;
        ArtEsperados = artEsperados;
        ArtEncontrados = artEncontrados;
        this.Clave = Clave;
        this.Fecha = Fecha;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setTFecha(String TV_Fecha) {
        this.Fecha = TV_Fecha;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String TV_Clave) {
        this.Clave = TV_Clave;
    }

    public String getDesc_Producto() {
        return Desc_Producto;
    }

    public void setDesc_Producto(String desc_Producto) {
        Desc_Producto = desc_Producto;
    }

    public String getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(String id_Producto) {
        this.id_Producto = id_Producto;
    }

    public String getArtEsperados() {
        return ArtEsperados;
    }

    public void setArtEsperados(String artEsperados) {
        ArtEsperados = artEsperados;
    }

    public String getArtEncontrados() {
        return ArtEncontrados;
    }

    public void setArtEncontrados(String artEncontrados) {
        ArtEncontrados = artEncontrados;
    }
}
