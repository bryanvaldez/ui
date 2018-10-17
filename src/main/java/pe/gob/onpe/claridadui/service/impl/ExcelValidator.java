/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.util.ExcelUtil;

/**
 *
 * @author bvaldez
 */
public class ExcelValidator extends ExcelUtil{
    
    public final XSSFWorkbook workbook;
    public final int type;
    
    public ExcelValidator(XSSFWorkbook workbook, int type){
        this.workbook = workbook;
        this.type = type;
    }
    
}
