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
    
    private String font;
    private int size;
    private boolean bold;   
    private String contenido;

    public DetalleInforme() {
    }

    public DetalleInforme(String font, int size, boolean bold, String contenido) {
        this.font = font;
        this.size = size;
        this.bold = bold;
        this.contenido = contenido;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


}
