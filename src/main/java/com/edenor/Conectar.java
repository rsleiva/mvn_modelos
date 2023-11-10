package com.edenor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.constantes.SQL;
import com.dto.AfectacionesDTO;
import com.dto.DatosDTO;
import com.dto.DocumentosDTO;

public class Conectar {

	private Connection con = null;
	private String driverClass; //"oracle.jdbc.driver.OracleDriver"
	private String cnn;
	private String usu;
	private String pss;

	public Conectar(DatosDTO d){
		this.driverClass= d.getDriverClass();
		this.cnn=d.getCadena();
		this.usu=d.getUser();
		this.pss=d.getPass();
	}

	public void Oracle_connect() throws Exception {
		try {
			System.out.println("Generando la conexion a Oracle...");
			Class.forName(driverClass).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.out.println("Error al cargar el driver: oracle.jdbc.driver.OracleDriver -Error: " + e);
			throw e;
		}

		try {
			this.con = DriverManager.getConnection(
					cnn,             //jdbc:oracle:thin:@ltronxgisbdpr03.pro.edenor:1528:GISPR03
					usu,             //SVC_ORA_GIS
					pss);            //jv506uzy
			System.out.println("Conexion establecida!");
		} catch (SQLException var3) {
				switch (var3.getErrorCode()) {
				case 1017:
						System.out.println("Usuario de la base de datos incorrecto");
						break;
				case 2391:
						System.out.println("LÃ�\u00admite de conexiones excedido, intentar mas tarde");
						break;
				case 28000:
						System.out.println("Cuenta bloqueada");
						break;
				case 28001:
						System.out.println("Clave de la base de datos expirada");
						break;
				default:
						System.out.println("Error al conectarse: " + var3);
				}
				throw var3;
		}
	}

	public List<DocumentosDTO> getDocumentosActivos(String sql) throws Exception {
		ResultSet rs;
		List<DocumentosDTO> lista= new ArrayList<>();
		try {
			System.out.println("Obteniendo Nuevos Documentos...");
			Statement sentencia = con.createStatement();
			rs = sentencia.executeQuery(sql);
			
			while (rs.next()) {
				DocumentosDTO  datos= new DocumentosDTO();
				datos.setId(rs.getInt("ID"));
				datos.setName(rs.getString("NAME"));
				datos.setCreation_time(rs.getDate("CREATION_TIME"));
				datos.setEstado(rs.getByte("TYPE_ID"));
				
				lista.add(datos);
			}
			if (lista.isEmpty()) {
				System.out.println("No hay documentos");
			} else {
				System.out.println("Se Obtuvieron documentos!");
			}
			rs.close();
		} catch (Exception e) {
			this.con.close();
			System.out.println("Error al obtener la lista de documentos activos. " + e);
			throw e;
		}

		return lista;
	}

	public List<AfectacionesDTO> getAfectaciones(String sql, int id) throws Exception {
		ResultSet rs;
		List<AfectacionesDTO> lista= new ArrayList<>();
		try {
			Statement sentencia = con.createStatement();
			rs = sentencia.executeQuery(String.format(sql,id));
			System.out.println(""
				+ "\tDOCUMENT_ID"
				+ "\tFECHA_DOCUMENTO"
				+ "\tORIGEN"
				+ "\t\tOBJECTID"
				+ "\tCT (NEXTID)"
				+ "\t\tCANT_AFECTACIONES"
			);
			while (rs.next()) {
				AfectacionesDTO  datos= new AfectacionesDTO();
				datos.setId(rs.getInt("ID"));
				datos.setNro_doc(rs.getString("NRO_DOCUMENTO"));
				datos.setObjectid(rs.getInt("OBJECTID"));
				datos.setOrigen(rs.getString("ORIGEN"));
				datos.setFecha_documento(rs.getDate("CREATION_TIME"));
				datos.setCt(
					rs.getString("CT")==null ? getCT(datos.getObjectid()) : rs.getString("CT")
				);
				datos.setCant_afectaciones(rs.getInt("CANT_AFECTACIONES"));
				datos.setIs_restored(rs.getInt("IS_RESTORE"));
				datos.setIs_affected(rs.getInt("IS_AFFECTED"));
				datos.setInicio(rs.getDate("FECHA_DOCUMENTO"));
				datos.setFin(
					getRestauracion(datos.getId(), datos.getObjectid())
				);
				// datos.setFin(rs.getDate("FECHA_RESTAURACION"));

				System.out.println(
					"\t"+datos.getId()
					+"\t"+datos.getFecha_documento()
					+"\t"+datos.getOrigen()
					+"\t"+datos.getObjectid()
					+"\t"+datos.getCt()
					+"\t"+datos.getCant_afectaciones()
				);
				
				lista.add(datos);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("Error al obtener la lista de documentos activos. " + e);
			throw e;
		} 

		return lista;
	}

	private Date getRestauracion(int id, int objectid) {
		ResultSet rs;
		Date fin=null;
		try {
			Statement sentencia= con.createStatement();
			rs = sentencia.executeQuery(
				String.format(SQL.SQL_RESTAURACION,id,objectid));
			while (rs.next()) {
				fin = rs.getDate("TIME");
			}
			rs.close();
		} catch (Exception e) {
			return null;
		}

		return fin;
	}

	private String getCT(int objectid){
		ResultSet rs;
		String ct="";
		int objectidNext= objectid;

		try {
			/*Busco por linkid 1198 */
			Statement sentencia= con.createStatement();
			rs = sentencia.executeQuery(
				String.format(SQL.SQL_CT_BY_LINKID_1198,objectid));
			while (rs.next()) {
				ct = rs.getString("CT");
			}
			/*Si no encuentra el CT o si es corto, busca por nextid */
			if (ct == null || ct.isEmpty() || ct.length()<7) {
				objectidNext=getNextID(objectid);
				sentencia= con.createStatement();
				rs = sentencia.executeQuery
					(String.format(SQL.SQL_CT_BY_LINKID_1018,objectidNext));
				while (rs.next()) {
					ct = rs.getString("CT");
				}
			}
			rs.close();
		} catch (Exception e) {
			ct="";
		}
		ct=ct.trim().length()<7 ? "CT-NULO" : ct.trim();
		return ct + " ("+objectidNext+")";
	}

	private int getNextID(int objectid){
		ResultSet rs;
		int nextid;
		/*
		 * Busco CT por nextid
		 */
		try {
			Statement sentencia = con.createStatement();
			nextid=objectid;
			do {
				rs = sentencia.executeQuery
					(String.format(SQL.SQL_CT_BY_NEXTID,nextid));
				while (rs.next()) {
					objectid=rs.getInt("OBJECTID");
					nextid= rs.getInt("NEXTID");
				}
			} while (nextid>0 && objectid!=nextid);

			return objectid;
		} catch (Exception e) {
			return objectid;
		}
	}

	public void cierraConexion() throws SQLException{
		this.con.close();
	}
}
