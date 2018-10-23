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

        JsonArray data = new JsonArray();

        IFormatoService factory  = new FormatoService();
        Formato formato = factory.getFormato(type);        
        
        validExcel_Sheet(workbook, formato);        
        
        if(validExcel){
            data = getSheetsData(formato);            
        }
        
        jResponse.addProperty("validData", validData);        
        jResponse.addProperty("msjValidData", msjValidData);        
        jResponse.addProperty("validExcel", validExcel);        
        jResponse.addProperty("msjValidExcel", msjValidExcel);
        jResponse.add("data", data);        
        
        if(!validData){
            saveFileObservation(workbook, formato); 
            System.out.println("[OBSERVACIONES]");
        }
        
        return new Gson().toJson(jResponse.get("data"));
    }
   
    //-----------------------------------------------------------Step 
    //1 Step
    public JsonArray getSheetsData(Formato formato){        
        JsonArray jResponse = new JsonArray();         
        JsonObject jSheetData;
             
        JsonArray formatSheets = new JsonParser().parse(formato.getDetalleHoja()).getAsJsonArray(); 
        for (int i = formatSheets.size()-1; i >=0 ; i--) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();  
            int position = formatSheet.get("hoja").getAsInt()-1;
            XSSFSheet sheet = workbook.getSheetAt(position);
            JsonObject coordinates = getCoordinates(sheet, formato, formatSheet);

            jSheetData = iteratorRow(formato, coordinates, position);  
            jResponse.add(jSheetData);                          
        }        
        return jResponse;
    }        
    //2 Step
    private JsonObject getCoordinates(XSSFSheet sheet, Formato formato, JsonObject formatSheet){
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
        jResponse.addProperty("columnaSuma", formatSheet.get("columnaSuma").getAsInt());
        return jResponse;        
    } 
    //3 Step
    private JsonObject iteratorRow(Formato formato, JsonObject coordinates, int position){        
        JsonObject jResponse = new JsonObject();
        JsonArray jdata = new JsonArray();
        XSSFSheet sheet = workbook.getSheetAt(position);
        
        if(coordinates.get("status").getAsBoolean()){                                  
            int initTable = coordinates.get("initRow").getAsInt();
            int finTable = coordinates.get("finRow").getAsInt();
            int subtotal = coordinates.get("subtotalRow").getAsInt();
            int total = coordinates.get("totalRow").getAsInt();   
            int columnaSuma = coordinates.get("columnaSuma").getAsInt(); 
            Iterator<Row> rowIterator = sheet.iterator();  
            Row row;            
            while (rowIterator.hasNext()) {
                JsonObject rowResult = new JsonObject();                
                row = rowIterator.next();                               
                //Data Table
                if (row.getRowNum() >= initTable && row.getRowNum() <= finTable) {
                    Iterator<Cell> cellIterator = row.cellIterator();  
                    Cell cell;
                    while (cellIterator.hasNext()) {
                        cell = cellIterator.next();
                        JsonObject cellValue = getCellData(position, cell, formato);                        
                        if(cellValue.get("success").getAsBoolean()){
                            rowResult.addProperty(cellValue.get("labelCell").getAsString(), cellValue.get("valueCell").getAsString());                        
                        }                       
                    }                    
                    if(!rowResult.entrySet().isEmpty()){
                        jdata.add(rowResult); 
                    }             
                }
                //Subtotal
                if(row.getRowNum() == subtotal && subtotal>0){
                    System.out.println("subtotal");
                }
                
                //Total
                if(row.getRowNum() == total && total>0){
                    Cell cellTotal = row.getCell(columnaSuma);
                    String valueCell = getValueCell(cellTotal);
                    System.out.println("TOTAL:  "+valueCell);
                  
                }
            }            
        }
        jResponse.add(sheet.getSheetName(), jdata);
        return jResponse;       
    }    
    //4 Step
    public JsonObject getCellData(int position, Cell cell, Formato formato){
        JsonObject jResponse = new JsonObject();
        boolean success  = false;

        for (DetalleFormato parameter : formato.getDetalle()) {
            if(parameter.getType() == Validaciones.FORMAT_READER && parameter.getHojaExcel() == position+1 ){                 
                if(parameter.getColumnaExcel() == cell.getColumnIndex()){
                    String valueCell = getValueCell(cell);
                    boolean validCell =  validData_EmptyOrRegex(cell, parameter, valueCell);
                    if(validCell){
                        success = true;
                        jResponse.addProperty("labelCell", parameter.getNombreColumna());
                        jResponse.addProperty("valueCell", valueCell);                         
                    }
                    break;
                }
               
            }
        }         
        jResponse.addProperty("success", success);        
        return jResponse;
    }
        
    
    //-----------------------------------------------------------Validations
    //1 Validation Excel 
    public void validExcel_Sheet(XSSFWorkbook workbook, Formato format) {
        int countSheetValid = 0;
        JsonArray formatSheets = new JsonParser().parse(format.getDetalleHoja()).getAsJsonArray();
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();
            for (int j = 0; j < workbook.getNumberOfSheets(); j++) {
                if (formatSheet.get("descripcion").getAsString().equalsIgnoreCase(workbook.getSheetName(j))) {
                    countSheetValid++;
                }
            }
        }
        
        if(countSheetValid != formatSheets.size() ||  workbook.getNumberOfSheets() != formatSheets.size()){
            validExcel = false;
            msjValidExcel = Mensajes.M_INVALID_EXCEL;            
        }

    }    
    //2 Validation Data
    public boolean validData_EmptyOrRegex(Cell cell, DetalleFormato parameter, String value) {
        
        boolean response = true;
        
        String regex = parameter.getValidacion();
        String messageRegexError = parameter.getMensajeValidacion();
        String messageEmptyError = parameter.getComentario();
        
        XSSFSheet sheet = (XSSFSheet) cell.getSheet();
        XSSFWorkbook wb = (XSSFWorkbook) sheet.getWorkbook();

        if (value.equalsIgnoreCase("")) {
            if (parameter.getObligatorio() == Validaciones.FORMAT_REQUIRED) {
                cell.setCellStyle(styleCellObservation(wb));
                cell.setCellComment(getComentario(cell, messageEmptyError));
                response = false;  
            }
        } else {
            if (regex != null && !regex.trim().isEmpty()) {
                if (!value.matches(regex)) {
                    cell.setCellStyle(styleCellObservation(wb));
                    cell.setCellComment(getComentario(cell, messageRegexError));
                    response = false;
                }
            }
        }
        
        validData = response;
        return response;
    }        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    public JsonObject getSheet4(XSSFSheet sheet, Formato formato, JsonObject sheetParam){
//        JsonObject jResponse = new JsonObject();
//        JsonObject data = new JsonObject();
//        boolean formatValid = true;
//        
//        if(sheetParam.get("status").getAsBoolean()){          
//                        
//            int initTable = sheetParam.get("initRow").getAsInt();
//            int finTable = sheetParam.get("finRow").getAsInt();
//            int subtotal = sheetParam.get("subtotalRow").getAsInt();
//            int total = sheetParam.get("totalRow").getAsInt();            
//            
//            Iterator<Row> rowIterator = sheet.iterator();  
//            Row row;            
//            while (rowIterator.hasNext()) {
//                JsonObject result = new JsonObject();
//                
//                row = rowIterator.next();
//                               
//                if (row.getRowNum() >= initTable && row.getRowNum() <= finTable) {
//                    Iterator<Cell> cellIterator = row.cellIterator();  
//                    Cell cell;
//                    while (cellIterator.hasNext()) {
//                        cell = cellIterator.next();
//                        String valueCell = getValueCell(cell);
//                        System.out.println("value:"+valueCell);
//                        
//                    }
//                    
//                    
//                    
//                }
//                
//            
//            }
//            
//        }
//        
//
//        
//        
//
//        JsonObject jDataSheet = new JsonObject();
//
//        return jResponse;
//    }         
//    public JsonObject getSheet1(XSSFSheet sheet, Formato formato){
//        JsonObject jResponse = new JsonObject();
//        JsonObject data = new JsonObject();
//        boolean success = true;
//        
//        for (DetalleFormato parameter : formato.getDetalle()) {
//            if(parameter.getType() == Validaciones.FORMAT_READER){ 
//                Row rowParameter = sheet.getRow(parameter.getFilaExcel());
//                Cell cellParameter = rowParameter.getCell(parameter.getColumnaExcel());
//                String valueCell = getValueCell(cellParameter);
//                data.addProperty(parameter.getNombreColumna(), valueCell);
//
//                if(validateEmptyOrRegex(cellParameter, parameter, valueCell)){
//                    
//                }else{
//                    validData = false;
//                }                
//            }
//        }                
//        jResponse.add("data", data);
//        jResponse.addProperty("success", success);
//        return jResponse;
//    }    
//    public JsonObject getSheet2(Formato formato, JsonObject formatSheet, int position){
//        JsonObject jResponse = new JsonObject();
//        
//        return jResponse;
//    }
//    public JsonObject getSheet3(Formato formato, JsonObject formatSheet, int position){
//        JsonObject jResponse = new JsonObject();
//        
//        return jResponse;
//    }       

    
    
}
