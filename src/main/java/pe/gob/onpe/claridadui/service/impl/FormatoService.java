/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import pe.gob.onpe.claridadui.Constants.Comentarios;
import pe.gob.onpe.claridadui.Constants.Mensajes;
import pe.gob.onpe.claridadui.Constants.Validaciones;
import pe.gob.onpe.claridadui.enums.FormatoEnum;
import pe.gob.onpe.claridadui.model.DetalleFormato;
import pe.gob.onpe.claridadui.model.Formato;
import pe.gob.onpe.claridadui.service.iface.IFormatoService;

/**
 *
 * @author bvaldez
 */
public class FormatoService implements IFormatoService {

    @Override
    public Formato getFormato(int type) {
        Formato formato = new Formato();
        if (validFormat(type)) {
            formato.setDescripcion(getDescription(type));
            formato.setDetalleHoja(getSheetDetail(type));
            formato.setCantidadHoja(getNumberSheet(type));
            formato.setTipoFormato(type);
            formato.setIndicacion(getIndication(type));
            formato.setRutaObservaciones(pathObs(type));
            formato.setDetalle(getDetalle(type));
        }
        return formato;
    }

    private boolean validFormat(int type) {
        if (type == FormatoEnum.FORMATO_5.getId()) {
            return true;
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            return true;
        }
        return false;
    }

    private String getDescription(int type) {
        String response = "";
        if (type == FormatoEnum.FORMATO_5.getId()) {
            response = "Cédula de Aportaciones del Financimiento Privado";

        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            response = "Cédula de Gastos del Financimiento Privado";
        }
        return new Gson().toJson(response);
    }

    private String getSheetDetail(int type) {
        JsonArray jResponse = new JsonArray();
        if (type == FormatoEnum.FORMATO_5.getId()) {
            JsonObject hoja1 = new JsonObject();
            JsonObject hoja2 = new JsonObject();
            JsonObject hoja3 = new JsonObject();
            JsonObject hoja4 = new JsonObject();

            hoja1.addProperty("hoja", 1);
            hoja1.addProperty("descripcion", "Formato-5");
            hoja2.addProperty("hoja", 2);
            hoja2.addProperty("descripcion", "Anexo-5A");
            hoja3.addProperty("hoja", 3);
            hoja3.addProperty("descripcion", "Anexo-5B");
            hoja4.addProperty("hoja", 4);
            hoja4.addProperty("descripcion", "Anexo-5C");

            jResponse.add(hoja1);
            jResponse.add(hoja2);
            jResponse.add(hoja3);
            jResponse.add(hoja4);

        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            JsonObject hoja1 = new JsonObject();
            JsonObject hoja2 = new JsonObject();
            JsonObject hoja3 = new JsonObject();
            JsonObject hoja4 = new JsonObject();

            hoja1.addProperty("hoja", 1);
            hoja1.addProperty("descripcion", "Formato-6");
            hoja2.addProperty("hoja", 2);
            hoja2.addProperty("descripcion", "Anexo-6A");
            hoja3.addProperty("hoja", 3);
            hoja3.addProperty("descripcion", "Anexo-6B");
            hoja4.addProperty("hoja", 4);
            hoja4.addProperty("descripcion", "Anexo-6C");

            jResponse.add(hoja1);
            jResponse.add(hoja2);
            jResponse.add(hoja3);
            jResponse.add(hoja4);
        }
        return new Gson().toJson(jResponse);
    }

    private String getIndication(int type) {
        JsonObject jResponse = new JsonObject();
        if (type == FormatoEnum.FORMATO_5.getId()) {
            jResponse.addProperty("plantilla", "Plantilla para la Cédula de Aportaciones del Financimiento Privado");
            jResponse.addProperty("carga", "Descargue la plantilla.");
            jResponse.addProperty("archivo", "F5_CEDULA_INGRESOS_FPD");
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            jResponse.addProperty("plantilla", "Plantilla para la Cédula de Gastos del Financimiento Privado");
            jResponse.addProperty("carga", "Descargue la plantilla.");
            jResponse.addProperty("archivo", "F6_CEDULA_GASTOS_FPD");
        }
        return new Gson().toJson(jResponse);

    }
    
    private int getNumberSheet(int type){
        if (type == FormatoEnum.FORMATO_5.getId()) {
            return 4;
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            return 4;
        }
        return 0;    
    }
    
    private String pathObs(int type){
        if (type == FormatoEnum.FORMATO_5.getId()) {
            return "D:\\CLARIDAD3\\OBSERVACIONES\\INGRESOS";
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            return "D:\\CLARIDAD3\\OBSERVACIONES\\GASTOS";
        }
        return "";     
    }
    
