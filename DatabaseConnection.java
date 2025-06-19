package penitipan_barang;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/penitipan";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "RadithyaD!4";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi database gagal: " + e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS penitipan");
            stmt.close();
            conn.close();

            conn = getConnection();
            if (conn != null) {
                stmt = conn.createStatement();

                String createTableQuery = """
                    CREATE TABLE IF NOT EXISTS barang (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nama_pemilik VARCHAR(100) NOT NULL,
                        nomor_telepon VARCHAR(20),
                        jenis_barang VARCHAR(100) NOT NULL,
                        deskripsi TEXT,
                        jaminan VARCHAR(100),
                        nomor_faktur VARCHAR(50),
                        tanggal_masuk DATETIME DEFAULT CURRENT_TIMESTAMP,
                        tanggal_diambil DATETIME,
                        status ENUM('Dititipkan', 'Diambil') DEFAULT 'Dititipkan'
                    )
                """;

                stmt.executeUpdate(createTableQuery);
                stmt.close();
                conn.close();
                System.out.println("Database dan tabel berhasil diinisialisasi");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error inisialisasi database: " + e.getMessage());
        }
    }
}
