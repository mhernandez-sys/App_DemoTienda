package com.example.demo.recyclerview;

import java.io.Serializable;

public class ListInventario implements Serializable {
    public String id;
    public String descripcion;
    public String encontrados;
    public String esperados;
    public String Rfids;
    public String Sucursales;
    public String IdDet;


    public ListInventario(String id, String descripcion, String encontrados, String esperados, String Rfids, String sucursales, String idDet) {
        this.id = id;
        this.descripcion = descripcion;
        this.encontrados = encontrados;
        this.esperados = esperados;
        this.Rfids = Rfids;
        this.Sucursales = sucursales;
        this.IdDet = idDet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRfids() {
        return Rfids;
    }

    public void setRfids(String rfids) {
        Rfids = rfids;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEncontrados() {
        return encontrados;
    }

    public void setEncontrados(String encontrados) {
        this.encontrados = encontrados;
    }

    public String getEsperados() {
        return esperados;
    }

    public void setEsperados(String esperados) {
        this.esperados = esperados;

    }
    public String getSucursales() {
        return Sucursales;
    }

    public void setSucursales(String sucursales) {
        Sucursales = sucursales;
    }
    public String getIdDet() {
        return IdDet;
    }

    public void setIdDet(String idDet) {
        IdDet = idDet;
    }


}
