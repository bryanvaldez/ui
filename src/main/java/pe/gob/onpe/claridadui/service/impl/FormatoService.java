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
            formato.setId(type);
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
            hoja1.addProperty("iniTabla", "TOTAL S/");
            hoja1.addProperty("subtotal", "");
            hoja1.addProperty("total", "TOTAL INGRESOS DE CAMPAÑA");
            
            hoja2.addProperty("hoja", 2);
            hoja2.addProperty("descripcion", "Anexo-5A");
            hoja2.addProperty("iniTabla", "Apellido Paterno");
            hoja2.addProperty("subtotal", "SUBTOTALES");
            hoja2.addProperty("total", "TOTAL INGRESOS"); 
            
            hoja3.addProperty("hoja", 3);
            hoja3.addProperty("descripcion", "Anexo-5B");
            hoja3.addProperty("iniTabla", "Fecha de la actividad");
            hoja3.addProperty("subtotal", "");
            hoja3.addProperty("total", "TOTAL INGRESOS");
            
            hoja4.addProperty("hoja", 4);
            hoja4.addProperty("descripcion", "Anexo-5C");
            hoja4.addProperty("iniTabla", "Fecha del ingreso");
            hoja4.addProperty("subtotal", "");
            hoja4.addProperty("total", "Total");   
            
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
            hoja1.addProperty("iniTabla", "TOTAL S/");
            hoja1.addProperty("subtotal", "");
            hoja1.addProperty("total", "TOTAL GASTOS DE CAMPAÑA");            
            
            hoja2.addProperty("hoja", 2);
            hoja2.addProperty("descripcion", "Anexo-6A");
            hoja2.addProperty("iniTabla", "Monto  S/.");
            hoja2.addProperty("subtotal", "");
            hoja2.addProperty("total", "TOTAL");            
            
            hoja3.addProperty("hoja", 3);
            hoja3.addProperty("descripcion", "Anexo-6B");
            hoja3.addProperty("iniTabla", "Monto S/");
            hoja3.addProperty("subtotal", "");
            hoja3.addProperty("total", "TOTAL");            
            
            hoja4.addProperty("hoja", 4);
            hoja4.addProperty("descripcion", "Anexo-6C");
            hoja4.addProperty("iniTabla", "Monto S/");
            hoja4.addProperty("subtotal", "");
            hoja4.addProperty("total", "TOTAL");            

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
            response = getIncomeDetail();
        } else if (type == FormatoEnum.FORMATO_6.getId()) {
            response = getExpensesDetail();
        }
        return response;        
    }

    private List<DetalleFormato> getIncomeDetail(){
        List<DetalleFormato> p = new ArrayList<>();
        //PARAMETROS DE LECTURA - FORMATO 5
        p.add(new DetalleFormato(1, 8, 10, "5A", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 1, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 8, 11, "5B", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 2, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 8, 12, "5C", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 3, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 8, 13, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 4, 0, Validaciones.T_TOTAL, false));
          
        //PARAMETROS DE LECTURA ANEXO 5A 
        p.add(new DetalleFormato(2, 2, 0, "fecAporte", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 3, 0, "numComprobante", Validaciones.COMPROB, Mensajes.I_COMPROBANTE, Comentarios.I_COMPROBANTE, 0, 1, 2, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 4, 0, "apPaterno", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 5, 0, "apMaterno", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 4, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 6, 0, "nombres", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 7, 0, "documento", Validaciones.DOCUMENTO, Mensajes.I_DOCUMENTO, Comentarios.I_DOCUMENTO, 0, 1, 6, 0, Validaciones.T_TABLE, false));                
        p.add(new DetalleFormato(2, 8, 0, "direccion", Validaciones.DIRECCION, Mensajes.I_DIRECCION, Comentarios.I_DIRECCION, 0, 1, 7, 0, Validaciones.T_TABLE, false));        
        p.add(new DetalleFormato(2, 9, 0, "codAporte", Validaciones.TABLA1, Mensajes.I_TABLA1, Comentarios.I_TABLA1, 0, 1, 8, 0, Validaciones.T_TABLE, false));         
        p.add(new DetalleFormato(2, 10, 0, "montoEfectivo", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 9, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(2, 11, 0, "montoEspecie", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 10, 0, Validaciones.T_TABLE, true));        
        p.add(new DetalleFormato(2, 12, 0, "detalle", Validaciones.DETALLE, Mensajes.I_DETALLE, Comentarios.I_DETALLE, 0, 1, 11, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 10, 0, "subTotalEfectivo", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 12, 0, Validaciones.T_SUBTOTAL, false)); 
        p.add(new DetalleFormato(2, 11, 0, "subTotalEspecie", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 13, 0, Validaciones.T_SUBTOTAL, false));         
        p.add(new DetalleFormato(2, 10, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 14, 0, Validaciones.T_TOTAL, false)); 
        
        //PARAMETROS DE LECTURA ANEXO 5B        
        p.add(new DetalleFormato(3, 2, 0, "fecActividad", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 3, 0, "codActividad", Validaciones.TABLA2, Mensajes.I_TABLA2, Comentarios.I_TABLA2, 0, 1, 2, 0, Validaciones.T_TABLE, false)); 
        p.add(new DetalleFormato(3, 4, 0, "lugarActividad", Validaciones.LUGAR, Mensajes.I_LUGAR, Comentarios.I_LUGAR, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 6, 0, "importe", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 4, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(3, 7, 0, "detalle", Validaciones.DETALLE, Mensajes.I_DETALLE, Comentarios.I_DETALLE, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 6, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 6, 0, Validaciones.T_TOTAL, false));          
        
        //PARAMETROS DE LECTURA ANEXO 5C        
        p.add(new DetalleFormato(4, 2, 0, "fecAporte", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 3, 0, "codAporte", Validaciones.TABLA3, Mensajes.I_TABLA3, Comentarios.I_TABLA3, 0, 1, 2, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 4, 0, "sustento", Validaciones.SUSTENTO, Mensajes.I_SUSTENTO, Comentarios.I_SUSTENTO, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 5, 0, "apPaterno", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 4, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 6, 0, "apMaterno", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 7, 0, "nombres", Validaciones.NOMBRES, Mensajes.I_NOMBRES, Comentarios.I_NOMBRES, 0, 1, 6, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 8, 0, "documento", Validaciones.DOCUMENTO, Mensajes.I_DOCUMENTO, Comentarios.I_DOCUMENTO, 0, 1, 7, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 9, 0, "procedencia", Validaciones.PROCEDENCIA, Mensajes.I_PROCEDENCIA, Comentarios.I_PROCEDENCIA, 0, 1, 8, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 10, 0, "aporte", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 9, 0, Validaciones.T_TABLE, true));        
        p.add(new DetalleFormato(4, 10, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 10, 0, Validaciones.T_TOTAL, false));          
        
        return p;         
    }
    
    private List<DetalleFormato> getExpensesDetail(){
        List<DetalleFormato> p = new ArrayList<>();        
        //PARAMETROS DE LECTURA - FORMATO 6
        p.add(new DetalleFormato(1, 7, 8, "6A", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 1, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 7, 9, "6B", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 2, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 7, 10, "6C", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 3, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(1, 7, 11, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 4, 0, Validaciones.T_TOTAL, false));   
        
        //PARAMETROS DE LECTURA ANEXO 6A
        p.add(new DetalleFormato(2, 2, 0, "fecComprobante", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 3, 0, "tipoMedio", Validaciones.MEDIO, Mensajes.I_MEDIO, Comentarios.I_MEDIO, 0, 1, 2, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 4, 0, "razonSocial", Validaciones.RAZON, Mensajes.I_RAZON, Comentarios.I_RAZON, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 5, 0, "ruc", Validaciones.RUC, Mensajes.I_RUC, Comentarios.I_RUC, 0, 1, 4, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 6, 0, "tipoComprobante", Validaciones.TIPCOMPROB, Mensajes.I_TIPCOMPROB, Comentarios.I_TIPCOMPROB, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 7, 0, "numComprobante", Validaciones.NUMCOMPROB, Mensajes.I_NUMCOMPROB, Comentarios.I_NUMCOMPROB, 0, 1, 6, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 8, 0, "tipoPago", Validaciones.TIPOPAGO, Mensajes.I_TIPOPAGO, Comentarios.I_TIPOPAGO, 0, 1, 7, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 9, 0, "monto", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 8, 0, Validaciones.T_TABLE, true));
        p.add(new DetalleFormato(2, 10, 0, "descripcion", Validaciones.ESPECIF, Mensajes.I_ESPECIFIC, Comentarios.I_ESPECIFIC, 0, 1, 9, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(2, 9, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 10, 0, Validaciones.T_TOTAL, false));
        
        //PARAMETROS DE LECTURA ANEXO 6B
        p.add(new DetalleFormato(3, 2, 0, "fecComprobante", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 3, 0, "tipoGasto", Validaciones.TIPOGASTO, Mensajes.I_TIPOGASTO, Comentarios.I_TIPOGASTO, 0, 1, 2, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 4, 0, "razonSocial", Validaciones.RAZON, Mensajes.I_RAZON, Comentarios.I_RAZON, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 5, 0, "tipoDocumento", Validaciones.TIPODOC, Mensajes.I_TIPODOC, Comentarios.I_TIPODOC, 0, 1, 4, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 6, 0, "documento", Validaciones.DOCUMENTO, Mensajes.I_DOCUMENTO, Comentarios.I_DOCUMENTO, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 7, 0, "tipoComprobante", Validaciones.TIPCOMPROB, Mensajes.I_TIPCOMPROB, Comentarios.I_TIPCOMPROB, 0, 1, 6, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 8, 0, "numComprobante", Validaciones.NUMCOMPROB, Mensajes.I_NUMCOMPROB, Comentarios.I_NUMCOMPROB, 0, 1, 7, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 9, 0, "tipoPago", Validaciones.TIPOPAGO, Mensajes.I_TIPOPAGO, Comentarios.I_TIPOPAGO, 0, 1, 8, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 10, 0, "monto", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 9, 0, Validaciones.T_TABLE, true));        
        p.add(new DetalleFormato(3, 11, 0, "descripcion", Validaciones.ESPECIF, Mensajes.I_ESPECIFIC, Comentarios.I_ESPECIFIC, 0, 1, 10, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(3, 10, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 11, 0, Validaciones.T_TOTAL, false));
                
        //PARAMETROS DE LECTURA ANEXO 6B
        p.add(new DetalleFormato(4, 2, 0, "fecGasto", Validaciones.FECHA, Mensajes.I_FECHA, Comentarios.I_FECHA, 0, 1, 1, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 3, 0, "concepto", Validaciones.ESPECIF, Mensajes.I_ESPECIFIC, Comentarios.I_ESPECIFIC, 0, 1, 2, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 4, 0, "tipoPago", Validaciones.TIPOPAGO, Mensajes.I_TIPOPAGO, Comentarios.I_TIPOPAGO, 0, 1, 3, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 5, 0, "monto", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 4, 0, Validaciones.T_TABLE, true));         
        p.add(new DetalleFormato(4, 6, 0, "razonSocial", Validaciones.RAZON, Mensajes.I_RAZON, Comentarios.I_RAZON, 0, 1, 5, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 7, 0, "tipoDocumento", Validaciones.TIPODOC, Mensajes.I_TIPODOC, Comentarios.I_TIPODOC, 0, 1, 6, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 8, 0, "documento", Validaciones.DOCUMENTO, Mensajes.I_DOCUMENTO, Comentarios.I_DOCUMENTO, 0, 1, 7, 0, Validaciones.T_TABLE, false));   
        p.add(new DetalleFormato(4, 9, 0, "tipoComprobante", Validaciones.TIPCOMPROB, Mensajes.I_TIPCOMPROB, Comentarios.I_TIPCOMPROB, 0, 1, 8, 0, Validaciones.T_TABLE, false));
        p.add(new DetalleFormato(4, 10, 0, "numComprobante", Validaciones.NUMCOMPROB, Mensajes.I_NUMCOMPROB, Comentarios.I_NUMCOMPROB, 0, 1, 9, 0, Validaciones.T_TABLE, false));        
        p.add(new DetalleFormato(4, 5, 0, "total", Validaciones.MONTO, Mensajes.I_MONTO, Comentarios.I_MONTO, 0, 1, 10, 0, Validaciones.T_TOTAL, false));
        
        return p;
    }

}
