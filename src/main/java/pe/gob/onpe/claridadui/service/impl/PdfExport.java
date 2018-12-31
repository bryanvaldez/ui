/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import pe.gob.onpe.claridadui.service.iface.IPdfExportService;

/**
 *
 * @author Bryan Luis Valdez Jara <ibryan.valdez@gmail.com>
 */
public class PdfExport implements IPdfExportService{
    
    public final Document document = new Document();
    public final String path;
    public final int candidato;
    
    public final Float widthPage = PageSize.A4.getWidth();// 595
    public final Float heightPage = PageSize.A4.getHeight();//842 
    
    public int countPage = 0;    
            
    public PdfExport(String path, int candidato){
        this.path = path;
        this.candidato = candidato;
    }    

    @Override
    public Document export() {
        try {                            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            
            setNewPage(document);  
            createHeader(writer);  
     
                        
            setNewPage(document);
            createHeader(writer);
            createFooter(writer);     
            
            setNewPage(document);
            createHeader(writer);
            createFooter(writer);             
            
        } catch (Exception e) {
            
        }
        
        return document;
    }
    
    
    public void setNewPage(Document document) { 
        countPage++;
        document.newPage();        
        Rectangle size = new Rectangle(widthPage, heightPage);          
        document.setMargins(94f, 113f, 94f, 113f);
        document.setPageSize(size);
    }    
            
    private void createHeader(PdfWriter writer){
        try {
            Float x = 113f;
            Float y = heightPage;
            Float width = widthPage-(113f*2);
            Float height = 25f;
            Paragraph p1 = new Paragraph("\"Decenio de la Igualdad de oportunidades para mujeres y hombres\"");
            Paragraph p2 = new Paragraph("\"Año del Diálogo y la Reconciliación Nacional\"");
            
            PdfPTable firstColumn = new PdfPTable(1);
            
            String path = PdfExport.class.getResource("/imagenes/onpe.png").toURI().getPath(); 

            firstColumn.setTotalWidth(new float[]{width});
            firstColumn.addCell(createImgCell(path, 50f));
            firstColumn.addCell(createTextCell(p1, height));  
            firstColumn.addCell(createTextCell(p2, height));
            PdfContentByte canvas = writer.getDirectContentUnder();
            firstColumn.completeRow();
            firstColumn.writeSelectedRows(0, -1, x, y, canvas);               
        } catch (Exception e) {
        }           
    }        
    
    public void createFooter(PdfWriter writer){
        try {
            Float x = 113f;
            Float y = 25f;
            Float width = widthPage-(113f*2);
            Float height = 25f;
            String page = ""+countPage;
            Paragraph p = new Paragraph(page);
            PdfPTable firstColumn = new PdfPTable(1);
            firstColumn.setTotalWidth(new float[]{width});
            firstColumn.addCell(createTextCell(p, height));        
            PdfContentByte canvas = writer.getDirectContentUnder();
            firstColumn.completeRow();
            firstColumn.writeSelectedRows(0, -1, x, y, canvas);              
        } catch (Exception e) {
        }
      
    }    
    
    public PdfPCell createTextCell(Paragraph text, Float height) {
        PdfPCell cell = new PdfPCell(text);        
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);       
        cell.setBackgroundColor(BaseColor.YELLOW);
        cell.setBorderWidth(0);
        return cell;
    }
    public PdfPCell createImgCell(String path, Float height){
        PdfPCell cell = new PdfPCell();
        try {
            Image image = Image.getInstance(path);
            cell = new PdfPCell(image);
            cell.setFixedHeight(height);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
            cell.setBackgroundColor(BaseColor.CYAN);
            cell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
        }   
        return cell; 
    }
    
    
    
    
}
