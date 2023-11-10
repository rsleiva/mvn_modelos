package com.dto;

import java.sql.Date;

public class AfectacionesDTO {
    
    private int id;
    private String nro_doc;
    private int objectid;
    private String origen;
    private Date fecha_documento;
    private String ct;
    private int cant_afectaciones;
    private Date inicio;
    private Date fin;
    private int is_restored;
    private int is_affected;
    private int op_logidto;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNro_doc() {
        return nro_doc;
    }
    public void setNro_doc(String nro_doc) {
        this.nro_doc = nro_doc;
    }
    public int getObjectid() {
        return objectid;
    }
    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
    public String getOrigen() {
        return origen;
    }
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public Date getFecha_documento() {
        return fecha_documento;
    }
    public void setFecha_documento(Date fecha_documento) {
        this.fecha_documento = fecha_documento;
    }
    public String getCt() {
        return ct;
    }
    public void setCt(String ct) {
        this.ct = ct;
    }
    public int getCant_afectaciones() {
        return cant_afectaciones;
    }
    public void setCant_afectaciones(int cant_afectaciones) {
        this.cant_afectaciones = cant_afectaciones;
    }
    public Date getInicio() {
        return inicio;
    }
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    public Date getFin() {
        return fin;
    }
    public void setFin(Date fin) {
        this.fin = fin;
    }
    public int getIs_restored() {
        return is_restored;
    }
    public void setIs_restored(int is_restored) {
        this.is_restored = is_restored;
    }
    public int getIs_affected() {
        return is_affected;
    }
    public void setIs_affected(int is_affected) {
        this.is_affected = is_affected;
    }
    public int getOp_logidto() {
        return op_logidto;
    }
    public void setOp_logidto(int op_logidto) {
        this.op_logidto = op_logidto;
    }


}
