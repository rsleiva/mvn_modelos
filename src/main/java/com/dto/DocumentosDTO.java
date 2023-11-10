package com.dto;

import java.sql.Date;

public class DocumentosDTO {
    
    private int id;
    private String name;
    private Date creation_time;
    private byte estado;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte getEstado() {
        return estado;
    }
    public void setEstado(byte estado) {
        this.estado = estado;
    }
    public Date getCreation_time() {
        return creation_time;
    }
    public void setCreation_time(Date creation_time) {
        this.creation_time = creation_time;
    }
    
}
