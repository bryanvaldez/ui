/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.Constants;

/**
 *
 * @author bvaldez
 */
public class Validaciones {
    
    public static final int FORMAT_READER = 0; 
    public static final int FORMAT_WRITE = 1; 
    
    public static final int FORMAT_REQUIRED = 1;  
    
    public static final String MONTO = "^\\d+(\\.\\d{1,2})?$";     
    public static final String FECHA = "^(\\d{2}\\/([0][1-9]|[1][0-2])\\/(199[6-9]|2[0-9]{3}))$";     
    public static final String TABLA3 = "^(1|2|3|4|5|1.0|2.0|3.0|4.0|5.0)$";     
    public static final String SUSTENTO = "^(.{1,150})$"; 
    public static final String NOMBRES = "^(.{1,150})$"; 
    public static final String DOCUMENTO = "^(.{1,150})$"; 
    public static final String PROCEDENCIA = "^(.{1,350})$"; 
    
    

    
}
