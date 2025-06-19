package penitipan_barang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BarangDAO {
    
    public boolean simpanBarang(Barang barang) {
        String query = "INSERT INTO barang (nama_pemilik, nomor_telepon, jenis_barang, deskripsi, jaminan, nomor_faktur) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, barang.getNamaPemilik());
            pstmt.setString(2, barang.getNomorTelepon());
            pstmt.setString(3, barang.getJenisBarang());
            pstmt.setString(4, barang.getDeskripsi());
            pstmt.setString(5, barang.getJaminan());
            pstmt.setString(6, barang.getNomorFaktur());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error menyimpan data: " + e.getMessage());
            return false;
        }
    }
    
    public List<Barang> getAllBarang() {
        List<Barang> listBarang = new ArrayList<>();
        String query = "SELECT * FROM barang ORDER BY tanggal_masuk DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Barang barang = new Barang();
                barang.setId(rs.getInt("id"));
                barang.setNamaPemilik(rs.getString("nama_pemilik"));
                barang.setNomorTelepon(rs.getString("nomor_telepon"));
                barang.setJenisBarang(rs.getString("jenis_barang"));
                barang.setDeskripsi(rs.getString("deskripsi"));
                barang.setJaminan(rs.getString("jaminan"));
                barang.setNomorFaktur(rs.getString("nomor_faktur"));
                barang.setTanggalMasuk(rs.getTimestamp("tanggal_masuk"));
                barang.setTanggalDiambil(rs.getTimestamp("tanggal_diambil"));
                barang.setStatus(rs.getString("status"));
                
                listBarang.add(barang);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error mengambil data: " + e.getMessage());
        }
        
        return listBarang;
    }
    
    public List<Barang> getBarangAktif() {
        List<Barang> listBarang = new ArrayList<>();
        String query = "SELECT * FROM barang WHERE status = 'Dititipkan' ORDER BY tanggal_masuk DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Barang barang = new Barang();
                barang.setId(rs.getInt("id"));
                barang.setNamaPemilik(rs.getString("nama_pemilik"));
                barang.setNomorTelepon(rs.getString("nomor_telepon"));
                barang.setJenisBarang(rs.getString("jenis_barang"));
                barang.setDeskripsi(rs.getString("deskripsi"));
                barang.setJaminan(rs.getString("jaminan"));
                barang.setNomorFaktur(rs.getString("nomor_faktur"));
                barang.setTanggalMasuk(rs.getTimestamp("tanggal_masuk"));
                barang.setTanggalDiambil(rs.getTimestamp("tanggal_diambil"));
                barang.setStatus(rs.getString("status"));
                
                listBarang.add(barang);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error mengambil data barang aktif: " + e.getMessage());
        }
        
        return listBarang;
    }
    
    public boolean updateStatus(int id, String status) {
        String query = "UPDATE barang SET status = ?, tanggal_diambil = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            if ("Diambil".equals(status)) {
                pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else {
                pstmt.setNull(2, Types.TIMESTAMP);
            }
            pstmt.setInt(3, id);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error update status: " + e.getMessage());
            return false;
        }
    }
}
