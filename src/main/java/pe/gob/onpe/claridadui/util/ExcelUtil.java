/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.Constants.Validaciones;
import pe.gob.onpe.claridadui.model.DetalleFormato;
import pe.gob.onpe.claridadui.model.Formato;

/**
 *
 * @author bvaldez
 */
public class ExcelUtil extends CellStyle{

    public boolean isSheetValid(XSSFWorkbook workbook, Formato format) {
        boolean response = false;
        int isSheetValid = 0;
        JsonArray formatSheets = new JsonParser().parse(format.getDetalleHoja()).getAsJsonArray();
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();
            for (int j = 0; j < workbook.getNumberOfSheets(); j++) {
                if (formatSheet.get("descripcion").getAsString().equalsIgnoreCase(workbook.getSheetName(j))) {
                    isSheetValid++;
                }
            }
        }
        if (isSheetValid == formatSheets.size() && workbook.getNumberOfSheets() == formatSheets.size()) {
            response = true;
        }
        return response;
    }
    
    public String getValueCell(Cell celda) {
        String response = "";
        switch (celda.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    response = df.format(celda.getDateCellValue());
                } else {
                    response = fmt(round(celda.getNumericCellValue(), 2));                   
                }
                break;
            case Cell.CELL_TYPE_STRING:
                response = celda.getStringCellValue().trim();
                break;
            case Cell.CELL_TYPE_BLANK:
                response = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                System.out.println(celda.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                switch (celda.getCachedFormulaResultType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        response = fmt(round(celda.getNumericCellValue(), 2));                
                        break;
                    case Cell.CELL_TYPE_STRING:
                        response = celda.getRichStringCellValue().getString();
                        break;
                }
                break;
            default:
                response = "";
        }
        return response;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String fmt(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format(Locale.US, "%.2f", d, 2);
        }
    }
        
    public Comment getComentario(Cell cell, String mensaje) {
        XSSFSheet sheet = (XSSFSheet) cell.getSheet();        
        XSSFCreationHelper richTextFactory = sheet.getWorkbook().getCreationHelper();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 14);
        XSSFRichTextString rtf1 = richTextFactory.createRichTextString(mensaje);
        rtf1.applyFont(font);
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 2, 4, 4);
        XSSFComment comment1 = drawing.createCellComment(anchor);
        comment1.setAuthor("CLARIDAD");
        comment1.setString(rtf1);
        return comment1;
    }    
}