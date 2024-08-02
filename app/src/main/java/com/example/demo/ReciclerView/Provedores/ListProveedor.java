package com.example.demo.ReciclerView.Provedores;


import java.io.Serializable;

public class ListProveedor implements Serializable {
    private String id_prov;
    private String nombre;
    private String RFC;
    private String claveProv;

    public ListProveedor(String id_prov, String nombre, String RFC, String claveProv) {
        this.id_prov = id_prov;
        this.nombre = nombre;
        this.RFC = RFC;
        this.claveProv = claveProv;
    }

    public String getProv() {
        return id_prov;
    }

    public void setProv(String prov)  {
        this.id_prov = prov;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getClaveProv() {
        return claveProv;
    }

    public void setClaveProv(String claveProv) {
        this.claveProv = claveProv;
    }
}
