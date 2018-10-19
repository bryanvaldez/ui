/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author bvaldez
 */
public class CellStyle {
    
    public XSSFColor COLOR_CELL_ENUM = new XSSFColor(new java.awt.Color(215, 210, 183));    
    
    public XSSFCellStyle styleCellAmountTotal(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFont(getfontTotal(wb));
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(COLOR_CELL_ENUM);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }    
    
    private XSSFFont getfontTotal(XSSFWorkbook wb) {
        XSSFFont font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        return font;
    }    
    
}
