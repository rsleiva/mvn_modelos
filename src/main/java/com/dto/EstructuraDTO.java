package com.dto;

public class EstructuraDTO {
    
    private String Region;
    private String Zona; 
    private String BT; 
    private String MT; 
    private String Total;
    public String getRegion() {
        return Region;
    }
    public void setRegion(String region) {
        Region = region;
    }
    public String getZona() {
        return Zona;
    }
    public void setZona(String zona) {
        Zona = zona;
    }
    public String getBT() {
        return BT;
    }
    public void setBT(String bT) {
        BT = bT;
    }
    public String getMT() {
        return MT;
    }
    public void setMT(String mT) {
        MT = mT;
    }
    public String getTotal() {
        return Total;
    }
    public void setTotal(String total) {
        Total = total;
    } 

/*
 * 
 * 			fila.put("REGION", campo[0]);
			fila.put("ZONA", campo[1]);
			fila.put("BT", campo[2]);
			fila.put("MT", campo[3]);
			fila.put("TOTAL", campo[4]);
 */

}
