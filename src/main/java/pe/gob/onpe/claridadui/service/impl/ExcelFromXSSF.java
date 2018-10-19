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
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.Constants.Mensajes;
import pe.gob.onpe.claridadui.model.DetalleFormato;
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
        String message = null;
        JsonArray data = new JsonArray();

        IFormatoService factory  = new FormatoService();
        Formato formato = factory.getFormato(type);        
        
        if(isSheetValid(workbook, formato)){
            data = getSheetsData(formato);            
        }else{
            success = false;
            message = Mensajes.M_INVALID_EXCEL;
        }
        jResponse.addProperty("success", success);        
        jResponse.addProperty("message", message);
        jResponse.add("data", data);        
        
        if(invaliFormat){
            saveFileObservation(workbook, formato); 
            System.out.println("[OBSERVACIONES]");
        }
        
        return new Gson().toJson(jResponse);
    }
    
    public JsonArray getSheetsData(Formato formato){
        
        JsonArray jResponse = new JsonArray();         
        JsonObject jSheet1;
        JsonObject jSheet2;
        JsonObject jSheet3;
        JsonObject jSheet4;
                               
        JsonArray formatSheets = new JsonParser().parse(formato.getDetalleHoja()).getAsJsonArray(); 

        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();         
            int sheet = formatSheet.get("hoja").getAsInt();
            switch(sheet){
                case 1:
                    jSheet1 = getSheet1(formato, formatSheet);
                    jResponse.add(jSheet1);
                    break;
                case 2:
                    jSheet2 = getSheet2(formato, formatSheet);
                    jResponse.add(jSheet2);
                    break;
                case 3:
                    jSheet3 = getSheet3(formato, formatSheet);
                    jResponse.add(jSheet3);
                    break;
                case 4:
                    jSheet4 = getSheet4(formato, formatSheet);
                    jResponse.add(jSheet4);
                    break;                        
            }
        }
        
        return jResponse;
    }
    
    
    public JsonObject getSheet1(Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        JsonObject data = new JsonObject();
        boolean success = true;
        
        int position = formatSheet.get("hoja").getAsInt()-1;
        
        XSSFSheet sheet = workbook.getSheetAt(position);
        
        for (DetalleFormato parameter : formato.getDetalle()) {
            if(parameter.getType() == 0){
                Row rowParameter = sheet.getRow(parameter.getFilaExcel());
                Cell cellParameter = rowParameter.getCell(parameter.getColumnaExcel());
                String valueCell = getValueCell(cellParameter);
                data.addProperty(parameter.getNombreColumna(), valueCell);

                if(validateEmptyOrRegex(cellParameter, parameter, valueCell)){
                    
                }else{
                    System.out.println(valueCell);
                    invaliFormat = true;
                }
                
            }
        }
                
        jResponse.add("data", data);
        jResponse.addProperty("success", success);
        return jResponse;
    }
    
    public JsonObject getSheet2(Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        
        return jResponse;
    }

    public JsonObject getSheet3(Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        
        return jResponse;
    }

    public JsonObject getSheet4(Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        
        return jResponse;
    }    
    
}
