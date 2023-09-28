package com.edenor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conectar {

	private Connection con = null;
	private String driverClass; //"oracle.jdbc.driver.OracleDriver"
	private String cnn;
	private String usu;
	private String pss;

	public Conectar(String driverClass, String cnn, String usu, String pss){
		this.driverClass= driverClass;
		this.cnn=cnn;
		this.usu=usu;
		this.pss=pss;
	}

	public void Oracle_connect() throws Exception {
		try {
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

	public ResultSet get_clients() throws Exception {
		ResultSet var5;
		
		try {
			//String plsql1 = "SELECT * from NEXUS_GIS.CANT_CLIENTES";

			String plsql1="SELECT * FROM (\r\n" + //
					"SELECT \r\n" + //
					"    TRIM(P.AREANAME) PART_BARRIO,\r\n" + //
					"    COUNT(1) CANTIDAD\r\n" + //
					"FROM \r\n" + //
					"    NEXUS_GIS.SPRCLIENTS C,\r\n" + //
					"    NEXUS_GIS.SMSTREETS S, \r\n" + //
					"    NEXUS_GIS.AMAREAS P\r\n" + //
					"WHERE     \r\n" + //
					"    C.LOGIDTO = 0\r\n" + //
					"    AND C.LEVELTWOAREAID=P.AREAID\r\n" + //
					"    AND C.STREETID = S.STREETID\r\n" + //
					"    AND (S.DATETO > SYSDATE OR S.DATETO IS NULL)\r\n" + //
					"    AND P.AREAID <> 61\r\n" + //
					"GROUP BY\r\n" + //
					"    P.AREANAME\r\n" + //
					"UNION\r\n" + //
					"SELECT \r\n" + //
					"    TRIM(L.AREANAME) PART_BARRIO,\r\n" + //
					"    COUNT(1) CANTIDAD\r\n" + //
					"FROM \r\n" + //
					"    NEXUS_GIS.SPRCLIENTS C,\r\n" + //
					"    NEXUS_GIS.SMSTREETS S, \r\n" + //
					"    NEXUS_GIS.AMAREAS P,\r\n" + //
					"    NEXUS_GIS.AMAREAS L\r\n" + //
					"WHERE     \r\n" + //
					"    C.LOGIDTO = 0\r\n" + //
					"    AND C.LEVELTWOAREAID=P.AREAID\r\n" + //
					"    AND C.LEVELONEAREAID=L.AREAID\r\n" + //
					"    AND C.STREETID = S.STREETID\r\n" + //
					"    AND (S.DATETO > SYSDATE OR S.DATETO IS NULL)\r\n" + //
					"    AND P.AREAID = 61\r\n" + //
					"GROUP BY\r\n" + //
					"    L.AREANAME\r\n" + //
					") CANT_CLIENTES \r\n" + //
					"ORDER BY PART_BARRIO\r\n" + //
					"";

			System.out.println("Cantidad de clientes es " + plsql1);
			Statement stmt = this.con.createStatement(1004, 1007);
			ResultSet rs1 = stmt.executeQuery(plsql1);
			var5 = rs1;
		} catch (Exception var14) {
			this.con.close();
			System.out.println("Error en raster_bt()" + var14);
			throw var14;
		} finally {
			try {
				System.out.println("Final One");
			} catch (Exception var13) {
			}

		}

		return var5;
	}

	public ResultSet raster_bt() throws Exception {
		ResultSet var5;

		try {
				String plsql1 = "SELECT SEC.PARTIDO, trim(count(*)) cortes, trim(sum(TE.CANTIDAD_EST_USU_AFECTADOS)) Clientes_afect  \n  FROM nexus_gis.tabla_enre te, nexus_gis.amareas am, nexus_gis.amareas amz, NEXUS_GIS.OMS_DOCUMENT doc, nexus_gis.llam_sectores sec,NEXUS_GIS.OMS_ADDRESS a       \n WHERE AM.AREANAME = te.partido \n   AND AM.AREATYPEID = 9 \n   AND am.superarea = amz.areaid \n   AND amz.areatypeid = 8 \n   AND TE.ESTADO not in ('Cerrado','Cancelado')\n   and DOC.NAME = TE.NRO_DOCUMENTO \n   and A.ID = DOC.ADDRESS_ID\n   and A.LARGE_AREA_ID = SEC.area_id\n   and A.MEDIUM_AREA_ID = SEC.part_ID\n   and A.SMALL_AREA_ID = SEC.LOCA_ID   \n   and SEC.PARTIDO != 'CAPITAL FEDERAL'\n GROUP BY  SEC.PARTIDO\n UNION  \nSELECT SEC.LOCALIDAD PARTIDO, trim(count(*)) cortes, trim(sum(TE.CANTIDAD_EST_USU_AFECTADOS)) Clientes_afect  \n  FROM nexus_gis.tabla_enre te, nexus_gis.amareas am, nexus_gis.amareas amz, NEXUS_GIS.OMS_DOCUMENT doc, nexus_gis.llam_sectores sec,NEXUS_GIS.OMS_ADDRESS a       \n WHERE AM.AREANAME = te.partido \n   AND AM.AREATYPEID = 9 \n   AND am.superarea = amz.areaid \n   AND amz.areatypeid = 8 \n   AND TE.ESTADO not in ('Cerrado','Cancelado')\n   and DOC.NAME = TE.NRO_DOCUMENTO \n   and A.ID = DOC.ADDRESS_ID\n   and A.LARGE_AREA_ID = SEC.area_id\n   and A.MEDIUM_AREA_ID = SEC.part_ID\n   and A.SMALL_AREA_ID = SEC.LOCA_ID\n   and SEC.PARTIDO = 'CAPITAL FEDERAL'   \n GROUP BY SEC.LOCALIDAD \n order by 1 asc";
				System.out.println("Raster bt es: " + plsql1);
				Statement stmt = this.con.createStatement(1004, 1007);
				ResultSet rs1 = stmt.executeQuery(plsql1);
				var5 = rs1;
		} catch (Exception var14) {
				this.con.close();
				System.out.println("Error en raster_bt()" + var14);
				throw var14;
		} finally {
				try {
						System.out.println("Final One");
				} catch (Exception var13) {
			}

		}

		return var5;
	}

	public ResultSet raster_mtat_loc() throws Exception {
		ResultSet var5;

		try {
			this.Oracle_connect();
			String plsql1 = "SELECT zona, COUNT (zona) AS CANT_CORTES, SUM (cli_actual) AS CLI_AFECT\r\n" + 
					"    FROM ( (SELECT DISTINCT (tdet.NRO_DOCUMENTO),\r\n" + 
					"                            --        SEC.REGION,\r\n" + 
					"                            sec.PARTIDO zona,\r\n" + 
					"                            (SELECT SUM (tdet1.cant_afectaciones)\r\n" + 
					"                               FROM NEXUS_GIS.TABLA_ENREMTAT_DET tdet1\r\n" + 
					"                              WHERE tdet1.NRO_DOCUMENTO = tdet.NRO_DOCUMENTO)\r\n" + 
					"                               cli_actual\r\n" + 
					"              FROM NEXUS_GIS.TABLA_ENREMTAT_DET tdet,\r\n" + 
					"                   nexus_gis.oms_document     doc,\r\n" + 
					"                   nexus_gis.llam_sectores    sec,\r\n" + 
					"                   NEXUS_GIS.OMS_ADDRESS      a,\r\n" + 
					"                   NEXUS_GIS.AMAREAS          am\r\n" + 
					"             WHERE     tdet.estado NOT IN ('Cerrado', 'Cancelado')\r\n" + 
					"                   AND tdet.fecha_documento IS NOT NULL\r\n" + 
					"                   AND tdet.fecha_documento = (SELECT MIN (fecha_documento)\r\n" + 
					"                                                 FROM NEXUS_GIS.TABLA_ENREMTAT_DET\r\n" + 
					"                                                WHERE nro_documento = tdet.NRO_DOCUMENTO)\r\n" + 
					"                   AND tdet.zona IS NOT NULL\r\n" + 
					"                   AND tdet.nro_documento = DOC.NAME\r\n" + 
					"                   AND DOC.OPERATIVE_AREA_ID = AM.AREAID\r\n" + 
					"                   AND A.ID = DOC.ADDRESS_ID\r\n" + 
					"                   AND SEC.PARTIDO != 'CAPITAL FEDERAL'\r\n" + 
					"                   AND A.LARGE_AREA_ID = SEC.area_id\r\n" + 
					"                   AND A.MEDIUM_AREA_ID = SEC.part_ID\r\n" + 
					"                   AND A.SMALL_AREA_ID = SEC.LOCA_ID)\r\n" + 
					"          UNION\r\n" + 
					"          (SELECT DISTINCT (tdet.NRO_DOCUMENTO),\r\n" + 
					"                           --        SEC.REGION,\r\n" + 
					"                           sec.LOCALIDAD zona,\r\n" + 
					"                           (SELECT SUM (tdet1.cant_afectaciones)\r\n" + 
					"                              FROM NEXUS_GIS.TABLA_ENREMTAT_DET tdet1\r\n" + 
					"                             WHERE tdet1.NRO_DOCUMENTO = tdet.NRO_DOCUMENTO)\r\n" + 
					"                              cli_actual\r\n" + 
					"             FROM NEXUS_GIS.TABLA_ENREMTAT_DET tdet,\r\n" + 
					"                  nexus_gis.oms_document     doc,\r\n" + 
					"                  nexus_gis.llam_sectores    sec,\r\n" + 
					"                  NEXUS_GIS.OMS_ADDRESS      a,\r\n" + 
					"                  NEXUS_GIS.AMAREAS          am\r\n" + 
					"            WHERE     tdet.estado NOT IN ('Cerrado', 'Cancelado')\r\n" + 
					"                  AND tdet.fecha_documento IS NOT NULL\r\n" + 
					"                  AND tdet.fecha_documento = (SELECT MIN (fecha_documento)\r\n" + 
					"                                                FROM NEXUS_GIS.TABLA_ENREMTAT_DET\r\n" + 
					"                                               WHERE nro_documento = tdet.NRO_DOCUMENTO)\r\n" + 
					"                  AND tdet.zona IS NOT NULL\r\n" + 
					"                  AND tdet.nro_documento = DOC.NAME\r\n" + 
					"                  AND DOC.OPERATIVE_AREA_ID = AM.AREAID\r\n" + 
					"                  AND A.ID = DOC.ADDRESS_ID\r\n" + 
					"                  AND SEC.PARTIDO = 'CAPITAL FEDERAL'\r\n" + 
					"                  AND A.LARGE_AREA_ID = SEC.area_id\r\n" + 
					"                  AND A.MEDIUM_AREA_ID = SEC.part_ID\r\n" + 
					"                  AND A.SMALL_AREA_ID = SEC.LOCA_ID))\r\n" + 
					"GROUP BY zona";
			System.out.println("Raster MT AT LOC es " + plsql1);
			Statement stmt = this.con.createStatement(1004, 1007);
			ResultSet rs1 = stmt.executeQuery(plsql1);
			var5 = rs1;
		} catch (Exception var14) {
			this.con.close();
			System.out.println("Error en raster_bt()" + var14);
			throw var14;
		} finally {
			try {
				System.out.println("Final One");
			} catch (Exception var13) {
			}

		}

		return var5;
	}

	public ResultSet Fechainforme() throws Exception {
		ResultSet rs = null;

		try {
			String query_variable = "select to_char (sysdate, 'dd/mm/yyyy HH24:MI:SS') as FECHA from dual";
			Statement sentencia = con.createStatement();
			rs = sentencia.executeQuery(query_variable);
			return rs;
		} catch (SQLException var5) {
			throw var5;
		}
	}

	public ResultSet raster_tablahtml() throws Exception {
		ResultSet var5;
		
		try {
			this.Oracle_connect();
			String plsql1 = "select REGION, zona,sum(CLIENTES_BT) CLIENTES_BT,sum (CLIENTES_MTA) CLIENTES_MTA from (\n(SELECT sec.REGION as REGION, sec.SECTOR zona , sum(TE.CANTIDAD_EST_USU_AFECTADOS) CLIENTES_BT, 0 CLIENTES_MTA\n  FROM nexus_gis.tabla_enre te, nexus_gis.amareas am, nexus_gis.amareas amz, NEXUS_GIS.OMS_DOCUMENT doc, nexus_gis.llam_sectores sec,NEXUS_GIS.OMS_ADDRESS a       \n WHERE AM.AREANAME = te.partido \n   AND AM.AREATYPEID = 9 \n   AND am.superarea = amz.areaid \n   AND amz.areatypeid = 8 \n   AND TE.ESTADO not in ('Cerrado','Cancelado')\n   and DOC.NAME = TE.NRO_DOCUMENTO \n   and A.ID = DOC.ADDRESS_ID\n   and A.LARGE_AREA_ID = SEC.area_id\n   and A.MEDIUM_AREA_ID = SEC.part_ID\n   and A.SMALL_AREA_ID = SEC.LOCA_ID   \n GROUP BY sec.REGION, sec.SECTOR)\nunion\n(select REGION, zona, 0 CLIENTES_BT,sum (cli_actual) as CLIENTES_MTA from ( \n select distinct(tdet.NRO_DOCUMENTO),\n        SEC.REGION,SEC.SECTOR zona,\n        (select sum(tdet1.cant_afectaciones) from NEXUS_GIS.TABLA_ENREMTAT_DET tdet1 where tdet1.NRO_DOCUMENTO=tdet.NRO_DOCUMENTO ) cli_actual,\n        tdet.fecha_documento\n   from NEXUS_GIS.TABLA_ENREMTAT_DET tdet,\n        nexus_gis.oms_document doc,\n        nexus_gis.llam_sectores sec,\n        NEXUS_GIS.OMS_ADDRESS a,\n        NEXUS_GIS.AMAREAS am\n  where tdet.estado not in  ('Cerrado','Cancelado')\n    and tdet.fecha_documento is not null\n    and tdet.fecha_documento = (select min(fecha_documento) from NEXUS_GIS.TABLA_ENREMTAT_DET where nro_documento=tdet.NRO_DOCUMENTO)\n    and tdet.zona is not null\n    and tdet.nro_documento= DOC.NAME\n    and DOC.OPERATIVE_AREA_ID = AM.AREAID\n    and A.ID = DOC.ADDRESS_ID\n    and A.LARGE_AREA_ID = SEC.area_id\n    and A.MEDIUM_AREA_ID = SEC.part_ID\n    and A.SMALL_AREA_ID = SEC.LOCA_ID) group by REGION, zona)\n) group by region, zona\norder by 1,2 asc";
			System.out.println("Raster raster_tablahtml es " + plsql1);
			Statement stmt = this.con.createStatement(1004, 1007);
//         Statement stmt = this.con_1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs1 = stmt.executeQuery(plsql1);
			var5 = rs1;
		} catch (Exception var14) {
			this.con.close();
			System.out.println("Error en raster_bt()" + var14);
			throw var14;
		} finally {
			try {
				System.out.println("Final One");
			} catch (Exception var13) {
			}

		}

		return var5;
	}


}
