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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pe.gob.onpe.claridadui.Constants.Mensajes;
import pe.gob.onpe.claridadui.Constants.Validaciones;
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
        for (int i = formatSheets.size()-1; i >=0 ; i--) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();  
            int position = formatSheet.get("hoja").getAsInt()-1;
            XSSFSheet sheet = workbook.getSheetAt(position);
            JsonObject sheetParam = getSheetParam(sheet, formato, formatSheet);

            if(validateNameSheet(sheet, formatSheet)){
                switch(position){
                    case 0:
                        jSheet1 = getSheet1(sheet, formato);
                        jResponse.add(jSheet1);
                        break;
                    case 1:
                        jSheet2 = getSheet2(formato, formatSheet, position);
                        jResponse.add(jSheet2);
                        break;
                    case 2:
                        jSheet3 = getSheet3(formato, formatSheet, position);
                        jResponse.add(jSheet3);
                        break;
                    case 3:
                        jSheet4 = getSheet4(sheet, formato, sheetParam);
                        jResponse.add(jSheet4);
                        break;                        
                }             
            }           
        }        
        return jResponse;
    }    
    public JsonObject getSheet4(XSSFSheet sheet, Formato formato, JsonObject sheetParam){
        JsonObject jResponse = new JsonObject();
        JsonObject data = new JsonObject();
        boolean formatValid = true;
        
        Iterator<Row> rowIterator = sheet.iterator();
        
        

        JsonObject jDataSheet = new JsonObject();
        
        
        
        return jResponse;
    }         
    public JsonObject getSheet1(XSSFSheet sheet, Formato formato){
        JsonObject jResponse = new JsonObject();
        JsonObject data = new JsonObject();
        boolean success = true;
        
        for (DetalleFormato parameter : formato.getDetalle()) {
            if(parameter.getType() == Validaciones.FORMAT_READER){ 
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
    public JsonObject getSheet2(Formato formato, JsonObject formatSheet, int position){
        JsonObject jResponse = new JsonObject();
        
        return jResponse;
    }
    public JsonObject getSheet3(Formato formato, JsonObject formatSheet, int position){
        JsonObject jResponse = new JsonObject();
        
        return jResponse;
    }       

    
    private JsonObject getSheetParam(XSSFSheet sheet, Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        boolean success = true;        
        Iterator<Row> rowIterator = sheet.iterator();
          
        String initTable = formatSheet.get("iniTabla").getAsString();
        String subTotalTable = formatSheet.get("subtotal").getAsString();
        String totalTable = formatSheet.get("total").getAsString();        
        
        int initRow = 0;
        int finRow = 0;
        int subtotalRow = 0;
        int totalRow = 0;

        boolean valve = true;

        if(!initTable.equalsIgnoreCase("")){
            Row row;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell celda;
                if (valve) {
                    while (cellIterator.hasNext()) {
                        celda = cellIterator.next();
                        String valueCell = getValueCell(celda).trim();

                        if(valueCell.equalsIgnoreCase(initTable)){
                            initRow = celda.getRow().getRowNum() + 1;                        
                        }else{
                            if(subTotalTable.equalsIgnoreCase("")){
                                if (valueCell.equalsIgnoreCase(totalTable)){
                                    finRow = celda.getRow().getRowNum() - 1;
                                    subtotalRow = 0;
                                    totalRow = celda.getRow().getRowNum();
                                    valve = false;
                                    break;
                                }                                
                            }else{
                                if (valueCell.equalsIgnoreCase(subTotalTable)){
                                    subtotalRow = celda.getRow().getRowNum();
                                    finRow = celda.getRow().getRowNum() - 1;
                                }else if(valueCell.equalsIgnoreCase(totalTable)){
                                    totalRow = celda.getRow().getRowNum();
                                    valve = false;
                                    break;                                    
                                }                                                              
                            }                            
                        }
                    }
                } else {
                    break;
                }
            }             
        }else{
            success = false;
        }        
                
        jResponse.addProperty("initRow", initRow);
        jResponse.addProperty("finRow", finRow);
        jResponse.addProperty("subtotalRow", subtotalRow);
        jResponse.addProperty("totalRow", totalRow);
        jResponse.addProperty("status", success);        
        return jResponse;        
    }    
    private boolean validateNameSheet(XSSFSheet sheet, JsonObject formatSheet){
        boolean response = true;
        String nameSheet = formatSheet.get("descripcion").getAsString();
        if(!nameSheet.equalsIgnoreCase(sheet.getSheetName())){
            response = false;
        }
        return response;        
    }
    
}
