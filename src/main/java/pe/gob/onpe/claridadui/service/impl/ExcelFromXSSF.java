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
import pe.gob.onpe.claridadui.enums.FormatoEnum;
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
        
        JsonObject formatsSheetsValid = getSheetValidIndex(formato, formatSheets, 0);
        indexData = formatsSheetsValid.get("dataIndex").getAsJsonObject();
        formatSheets =  formatsSheetsValid.get("formatSheets").getAsJsonArray();            
        
        for (int i = formatSheets.size()-1; i >=0 ; i--) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();  
            int position = formatSheet.get("hoja").getAsInt()-1;
            XSSFSheet sheet = workbook.getSheetAt(position);
            JsonObject coordinates = getCoordinates(sheet, formato, formatSheet);
            jSheetData = getTableIterator(formato, coordinates, position);
            System.out.println("Hoja" +  position);
            jResponse.add(jSheetData);                          
        }    

        jResponse.add(indexData);        
        return jResponse;
    }       
    //2 Get Sheet Cordinates
    private JsonObject getSheetValidIndex(Formato formato, JsonArray formatSheets, int sheetIndex){        
        JsonObject jResponse = new JsonObject();  
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        JsonObject formatSheet = formatSheets.get(sheetIndex).getAsJsonObject();
        int position = formatSheet.get("hoja").getAsInt()-1;
        JsonObject coordinates = getCoordinates(sheet, formato, formatSheet);        
        if(formato.getId() == FormatoEnum.FORMATO_5.getId()){
            jResponse = validFormat5(formato, formatSheets, coordinates, position);
        }else if(formato.getId() == FormatoEnum.FORMATO_6.getId()){
            jResponse = validFormat6(formato, formatSheets, coordinates, position);            
        }        
        return jResponse;        
    }    
    //3 Step
    private JsonObject getCoordinates(XSSFSheet sheet, Formato formato, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        boolean success = true;        
        Iterator<Row> rowIterator = sheet.iterator();
          
        boolean isIndex = formatSheet.get("isIndex").getAsBoolean();
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
                
        jResponse.addProperty("isIndex", isIndex);        
        jResponse.addProperty("initRow", initRow);
        jResponse.addProperty("finRow", finRow);
        jResponse.addProperty("subtotalRow", subtotalRow);
        jResponse.addProperty("totalRow", totalRow);
        jResponse.addProperty("status", success);
        return jResponse;        
    } 
    //4 Step
    private JsonObject getTableIterator(Formato formato, JsonObject coordinates, int position){    
        
        JsonObject jResponse = new JsonObject();        
        JsonArray jdata = new JsonArray();
        JsonArray subTotal = new JsonArray();
        JsonArray total = new JsonArray();
        XSSFSheet sheet = workbook.getSheetAt(position);
        String formatName = sheet.getSheetName();
                
        double sumCol1 = 0, sumCol2= 0;
        
        if(coordinates.get("status").getAsBoolean()){ 
            //int position = coordinates.get("hoja").getAsInt()-1;
            int rowInitTable = coordinates.get("initRow").getAsInt();
            int rowFinTable = coordinates.get("finRow").getAsInt();
            int rowSubtotal = coordinates.get("subtotalRow").getAsInt();
            int rowTotal = coordinates.get("totalRow").getAsInt();  
            boolean isIndex = coordinates.get("isIndex").getAsBoolean();
            
            Iterator<Row> rowIterator = sheet.iterator();  
            Row row;            
            while (rowIterator.hasNext()) {               
                row = rowIterator.next();                           
                JsonObject rowOut;                                                                 
                if (row.getRowNum() >= rowInitTable && row.getRowNum() <= rowFinTable) { //Data Table
                    rowOut = getRowIterator(formato, position, row, Validaciones.T_TABLE);                                        
                    if(rowOut.get("success").getAsBoolean()){        
                        jdata.add(rowOut.getAsJsonObject("data"));                        
                        sumCol1+= getRowAmount(rowOut, 1); 
                        sumCol2+= getRowAmount(rowOut, 2);
                        
                        

                        //validData_MatchIndex(row, rowOut, isIndex);
                        
                        
                        //CUSTOM VALIDATION -- COLOCAR AQUI VALIDACIONES ADICIONALES
                        
                    }                    
                }else if(row.getRowNum() == rowSubtotal && rowSubtotal>0){  //Data Subtotal
                    rowOut = getRowIterator(formato, position, row, Validaciones.T_SUBTOTAL);
                    if(rowOut.get("success").getAsBoolean()){
                        subTotal.add(rowOut.getAsJsonObject("data"));
                        validData_SubTotal(row, rowOut, sumCol1, sumCol2);                     
                    }                      
                }else if(row.getRowNum() == rowTotal && rowTotal>0){  //Data Total
                    rowOut = getRowIterator(formato, position, row, Validaciones.T_TOTAL);
                    if(rowOut.get("success").getAsBoolean()){
                        total.add(rowOut.getAsJsonObject("data"));
                        validData_Total(row, rowOut, sumCol1, sumCol2);
                    }                      
                }                
            }            
        }        
                      
        jResponse.add("data", jdata);
        jResponse.add("subTotal", subTotal);
        jResponse.add("total", total);
        jResponse.addProperty("formato", formatName); 
        return jResponse;       
    }    
    //5 Step
    private JsonObject getRowIterator(Formato formato, int position, Row row, int TypeTable){        
        JsonObject jResponse = new JsonObject();
        JsonObject rowResult = new JsonObject();  
        boolean succes = false;      
        
        JsonArray jAmount = new JsonArray();
        
        double amountCol1 = 0, amountCol2 = 0;        
        int columnAmount1 = 0, columnAmount2 = 0;

        Iterator<Cell> cellIterator = row.cellIterator();  
        Cell cell;
        while (cellIterator.hasNext()) {
            cell = cellIterator.next();
            JsonObject cellValue = getCellValue(position, cell, formato, TypeTable);                        
            JsonObject monto = new JsonObject(); 
            if(cellValue.get("success").getAsBoolean()){
                if(cellValue.get("isSuma").getAsBoolean()){                    
                    int order = cellValue.get("order").getAsInt();                    
                    if(order == 0 || order == 1){
                        amountCol1 = cellValue.get("valueCell").getAsDouble();
                        columnAmount1 = cell.getColumnIndex();
                        monto.addProperty("monto", amountCol1);
                        monto.addProperty("columna", columnAmount1);
                        monto.addProperty("group", 1);
                        jAmount.add(monto);                
                    }else if(order == 2){
                        amountCol2 = cellValue.get("valueCell").getAsDouble();
                        columnAmount2 = cell.getColumnIndex();
                        monto.addProperty("monto", amountCol2);
                        monto.addProperty("columna", columnAmount2);
                        monto.addProperty("group", 2);
                        jAmount.add(monto);                         
                    }                                    
                }
                rowResult.addProperty(cellValue.get("labelCell").getAsString(), cellValue.get("valueCell").getAsString()); 
                succes = true;
            }                       
        }                    
        jResponse.add("amount", jAmount);    
        jResponse.add("data", rowResult);
        jResponse.addProperty("success", succes);       
        return jResponse;    
    }    
    //6 Step
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
                        jResponse.addProperty("order", parameter.getOrden());
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
                            jResponse.addProperty("order", parameter.getOrden());
                        }
                        break;
                    }                                    
                }
            }                       
        }         
        jResponse.addProperty("success", success);        
        return jResponse;
    }    
    //7 Get amount Row
    private double getRowAmount(JsonObject rowOut, int position){                   
        double response = 0;                
        JsonArray aAmounts = rowOut.getAsJsonArray("amount");                        
        for (int i = 0; i < aAmounts.size(); i++) {
            JsonObject amount = aAmounts.get(i).getAsJsonObject();            
            if(position == amount.get("group").getAsInt()){
                response = amount.get("monto").getAsDouble();
            }                         
        }                   
        return response;
    }
    
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
            }else{
                response = false;
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
    //Validate Amount SubTotal  
    private void validData_SubTotal(Row row, JsonObject rowOut, double sumCol1, double sumCol2){
        JsonArray aAmounts = rowOut.getAsJsonArray("amount"); 
        for (int i = 0; i < aAmounts.size(); i++) {
            JsonObject amount = aAmounts.get(i).getAsJsonObject();
            int columna = amount.get("columna").getAsInt();
            double monto = amount.get("monto").getAsDouble();  
            if(i==0){
                validData_Amount(row, columna, monto, sumCol1);

            }else if(i == 1){
                validData_Amount(row, columna, monto, sumCol2);                 
            }                            
        }      
    }
    //Validate Amount Total  
    private void validData_Total(Row row, JsonObject rowOut, double sumCol1, double sumCol2){
        JsonArray aAmounts = rowOut.getAsJsonArray("amount"); 
        for (int i = 0; i < aAmounts.size(); i++) {
            JsonObject amount = aAmounts.get(i).getAsJsonObject();
            int columna = amount.get("columna").getAsInt();
            double monto = amount.get("monto").getAsDouble();                              
            validData_Amount(row, columna, monto, sumCol1+sumCol2);
        }     
    }    
    //Validate Amount  
    private void validData_Amount(Row row, int columna, double amount1, double amount2){                      
        Cell cell = row.getCell(columna);
        if(amount1 != amount2){
            cell.setCellStyle(styleCellObservation(workbook));
            cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT));                
            validData = false; 
        }
    }        
    //Validate Amount sheet
    private void validData_MatchIndex(Row row, JsonObject rowOut, boolean isIndex){
              
        if(isIndex){
            if(!indexData.entrySet().isEmpty()){
                System.out.println("pe.gob.onpe.claridadui.service.impl.ExcelFromXSSF.validData_MatchIndex()");
                
            }

        
        }        
    }
    
    //-----------------------------------------------------------CUSTOM VALIDATION       
    private JsonObject validFormat5(Formato formato, JsonArray formatSheets, JsonObject coordinates, int position ){        
        JsonObject jResponse = new JsonObject();
        JsonArray jFormatSheets = new JsonArray();        
        JsonObject jSheetData = getTableIterator(formato, coordinates, position);
        JsonArray sheetCordinates = jSheetData.get("data").getAsJsonArray();     
        boolean is5A = false, is5B = false, is5C = false;

        for (int i = 0; i < sheetCordinates.size(); i++) {
            JsonObject cordinate = sheetCordinates.get(i).getAsJsonObject();                
            if(!is5A){
                is5A = cordinate.get("5A") != null;
            }
            if(!is5B){
                is5B = cordinate.get("5B") != null;
            }
            if(!is5C){
                is5C = cordinate.get("5C") != null;
            }                 
        }               
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject temp = formatSheets.get(i).getAsJsonObject();
            String desc = temp.get("descripcion").getAsString(); 
            if(desc.equalsIgnoreCase("Formato-5")){
               //jFormatSheets.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-5A") && is5A){
                jFormatSheets.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-5B") && is5B){
                jFormatSheets.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-5C") && is5C){
                jFormatSheets.add(temp);
            }                
        }        
        
        jResponse.add("formatSheets", jFormatSheets);
        jResponse.add("dataIndex", jSheetData);
        return jResponse; 
    }    
    private JsonObject validFormat6(Formato formato, JsonArray formatSheets, JsonObject coordinates, int position ){        
        JsonObject jResponse = new JsonObject();
        JsonArray jFormatSheets = new JsonArray();        
        JsonObject jSheetData = getTableIterator(formato, coordinates, position);
        JsonArray sheetCordinates = jSheetData.get("data").getAsJsonArray();     
        boolean is6A = false, is6B = false, is6C = false;

        for (int i = 0; i < sheetCordinates.size(); i++) {
            JsonObject cordinate = sheetCordinates.get(i).getAsJsonObject();                
            if(!is6A){
                is6A = cordinate.get("6A") != null;
            }
            if(!is6B){
                is6B = cordinate.get("6B") != null;
            }
            if(!is6C){
                is6C = cordinate.get("6C") != null;
            }                 
        }               
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject temp = formatSheets.get(i).getAsJsonObject();
            String desc = temp.get("descripcion").getAsString(); 
            if(desc.equalsIgnoreCase("Formato-6")){
                //jResponse.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-6A") && is6A){
                jFormatSheets.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-6B") && is6B){
                jFormatSheets.add(temp);
            }else if(desc.equalsIgnoreCase("Anexo-6C") && is6C){
                jFormatSheets.add(temp);
            }                
        }        
        jResponse.add("formatSheets", jFormatSheets);
        jResponse.add("dataIndex", jSheetData);
        return jResponse;  
    }    
    
}