    private List<DetalleFormato> getDetalle(int type){
        List<DetalleFormato> response = new ArrayList<>();
        
        if (type == FormatoEnum.FORMATO_5.getId()) {
            response = getIncomeDetail(type);
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            
        }
        return response;        
    }
    
    private List<DetalleFormato> getIncomeDetail(int type){
        List<DetalleFormato> response;
        response = getIncomeDetailSheetOne(type);
        return response;    
    }
    
    private List<DetalleFormato> getIncomeDetailSheetOne(int type){
        List<DetalleFormato> response = new ArrayList<>();
        DetalleFormato detalle;   
        
        //Escritura  
//        detalle = new DetalleFormato();
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(1);
//        detalle.setFilaExcel(2);
//        detalle.setNombreColumna("Simbolo");
//        detalle.setType(1);
//        response.add(detalle); 
//        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(2);
//        detalle.setFilaExcel(5);
//        detalle.setNombreColumna("Período");
//        detalle.setType(1);
//        response.add(detalle);       
//        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(4);
//        detalle.setFilaExcel(19);
//        detalle.setNombreColumna("nombreContador");
//        detalle.setType(1);
//        response.add(detalle);    
//        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(4);
//        detalle.setFilaExcel(20);
//        detalle.setNombreColumna("dniContador");
//        detalle.setType(1);
//        response.add(detalle);         
//        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(4);
//        detalle.setFilaExcel(21);
//        detalle.setNombreColumna("matriculaContador");
//        detalle.setType(1);
//        response.add(detalle); 
//        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(4);
//        detalle.setFilaExcel(22);
//        detalle.setNombreColumna("colegioContador");
//        detalle.setType(1);
//        response.add(detalle);         
//                        
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(7);
//        detalle.setFilaExcel(19);
//        detalle.setNombreColumna("nombreTesorero");
//        detalle.setType(1);
//        response.add(detalle);        
//
//        detalle = new DetalleFormato();        
//        detalle.setHojaExcel(1);
//        detalle.setColumnaExcel(7);
//        detalle.setFilaExcel(20);
//        detalle.setNombreColumna("dniTesorero");
//        detalle.setType(1);
//        response.add(detalle);         
                                  
        //Lectura
        detalle = new DetalleFormato();        
        detalle.setId(2);
        detalle.setHojaExcel(1);
        detalle.setColumnaExcel(8);
        detalle.setFilaExcel(10);
        detalle.setNombreColumna("5A");
        detalle.setValidacion(Validaciones.V_AMOUNT);
        detalle.setMensajeValidacion(Mensajes.M_INVALID_AMOUNT);
        detalle.setComentario(Comentarios.C_INVALID_AMOUNT);
        detalle.setUnico(0);
        detalle.setObligatorio(1);
        detalle.setOrden(1);
        detalle.setType(0);
        response.add(detalle);
        
        detalle = new DetalleFormato();        
        detalle.setId(3);
        detalle.setHojaExcel(1);
        detalle.setColumnaExcel(8);
        detalle.setFilaExcel(11);
        detalle.setNombreColumna("5B");
        detalle.setValidacion(Validaciones.V_AMOUNT);
        detalle.setMensajeValidacion(Mensajes.M_INVALID_AMOUNT);
        detalle.setComentario(Comentarios.C_INVALID_AMOUNT);
        detalle.setUnico(0);
        detalle.setObligatorio(1);
        detalle.setOrden(2);
        detalle.setType(0);
        response.add(detalle);      

        detalle = new DetalleFormato();        
        detalle.setId(4);
        detalle.setHojaExcel(1);
        detalle.setColumnaExcel(8);
        detalle.setFilaExcel(12);
        detalle.setNombreColumna("5C");
        detalle.setValidacion(Validaciones.V_AMOUNT);
        detalle.setMensajeValidacion(Mensajes.M_INVALID_AMOUNT);
        detalle.setComentario(Comentarios.C_INVALID_AMOUNT);
        detalle.setUnico(0);
        detalle.setObligatorio(1);
        detalle.setOrden(3);
        detalle.setType(0);
        response.add(detalle);     
        
        detalle = new DetalleFormato();        
        detalle.setId(4);
        detalle.setHojaExcel(1);
        detalle.setColumnaExcel(8);
        detalle.setFilaExcel(13);
        detalle.setNombreColumna("TOTAL");
        detalle.setValidacion(Validaciones.V_AMOUNT);
        detalle.setMensajeValidacion(Mensajes.M_INVALID_AMOUNT);
        detalle.setComentario(Comentarios.C_INVALID_AMOUNT);
        detalle.setUnico(0);
        detalle.setObligatorio(1);
        detalle.setOrden(4);
        detalle.setType(0);
        response.add(detalle);          
                
        return response;         
    }

}
