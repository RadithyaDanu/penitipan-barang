package penitipan_barang;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFReportGenerator {
    
    private Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
    private Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
    private Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
    
    public boolean generateReport(List<Barang> listBarang, String filePath) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            
            document.open();
            
            // Title
            Paragraph title = new Paragraph("LAPORAN PENITIPAN BARANG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Paragraph date = new Paragraph("Tanggal: " + sdf.format(new Date()), normalFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);
            
            // Summary
            int totalBarang = listBarang.size();
            int barangDititipkan = 0;
            int barangDiambil = 0;
            
            for (Barang barang : listBarang) {
                if ("Dititipkan".equals(barang.getStatus())) {
                    barangDititipkan++;
                } else {
                    barangDiambil++;
                }
            }
            
            Paragraph summary = new Paragraph(
                "Total Barang: " + totalBarang + 
                " | Dititipkan: " + barangDititipkan + 
                " | Diambil: " + barangDiambil, boldFont);
            summary.setAlignment(Element.ALIGN_CENTER);
            summary.setSpacingAfter(20);
            document.add(summary);
            
            // Table
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            
            // Headers
            String[] headers = {"ID", "Nama Pemilik", "No. Telepon", "Jenis Barang", 
                               "Deskripsi", "Jaminan", "Tgl Masuk", "Tgl Diambil", "Status"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(70, 130, 180));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            
            // Data rows
            for (Barang barang : listBarang) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(barang.getId()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(barang.getNamaPemilik(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(barang.getNomorTelepon() != null ? barang.getNomorTelepon() : "-", normalFont)));
                table.addCell(new PdfPCell(new Phrase(barang.getJenisBarang(), normalFont)));
                
                String desc = barang.getDeskripsi();
                if (desc != null && desc.length() > 30) {
                    desc = desc.substring(0, 27) + "...";
                }
                table.addCell(new PdfPCell(new Phrase(desc != null ? desc : "-", normalFont)));
                table.addCell(new PdfPCell(new Phrase(barang.getJaminan() != null ? barang.getJaminan() : "-", normalFont)));
                
                String tglMasuk = barang.getTanggalMasuk() != null ? sdf.format(barang.getTanggalMasuk()) : "-";
                table.addCell(new PdfPCell(new Phrase(tglMasuk, normalFont)));
                
                String tglDiambil = barang.getTanggalDiambil() != null ? sdf.format(barang.getTanggalDiambil()) : "-";
                table.addCell(new PdfPCell(new Phrase(tglDiambil, normalFont)));
                
                PdfPCell statusCell = new PdfPCell(new Phrase(barang.getStatus(), boldFont));
                if ("Diambil".equals(barang.getStatus())) {
                    statusCell.setBackgroundColor(new BaseColor(255, 200, 200));
                } else {
                    statusCell.setBackgroundColor(new BaseColor(200, 255, 200));
                }
                table.addCell(statusCell);
            }
            
            document.add(table);
            document.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
