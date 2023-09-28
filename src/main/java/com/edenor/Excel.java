package com.edenor;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import java.awt.Color;

public class Excel {

	private Conectar cnn;
    private String ruta_excel;
    private String Hora = null;

    public Excel(String ruta_excel, String Hora){
        this.ruta_excel=ruta_excel;
		this.Hora=Hora;
    }

	public void setConexion(Conectar cnn){
		this.cnn= cnn;
	}

	public void creaExcel() throws SQLException, Exception{
            Workbook wb = new XSSFWorkbook();
            Sheet shForzados = wb.createSheet("ConsolidadoBTMT");
            pueblaExel(shForzados, wb);
            FileOutputStream fileOut = new FileOutputStream(ruta_excel);
            Throwable var4 = null;

            try {
                wb.write(fileOut);
            } catch (Throwable var13) {
                var4 = var13;
                throw var13;
            } finally {
                if (fileOut != null) {
                    if (var4 != null) {
                        try {
                                fileOut.close();
                        } catch (Throwable var12) {
                                var4.addSuppressed(var12);
                        }
                    } else {
                        fileOut.close();
                    }
                }
            }
	}

	private void pueblaExel(Sheet sh, Workbook wb) throws SQLException, Exception {
		Integer rowCount = 0;
		int i = 0;
		int tot_clientes = 0;
		int tot_cortesbt = 0;
		int tot_afectbt = 0;
		int tot_cortesmt = 0;
		int tot_afectmt = 0;
		int tot_cliafect = 0;
		// int porc_total = false;
		rowCount++;
		Row row = sh.createRow(rowCount);
		CellStyle bordesgruesos = creaEstilosbordesgrueso(wb);
		CellStyle cabecerayellow = creaEstilosCabeyellow(wb);
		CellStyle cabecerared = creaEstilosCabered(wb);
		CellStyle cabeceragreen = creaEstilosCabegreen(wb);
		CellStyle cabecera = creaEstilosCabe(wb);
		// CellStyle datosLeft = creaEstilosDatos(wb, "left");
		// CellStyle datosRight = creaEstilosDatos(wb, "right");
		CellStyle datosCenter = creaEstilosDatos(wb, "center");
		// CellStyle datosred = creaEstilosColorRed(wb);
		CellStyle sinborde = creaEstilosnoborder(wb);
		CellStyle titulo = creaEstilostitulo(wb);
		// CellStyle totalesCenter = creaEstilosTotales(wb, "center", "celeste");
		// CellStyle totalesRight = creaEstilosTotales(wb, "right", "celeste");
		// CellStyle totalesGrisado = creaEstilosTotales(wb, "right", "grisado");
		Cell cell = row.createCell(0);

		for (int z = 0; z < 300; ++z) {
			row = sh.createRow(z);

			for (int j = 0; j < 300; ++j) {
				cell = row.createCell(j);
				cell.setCellStyle(sinborde);
				if (z == 7 && j == 2) {
					cell.setCellValue("Afectaciones y clientes sin servicio e indices de incidencia - " + this.Hora + " hs");
					cell.setCellStyle(titulo);
				}
			}
		}

		rowCount = 10;
		row = sh.getRow(rowCount);
		cell = row.createCell(1);
		cell.setCellValue("Partido/Barrio");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(0, 6000);
		cell = row.createCell(2);
		cell.setCellValue("Total Usuarios x Partido /Barrio");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(1, 4000);
		cell = row.createCell(3);
		cell.setCellValue("Afectaciones BT");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(2, 4500);
		cell = row.createCell(4);
		cell.setCellValue("Clientes afectados BT");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(3, 4500);
		cell = row.createCell(5);
		cell.setCellValue("Afectaciones MT");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(4, 4000);
		cell = row.createCell(6);
		cell.setCellValue("Clientes afectados MT");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(5, 5000);
		cell = row.createCell(7);
		cell.setCellValue("Consolidado Clientes Afectados");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(6, 5000);
		cell = row.createCell(8);
		cell.setCellValue("%Clientes afectados sobre total partido/barrio");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(7, 5000);

		ResultSet r = cnn.raster_bt();
		ResultSet rs1 = cnn.get_clients();
		String[] arraybarrio = new String[40];
		int[] cant_clientes = new int[40];
		int[] afectbt = new int[40];
		int[] cli_afectbt = new int[40];
		int[] cli_afectmt = new int[40];
		int[] afectmt = new int[40];
	
		for (ResultSet r1 = cnn.raster_mtat_loc(); rs1.next(); ++i) {
                    arraybarrio[i] = rs1.getString(1);
                    cant_clientes[i] = rs1.getInt(2);
                    r.beforeFirst();

                    String barrio1;
                    while (r.next()) {
                        barrio1 = r.getString(1).replace(" ", "");
                        if (barrio1.equals(arraybarrio[i].replace(" ", ""))) {
                            afectbt[i] = r.getInt(2);
                            cli_afectbt[i] = r.getInt(3);
                        }
                    }

                    r1.beforeFirst();

                    while (r1.next()) {
                            barrio1 = r1.getString(1).replace(" ", "");
                            if (barrio1.equals(arraybarrio[i].replace(" ", ""))) {
                                    afectmt[i] = r1.getInt(2);
                                    cli_afectmt[i] = r1.getInt(3);
                            }
                    }
		}

		int suma;
		for (suma = 0; suma < 40; ++suma) {
			System.out.println(arraybarrio[suma] + "||" + cant_clientes[suma] + "||" + afectbt[suma] + "||"
					+ cli_afectbt[suma] + "||" + afectmt[suma] + "||" + cli_afectmt[suma]);
		}

		rowCount = rowCount + 1;

		for (suma = 0; suma < 35; ++suma) {
			System.out.println(suma);
			System.out.println(rowCount);
			row = sh.getRow(rowCount);
			row.createCell(1).setCellValue(arraybarrio[suma]);
			row.getCell(1).setCellStyle(bordesgruesos);
			row.createCell(2).setCellValue((double) cant_clientes[suma]);
			tot_clientes += cant_clientes[suma];
			row.getCell(2).setCellStyle(datosCenter);
			row.createCell(3).setCellValue((double) afectbt[suma]);
			tot_cortesbt += afectbt[suma];
			row.getCell(3).setCellStyle(datosCenter);
			row.createCell(4).setCellValue((double) cli_afectbt[suma]);
			tot_afectbt += cli_afectbt[suma];
			row.getCell(4).setCellStyle(datosCenter);
			row.createCell(5).setCellValue((double) afectmt[suma]);
			tot_cortesmt += afectmt[suma];
			row.getCell(5).setCellStyle(datosCenter);
			row.createCell(6).setCellValue((double) cli_afectmt[suma]);
			tot_afectmt += cli_afectmt[suma];
			row.getCell(6).setCellStyle(datosCenter);
			row.createCell(7).setCellValue((double) (cli_afectbt[suma] + cli_afectmt[suma]));
			tot_cliafect = tot_cliafect + cli_afectbt[suma] + cli_afectmt[suma];
			row.getCell(7).setCellStyle(datosCenter);
			int suma2 = (cli_afectbt[suma] + cli_afectmt[suma]) * 100;
			float porcentaje1 = (float) suma2 / (float) cant_clientes[suma];
			row.createCell(8).setCellValue(String.format("%.2f", porcentaje1) + "%");
			this.setbgcolor(sh, wb, row, 8, porcentaje1, cant_clientes[suma]);
			rowCount = rowCount + 1;
		}

		row = sh.getRow(rowCount);
		cell = row.createCell(1);
		cell.setCellValue("TOTALES");
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(0, 2000);//Ancho de columna A
		cell = row.createCell(2);
		cell.setCellValue((double) tot_clientes);
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(1, 6000);//Ancho de columna B
		cell = row.createCell(3);
		cell.setCellValue((double) tot_cortesbt);
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(2, 4500);
		cell = row.createCell(4);
		cell.setCellValue((double) tot_afectbt);
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(3, 4500);
		cell = row.createCell(5);
		cell.setCellValue((double) tot_cortesmt);
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(4, 4000);
		cell = row.createCell(6);
		cell.setCellValue((double) tot_afectmt);
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(5, 5000);
		cell = row.createCell(7);
		cell.setCellValue((double) (tot_afectmt + tot_afectbt));
		cell.setCellStyle(cabecera);
		sh.setColumnWidth(6, 5000);
		cell = row.createCell(8);
		cell.setCellValue((double) ((tot_afectmt + tot_afectbt) * 100 / tot_clientes));
		suma = (tot_afectmt + tot_afectbt) * 100;
		float porcentaje1 = (float) suma / (float) tot_clientes;
		cell.setCellValue(String.format("%.2f", porcentaje1) + "%");
		if ((double) porcentaje1 < 1.5D) {
			cell.setCellStyle(cabeceragreen);
		} else if (porcentaje1 > 4.0F) {
			cell.setCellStyle(cabecerared);
		} else {
			cell.setCellStyle(cabecerayellow);
		}

		sh.setColumnWidth(7, 5000);
	}

