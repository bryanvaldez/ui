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
            
            if(!validData){
                saveFileObservation(workbook, formato); 
                System.out.println("[ OBSERVACIONES ]");
            }else{
                System.out.println("[ CORRECTO!! ]");
            }                        
        }
                              
        jResponse.addProperty("validData", validData);        
        jResponse.addProperty("msjValidData", msjValidData);        
        jResponse.addProperty("validExcel", validExcel);        
        jResponse.addProperty("msjValidExcel", msjValidExcel);
        jResponse.add("data", data);                         
        return new Gson().toJson(jResponse.get("data"));
    }
   
    //-----------------------------------------------------------Step 
    //1 Step
    private JsonArray getSheetsData(Formato formato){        
        JsonArray jResponse = new JsonArray();         
        JsonObject jSheetData;
             
        JsonArray formatSheets = new JsonParser().parse(formato.getDetalleHoja()).getAsJsonArray(); 
        for (int i = formatSheets.size()-1; i >=0 ; i--) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();  
            int position = formatSheet.get("hoja").getAsInt()-1;
            XSSFSheet sheet = workbook.getSheetAt(position);
            JsonObject coordinates = getCoordinates(sheet, formato, formatSheet);

            jSheetData = getRowIterator(formato, coordinates, position);  
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
        return jResponse;        
    } 
    //3 Step
    private JsonObject getRowIterator(Formato formato, JsonObject coordinates, int position){    
        
        JsonObject jResponse = new JsonObject();        
        JsonArray jdata = new JsonArray();
        XSSFSheet sheet = workbook.getSheetAt(position);
        
        String columna1 ="", columna2 ="";
        double sumaTablaCol1 = 0;
        double sumaTablaCol2= 0;
        
        double sumaSubtotal= 0;
        
        if(coordinates.get("status").getAsBoolean()){                                  
            int rowInitTable = coordinates.get("initRow").getAsInt();
            int rowFinTable = coordinates.get("finRow").getAsInt();
            int rowSubtotal = coordinates.get("subtotalRow").getAsInt();
            int rowTotal = coordinates.get("totalRow").getAsInt();    
            Iterator<Row> rowIterator = sheet.iterator();  
            Row row;            
            while (rowIterator.hasNext()) {               
                row = rowIterator.next();                           
                JsonObject rowOut;                                                                 
                if (row.getRowNum() >= rowInitTable && row.getRowNum() <= rowFinTable) { //Data Table
                    rowOut = getCellIterator(formato, position, row, Validaciones.T_TABLE);                                        
                    if(rowOut.get("success").getAsBoolean()){ 
                        jdata.add(rowOut.getAsJsonObject("data"));
                        String rowPosition = rowOut.get("cellPosition").getAsString();
                        System.out.println(rowPosition);
//                        
//                        if(columna1.equalsIgnoreCase("")){
//                            columna1 = rowPosition;
//                            sumaTablaCol1 += rowOut.get("suma").getAsDouble();
//                            System.out.println("HOJA:"+ (position+1) +" | columna:"+rowPosition +"  | SUMA1:  "+rowOut.get("suma").getAsDouble());                             
//                        }else if(columna1.equalsIgnoreCase(rowPosition)){
//                            sumaTablaCol1 += rowOut.get("suma").getAsDouble();
//                            System.out.println("HOJA:"+ (position+1) +" | columna:"+rowPosition +"  | SUMA1:  "+rowOut.get("suma").getAsDouble());
//                        }            
//                        
                        
//                        if(columna2.equalsIgnoreCase(rowPosition)){
//                            sumaTablaCol2 += rowOut.get("suma").getAsDouble();
//                            System.out.println("HOJA:"+ (position+1) +"  | COLUMNA2:  "+rowOut.get("suma").getAsDouble());                          
//                        }                        
                    }                    
                }else if(row.getRowNum() == rowSubtotal && rowSubtotal>0){  //Data Subtotal
                    rowOut = getCellIterator(formato, position, row, Validaciones.T_SUBTOTAL);
                    if(rowOut.get("success").getAsBoolean()){
                        //System.out.println("HOJA:"+ (position+1) +"  | SUBTOTAL:  "+rowOut.get("suma").getAsDouble());                        
                        rowOut = rowOut.getAsJsonObject("data");   
                    }                      
                }else if(row.getRowNum() == rowTotal && rowTotal>0){  //Data Total
                    rowOut = getCellIterator(formato, position, row, Validaciones.T_TOTAL);
                    if(rowOut.get("success").getAsBoolean()){
                        validData_TotalAmount(position, row, rowOut, sumaTablaCol1);
                    }                      
                }                
            }            
        }        
                      
        jResponse.add("data", jdata);         
        jResponse.addProperty("formato", sheet.getSheetName()); 
        return jResponse;       
    }    
    //4 Step
    private JsonObject getCellIterator(Formato formato, int position, Row row, int TypeTable){        
        JsonObject jResponse = new JsonObject();
        JsonObject rowResult = new JsonObject();  
        boolean succes = false;      
        double suma = 0;
        int cellPosition = 0;
        Iterator<Cell> cellIterator = row.cellIterator();  
        Cell cell;
        while (cellIterator.hasNext()) {
            cell = cellIterator.next();
            JsonObject cellValue = getCellValue(position, cell, formato, TypeTable);                        
            if(cellValue.get("success").getAsBoolean()){
                if(cellValue.get("isSuma").getAsBoolean()){
                    suma = cellValue.get("valueCell").getAsDouble();
                    //System.out.println("hoja: "+ (position+1) +" | columna:"+cell.getColumnIndex() +" | SUMAR:  "+suma);
                }
                cellPosition = cell.getColumnIndex();
                rowResult.addProperty(cellValue.get("labelCell").getAsString(), cellValue.get("valueCell").getAsString()); 
                succes = true;
            }                       
        }                    
        jResponse.addProperty("suma", suma);    
        jResponse.addProperty("cellPosition", cellPosition); 
        jResponse.add("data", rowResult);
        jResponse.addProperty("success", succes);
        System.out.println("hoja: "+ (position+1) +" | columna:"+cellPosition +" | SUMAR:  "+suma);        
        return jResponse;    
    }    
    //5 Step
    private JsonObject getCellValue(int position, Cell cell, Formato formato, int typeData){
        JsonObject jResponse = new JsonObject();
        boolean success  = false;
        for (DetalleFormato parameter : formato.getDetalle()) {            
            if(parameter.getProcesoDetalle()== Validaciones.FORMAT_READER && parameter.getHojaExcel() == position+1 && parameter.getTipoDato() == typeData){                 
                if(parameter.getColumnaExcel() == cell.getColumnIndex() &&  parameter.getFilaExcel() == 0 ){
                    String valueCell = getValueCell(cell);
                    boolean validCell =  validData_EmptyOrRegex(cell, parameter, valueCell);
                    if(validCell){
                        success = true;
                        jResponse.addProperty("labelCell", parameter.getNombreColumna());
                        jResponse.addProperty("valueCell", valueCell);
                        jResponse.addProperty("isSuma", parameter.isSuma());
                    }
                    break;
                }else{                
                    if(parameter.getColumnaExcel() == cell.getColumnIndex() &&  parameter.getFilaExcel() == cell.getRowIndex() ){
                        String valueCell = getValueCell(cell);
                        boolean validCell =  validData_EmptyOrRegex(cell, parameter, valueCell);
                        if(validCell){
                            success = true;
                            jResponse.addProperty("labelCell", parameter.getNombreColumna());
                            jResponse.addProperty("valueCell", valueCell);
                            jResponse.addProperty("isSuma", parameter.isSuma());                           
                        }
                        break;
                    }                                    
                }
            }                       
        }         
        jResponse.addProperty("success", success);        
        return jResponse;
    }    
    //6 Get amount table
    
    //-----------------------------------------------------------Validations
    //Validation Excel 
    public void validExcel_Sheet(XSSFWorkbook workbook, Formato format) {
        int countSheetValid = 0;
        JsonArray formatSheets = new JsonParser().parse(format.getDetalleHoja()).getAsJsonArray();
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();
            for (int j = 0; j < workbook.getNumberOfSheets(); j++) {
                if (formatSheet.get("descripcion").getAsString().equalsIgnoreCase(workbook.getSheetName(j))) {
                    countSheetValid++;
                    break;
                }
            }
        }        
        if(countSheetValid != formatSheets.size() ||  workbook.getNumberOfSheets() != formatSheets.size()){
            validExcel = false;
            msjValidExcel = Mensajes.M_INVALID_EXCEL;            
        }
    }    
    //Validation Data
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
                validData = response;
            }
        } else {
            if (regex != null && !regex.trim().isEmpty()) {
                if (!value.matches(regex)) {
                    cell.setCellStyle(styleCellObservation(wb));
                    cell.setCellComment(getComentario(cell, messageRegexError));
                    response = false;
                    validData = response;
                }
            }
        }                
        return response;
    }        
    //Validate Amount Total 
    private void validData_TotalAmount(int position, Row row, JsonObject rowOut, double sumaTabla){        
        int cellPosition  = rowOut.get("cellPosition").getAsInt();        
        double total = rowOut.getAsJsonObject("data").get("total").getAsDouble();               
        Cell cell = row.getCell(cellPosition);
        if(sumaTabla == total){
            //System.out.println("HOJA:"+ (position+1) +"TOTAL CORRECTO");
        }else{
            cell.setCellStyle(styleCellObservation(workbook));
            cell.setCellComment(getComentario(cell, Mensajes.M_INVALIDA_TOTAL));                
            validData = false;            
            //System.out.println("HOJA:"+ (position+1) +"TOTAL INCORRECTO");
        }
    }    
    //Validate Amount Subtotal     
    


}
