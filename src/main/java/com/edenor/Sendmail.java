package com.edenor;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.Address;

// import org.apache.poi.ss.formula.functions.Address;

public class Sendmail {

    private String mail_host;
    private String mail_from;
    private String mail_to;
    private String mail_subject;
	private String Hora= null;
	private String Dia= null;
	private Iterator<HashMap<String, String>> it;
	private String msgCancela = "";
    private String ruta_excel;
	private Vector<String> dir_no_encontradas = new Vector<>();

    public Sendmail(String mail_host, String mail_from, String mail_to, String mail_subject, String Hora, String Dia, String ruta_excel){
        this.mail_host=mail_host;
        this.mail_from=mail_from;
        this.mail_to=mail_to;
        this.mail_subject=mail_subject;
        this.Hora=Hora;
        this.Dia=Dia;
        this.ruta_excel=ruta_excel;
    }
    
	public void generar(List<HashMap<String, String>> tabla2, String encabezado, boolean hayDatos) throws Exception {
		Hashtable<String, String> hst_Mail = new Hashtable<>();
		boolean enviarMail = false;
		encabezado=" ";

		try {
			enviarMail = true;
			hst_Mail.put("mailHost", mail_host);              //mail.edenor
			hst_Mail.put("DE", mail_from);                    //centrodeinformacion@edenor.com
			hst_Mail.put("PARA", mail_to);
			hst_Mail.put("ASUNTO", String.format(mail_subject,this.Dia,this.Hora));              //"Informacion Enre - " + this.Dia + "-" + this.Hora + ":00 Hs.");
			String HTML_Estucture = "<html>";
			HTML_Estucture = HTML_Estucture + "<head>";
			HTML_Estucture = HTML_Estucture + "<style id='Mail_Styles'>";
			HTML_Estucture = HTML_Estucture + "<!--table";
			HTML_Estucture = HTML_Estucture + ".xl1530982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:bottom; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl6530982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:700; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:1.0pt solid windowtext; \tbackground:#FFC000; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl6630982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:5px; \tcolor:black; \tfont-size:10pt; \tfont-weight:700; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:center; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:.5pt solid windowtext; \tbackground:#C5D9F1; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl6730982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:700; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:right; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:1.0pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:.5pt solid windowtext; \tbackground:#FFC000; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl6830982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:5px; \tmso-ignore:padding; \tcolor:black; \tfont-size:10.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl6930982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'General Date'; \ttext-align:general; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7030982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'Short Time'; \ttext-align:general; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:1.0pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7130982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7230982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'General Date'; \ttext-align:general; \tvertical-align:middle; \tborder:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7330982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'Short Time'; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:1.0pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7430982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7530982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'General Date'; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7630982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:'Short Time'; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:1.0pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:.5pt solid windowtext; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7730982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:12.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:'Times New Roman', serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:bottom; \tmso-background-source:auto; \tmso-pattern:auto; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7830982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:5px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:1.0pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:1.0pt solid windowtext; \tbackground:#C5D9F1; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl7930982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:.5pt solid windowtext; \tborder-left:1.0pt solid windowtext; \tbackground:#C5D9F1; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".xl8030982";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:0px; \tmso-ignore:padding; \tcolor:black; \tfont-size:11.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:1.0pt solid windowtext; \tbackground:#C5D9F1; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + ".padded";
			HTML_Estucture = HTML_Estucture
					+ "\t{padding:5px; \tcolor:black; \tfont-size:10.0pt; \tfont-weight:400; \tfont-style:normal; \ttext-decoration:none; \tfont-family:Calibri, sans-serif; \tmso-font-charset:0; \tmso-number-format:General; \ttext-align:general; \tvertical-align:middle; \tborder-top:.5pt solid windowtext; \tborder-right:.5pt solid windowtext; \tborder-bottom:1.0pt solid windowtext; \tborder-left:1.0pt solid windowtext; \tbackground:#C5D9F1; \tmso-pattern:black none; \twhite-space:nowrap;}";
			HTML_Estucture = HTML_Estucture + "-->";
			HTML_Estucture = HTML_Estucture + "</style>";
			HTML_Estucture = HTML_Estucture + "</head>";
			HTML_Estucture = HTML_Estucture + "<body>";
			HTML_Estucture = HTML_Estucture + "<div id='Mail_body' align=left>";
			HTML_Estucture = HTML_Estucture
					+ "<table border=0 cellpadding=0 cellspacing=0 width=494 style='border-collapse: collapse;table-layout:auto;width:371pt'>";
			HTML_Estucture = HTML_Estucture + " <tr height=21 style='height:15.75pt'>";
			HTML_Estucture = HTML_Estucture
					+ "  <td height=21 class=xl7730982 colspan=9 width=494 style='height:15.75pt;width:371pt'>" + encabezado
					+ "</td>";
			HTML_Estucture = HTML_Estucture + " </tr>";
			HTML_Estucture = HTML_Estucture
					+ "<table border=0 cellpadding=0 cellspacing=0 style='border-collapse: collapse;table-layout:auto;width:150pt'>";
			HTML_Estucture = HTML_Estucture + " <tr height=27 style='mso-height-source:userset;height:20.25pt'>";
			HTML_Estucture = HTML_Estucture + "  <td height=27 width=90 class=xl6630982 style='height:20.25pt'>REGION</td>";
			HTML_Estucture = HTML_Estucture + "  <td height=27 width=90 class=xl6630982 style='height:20.25pt'>ZONA</td>";
			HTML_Estucture = HTML_Estucture + "  <td height=27 width=90 class=xl6630982 style='height:20.25pt'>BT</td>";
			HTML_Estucture = HTML_Estucture + "  <td height=27 width=90 class=xl6630982 style='height:20.25pt'>MT</td>";
			HTML_Estucture = HTML_Estucture + "  <td class=xl6630982 style='border-left:none'>TOTAL</td>";
			HTML_Estucture = HTML_Estucture + " </tr>";
			this.it = tabla2.iterator();

			while (true) {
                            if (!this.it.hasNext()) {
                                    HTML_Estucture = HTML_Estucture + "</table>";
                                    HTML_Estucture = HTML_Estucture + Constantes.FUENTE_HTML_FIN;
                                    System.out.println("enviado: " + HTML_Estucture);
                                    hst_Mail.put("CUERPO", HTML_Estucture);
                                    if (enviarMail) {
                                            this.mailSender(hst_Mail);
                                    }
                                    break;
                            }

                            HashMap<String, String> fila = (HashMap<String, String>) this.it.next();

                            if (fila.get("REGION").equals("TOTAL")) {
                                    HTML_Estucture = HTML_Estucture
                                                    + " <td colspan='2' height=27 width=90 class=xl7830982 align=center  style='height:40.50pt; font-weight: bold'>"
                                                    + fila.get("REGION") + "</td>";
                            } else {
                                    HTML_Estucture = HTML_Estucture
                                                    + " <td height=27 width=90 class=xl7830982 align=center  style='height:20.25pt'>" + fila.get("REGION")
                                                    + "</td>";
                                    HTML_Estucture = HTML_Estucture
                                                    + " <td height=27 width=90 class=xl7830982 align=center  style='height:20.25pt'>" + fila.get("ZONA")
                                                    + "</td>";
                            }

                            HTML_Estucture = HTML_Estucture
                                            + " <td height=27 width=90 class=xl6830982 align=center style='border-left:none'>" + fila.get("BT")
                                            + "</td>";
                            HTML_Estucture = HTML_Estucture
                                            + " <td height=27 width=90 class=xl6830982 align=center style='border-left:none'>" + fila.get("MT")
                                            + "</td>";
                            HTML_Estucture = HTML_Estucture
                                            + " <td height=27 width=90 class=xl6830982 align=center style='border-left:none'>" + fila.get("TOTAL")
                                            + "</td>";
                            HTML_Estucture = HTML_Estucture + " </tr>";
			}
		} catch (Exception var16) {
			System.out.println("Error en generar()" + var16);
			this.msgCancela = var16.toString();
			hst_Mail.put("CUERPO", this.msgCancela);
			this.mailSender(hst_Mail);
			throw var16;
		} finally {
			try {
				System.out.println("final ");
			} catch (Exception var15) {
			}

		}

	}