	private CellStyle creaEstilosbordesgrueso(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(IndexedColors.BLACK, null);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosCabeyellow(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		Font font = wb.createFont();
		font.setColor((short) 3);
		style.setFont(font);


		XSSFColor myColor = new XSSFColor(Color.decode("#C5D9F1"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosCabered(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		Font font = wb.createFont();
		font.setColor((short) 1);
		style.setFont(font);
		XSSFColor myColor = new XSSFColor(Color.decode("#C5D9F1"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosCabegreen(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		Font font = wb.createFont();
		font.setColor((short) 1);
		style.setFont(font);
		XSSFColor myColor = new XSSFColor(Color.decode("#C5D9F1"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosCabe(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		XSSFColor myColor = new XSSFColor(Color.decode("#C5D9F1"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosDatos(Workbook wb, String alignment) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		byte var4 = -1;
		switch (alignment.hashCode()) {
		case -1364013995:
			if (alignment.equals("center")) {
				var4 = 1;
			}
			break;
		case 3317767:
			if (alignment.equals("left")) {
				var4 = 2;
			}
			break;
		case 108511772:
			if (alignment.equals("right")) {
				var4 = 0;
			}
		}

		switch (var4) {
		case 0:
			style.setAlignment(HorizontalAlignment.RIGHT);
			break;
		case 1:
			style.setAlignment(HorizontalAlignment.CENTER);
			break;
		case 2:
			style.setAlignment(HorizontalAlignment.LEFT);
		}

		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosColorRed(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		XSSFColor myColor = new XSSFColor(Color.decode("#FF0000"),null);
		style.setFillForegroundColor(myColor.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosnoborder(Workbook wb) {
		XSSFCellStyle noborder = (XSSFCellStyle) wb.createCellStyle();
		XSSFColor blanco = new XSSFColor(Color.decode("#FFFFFF"),null);
		noborder.setBorderLeft(BorderStyle.NONE);
		noborder.setBorderRight(BorderStyle.NONE);
		noborder.setBorderBottom(BorderStyle.NONE);
		noborder.setBorderTop(BorderStyle.NONE);
		noborder.setBorderColor(BorderSide.LEFT, blanco);
		noborder.setBorderColor(BorderSide.TOP, blanco);
		noborder.setBorderColor(BorderSide.RIGHT, blanco);
		noborder.setBorderColor(BorderSide.BOTTOM, blanco);
		noborder.setBorderRight(BorderStyle.THIN);
		noborder.setRightBorderColor(IndexedColors.WHITE.getIndex());
		noborder.setBorderBottom(BorderStyle.THIN);
		noborder.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		noborder.setBorderLeft(BorderStyle.THIN);
		noborder.setLeftBorderColor(IndexedColors.WHITE.getIndex());
		noborder.setBorderTop(BorderStyle.THIN);
		noborder.setTopBorderColor(IndexedColors.WHITE.getIndex());
		noborder.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		//noborder.setFillForegroundColor((short) 9); //9
		noborder.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return noborder;
	}

	private CellStyle creaEstilostitulo(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFColor blanco = new XSSFColor(Color.decode("#FFFFFF"),null);
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		style.setFont(negrita);
		negrita.setItalic(true);
		negrita.setUnderline(FontUnderline.SINGLE);
		style.setBorderLeft(BorderStyle.NONE);
		style.setBorderRight(BorderStyle.NONE);
		style.setBorderBottom(BorderStyle.NONE);
		style.setBorderTop(BorderStyle.NONE);
		style.setBorderColor(BorderSide.LEFT, blanco);
		style.setBorderColor(BorderSide.TOP, blanco);
		style.setBorderColor(BorderSide.RIGHT, blanco);
		style.setBorderColor(BorderSide.BOTTOM, blanco);
		return style;
	}

/*
	private CellStyle creaEstilosTotales(Workbook wb, String alignment, String color) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		style.setFont(negrita);
		byte var7 = -1;
		switch (color.hashCode()) {
		case 287813943:
			if (color.equals("grisado")) {
				var7 = 0;
			}
			break;
		case 662958825:
			if (color.equals("celeste")) {
				var7 = 1;
			}
		}

		String codigo;
		switch (var7) {
		case 0:
			codigo = "#B8BDBF";
			break;
		case 1:
			codigo = "#C5D9F1";
			break;
		default:
			codigo = "#C5D9F1";
		}

		XSSFColor myColor = new XSSFColor(Color.decode(codigo),null);
		style.setFillForegroundColor(myColor);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		byte var8 = -1;
		switch (alignment.hashCode()) {
		case -1364013995:
			if (alignment.equals("center")) {
				var8 = 1;
			}
			break;
		case 3317767:
			if (alignment.equals("left")) {
				var8 = 2;
			}
			break;
		case 108511772:
			if (alignment.equals("right")) {
				var8 = 0;
			}
		}

		switch (var8) {
		case 0:
			style.setAlignment(HorizontalAlignment.RIGHT);
			break;
		case 1:
			style.setAlignment(HorizontalAlignment.CENTER);
			break;
		case 2:
			style.setAlignment(HorizontalAlignment.LEFT);
		}

		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}
*/

	private void setbgcolor(Sheet sh, Workbook wb, Row row, int numcel, float porcentaje1, int cant_total_usuarios) {
		// CellStyle cabecera = creaEstilosCabe(wb);
		CellStyle datosred = creaEstilosColorRed(wb);
		CellStyle datosgreen = creaEstilosColorGreen(wb);
		CellStyle datosyellow = creaEstilosColorYellow(wb);
		if (cant_total_usuarios < 10000) {
			if (porcentaje1 < 20.0F) {
				row.getCell(numcel).setCellStyle(datosgreen);
			} else if (porcentaje1 > 50.0F) {
				row.getCell(numcel).setCellStyle(datosred);
			} else {
				row.getCell(numcel).setCellStyle(datosyellow);
			}
		} else if (cant_total_usuarios > 50000) {
			if (porcentaje1 < 10.0F) {
				row.getCell(numcel).setCellStyle(datosgreen);
			} else if (porcentaje1 > 30.0F) {
				row.getCell(numcel).setCellStyle(datosred);
			} else {
				row.getCell(numcel).setCellStyle(datosyellow);
			}
		} else if (porcentaje1 < 15.0F) {
			row.getCell(numcel).setCellStyle(datosgreen);
		} else if (porcentaje1 > 40.0F) {
			row.getCell(numcel).setCellStyle(datosred);
		} else {
			row.getCell(numcel).setCellStyle(datosyellow);
		}

	}

	private CellStyle creaEstilosColorYellow(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		XSSFColor myColor = new XSSFColor(Color.decode("#FFFF00"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}

	private CellStyle creaEstilosColorGreen(Workbook wb) {
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont negrita = (XSSFFont) wb.createFont();
		negrita.setBold(true);
		// negrita.setBoldweight((short) 700);
		negrita.setFontHeightInPoints((short) 9);
		style.setFont(negrita);
		XSSFColor myColor = new XSSFColor(Color.decode("#00FF00"),null);
		style.setFillForegroundColor(myColor);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		XSSFColor negro = new XSSFColor(Color.decode("#000000"),null);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderColor(BorderSide.LEFT, negro);
		style.setBorderColor(BorderSide.TOP, negro);
		style.setBorderColor(BorderSide.RIGHT, negro);
		style.setBorderColor(BorderSide.BOTTOM, negro);
		return style;
	}


    
}
