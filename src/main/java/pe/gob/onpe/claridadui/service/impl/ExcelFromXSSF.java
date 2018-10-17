/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.Constants.Mensajes;
import pe.gob.onpe.claridadui.model.Formato;
import pe.gob.onpe.claridadui.service.iface.IExcelXSSFValidatorService;
import pe.gob.onpe.claridadui.service.iface.IFormatoService;

/**
 *
 * @author bvaldez
 */
public class ExcelFromXSSF extends ExcelValidator implements IExcelXSSFValidatorService{
    
    public ExcelFromXSSF(XSSFWorkbook file, int type){
        super(file, type);
    }

    @Override
    public String validate() {
        JsonObject jResponse = new JsonObject();
        
        boolean success = true;
        String message = "";
        JsonArray data = new JsonArray();

        IFormatoService factory  = new FormatoService();
        Formato formato = factory.getFormato(type);        
        
        if(isSheetValid(workbook, formato)){
            data = getSheetData(formato);            
        }else{
            success = false;
            message = Mensajes.M_INVALID_EXCEL;
        }
        jResponse.addProperty("success", success);        
        jResponse.addProperty("message", message);
        jResponse.add("data", data);        
        return new Gson().toJson(jResponse);
    }
    
    private boolean validateSheet(Formato formato){
        boolean success = true;
        JsonArray formatSheets = new JsonParser().parse(formato.getDetalleHoja()).getAsJsonArray();    
        if(isSheetValid(workbook, formato)){
            for (int i = 0; i < formatSheets.size(); i++) {
                JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();         
                int hoja = formatSheet.get("hoja").getAsInt();

            }
        }else{           
            success = false;            
        }   
        return success;
    }
    
    public JsonArray getSheetData(Formato formato){
        boolean success = true;
        JsonArray response = new JsonArray();        
        JsonArray formatSheets = new JsonParser().parse(formato.getDetalleHoja()).getAsJsonArray(); 
        if(isSheetValid(workbook, formato)){
            for (int i = 0; i < formatSheets.size(); i++) {
                JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();         
                int hoja = formatSheet.get("hoja").getAsInt();

            }
        }else{           
            success = false;            
        }          
        return response;
    }
    
}
