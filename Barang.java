package penitipan_barang;

import java.util.Date;

public class Barang {
    private int id;
    private String namaPemilik;
    private String nomorTelepon;
    private String jenisBarang;
    private String deskripsi;
    private String jaminan;
    private String nomorFaktur;
    private Date tanggalMasuk;
    private Date tanggalDiambil;
    private String status;

    // Constructor
    public Barang() {}

    public Barang(String namaPemilik, String nomorTelepon, String jenisBarang, 
                  String deskripsi, String jaminan, String nomorFaktur) {
        this.namaPemilik = namaPemilik;
        this.nomorTelepon = nomorTelepon;
        this.jenisBarang = jenisBarang;
        this.deskripsi = deskripsi;
        this.jaminan = jaminan;
        this.nomorFaktur = nomorFaktur;
        this.status = "Dititipkan";
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaPemilik() { return namaPemilik; }
    public void setNamaPemilik(String namaPemilik) { this.namaPemilik = namaPemilik; }

    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

    public String getJenisBarang() { return jenisBarang; }
    public void setJenisBarang(String jenisBarang) { this.jenisBarang = jenisBarang; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getJaminan() { return jaminan; }
    public void setJaminan(String jaminan) { this.jaminan = jaminan; }

    public String getNomorFaktur() { return nomorFaktur; }
    public void setNomorFaktur(String nomorFaktur) { this.nomorFaktur = nomorFaktur; }

    public Date getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(Date tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public Date getTanggalDiambil() { return tanggalDiambil; }
    public void setTanggalDiambil(Date tanggalDiambil) { this.tanggalDiambil = tanggalDiambil; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
