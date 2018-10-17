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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.model.Formato;

/**
 *
 * @author bvaldez
 */
public class ExcelUtil {
    
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
    
    public boolean saveFileObservation(XSSFWorkbook workbook, Formato formato){      
        try {
            File fileExcelResultado = new File(formato.getRutaObservaciones());        
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