	private void mailSender(Hashtable<String, String> hst_values_mail) throws Exception {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", hst_values_mail.get("mailHost"));
			properties.put("mail.from", hst_values_mail.get("DE"));
			properties.put("mail.debug", "true");
			Session session = Session.getInstance(properties, null);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress((String) hst_values_mail.get("DE")));
			msg.setFrom(InternetAddress.getLocalAddress(session));
			msg.setSubject((String) hst_values_mail.get("ASUNTO"));
			msg.setSentDate(new Date());
			InternetAddress[] paraArray = InternetAddress.parse((String) hst_values_mail.get("PARA"));
			msg.setRecipients(RecipientType.TO, paraArray);
			InternetAddress[] ccArray = null;
			if (hst_values_mail.get("CC") != null) {
				ccArray = InternetAddress.parse((String) hst_values_mail.get("CC"));
				msg.setRecipients(RecipientType.CC, ccArray);
			}

			InternetAddress[] bccArray = null;
			if (hst_values_mail.get("CCO") != null) {
				bccArray = InternetAddress.parse((String) hst_values_mail.get("CCO"));
				msg.setRecipients(RecipientType.BCC, bccArray);
			}

			MimeMultipart multiParte = new MimeMultipart();
			BodyPart logo = new MimeBodyPart();
			logo.setDataHandler(new DataHandler(new FileDataSource("./logo.png")));
			logo.setFileName("logo.png");
			multiParte.addBodyPart(logo);
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(ruta_excel)));
			adjunto.setFileName("ConsolidadoBTMT.xlsx");
			multiParte.addBodyPart(adjunto);
			BodyPart texto = new MimeBodyPart();
			texto.setDataHandler(new DataHandler(new ConsolidadoBTMT_HTMLDataSource((String) hst_values_mail.get("CUERPO"))));
			multiParte.addBodyPart(texto);
			msg.setContent(multiParte);
			int total = paraArray.length;
			if (ccArray != null) {
				total += ccArray.length;
			}

			if (bccArray != null) {
				total += bccArray.length;
			}

			InternetAddress[] address = new InternetAddress[total];

			int i;
			for (i = 0; i < paraArray.length; ++i) {
				address[i] = paraArray[i];
			}

			if (ccArray != null) {
				for (int j = 0; j < ccArray.length; ++j) {
					address[i] = ccArray[j];
					++i;
				}
			}

			if (bccArray != null) {
				for (int k = 0; k < bccArray.length; ++k) {
					address[i] = bccArray[k];
					++i;
				}
			}

			Transport transporte = session.getTransport(address[0]);
			transporte.connect();
			transporte.sendMessage(msg, address);
		} catch (SendFailedException var18) {
			Address[] listaInval = var18.getInvalidAddresses();
			Address[] var4 = listaInval;
			int var5 = listaInval.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				Address listaInval1 = var4[var6];
				this.dir_no_encontradas.add(listaInval1.toString());
				System.out.println("No encontrada: " + listaInval1.toString());
			}
		} catch (MessagingException var19) {
			System.out.println("Exception (mailSender) : " + var19);
			throw var19;
		}

	}




}
