/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.test;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.enums.FormatoEnum;
import pe.gob.onpe.claridadui.service.iface.IExcelXSSFValidatorService;
import pe.gob.onpe.claridadui.service.iface.IFactoryService;
import pe.gob.onpe.claridadui.service.impl.FactoryService;

/**
 *
 * @author bvaldez
 */
public class testExcel {

    public static final String PATH_FORMATO_5 = "D:\\CLARIDAD3\\PRUEBA\\Formato_5.xlsx";
    public static final String PATH_FORMATO_6 = "D:\\CLARIDAD3\\PRUEBA\\Formato_6.xlsx";
        
    public static void main(String[] args) {
        try {
            String path = testExcel.class.getResource("/formatos/Formato_5.xlsx").toURI().getPath();            
            int tipoFormato = FormatoEnum.FORMATO_5.getId();
            
            XSSFWorkbook file = new XSSFWorkbook(new FileInputStream(PATH_FORMATO_5));
            IFactoryService factory = new FactoryService();
            IExcelXSSFValidatorService excelValidator = factory.validateExcelXSSF(file, tipoFormato);
            
            String responseJson = excelValidator.validate();
            System.out.println(responseJson);
            
        } catch (Exception e) {
            System.out.println("ADVERTENCIA:"+e.getMessage());
        }
    }
       
}
