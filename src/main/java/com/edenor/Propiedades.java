package com.edenor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.dto.DatosDTO;

public class Propiedades {

    private static Properties properties;

    public Propiedades(){
		properties = new Properties();
		try (FileInputStream input = new FileInputStream("./config.properties")) {
			properties.load(input);
            // System.out.println("Abriendo propiedades...");
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("Error al obtener las propiedades! "+e);
		}
	}
    
    public String getdriverClass(){
        return properties.getProperty("driverClass");
    }
    public String getCadena(){
        return properties.getProperty("oracle_cnn_pro");
    }
    public String getUser(){
        return properties.getProperty("oracle_cnn_usu");
    }
    public String getPass(){
        return properties.getProperty("oracle_cnn_pss");
    }
    public String getHost(){
        return properties.getProperty("mail_host");
    }
    public String getFrom(){
        return properties.getProperty("mail_from");
    }
    public String getTo(){
        return properties.getProperty("mail_to");
    }
    public String getCC(){
        return properties.getProperty("mail_cc");
    }
    public String getCO(){
        return properties.getProperty("mail_co");
    }
    public String getSubject(){
        return properties.getProperty("mail_subject");
    }
    public String getBody(){
        return properties.getProperty("mail_body");
    }
    public String getExcel(){
        return properties.getProperty("excel");
    }
    public String getNotepad(){
        return properties.getProperty("notepad");
    }
    public String getDoc(){
        return properties.getProperty("doc");
    }
	public DatosDTO getDatos(){
		DatosDTO datos=new DatosDTO();
        try {
            // System.out.println("Obteniendo Propiedades...");
            datos.setDriverClass(getdriverClass());
            datos.setCadena(getCadena());
            datos.setUser(getUser());
            datos.setPass(getPass());
            datos.setHost(getHost());
            datos.setFrom(getFrom());
            datos.setTo(getTo());
            datos.setCC(getCC());
            datos.setCO(getCO());
            datos.setSubject(getSubject());
            datos.setBody(getBody());
            datos.setRuta_excel(getExcel());
            datos.setNotepad(getNotepad());
            datos.setDoc(getDoc());

            // System.out.println("Propiedades obtenidas!");            
        } catch (Exception e) {
            System.out.println("Error al obtener propiedades!"); 
            datos=null;
        }

        return datos;
	}    

}
