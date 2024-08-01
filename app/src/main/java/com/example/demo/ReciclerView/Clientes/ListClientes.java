package com.example.demo.ReciclerView.Clientes;

import java.io.Serializable;

public class ListClientes implements Serializable {
    public String Id;
    public String DesClientes;
    public String RFC;
    public String Clave;

    public ListClientes(String id, String desClientes, String RFC, String clave) {
        Id = id;
        DesClientes = desClientes;
        this.RFC = RFC;
        Clave = clave;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDesClientes() {
        return DesClientes;
    }

    public void setDesClientes(String desClientes) {
        DesClientes = desClientes;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }
}
