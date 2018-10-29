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
import java.text.ParseException;
import java.util.Date;
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
        JsonArray jCordinates = getCoordinates(formatSheets); 
        
        for (int i = 0; i < jCordinates.size(); i++) {
            JsonObject jCordinate = jCordinates.get(i).getAsJsonObject();            
            if(jCordinate.get("isIndex").getAsBoolean()){
                jSheetData = getTableIterator(formato, jCordinate);
                jResponse.add(jSheetData);                
                JsonObject response = getSheetValidIndex(formato, jCordinates, jSheetData);                       
                jCordinates =  response.get("jCoordinates").getAsJsonArray();                                 
                System.out.println("Hoja: " +  (jCordinate.get("hoja").getAsInt()) +" | success");                
            }
        }
        
        for (int i = 0; i < jCordinates.size(); i++) {
            JsonObject jCordinate = jCordinates.get(i).getAsJsonObject();            
            if(!jCordinate.get("isIndex").getAsBoolean()){
                jSheetData = getTableIterator(formato, jCordinate);
                jResponse.add(jSheetData);
                System.out.println("Hoja: " +  (jCordinate.get("hoja").getAsInt()) + " | success"); 
            }
        }        
        
        return jResponse;
    }       
    //2 Get Sheet Cordinates
    private JsonObject getSheetValidIndex(Formato formato, JsonArray jCordinates, JsonObject jSheetIndexData){        
        JsonObject jResponse = new JsonObject();  
                            
        if(formato.getId() == FormatoEnum.FORMATO_5.getId()){
            jResponse = validFormat5(formato, jCordinates, jSheetIndexData);
        }else if(formato.getId() == FormatoEnum.FORMATO_6.getId()){
            jResponse = validFormat6(formato, jCordinates, jSheetIndexData);            
        }        
        return jResponse;        
    }    
    //3 Step
    
    private JsonArray getCoordinates(JsonArray formatSheets){
        JsonArray jResponse = new JsonArray();         
        for (int i = 0; i < formatSheets.size(); i++) {
            JsonObject formatSheet = formatSheets.get(i).getAsJsonObject();
            int position = formatSheet.get("hoja").getAsInt()-1;
            XSSFSheet sheet = workbook.getSheetAt(position);           
            jResponse.add(getCoordinate(sheet, formatSheet));
        }                
        return jResponse;
    }    
    
    private JsonObject getCoordinate(XSSFSheet sheet, JsonObject formatSheet){
        JsonObject jResponse = new JsonObject();
        boolean success = true;        
        Iterator<Row> rowIterator = sheet.iterator();
          
        int hoja = formatSheet.get("hoja").getAsInt();
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
                
        jResponse.addProperty("hoja", hoja);
        jResponse.addProperty("isIndex", isIndex);        
        jResponse.addProperty("initRow", initRow);
        jResponse.addProperty("finRow", finRow);
        jResponse.addProperty("subtotalRow", subtotalRow);
        jResponse.addProperty("totalRow", totalRow);
        jResponse.addProperty("status", success);
        return jResponse;        
    } 
    //4 Step
    private JsonObject getTableIterator(Formato formato, JsonObject coordinate){    
        
        JsonObject jResponse = new JsonObject();        
        JsonArray jdata = new JsonArray();
        JsonArray subTotal = new JsonArray();
        JsonArray total = new JsonArray();        
        String formatName = "";
                
        double sumCol1 = 0, sumCol2= 0;
        
        if(coordinate.get("status").getAsBoolean()){ 
            int hoja = coordinate.get("hoja").getAsInt()-1;
            int rowInitTable = coordinate.get("initRow").getAsInt();
            int rowFinTable = coordinate.get("finRow").getAsInt();
            int rowSubtotal = coordinate.get("subtotalRow").getAsInt();
            int rowTotal = coordinate.get("totalRow").getAsInt();  
            boolean isIndex = coordinate.get("isIndex").getAsBoolean();
            
            XSSFSheet sheet = workbook.getSheetAt(hoja);
            formatName = sheet.getSheetName(); 
            Iterator<Row> rowIterator = sheet.iterator();  
            Row row;            
            while (rowIterator.hasNext()) {               
                row = rowIterator.next();                           
                JsonObject rowOut;                                                                 
                if (row.getRowNum() >= rowInitTable && row.getRowNum() <= rowFinTable) { //Data Table
                    rowOut = getRowIterator(formato, hoja, row, Validaciones.T_TABLE);                                        
                    if(rowOut.get("success").getAsBoolean()){        
                        jdata.add(rowOut.getAsJsonObject("data"));                        
                        sumCol1+= getRowAmount(rowOut, 1); 
                        sumCol2+= getRowAmount(rowOut, 2);                        
                        //System.out.println("Hoja Tabla:   "+hoja+  "| "+jdata);
                        validCustom_Date(hoja, formato, jdata);
                        //CUSTOM VALIDATION -- COLOCAR AQUI VALIDACIONES ADICIONALES
                    }                    
                }else if(row.getRowNum() == rowSubtotal && rowSubtotal>0){  //Data Subtotal
                    rowOut = getRowIterator(formato, hoja, row, Validaciones.T_SUBTOTAL);
                    if(rowOut.get("success").getAsBoolean()){
                        subTotal.add(rowOut.getAsJsonObject("data"));
                        validData_SubTotal(row, rowOut, sumCol1, sumCol2);         
                    }                      
                }else if(row.getRowNum() == rowTotal && rowTotal>0){  //Data Total
                    rowOut = getRowIterator(formato, hoja, row, Validaciones.T_TOTAL);
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

    //-----------------------------------------------------------CUSTOM VALIDATION INDEX 
    private JsonObject validFormat5(Formato formato, JsonArray jCordinates, JsonObject jSheetIndexData){        
        JsonObject jResponse = new JsonObject();
        JsonArray jResponseCoordinates = new JsonArray();        
        JsonArray sheetsActive = jSheetIndexData.get("data").getAsJsonArray();     
        boolean is5A = false, is5B = false, is5C = false;
        double TotalIndex5A = 0, TotalIndex5B = 0, TotalIndex5C = 0;
        double Total5A = 0, Total5B = 0, Total5C = 0;

        for (int i = 0; i < sheetsActive.size(); i++) {
            JsonObject sheetActive = sheetsActive.get(i).getAsJsonObject();                
            if(!is5A){
                is5A = sheetActive.get("5A") != null;
                if(is5A){TotalIndex5A = sheetActive.get("5A").getAsDouble();}
            }
            if(!is5B){
                is5B = sheetActive.get("5B") != null;
                if(is5B){TotalIndex5A = sheetActive.get("5B").getAsDouble();}
            }
            if(!is5C){
                is5C = sheetActive.get("5C") != null;
                if(is5C){TotalIndex5A = sheetActive.get("5C").getAsDouble();}
            }                 
        }               
        
        for (int i = 0; i < jCordinates.size(); i++) {
            JsonObject jCordinate = jCordinates.get(i).getAsJsonObject();
            int sheetPosition = jCordinate.get("hoja").getAsInt(); 
            if(sheetPosition == 1){
               //jFormatSheets.add(temp);
            }else if(sheetPosition == 2 && is5A){
                jResponseCoordinates.add(jCordinate);
                Total5A = getTotalBySheet(formato, jCordinate);
            }else if(sheetPosition == 3 && is5B){
                jResponseCoordinates.add(jCordinate);
                Total5B = getTotalBySheet(formato, jCordinate);
            }else if(sheetPosition == 4 && is5C){
                jResponseCoordinates.add(jCordinate);
                Total5C = getTotalBySheet(formato, jCordinate);
            }                
        }                 
        
        XSSFSheet sheet = workbook.getSheetAt(0);
        if(is5A){
            if(TotalIndex5A != Total5A){
                Row rowTotal = sheet.getRow(10);
                Cell cell = rowTotal.getCell(8); 
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }
        }
        if(is5B){
            if(TotalIndex5A != Total5A){
                Row rowTotal = sheet.getRow(11);
                Cell cell = rowTotal.getCell(8);   
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }        
        }
        if(is5C){
            if(TotalIndex5A != Total5A){
                Row rowTotal = sheet.getRow(12);
                Cell cell = rowTotal.getCell(8); 
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }        
        }
        
        jResponse.add("jCoordinates", jResponseCoordinates);  
        return jResponse; 
    }    
    private JsonObject validFormat6(Formato formato, JsonArray jCordinates, JsonObject jSheetIndexData){     
        
        JsonObject jResponse = new JsonObject();
        JsonArray jResponseCoordinates = new JsonArray();        
        JsonArray sheetsActive = jSheetIndexData.get("data").getAsJsonArray();     
        boolean is6A = false, is6B = false, is6C = false;
        double TotalIndex6A = 0, TotalIndex6B = 0, TotalIndex6C = 0;
        double Total6A = 0, Total6B = 0, Total6C = 0;

        for (int i = 0; i < sheetsActive.size(); i++) {
            JsonObject sheetActive = sheetsActive.get(i).getAsJsonObject();                
            if(!is6A){
                is6A = sheetActive.get("6A") != null;
                if(is6A){TotalIndex6A = sheetActive.get("6A").getAsDouble();}
            }
            if(!is6B){
                is6B = sheetActive.get("6B") != null;
                if(is6B){TotalIndex6A = sheetActive.get("6B").getAsDouble();}
            }
            if(!is6C){
                is6C = sheetActive.get("6C") != null;
                if(is6C){TotalIndex6A = sheetActive.get("6C").getAsDouble();}
            }                 
        }               
        
        for (int i = 0; i < jCordinates.size(); i++) {
            JsonObject jCordinate = jCordinates.get(i).getAsJsonObject();
            int sheetPosition = jCordinate.get("hoja").getAsInt(); 
            if(sheetPosition == 1){
               //jFormatSheets.add(temp);
            }else if(sheetPosition == 2 && is6A){
                jResponseCoordinates.add(jCordinate);
                Total6A = getTotalBySheet(formato, jCordinate);
            }else if(sheetPosition == 3 && is6B){
                jResponseCoordinates.add(jCordinate);
                Total6B = getTotalBySheet(formato, jCordinate);
            }else if(sheetPosition == 4 && is6C){
                jResponseCoordinates.add(jCordinate);
                Total6C = getTotalBySheet(formato, jCordinate);
            }                
        }                 
        
        XSSFSheet sheet = workbook.getSheetAt(0);
        if(is6A){
            if(TotalIndex6A != Total6A){
                Row rowTotal = sheet.getRow(8);
                Cell cell = rowTotal.getCell(7); 
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }
        }
        if(is6B){
            if(TotalIndex6A != Total6A){
                Row rowTotal = sheet.getRow(9);
                Cell cell = rowTotal.getCell(7);   
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }        
        }
        if(is6C){
            if(TotalIndex6A != Total6A){
                Row rowTotal = sheet.getRow(10);
                Cell cell = rowTotal.getCell(7); 
                cell.setCellStyle(styleCellObservation(workbook));
                cell.setCellComment(getComentario(cell, Mensajes.M_INVALID_AMOUNT_SHEET));                
                validData = false;                 
            }        
        }        
        jResponse.add("jCoordinates", jResponseCoordinates);  
        return jResponse;         

    }        
    private double getTotalBySheet(Formato formato, JsonObject jCordinate){
        double amount = 0;
        boolean success = true;
                  
        if(jCordinate.get("status").getAsBoolean()){
            int hoja = jCordinate.get("hoja").getAsInt();
            XSSFSheet sheet = workbook.getSheetAt(hoja-1);
            int row = jCordinate.get("totalRow").getAsInt();
            for (DetalleFormato parameter : formato.getDetalle()) {  
                if(parameter.getHojaExcel() == hoja){
                    if(parameter.getTipoDato() == Validaciones.T_TOTAL){                        
                        Row rowTotal = sheet.getRow(row);
                        Cell cell = rowTotal.getCell(parameter.getColumnaExcel()); 
                        String valueCell= getValueCell(cell);
                        String regex = "^\\d+(\\.\\d{1,2})?$";
                        if(valueCell.equalsIgnoreCase("")){
                            success = false;
                        }else{
                            if (!valueCell.matches(regex)) {
                                success = false;
                            }else{
                                amount = Double.parseDouble(valueCell);
                            }
                        }
                    }                        
                }
            }                                               
        }           
        return amount;
    }    
    
    //-----------------------------------------------------------CUSTOM VALIDATION  TABLE    
    private void validCustom_Date(int hoja, Formato formato, JsonArray jdata) {          
        XSSFSheet sheet = workbook.getSheetAt(hoja);
        Date date = new Date();
      
        int lastPosition = jdata.size()-1;
        JsonObject jRowData = jdata.get(lastPosition).getAsJsonObject();  
        boolean isDateValue = jRowData.get("fecAporte") != null;
        
        try {
            if(isDateValue){
                date = df.parse(jRowData.get("fecAporte").getAsString());               
                System.out.println("Hoja Tabla:   "+hoja+  "| Fecha:  "+date);            
            }
        } catch (Exception e) {
            System.out.println(e);
        }
 
 
        for (DetalleFormato parameter : formato.getDetalle()) { 
            if(parameter.getHojaExcel() == hoja){
            
            }
        
        }

        //System.out.println("Hoja Tabla:   "+hoja+  "| "+jdata);
    
    }
    
}
