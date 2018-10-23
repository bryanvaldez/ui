/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.Constants.Validaciones;
import pe.gob.onpe.claridadui.model.DetalleFormato;
import pe.gob.onpe.claridadui.model.Formato;
import pe.gob.onpe.claridadui.util.ExcelUtil;

/**
 *
 * @author bvaldez
 */
public class ExcelValidator extends ExcelUtil{
    
    public final XSSFWorkbook workbook;
    public final int type;
    
    public boolean validExcel = true;
    public boolean validData = true;
    
    public String msjValidExcel = "";
    public String msjValidData = "";
    
    public static final String PATH_OBSERVATION = "D:\\CLARIDAD3\\OBSERVACIONES\\INGRESOS\\prueba.xlsx";
    
    public ExcelValidator(XSSFWorkbook workbook, int type){
        this.workbook = workbook;
        this.type = type;
    }
    
    public boolean saveFileObservation(XSSFWorkbook workbook, Formato formato) {
        try {
            String path = PATH_OBSERVATION;
            File fileExcelResultado = new File(path);
            OutputStream outputStream = new FileOutputStream(fileExcelResultado);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }    
    
}
