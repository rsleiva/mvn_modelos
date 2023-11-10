package com.dto;

public class DatosDTO {
    
    //Constantes
    public final String FUENTE_HTML_FIN = "</table> <br><table class='tlogo'> <tr>  <td>  <img width=450 height=145 src='logo.png'  alt='Descripci�n: logo de Edenor' >  </td> </tr></table></body> </html>";

    //Conectar a DB
    private String driverClass;
    private String Cadena;
    private String User;
    private String Pass;

    //Send Mail
    private String Host;
    private String From;
    private String To;
    private String CC;
    private String CO;
    private String Subject;
    private String Body;

    //Excel
    private String ruta_excel="../ConsolidadoBTMT.xlsx";
    private String notepad;
    private String doc;

    //Getters and Setters
    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getCadena() {
        return Cadena;
    }

    public void setCadena(String cadena) {
        Cadena = cadena;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String cC) {
        CC = cC;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String cO) {
        CO = cO;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getRuta_excel() {
        return ruta_excel;
    }

    public void setRuta_excel(String ruta_excel) {
        this.ruta_excel = ruta_excel;
    }

    public String getNotepad() {
        return notepad;
    }

    public void setNotepad(String notepad) {
        this.notepad = notepad;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
