package com.example.demo.spinners;

public class TipoItem {
    private String id;
    private String descripcion;

    public TipoItem(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion; // Esto es lo que se mostrará en el Spinner
    }
}
