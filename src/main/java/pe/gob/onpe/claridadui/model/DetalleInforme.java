/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.model;

/**
 *
 * @author Bryan Valdez Jara <iBryan.valdez@gmail.com>
 */
public class DetalleInforme {
    
    private int id;
    private int tipo;   
    private int orden; 
    private String contenido;

    public DetalleInforme() {
    }

    public DetalleInforme(int id, int tipo, int orden, String contenido) {
        this.id = id;
        this.tipo = tipo;
        this.orden = orden;
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    
    
}
