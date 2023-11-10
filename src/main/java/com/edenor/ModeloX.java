package com.edenor;

import java.util.Date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.constantes.SQL;
import com.dto.AfectacionesDTO;
import com.dto.DocumentosDTO;
import com.interfaces.IModelo;

public class ModeloX implements IModelo {
    
    private Propiedades p;
    private Conectar cnn;
    private int nroModelo;
    private String fecha;
    private List<DocumentosDTO> doc;
    private List<String> docs;

    public ModeloX(int nroModelo, Propiedades p){
        this.nroModelo=nroModelo;
        this.p= p;
        cnn= new Conectar(p.getDatos());
        fecha= getDate(new Date(), "yyyyMMddHHmm");
    }

    @Override
    public void obtieneDocumentosNuevos() {
        List<AfectacionesDTO> afe = new ArrayList<AfectacionesDTO>();
        List<String> notepad = new ArrayList<String>();
        try {
            /*
             * Obtiene la lista de los documentos que aparecieron con cts
             */
            if (docs==null || docs.isEmpty()) {
                docs= obtieneListaDocs();
            }            
            /*
             * Genero la conexion a la bd
             */
            cnn.Oracle_connect();
            /*
             * Obtengo los nuevos documentos
             */
            doc= cnn.getDocumentosActivos(SQL.NUEVOS_DOCUMENTOS);
            /*
             * Envio cada Documento para guardar en el archivo notepad, dependiendo del modelo
             */
            if (!doc.isEmpty()) {
                System.out.println(""
                    +"\n##########################################################################################################################"
                    +"\nCAMPAÑA: "+fecha
                    +"\n##########################################################################################################################"
                    );
                for (DocumentosDTO d : doc) {
                    if (!existeDoc(d.getName())) {
            
                        // System.out.println(
                        //     "--------------------------------------------------------------------------------------------------------------------------"
                        //     + "\n\tDOCUMENTO: " + d.getName()
                        //     // + "\n\t-------------------------------------"
                        //    );
                        /*
                        * Se obtiene datos del Modelo, segun corresponda
                        */
                        try {
                            switch (nroModelo) {
                                case 1:
                                    afe= cnn.getAfectaciones(SQL.SQL_MODELO,d.getId());
                                    break;
                                case 2:
                                    afe= cnn.getAfectaciones(SQL.SQL_MODELO_2,d.getId());
                                    break;
                                default:
                                    break;
                            }                            
                        } catch (Exception e) {
                            System.out.println("Error al obtener las Afectaciones");
                        }
                        /*
                         * Si el documento tiene cts afectados lo guardo en listaDocs
                         */
                        if (!afe.isEmpty()) {
                            exportaDoc(d.getName());
                        }
                        /*
                        * Muestro datos y exporto a notepad
                        */
                        for (AfectacionesDTO a : afe) {
                            notepad.add(
                                fecha
                                +";"+a.getOrigen()
                                +";"+d.getId()
                                +";"+d.getName()
                                +";"+d.getEstado()
                                +";"+getDate(a.getFecha_documento(), "dd/MM/yyyy HH:mm:ss")
                                +";"+getDate(a.getInicio(),"dd/MM/yyyy HH:mm:ss")
                                +";"+getDate(a.getFin(),"dd/MM/yyyy HH:mm:ss")
                                +";"+a.getObjectid()
                                +";"+a.getCt().replace(" ",";")
                                +";"+a.getCant_afectaciones()
                                +";"+a.getIs_affected()
                                +";"+a.getIs_restored()
                                +";"+analizaCT(
                                    d.getId(),
                                    d.getName(),
                                    a.getObjectid(),
                                    a.getCt().replace(" ",";"),
                                    a.getCant_afectaciones()
                                )
                            );
                        }
                    }
                }
                // Exporta al notepad
                exportaTXT(notepad);
                System.out.println("\n\n");
            }
            // Cerrar la conexion
            cnn.cierraConexion();
        } catch (Exception e) {
            System.out.println("No se pudo establecer conexion con Oracle");
        } 
    }

    private String analizaCT(int iddoc, String nro_documento, int objectid, String ct, int afectaciones) {
        // Obtengo las veces que el objectid se encuentra afectado, si aparece mas de una vez no cargo este CT
        ct= extraerNumero(ct);
        //
        Integer otroDocActivo= cnn.getOtroDocActivo(objectid);
        Integer otraOperacion= cnn.getOtraOperacion(nro_documento, objectid);
        Integer confirmado= cnn.getConfirmado(iddoc);
        String delta= cnn.getDelta(ct);

        if (ct.length()<4){
            return "CT Corto.";
        } else if (ct.equals(null) || ct.isEmpty()) {
            return "CT Nulo.";
        } else if (otroDocActivo>0) {
            return "CT en otro Doc.";
        } else if (otraOperacion>0){
            return "CT en otra Operacion.";
        } else if (afectaciones==0){
            return "CT sin afectacion";
        } else if (!(confirmado==1 || delta.equals("DELTA"))) {
            return "CT no confirmado o no es del delta";
        } else {
            return "Ok";
        }
    }

    private String extraerNumero(String cadena) {
        // Usando expresiones regulares para extraer el número
        String patron = "\\d+";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(patron);
        java.util.regex.Matcher m = p.matcher(cadena);

        if (m.find()) {
            return m.group(); // Devuelve el primer número encontrado en la cadena
        }

        return null; // Devuelve null si no se encuentra ningún número
    }    

    @Override
    public void exportaTXT(List<String> notepad) {
        try {
            // Crear un objeto FileWriter
            FileWriter writer = new FileWriter(p.getNotepad(),true);

            // Escribir los datos en el archivo
            for (String linea : notepad) {
                writer.write(linea + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error al guardar registros al Notepad");
        }
    }

    @Override
    public void exportaDoc(String doc){
        try {
            // Crear un objeto FileWriter
            FileWriter writer = new FileWriter(p.getDoc(),true);

            // Escribir los datos en el archivo
            writer.write(doc + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error al guardar registros al Doc");
        }        
    }

    @Override
    public List<String> obtieneListaDocs(){
        File listaDocs = new File(p.getDoc());
        List<String> docs = new ArrayList<>();

        // Leer las líneas del notepad
        try (BufferedReader reader = new BufferedReader(new FileReader(listaDocs))) {
            String line;
            while ((line = reader.readLine()) != null) {
                docs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return docs;
    }

    @Override
    public boolean existeDoc(String doc){
        return docs.contains(doc);
    }

    public String getDate(Date fechaActual, String mascara){
        if (fechaActual==null) {
            return "Null";
        }
        SimpleDateFormat formatoFecha = new SimpleDateFormat(mascara);
        // Convertir la fecha a una cadena
        String fechaEnCadena = formatoFecha.format(fechaActual);
        // Imprimir la fecha en la consola
        return fechaEnCadena;        
    }

}
