package penitipan_barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class PenitipanBarangGUI extends JFrame {
    private JTextField txtNamaPemilik, txtNomorTelepon, txtJenisBarang, txtJaminan, txtNomorFaktur;
    private JTextArea txtDeskripsi;
    private JTable tableBarang, tableHistori;
    private DefaultTableModel tableModel, historiModel;
    private BarangDAO barangDAO;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    
    public PenitipanBarangGUI() {
        barangDAO = new BarangDAO();
        initializeComponents();
        loadData();
        updateStatusInfo();
    }
    
    private void initializeComponents() {
        setTitle("Sistem Penitipan Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(true);
        
        // Title Label
        JLabel titleLabel = new JLabel("SISTEM PENITIPAN BARANG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(false);
        
        // Status Info Label
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setOpaque(false);
        
        // Panel untuk menampung title dan status
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(statusLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Tab 1: Data Aktif
        JPanel mainPanel = createMainPanel();
        tabbedPane.addTab("📦 Data Barang Aktif", mainPanel);
        
        // Tab 2: Histori
        JPanel historiPanel = createHistoriPanel();
        tabbedPane.addTab("📋 Histori Lengkap", historiPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Left Panel (Form Input)
        JPanel leftPanel = createInputPanel();
        leftPanel.setPreferredSize(new Dimension(380, 0));
        
        // Right Panel (Tabel dan Tombol)
        JPanel rightPanel = createRightPanel();
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "📝 Form Input Barang Baru",
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Input fields
        panel.add(createFieldPanel("👤 Nama Pemilik *", txtNamaPemilik = new JTextField()));
        panel.add(createFieldPanel("📞 Nomor Telepon", txtNomorTelepon = new JTextField()));
        panel.add(createFieldPanel("📦 Jenis Barang *", txtJenisBarang = new JTextField()));
        
        // Deskripsi
        JLabel lblDeskripsi = new JLabel("📝 Deskripsi Barang");
        lblDeskripsi.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblDeskripsi);
        panel.add(Box.createVerticalStrut(3));
        
        txtDeskripsi = new JTextArea(4, 0);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(scrollDesc);
        
        panel.add(Box.createVerticalStrut(8));
        panel.add(createFieldPanel("🔒 Jaminan", txtJaminan = new JTextField()));
        panel.add(createFieldPanel("🧾 Nomor Faktur", txtNomorFaktur = new JTextField()));
        
        panel.add(Box.createVerticalStrut(15));
        
        // Buttons 
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        
        JButton btnSimpan = new JButton("💾 SIMPAN BARANG");
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 12));
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setContentAreaFilled(true); 
        btnSimpan.setOpaque(true);            
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnSimpan.addActionListener(e -> simpanBarang());
        
        JButton btnClear = new JButton("🗑️ CLEAR FORM");
        btnClear.setFont(new Font("Arial", Font.BOLD, 12));
        btnClear.setBackground(new Color(149, 165, 166));
        btnClear.setForeground(Color.WHITE);
        btnClear.setContentAreaFilled(true); 
        btnClear.setOpaque(true);           
        btnClear.setFocusPainted(false);
        btnClear.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnClear);
        panel.add(buttonPanel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setFont(new Font("Arial", Font.PLAIN, 11));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(3));
        fieldPanel.add(textField);
        fieldPanel.add(Box.createVerticalStrut(8));
        
        return fieldPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "📋 Data Barang yang Sedang Dititipkan",
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        String[] columnNames = {"ID", "Nama Pemilik", "No. Telepon", "Jenis Barang", 
                               "Deskripsi", "Jaminan", "Tanggal Masuk", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableBarang = new JTable(tableModel);
        tableBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBarang.setRowHeight(25);
        tableBarang.setFont(new Font("Arial", Font.PLAIN, 11));
        tableBarang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tableBarang.getTableHeader().setBackground(new Color(70, 130, 180));
        tableBarang.getTableHeader().setForeground(Color.WHITE);
        tableBarang.setSelectionBackground(new Color(184, 207, 229));
        
        // Custom renderer status
        tableBarang.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("Dititipkan".equals(value)) {
                        c.setBackground(new Color(200, 255, 200));
                        c.setForeground(new Color(0, 100, 0));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 11));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        // Action Buttons Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnAmbil = new JButton("✅ BARANG DIAMBIL");
        btnAmbil.setFont(new Font("Arial", Font.BOLD, 12));
        btnAmbil.setBackground(new Color(231, 76, 60));
        btnAmbil.setForeground(Color.WHITE);
        btnAmbil.setOpaque(true);
        btnAmbil.setFocusPainted(false);
        btnAmbil.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAmbil.addActionListener(e -> ambilBarang());
        
        JButton btnRefresh = new JButton("🔄 REFRESH DATA");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setOpaque(true);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRefresh.addActionListener(e -> {
            loadData();
            updateStatusInfo();
        });
        
        JButton btnDetail = new JButton("🔍 LIHAT DETAIL");
        btnDetail.setFont(new Font("Arial", Font.BOLD, 12));
        btnDetail.setBackground(new Color(155, 89, 182));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setOpaque(true);
        btnDetail.setFocusPainted(false);
        btnDetail.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnDetail.addActionListener(e -> lihatDetail());
        
        actionPanel.add(btnAmbil);
        actionPanel.add(btnRefresh);
        actionPanel.add(btnDetail);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHistoriPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        
        // Header2
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("📊 HISTORI SEMUA BARANG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JButton btnExportPDF = new JButton("📄 EXPORT PDF");
        btnExportPDF.setFont(new Font("Arial", Font.BOLD, 12));
        btnExportPDF.setBackground(new Color(46, 204, 113));
        btnExportPDF.setForeground(Color.WHITE);
        btnExportPDF.setOpaque(true);
        btnExportPDF.setFocusPainted(false);
        btnExportPDF.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnExportPDF.addActionListener(e -> exportToPDF());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnExportPDF, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Histori
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        
        String[] historiColumns = {"ID", "Nama Pemilik", "No. Telepon", "Jenis Barang", 
                                  "Deskripsi", "Jaminan", "Tanggal Masuk", "Tanggal Diambil", "Status"};
        historiModel = new DefaultTableModel(historiColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableHistori = new JTable(historiModel);
        tableHistori.setRowHeight(25);
        tableHistori.setFont(new Font("Arial", Font.PLAIN, 11));
        tableHistori.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tableHistori.getTableHeader().setBackground(new Color(70, 130, 180));
        tableHistori.getTableHeader().setForeground(Color.WHITE);
        tableHistori.setSelectionBackground(new Color(184, 207, 229));
        
        // Custom renderer status histori
        tableHistori.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("Dititipkan".equals(value)) {
                        c.setBackground(new Color(200, 255, 200));
                        c.setForeground(new Color(0, 100, 0));
                    } else if ("Diambil".equals(value)) {
                        c.setBackground(new Color(255, 200, 200));
                        c.setForeground(new Color(150, 0, 0));
                    }
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 11));
                return c;
            }
        });
        
        JScrollPane historiScroll = new JScrollPane(tableHistori);
        historiScroll.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(historiScroll, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateStatusInfo() {
        List<Barang> allBarang = barangDAO.getAllBarang();
        int totalBarang = allBarang.size();
        int barangDititipkan = 0;
        int barangDiambil = 0;
        
        for (Barang barang : allBarang) {
            if ("Dititipkan".equals(barang.getStatus())) {
                barangDititipkan++;
            } else {
                barangDiambil++;
            }
        }
        
        String statusText = String.format("Total: %d barang | Dititipkan: %d | Diambil: %d", 
                                        totalBarang, barangDititipkan, barangDiambil);
        statusLabel.setText(statusText);
    }
    
    private void simpanBarang() {
        // Validasi input
        if (txtNamaPemilik.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama Pemilik harus diisi!", 
                "Validasi Input", 
                JOptionPane.WARNING_MESSAGE);
            txtNamaPemilik.requestFocus();
            return;
        }
        
        if (txtJenisBarang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Jenis Barang harus diisi!", 
                "Validasi Input", 
                JOptionPane.WARNING_MESSAGE);
            txtJenisBarang.requestFocus();
            return;
        }
        
        // Membuat object barang
        Barang barang = new Barang(
            txtNamaPemilik.getText().trim(),
            txtNomorTelepon.getText().trim().isEmpty() ? null : txtNomorTelepon.getText().trim(),
            txtJenisBarang.getText().trim(),
            txtDeskripsi.getText().trim().isEmpty() ? null : txtDeskripsi.getText().trim(),
            txtJaminan.getText().trim().isEmpty() ? null : txtJaminan.getText().trim(),
            txtNomorFaktur.getText().trim().isEmpty() ? null : txtNomorFaktur.getText().trim()
        );
        
        // Simpan ke db
        if (barangDAO.simpanBarang(barang)) {
            JOptionPane.showMessageDialog(this, 
                "✅ Data barang berhasil disimpan!\n\nBarang milik " + barang.getNamaPemilik() + 
                " telah tercatat dalam sistem.", 
                "Berhasil Disimpan", 
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadData();
            updateStatusInfo();
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Gagal menyimpan data barang!\nSilakan coba lagi.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData() {
        // Load data barang aktif
        tableModel.setRowCount(0);
        List<Barang> listBarangAktif = barangDAO.getBarangAktif();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (Barang barang : listBarangAktif) {
            Object[] row = {
                barang.getId(),
                barang.getNamaPemilik(),
                barang.getNomorTelepon() != null ? barang.getNomorTelepon() : "-",
                barang.getJenisBarang(),
                barang.getDeskripsi() != null ? (barang.getDeskripsi().length() > 30 ? 
                    barang.getDeskripsi().substring(0, 27) + "..." : barang.getDeskripsi()) : "-",
                barang.getJaminan() != null ? barang.getJaminan() : "-",
                barang.getTanggalMasuk() != null ? sdf.format(barang.getTanggalMasuk()) : "-",
                barang.getStatus()
            };
            tableModel.addRow(row);
        }
        
        // Load histori semua barang
        historiModel.setRowCount(0);
        List<Barang> listSemua = barangDAO.getAllBarang();
        
        for (Barang barang : listSemua) {
            Object[] row = {
                barang.getId(),
                barang.getNamaPemilik(),
                barang.getNomorTelepon() != null ? barang.getNomorTelepon() : "-",
                barang.getJenisBarang(),
                barang.getDeskripsi() != null ? (barang.getDeskripsi().length() > 25 ? 
                    barang.getDeskripsi().substring(0, 22) + "..." : barang.getDeskripsi()) : "-",
                barang.getJaminan() != null ? barang.getJaminan() : "-",
                barang.getTanggalMasuk() != null ? sdf.format(barang.getTanggalMasuk()) : "-",
                barang.getTanggalDiambil() != null ? sdf.format(barang.getTanggalDiambil()) : "-",
                barang.getStatus()
            };
            historiModel.addRow(row);
        }
    }
    
    private void ambilBarang() {
        int selectedRow = tableBarang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Pilih barang yang akan diambil dari tabel terlebih dahulu!", 
                "Tidak Ada Barang Dipilih", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String namaPemilik = (String) tableModel.getValueAt(selectedRow, 1);
        String jenisBarang = (String) tableModel.getValueAt(selectedRow, 3);
        
        // konfirmasi
        String message = String.format(
            "Apakah barang berikut sudah diambil oleh pemiliknya?\n\n" +
            "📦 Jenis Barang: %s\n" +
            "👤 Nama Pemilik: %s\n" +
            "🆔 ID Barang: %d\n\n" +
            "Status akan berubah menjadi 'DIAMBIL' dan tidak dapat dikembalikan.",
            jenisBarang, namaPemilik, id
        );
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            message, 
            "Konfirmasi Pengambilan Barang", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (barangDAO.updateStatus(id, "Diambil")) {
                JOptionPane.showMessageDialog(this, 
                    String.format("✅ Barang berhasil diambil!\n\n" +
                                "📦 %s milik %s\n" +
                                "🕐 Tanggal pengambilan: %s\n" +
                                "📋 Status berubah menjadi: DIAMBIL", 
                                jenisBarang, namaPemilik, 
                                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())), 
                    "Pengambilan Berhasil", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
                updateStatusInfo();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal mengupdate status barang!\nSilakan coba lagi.", 
                    "Error Update Status", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void lihatDetail() {
        int selectedRow = tableBarang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Pilih barang yang ingin dilihat detailnya!", 
                "Tidak Ada Barang Dipilih", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        // Ambil data detail dari db
        List<Barang> allBarang = barangDAO.getAllBarang();
        Barang selectedBarang = null;
        
        for (Barang barang : allBarang) {
            if (barang.getId() == id) {
                selectedBarang = barang;
                break;
            }
        }
        
        if (selectedBarang != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String detail = String.format(
                "📋 DETAIL BARANG\n\n" +
                "🆔 ID Barang: %d\n" +
                "👤 Nama Pemilik: %s\n" +
                "📞 No. Telepon: %s\n" +
                "📦 Jenis Barang: %s\n" +
                "📝 Deskripsi: %s\n" +
                "🔒 Jaminan: %s\n" +
                "🧾 No. Faktur: %s\n" +
                "📅 Tanggal Masuk: %s\n" +
                "📅 Tanggal Diambil: %s\n" +
                "📊 Status: %s",
                selectedBarang.getId(),
                selectedBarang.getNamaPemilik(),
                selectedBarang.getNomorTelepon() != null ? selectedBarang.getNomorTelepon() : "Tidak ada",
                selectedBarang.getJenisBarang(),
                selectedBarang.getDeskripsi() != null ? selectedBarang.getDeskripsi() : "Tidak ada deskripsi",
                selectedBarang.getJaminan() != null ? selectedBarang.getJaminan() : "Tidak ada",
                selectedBarang.getNomorFaktur() != null ? selectedBarang.getNomorFaktur() : "Tidak ada",
                selectedBarang.getTanggalMasuk() != null ? sdf.format(selectedBarang.getTanggalMasuk()) : "-",
                selectedBarang.getTanggalDiambil() != null ? sdf.format(selectedBarang.getTanggalDiambil()) : "Belum diambil",
                selectedBarang.getStatus()
            );
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane,"Detail Barang", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtNamaPemilik.setText("");
        txtNomorTelepon.setText("");
        txtJenisBarang.setText("");
        txtDeskripsi.setText("");
        txtJaminan.setText("");
        txtNomorFaktur.setText("");
        txtNamaPemilik.requestFocus();
    }
    
    private void exportToPDF() {
    List<Barang> semuaBarang = barangDAO.getAllBarang();
    PDFReportGenerator pdfGenerator = new PDFReportGenerator();

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Laporan PDF");
    fileChooser.setSelectedFile(new java.io.File("laporan_penitipan.pdf"));

    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();

        // Tambahkan ekstensi .pdf jika belum ada
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        boolean success = pdfGenerator.generateReport(semuaBarang, filePath);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "✅ PDF berhasil disimpan di:\n" + filePath,
                "Export Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Gagal membuat PDF.\nPeriksa apakah file sedang dibuka atau ada masalah akses.",
                "Export Gagal",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
        e.printStackTrace();
        }

            
            new PenitipanBarangGUI().setVisible(true);
        });
    }
}
