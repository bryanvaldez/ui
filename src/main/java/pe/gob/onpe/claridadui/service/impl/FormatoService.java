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
            hoja1.addProperty("iniTabla", "");
            hoja1.addProperty("subtotal", "");
            hoja1.addProperty("total", "");
            hoja1.addProperty("columnaSuma", 8);
            
            hoja2.addProperty("hoja", 2);
            hoja2.addProperty("descripcion", "Anexo-5A");
            hoja2.addProperty("iniTabla", "Apellido Paterno");
            hoja2.addProperty("subtotal", "SUBTOTALES");
            hoja2.addProperty("total", "TOTAL INGRESOS"); 
            hoja2.addProperty("columnaSuma", 10);
            
            hoja3.addProperty("hoja", 3);
            hoja3.addProperty("descripcion", "Anexo-5B");
            hoja3.addProperty("iniTabla", "Fecha de la actividad");
            hoja3.addProperty("subtotal", "");
            hoja3.addProperty("total", "TOTAL INGRESOS");
            hoja3.addProperty("columnaSuma", 6);
            
            hoja4.addProperty("hoja", 4);
            hoja4.addProperty("descripcion", "Anexo-5C");
            hoja4.addProperty("iniTabla", "Fecha del ingreso");
            hoja4.addProperty("subtotal", "");
            hoja4.addProperty("total", "Total");   
            hoja4.addProperty("columnaSuma", 10);
            
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
        
        //PARAMETROS DE LECTURA - FORMATO 5
        response.add(new DetalleFormato(1, 8, 10, "5A", Validaciones.MONTO, Mensajes.INVALID_MONTO, Comentarios.INVALID_MONTO, 0, 1, 1, 0));
        response.add(new DetalleFormato(1, 8, 11, "5B", Validaciones.MONTO, Mensajes.INVALID_MONTO, Comentarios.INVALID_MONTO, 0, 1, 2, 0));
        response.add(new DetalleFormato(1, 8, 12, "5C", Validaciones.MONTO, Mensajes.INVALID_MONTO, Comentarios.INVALID_MONTO, 0, 1, 3, 0));
        response.add(new DetalleFormato(1, 8, 13, "total", Validaciones.MONTO, Mensajes.INVALID_MONTO, Comentarios.INVALID_MONTO, 0, 1, 4, 0));
        
        //PARAMETROS DE LECTURA ANEXO 5C        
        response.add(new DetalleFormato(4, 2, 10, "fecAporte", Validaciones.FECHA, Mensajes.INVALID_FECHA, Comentarios.INVALID_FECHA, 0, 1, 1, 0));
        response.add(new DetalleFormato(4, 3, 10, "codAporte", Validaciones.TABLA3, Mensajes.INVALID_TABLA3, Comentarios.INVALID_TABLA3, 0, 1, 2, 0));
        response.add(new DetalleFormato(4, 4, 10, "sustento", Validaciones.SUSTENTO, Mensajes.INVALID_SUSTENTO, Comentarios.INVALID_SUSTENTO, 0, 1, 3, 0));
        response.add(new DetalleFormato(4, 5, 10, "apPaterno", Validaciones.NOMBRES, Mensajes.INVALID_NOMBRES, Comentarios.INVALID_NOMBRES, 0, 1, 4, 0));
        response.add(new DetalleFormato(4, 6, 10, "apMaterno", Validaciones.NOMBRES, Mensajes.INVALID_NOMBRES, Comentarios.INVALID_NOMBRES, 0, 1, 5, 0));
        response.add(new DetalleFormato(4, 7, 10, "nombres", Validaciones.NOMBRES, Mensajes.INVALID_NOMBRES, Comentarios.INVALID_NOMBRES, 0, 1, 6, 0));
        response.add(new DetalleFormato(4, 8, 10, "documento", Validaciones.DOCUMENTO, Mensajes.INVALID_DOCUMENTO, Comentarios.INVALID_DOCUMENTO, 0, 1, 7, 0));
        response.add(new DetalleFormato(4, 9, 10, "procedencia", Validaciones.PROCEDENCIA, Mensajes.INVALID_PROCEDENCIA, Comentarios.INVALID_PROCEDENCIA, 0, 1, 8, 0));
        response.add(new DetalleFormato(4, 10, 10, "aporte", Validaciones.MONTO, Mensajes.INVALID_MONTO, Comentarios.INVALID_MONTO, 0, 1, 9, 0));
        
        return response;         
    }

}
