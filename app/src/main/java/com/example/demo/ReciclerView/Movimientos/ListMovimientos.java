package com.example.demo.ReciclerView.Movimientos;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.Serializable;

public class ListMovimientos implements Serializable {
    public String id_TipoMovimiento;
    public String id_Producto;
    public String id_Concepto;
    public String Desc_Movimiento;
    public String Desc_Producto;
    public String Desc_Concepto;
    public String SKU_NumLote;
    public String Existencia;
    public String Fecha;


    public ListMovimientos(String id_TipoMovimiento, String id_Producto, String desc_Movimiento, String desc_Producto, String SKU_NumLote, String existencia, String fecha, String id_Concepto, String desc_Concepto) {
        this.id_TipoMovimiento = id_TipoMovimiento;
        this.id_Producto = id_Producto;
        Desc_Movimiento = desc_Movimiento;
        Desc_Producto = desc_Producto;
        this.SKU_NumLote = SKU_NumLote;
        Existencia = existencia;
        Fecha = fecha;
        this.id_Concepto = id_Concepto;
        Desc_Concepto = desc_Concepto;
    }

    public String getDesc_Concepto() {
        return Desc_Concepto;
    }

    public void setDesc_Concepto(String desc_Concepto) {
        Desc_Concepto = desc_Concepto;
    }

    public String getId_Concepto() {
        return id_Concepto;
    }

    public void setId_Concepto(String id_Concepto) {
        this.id_Concepto = id_Concepto;
    }

    public String getId_TipoMovimiento() {
        return id_TipoMovimiento;
    }

    public void setId_TipoMovimiento(String id_TipoMovimiento) {
        this.id_TipoMovimiento = id_TipoMovimiento;
    }

    public String getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(String id_Producto) {
        this.id_Producto = id_Producto;
    }

    public String getDesc_Movimiento() {
        return Desc_Movimiento;
    }

    public void setDesc_Movimiento(String desc_Movimiento) {
        Desc_Movimiento = desc_Movimiento;
    }

    public String getDesc_Producto() {
        return Desc_Producto;
    }

    public void setDesc_Producto(String desc_Producto) {
        Desc_Producto = desc_Producto;
    }

    public String getSKU_NumLote() {
        return SKU_NumLote;
    }

    public void setSKU_NumLote(String SKU_NumLote) {
        this.SKU_NumLote = SKU_NumLote;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
