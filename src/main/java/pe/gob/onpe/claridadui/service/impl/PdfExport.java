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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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
    
    public final Float widthPage = PageSize.LETTER.getWidth();// 612
    public final Float heightPage = PageSize.LETTER.getHeight();// 792 
    
    public final Float mLeft = 85f;   
    public final Float mRight = 85f;   
    public final Float mTop = 92.13f;    
    public final Float mBot = 92.13f;

    public final Float spacing = 10f;
    public final Float indent = 30f;
    
    public int countPage = 0;    
                
    public PdfExport(String path, int candidato){
        this.path = path;
        this.candidato = candidato;
    }    

    @Override
    public Document export() {
        try {         
            FontFactory.register(PdfExport.class.getResource("/fuentes/arial.ttf").toURI().getPath(), "arial");
            
            setPage();                             
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            
            createHeader(writer);
            createFooter(writer);
                        
            document.open(); 

            
            createIntro(writer);            
           
            createSubTitle(writer);
            createParagraph();
        } catch (Exception e) {            
        }        
        return document;
    }        
    
    private void createHeader(PdfWriter writer){
        HeaderTable event = new HeaderTable();
        writer.setPageEvent(event);                     
    }            
    private void createFooter(PdfWriter writer){
        FooterTable event = new FooterTable();
        writer.setPageEvent(event);         
    }          
    private void setPage() { 
        Rectangle size = new Rectangle(widthPage, heightPage); 
        document.setPageSize(size);                 
        document.setMargins(mLeft, mRight, mTop, mBot);
    }                          
    
    private void createIntro(PdfWriter writer){
        try {
            
            IFormatoService factory  = new FormatoService();  
            java.util.List<DetalleInforme> data = factory.getDataInforme(11);              
                        
            Float x = mLeft;
            Float y = heightPage-100f;
            Float width = widthPage-(mLeft+ mRight);
            Float height = 25f;
            
            Font f1 = new Font(FontFamily.UNDEFINED, 16, Font.BOLD);          
            Paragraph p1 = new Paragraph(data.get(0).getContenido(), f1);
                                 
            PdfPTable firstColumn = new PdfPTable(1);
            firstColumn.setTotalWidth(new float[]{width});
            firstColumn.addCell(createTextCell(p1, height));   
            PdfContentByte canvas = writer.getDirectContentUnder();
            firstColumn.completeRow();
            firstColumn.writeSelectedRows(0, -1, x, y, canvas);  
                        
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

            document.newPage();            
            
        } catch (Exception e) {
        }           
    }   
    private void createSubTitle(PdfWriter writer){
        try {
            Float x = mLeft;
            Float y = heightPage-100f;
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
            t1.setWidthPercentage(100);
            t1.addCell(createTextCell(p1, height));  
            t1.addCell(cell2);
            t1.completeRow();
            document.add(t1);               
        } catch (Exception e) {
        }       
    }                          
    private void createParagraph(){
        try {
            
            
            
            
            
            
            int count = 0;            
            IFormatoService factory  = new FormatoService();  
            java.util.List<DetalleInforme> subtitles = factory.getDataInforme(count);             
            
            RomanList list = new RomanList();  
            list.setAutoindent(false);
            list.setSymbolIndent(indent);            
            for (DetalleInforme subtitle : subtitles) {
                count++;
                java.util.List<DetalleInforme> items = factory.getDataInforme(1);
                List subList = new List(List.ORDERED);
                subList.setAutoindent(false);
                subList.setIndentationLeft(-indent);
                subList.setSymbolIndent(indent); 
                subList.setPreSymbol(String.valueOf(1) + ".");               
                for (DetalleInforme item : items) {
                    Paragraph paragraph = new Paragraph(item.getContenido(), getFont(item));
                    paragraph.setSpacingBefore(spacing);
                    paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
                    ListItem listItem = new ListItem(paragraph);                   
                    subList.add(listItem);
                }                                
                ListItem item = new ListItem(new Paragraph(subtitle.getContenido(), getFont(subtitle)));            
                item.setSpacingAfter(spacing);  
                item.setSpacingBefore(spacing);                  
                list.add(item);
                list.add(subList);

            }

                       
            document.add(list);            
             
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
    public PdfPCell createLogoTitle(){
        PdfPCell cell = new PdfPCell();
        try {
            String path = PdfExport.class.getResource("/imagenes/logoBryan.png").toURI().getPath();
            Image image = Image.getInstance(path);
            cell = new PdfPCell(image);
            cell.setFixedHeight(53f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
            cell.setBorder(Rectangle.NO_BORDER);
        } catch (Exception e) {
        }   
        return cell; 
    }         
    private Font getFont(DetalleInforme param){
        Font font = new Font();
        font.setSize(param.getSize());
        if(param.isBold()){
            font.setStyle(Font.BOLD);            
        }
        return font;                    
    }    
    public class HeaderTable extends PdfPageEventHelper {

        protected float tableHeight;
        PdfPTable header = new PdfPTable(1);
        
        public HeaderTable() {            
            header.setTotalWidth(widthPage-(mLeft+ mRight));            
            header.addCell(createLogoTitle());                        
        }

        public void onEndPage(PdfWriter writer, Document document) {      
            header.writeSelectedRows(0, -1, mLeft, heightPage-30f, writer.getDirectContent());                
        }
    }        
    public class FooterTable extends PdfPageEventHelper {
        protected PdfPTable table;
        protected float tableHeight;
        
        public FooterTable() {
        }
        public float getTableHeight() {
            return tableHeight;
        }
        public void onEndPage(PdfWriter writer, Document document) {
            if(document.getPageNumber() == 1){
                          
            }else{
                Font f1 = new Font();
                f1.setSize(9);                
                Paragraph p = new Paragraph((document.getPageNumber()-1)+"", f1);
                PdfPTable table = new PdfPTable(1);
                table.setTotalWidth(widthPage-(mLeft+ mRight));
                
                PdfPCell cell = new PdfPCell(p);        
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(mBot);       
                cell.setBorderWidth(0);                
                
                table.addCell(cell);           
                table.writeSelectedRows(0, -1, mLeft, mBot, writer.getDirectContent());
            }
        }
    }        
    
}
