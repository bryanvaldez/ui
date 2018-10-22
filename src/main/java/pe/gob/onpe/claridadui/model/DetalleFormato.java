/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.model;

/**
 *
 * @author bvaldez
 */
public class DetalleFormato {
    private int id;
    private int hojaExcel;
    private int columnaExcel;    
    private int filaExcel;      
    private String nombreColumna;
    private String validacion;
    private String mensajeValidacion;    
    private String comentario;    
    private int unico;
    private int obligatorio;
    private int orden;
    private int type;

    public DetalleFormato() {
    }

    public DetalleFormato(int hojaExcel, int columnaExcel, int filaExcel, String nombreColumna, String validacion, String mensajeValidacion, String comentario, int unico, int obligatorio, int orden, int type) {
        this.hojaExcel = hojaExcel;
        this.columnaExcel = columnaExcel;
        this.filaExcel = filaExcel;
        this.nombreColumna = nombreColumna;
        this.validacion = validacion;
        this.mensajeValidacion = mensajeValidacion;
        this.comentario = comentario;
        this.unico = unico;
        this.obligatorio = obligatorio;
        this.orden = orden;
        this.type = type;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHojaExcel() {
        return hojaExcel;
    }

    public void setHojaExcel(int hojaExcel) {
        this.hojaExcel = hojaExcel;
    }

    public int getColumnaExcel() {
        return columnaExcel;
    }

    public void setColumnaExcel(int columnaExcel) {
        this.columnaExcel = columnaExcel;
    }

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public String getValidacion() {
        return validacion;
    }

    public void setValidacion(String validacion) {
        this.validacion = validacion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getUnico() {
        return unico;
    }

    public void setUnico(int unico) {
        this.unico = unico;
    }

    public int getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(int obligatorio) {
        this.obligatorio = obligatorio;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getFilaExcel() {
        return filaExcel;
    }

    public void setFilaExcel(int filaExcel) {
        this.filaExcel = filaExcel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}
