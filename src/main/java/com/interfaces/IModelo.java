package com.interfaces;

import java.util.List;

public interface IModelo {
    
    public void obtieneDocumentosNuevos();

    public void exportaTXT(List<String> notepad);

    public void exportaDoc(String doc);

    public List<String> obtieneListaDocs();

    public boolean existeDoc(String doc);
    
}
