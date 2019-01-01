/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.claridadui.service.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import pe.gob.onpe.claridadui.model.DetalleInforme;
import pe.gob.onpe.claridadui.service.iface.IFormatoService;
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
    
    public final Float mLeft = 94f;   
    public final Float mRight = 94f;   
    public final Float mTop = 94f;    
    public final Float mBot = 94f;    
    
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
            createIntro(writer);
            countPage++;
                        
            setNewPage(document);
            createHeader(writer);
            createSubTitle(writer);
            createParagraph(writer);
            createFooter(writer);
            countPage++;
            
            setNewPage(document);
            createHeader(writer);
            createFooter(writer);             
            countPage++;
            
            
        } catch (Exception e) {
            
        }
        
        return document;
    }        
    private void setNewPage(Document document) { 
        document.newPage();        
        Rectangle size = new Rectangle(widthPage, heightPage); 
        if(countPage==0){
            document.setMargins(mLeft, mRight, 250f, mBot);            
        }else{
            document.setMargins(mTop, mRight, mBot, mLeft);            
        }

        document.setPageSize(size);
    }                
    private void createHeader(PdfWriter writer){
        try {
            Float x = mLeft;
            Float y = heightPage-25;
            Float width = widthPage-(mLeft+ mRight);
            Float height = 25f;
            
            Font f1 = new Font();
            f1.setSize(9);
            f1.setStyle(Font.ITALIC);            
            Paragraph p1 = new Paragraph("\"Decenio de la Igualdad de oportunidades para mujeres y hombres\" \n \"Año del Diálogo y la Reconciliación Nacional\"", f1);
            
            PdfPTable firstColumn = new PdfPTable(1);
            
            String path = PdfExport.class.getResource("/imagenes/onpe.png").toURI().getPath(); 

            firstColumn.setTotalWidth(new float[]{width});
            firstColumn.addCell(createLogoTitle(path, 50f));
            firstColumn.addCell(createTextCell(p1, height));  
            PdfContentByte canvas = writer.getDirectContentUnder();
            firstColumn.completeRow();
            firstColumn.writeSelectedRows(0, -1, x, y, canvas);               
        } catch (Exception e) {
        }           
    }            
    private void createFooter(PdfWriter writer){
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
    private void createIntro(PdfWriter writer){
        try {
            Float x = mLeft;
            Float y = heightPage-100f;
            Float width = widthPage-(mLeft+ mRight);
            Float height = 25f;
            Font f1 = new Font();
            f1.setSize(16);
            f1.setStyle(Font.BOLD);            
            Paragraph p1 = new Paragraph("INFORME 063-2018-PAS-JANRFP-SGTN-GSFP/ONPE", f1);
            
            PdfPTable firstColumn = new PdfPTable(1);
            firstColumn.setTotalWidth(new float[]{width});
            firstColumn.addCell(createTextCell(p1, height));  
            PdfContentByte canvas = writer.getDirectContentUnder();
            firstColumn.completeRow();
            firstColumn.writeSelectedRows(0, -1, x, y, canvas);  
            
            
            //Font f2 = new Font(FontFamily.ARIAL, 12, Font.BOLD);
            Font f2 = new Font();
            f2.setSize(12);
            f2.setStyle(Font.BOLD);
            Paragraph p2 = new Paragraph("INFORME SOBRE LAS ACTUACIONES PREVIAS AL INICIO DEL "
                    + "PROCEDIMIENTO ADMINISTRATIVO SANCIONADOR CONTRA EL MOVIMIENTO REGIONAL \"YO SOY CALLAO\" "
                    + "POR NO PRESENTAR LA INFORMACIÓN FINANCIERA ANUAL 2017 EN EL PLAZO ESTABLECIDO POR LEY", f2);            
            
            Font f3 = new Font();
            f3.setSize(9);
            f3.setStyle(Font.ITALIC);
            Paragraph p3 = new Paragraph("Jefatura del Área de Normativa y Regulación de Finanzas Partidarias \n"
                    + "Gerencia de Supervición de Fondos Partidario \n "
                    + "Octubre 2018", f3);            
            
            
            PdfPCell cell = new PdfPCell(p2);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setFixedHeight(100F);       
            cell.setBackgroundColor(BaseColor.GREEN);
            cell.setBorderWidth(0);     
            
            PdfPCell cell3 = new PdfPCell(p3);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell3.setFixedHeight(50F);       
            cell3.setBackgroundColor(BaseColor.GREEN);
            cell3.setBorderWidth(0);                   
            
            
            PdfPTable textBot = new PdfPTable(1);
            textBot.setTotalWidth(new float[]{width});                     
            textBot.addCell(cell);  
            textBot.addCell(cell3);
            textBot.completeRow();
            textBot.writeSelectedRows(0, -1, x, mBot+150F, canvas);       
            
            
            
            
            
        } catch (Exception e) {
        }           
    }   
    private void createSubTitle(PdfWriter writer){
        try {
            Float x = mLeft;
            Float y = heightPage-100f;
            Float width = widthPage-(mLeft+ mRight);
            Float height = 25f;    
            
            Font f1 = new Font();
            f1.setSize(14);
            f1.setStyle(Font.BOLD); 
            Paragraph p1 = new Paragraph("INFORME 063-2018-PAS-JANRFP-SGTN-GSFP/ONPE", f1);

            Font f2 = new Font();
            f2.setSize(14);
            f2.setStyle(Font.NORMAL); 
            Paragraph p2 = new Paragraph("INFORME SOBRE LAS ACTUACIONES PREVIAS AL INICIO DEL PROCEDIMIENTO ADMINISTRATIVO"
                    + " SANCIONADOR CONTRA EL MOVIMIENTO REGIONAL \"YO SOY CALLAO\" POR NO PRESENTAR LA INFORMACIÓN FINANCIERA ANUAL 2017"
                    + " EN EL PLAZO ESTABLECIDO POR LEY", f2);            
            
            PdfPCell cell2 = new PdfPCell(p2);        
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setFixedHeight(100f);       
            cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell2.setBorderWidth(1);          
            
            
            PdfPTable t1 = new PdfPTable(1);
            t1.setTotalWidth(new float[]{width});
            t1.addCell(createTextCell(p1, height));  
            t1.addCell(cell2);
            PdfContentByte canvas = writer.getDirectContentUnder();
            t1.completeRow();
            t1.writeSelectedRows(0, -1, x, y, canvas);               
        } catch (Exception e) {
        }       
    }
    
    private void createParagraph(PdfWriter writer){
        try {
            
            IFormatoService factory  = new FormatoService();            
            java.util.List<DetalleInforme> detalleInforme = factory.getDataInforme(1);               
            
            List subList1 = new List(List.ORDERED);
            subList1.setPreSymbol(String.valueOf(1) + ".");
            for (DetalleInforme item : detalleInforme) {
                subList1.add(new ListItem(item.getContenido()));
            }

            RomanList list = new RomanList();
            list.add(new ListItem("ANTECEDENTES"));
            list.add(subList1);
            list.add(new ListItem("BASE LEGAL"));
            list.add(new ListItem("ANÁLISIS"));    
            list.add(new ListItem("CONCLUSIONES"));
            list.add(new ListItem("RECOMENDACIÓN"));
            
            Float x = mLeft;
            Float y = heightPage-200f;
            Float width = widthPage-(mLeft+ mRight);
            Float height = 25f;               
            document.add(list);
            
//            PdfPCell cell1 = new PdfPCell();    
//            cell1.addElement(list);
//            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);    
//            cell1.setBackgroundColor(BaseColor.YELLOW);
//            cell1.setBorder(Rectangle.NO_BORDER);            
//            
//            PdfContentByte canvas = writer.getDirectContentUnder();            
//            
//            PdfPTable t1 = new PdfPTable(1);
//            t1.setTotalWidth(new float[]{width});
//            t1.addCell(cell1);
//            t1.completeRow();
//            t1.setSkipFirstHeader(true);
//            t1.writeSelectedRows(0, -1, x, y, canvas);  
             
        } catch (Exception e) {
        }       
    }    
          
    public PdfPCell createTextCell(Paragraph text, Float height) {
        PdfPCell cell = new PdfPCell(text);        
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(height);       
        cell.setBackgroundColor(BaseColor.CYAN);
        cell.setBorderWidth(0);
        return cell;
    }    
    public PdfPCell createLogoTitle(String path, Float height){
        PdfPCell cell = new PdfPCell();
        try {
            Image image = Image.getInstance(path);
            cell = new PdfPCell(image);
            cell.setFixedHeight(height);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            if(countPage == 0){
                cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
            }else{
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);                 
            }
            cell.setBackgroundColor(BaseColor.CYAN);
            cell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
        }   
        return cell; 
    }       
    
}
