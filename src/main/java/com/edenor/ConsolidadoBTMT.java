package com.edenor;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.io.FileInputStream;

final class ConsolidadoBTMT {
	List<HashMap<String, String>> tabla2 = null;
	
	private static Properties properties;
	private static String excel;

	public static void main(String[] args) {
		try {
			findPropiedades();
			if (excel!=null) {
				new ConsolidadoBTMT();
				System.out.println("\nProcedimiento MAIL terminado exitosamente");
			} else {
				System.out.println("\nError en PROPIERTIES");
			}
			System.exit(0);
		} catch (Exception var2) {
			System.out.println(var2);
			System.exit(1);
		}
	}
        
	public static void findPropiedades(){
		properties= new Properties();
		try (FileInputStream input = new FileInputStream("./config.properties")) {
			properties.load(input);
			excel=properties.getProperty("excel");                          //"/ias/ConsolidadoBTMT/ConsolidadoBTMT.xlsx"
		} catch (IOException e) {
			e.printStackTrace();
			excel=null;
		}
		
		System.out.println(String.format(properties.getProperty("mail_subject"),5,7));
	}   		

	public ConsolidadoBTMT() throws Exception {
		String tablahtml = "";
		String Hora = null;
		String Dia = null;

		try {
			Conectar cnn= new Conectar(
				properties.getProperty("driverClass"), 
				properties.getProperty("oracle_cnn_pro"),
				properties.getProperty("oracle_cnn_usu"),
				properties.getProperty("oracle_cnn_pss"));
			cnn.Oracle_connect();

			for (ResultSet rs = cnn.Fechainforme(); rs.next(); Dia = rs.getString("FECHA")) {
			}

			Hora = Dia.substring(11, 13);
			Dia = Dia.substring(0, 5);
			int i = 0;
			int totbt = 0;
			int totmt = 0;

			ResultSet rs2;
			rs2 = cnn.raster_tablahtml();

			// Armo lista base
			Map<Integer, String> listaPartidos = new HashMap<>();
			listaPartidos.put(0, "R1;NORTE;0;0;0,");
			listaPartidos.put(1, "R1;OLIVOS;0;0;0,");
			listaPartidos.put(2, "R1;SAN MARTIN;0;0;0,");
			listaPartidos.put(3, "R2;MATANZA;0;0;0,");
			listaPartidos.put(4, "R2;MERLO;0;0;0,");
			listaPartidos.put(5, "R2;MORON;0;0;0,");
			listaPartidos.put(6, "R3;MORENO;0;0;0,");
			listaPartidos.put(7, "R3;PILAR;0;0;0,");
			listaPartidos.put(8, "R3;SAN MIGUEL;0;0;0,");
			listaPartidos.put(9, "R3;TIGRE;0;0;0,");

			// Armo lista y totales con lo que me llega de la DB
			Map<String, String> registros = new HashMap<>();
			while (rs2.next()) {
				registros.put(rs2.getString(2).trim(), rs2.getString(1) + ";" + rs2.getString(2).trim() + ";" + rs2.getString(3) + ";"
						+ rs2.getString(4) + ";" + (rs2.getInt(3) + rs2.getInt(4)) + ",");
				totbt += rs2.getInt(3);
				totmt += rs2.getInt(4);
			}

			// Recorro y si existe registro en la DB, uso esos datos
			for (i = 0; i < 10; i++) {
				String res = listaPartidos.get(i);
				String partido = res.split(";")[1];

				if (registros.containsKey(partido)) {
					res = registros.get(partido);
				}

				tablahtml += res;

			}

			tablahtml = tablahtml + "TOTAL; ;" + totbt + ";" + totmt + ";" + (totbt + totmt) + ",";
			this.tabla2 = this.strToHash2(tablahtml);
			msexcel e= new msexcel(excel,Hora);
			e.setConexion(cnn);
			e.creaExcel();

			sendmail m= new sendmail(
				properties.getProperty("mail_host"), 
				properties.getProperty("mail_from"), 
				properties.getProperty("mail_to"), 
				properties.getProperty("mail_subject"),
				Hora,
				Dia, 
				excel);
			m.generar(this.tabla2, null, true);
			return;
		} catch (Exception var8) {
			System.out.println("Error en Mail_Sender() = " + var8);
			var8.printStackTrace();
			throw var8;
		}
	}


	private List<HashMap<String, String>> strToHash2(String reporte) {
		List<HashMap<String, String>> tabla = new ArrayList<>();
		String[] lineas = reporte.split(",");
		String[] var5 = lineas;
		int var6 = lineas.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			String linea = var5[var7];
			HashMap<String, String> fila = new HashMap<>();
			String[] campo = linea.split(";");
			fila.put("REGION", campo[0]);
			fila.put("ZONA", campo[1]);
			fila.put("BT", campo[2]);
			fila.put("MT", campo[3]);
			fila.put("TOTAL", campo[4]);
			tabla.add(fila);
		}

		return tabla;
	}

}