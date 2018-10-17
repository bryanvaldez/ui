/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.service.iface.IFactoryService;
import pe.gob.onpe.claridadui.service.iface.IExcelXSSFValidatorService;

/**
 *
 * @author bvaldez
 */
public class FactoryService implements IFactoryService{

    @Override
    public IExcelXSSFValidatorService validateExcelXSSF(XSSFWorkbook file, int type) {
        return new ExcelFromXSSF(file, type);
    }
    
}
