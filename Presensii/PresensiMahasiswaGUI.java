import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PresensiMahasiswaGUI extends JFrame {
    private JComboBox<String> comboMataKuliah;
    private JTextField txtCariNIM;
    private JLabel lblNama, lblStatus, lblSisaJatah;
    private JRadioButton rbHadir, rbTidakHadir, rbIzin, rbAlpha;
    private JButton btnSimpan, btnDaftar;
    private JTable tabelPresensi;
    private DefaultTableModel model;
    private boolean isLoggedIn = false;
    private String currentNIM = "";
    private String currentNama = "";
    private Timer viewTimer;

    // 🔌 Koneksi Database
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE"; // Ganti SESUAI SERVICE KAMU
    private static final String JDBC_USER = "akademik";                           // Ganti SESUAI USER KAMU
    private static final String JDBC_PASSWORD = "akademik";                       // Ganti SESUAI PASSWORD KAMU

    // Warna modern
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color PANEL_BG = new Color(248, 249, 250);

    public PresensiMahasiswaGUI() {
        setTitle("Presensi Mahasiswa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 15));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));

        JLabel lblJudul = new JLabel("Presensi Mahasiswa");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblJudul.setForeground(Color.WHITE);
        headerPanel.add(lblJudul);

        btnDaftar = new JButton("Daftar");
        styleButton(btnDaftar, Color.BLACK, PRIMARY_COLOR.darker());
        btnDaftar.addActionListener(e -> tampilkanFormDaftar());
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(btnDaftar);

        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblMK = new JLabel("Mata Kuliah:");
        lblMK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(lblMK, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        comboMataKuliah = new JComboBox<>(new String[]{
                "Pilih Mata Kuliah",
                "Pemrograman berbasis Objek Lanjutan",
                "Struktur Data non Linear",
                "Sistem Cerdas"
        });
        comboMataKuliah.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(comboMataKuliah, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        txtCariNIM = new JTextField("Klik untuk login & isi presensi");
        txtCariNIM.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtCariNIM.setEditable(false);
        txtCariNIM.setColumns(20);
        txtCariNIM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txtCariNIM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!comboMataKuliah.getSelectedItem().equals("Pilih Mata Kuliah")) {
                    doLogin();
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih mata kuliah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        contentPanel.add(txtCariNIM, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        infoPanel.setBackground(PANEL_BG);
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        infoPanel.setPreferredSize(new Dimension(0, 40));

        lblNama = new JLabel("Selamat datang.");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoPanel.add(lblNama);

        lblStatus = new JLabel("Status: Belum login");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatus.setForeground(DANGER_COLOR);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(lblStatus);

        lblSisaJatah = new JLabel("Sisa Jatah Alpha: 0");
        lblSisaJatah.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSisaJatah.setForeground(DANGER_COLOR);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(lblSisaJatah);

        contentPanel.add(infoPanel, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lblPresensi = new JLabel("Pilih Status Presensi:");
        lblPresensi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(lblPresensi, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rbHadir = new JRadioButton("Hadir");
        rbTidakHadir = new JRadioButton("Tidak Hadir");
        rbIzin = new JRadioButton("Izin");
        rbAlpha = new JRadioButton("Alpha");

        ButtonGroup group = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbHadir, rbTidakHadir, rbIzin, rbAlpha}) {
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            rb.setEnabled(false);
            group.add(rb);
            radioPanel.add(rb);
        }
        contentPanel.add(radioPanel, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        btnSimpan = new JButton("Simpan Presensi");
        styleButton(btnSimpan, Color.BLACK, PRIMARY_COLOR);
        btnSimpan.setEnabled(false);
        btnSimpan.addActionListener(e -> handleSimpanPresensi());
        contentPanel.add(btnSimpan, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 3; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        String[] kolom = {"NIM", "Nama", "Mata Kuliah", "Dosen", "Pertemuan", "Tanggal", "Status"};
        model = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelPresensi = new JTable(model);
        tabelPresensi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelPresensi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelPresensi.setRowHeight(25);
        tabelPresensi.setSelectionBackground(new Color(220, 235, 255));
        loadPresensiFromDatabase(); // Ganti dari file ke database

        JScrollPane scrollPane = new JScrollPane(tabelPresensi);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Riwayat Presensi",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                Color.GRAY
        ));
        contentPanel.add(scrollPane, gbc);

        add(contentPanel, BorderLayout.CENTER);

        comboMataKuliah.addActionListener(e -> {
            String mk = comboMataKuliah.getSelectedItem().toString();
            txtCariNIM.setEditable(!mk.equals("Pilih Mata Kuliah"));
            if (!mk.equals("Pilih Mata Kuliah")) {
                updateTabelByMataKuliah(mk);
                updateSisaJatahAlpha(mk);
            }
        });

        getContentPane().setBackground(BG_COLOR);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder());

        pack();
        setMinimumSize(getSize());
    }

    // 🔌 Method untuk konek ke database
    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    // ✅ Method untuk load data presensi dari database
    private void loadPresensiFromDatabase() {
        String sql = "SELECT NIM, NAMA, MATA_KULIAH, DOSEN, PERTEMUAN_KE, TANGGAL, STATUS FROM PRESENSI ORDER BY TANGGAL DESC, NIM";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            model.setRowCount(0);
            while (rs.next()) {
                String tanggal = rs.getDate("TANGGAL").toString(); // Ambil sebagai String
                model.addRow(new Object[]{
                        rs.getString("NIM"),
                        rs.getString("NAMA"),
                        rs.getString("MATA_KULIAH"),
                        rs.getString("DOSEN"),
                        rs.getInt("PERTEMUAN_KE"),
                        tanggal,
                        rs.getString("STATUS")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data presensi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ Method untuk simpan presensi ke database
    private void simpanPresensiKeDatabase(String nim, String nama, String mk, String dosen, int pertemuan, String tanggalStr, String status) {
        String sql = "INSERT INTO PRESENSI (NIM, NAMA, MATA_KULIAH, DOSEN, PERTEMUAN_KE, TANGGAL, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);
            stmt.setString(2, nama);
            stmt.setString(3, mk);
            stmt.setString(4, dosen);
            stmt.setInt(5, pertemuan);
            // Ganti dari:
            // stmt.setDate(6, Date.valueOf(tanggalStr));
            // Jadi:
            stmt.setDate(6, java.sql.Date.valueOf(tanggalStr));
            stmt.setString(7, status);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan presensi ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ Method untuk hapus presensi lama (jika update)
    private boolean removeExistingPresensi(String nim, String mk, String tanggalStr) {
        String sql = "DELETE FROM PRESENSI WHERE NIM = ? AND MATA_KULIAH = ? AND TANGGAL = ?";
        int rowsAffected = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);
            stmt.setString(2, mk);
            // Ganti dari:
            // stmt.setDate(3, Date.valueOf(tanggalStr));
            // Jadi:
            stmt.setDate(3, java.sql.Date.valueOf(tanggalStr));

            rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return rowsAffected > 0;
    }

    // ✅ Method untuk load data mahasiswa dari database (untuk login)
    private Map<String, String[]> loadUsersFromDatabase() {
        Map<String, String[]> users = new HashMap<>();
        String sql = "SELECT NIM, NAMA, PASSWORD FROM MAHASISWA";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nim = rs.getString("NIM");
                String nama = rs.getString("NAMA");
                String password = rs.getString("PASSWORD");
                users.put(nim, new String[]{password, nama});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data mahasiswa dari database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return users;
    }

    // ✅ Method untuk cek login
    private void doLogin() {
        JTextField nimField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] message = {"NIM:", nimField, "Password:", passField};

        int option = JOptionPane.showConfirmDialog(this, message, "🔐 Login Mahasiswa", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String nim = nimField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        Map<String, String[]> users = loadUsersFromDatabase(); // Ganti dari file ke database

        if (users.containsKey(nim) && users.get(nim)[0].equals(pass)) {
            isLoggedIn = true;
            currentNIM = nim;
            currentNama = users.get(nim)[1];
            lblNama.setText("Selamat datang, " + currentNama);
            lblStatus.setText("Status: Login aktif");
            lblStatus.setForeground(SUCCESS_COLOR);

            enablePresensiControls(true);
            String mk = comboMataKuliah.getSelectedItem().toString();
            if (!mk.equals("Pilih Mata Kuliah")) updateSisaJatahAlpha(mk);

            int pilihan = JOptionPane.showOptionDialog(this,
                    "Ingin mengisi presensi atau hanya melihat data?",
                    "Akses Presensi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Isi Presensi", "👁️ Lihat Saja"},
                    "Isi Presensi"
            );

            if (pilihan == 1) startViewTimer();
        } else {
            JOptionPane.showMessageDialog(this, "NIM atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ Method untuk form daftar mahasiswa baru (masukin ke database)
    private void tampilkanFormDaftar() {
        JTextField fieldNIM = new JTextField();
        JTextField fieldNama = new JTextField();
        JPasswordField fieldPass = new JPasswordField();

        Object[] message = {
                "NIM:", fieldNIM,
                "Nama Lengkap:", fieldNama,
                "Password:", fieldPass
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Daftar Mahasiswa Baru", JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            String nim = fieldNIM.getText().trim();
            String nama = fieldNama.getText().trim();
            String pass = new String(fieldPass.getPassword()).trim();

            if (nim.isEmpty() || nama.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, String[]> users = loadUsersFromDatabase(); // Cek dari database
            if (users.containsKey(nim)) {
                JOptionPane.showMessageDialog(this, "NIM sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simpan ke database
            String sql = "INSERT INTO MAHASISWA (NIM, NAMA, PASSWORD) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nim);
                stmt.setString(2, nama);
                stmt.setString(3, pass);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!\nSilakan login untuk mengisi presensi.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ✅ Method lainnya (ambil dosen, pertemuan, sisa alpha, dll) juga diganti ke database
    private String getDosenPengampu(String mk) {
        String sql = "SELECT DOSEN_PENGAMPU FROM MATAKULIAH WHERE NAMA_MK = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("DOSEN_PENGAMPU");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Dosen Tidak Diketahui";
    }

    private int getSisaJatahAlpha(String mk) {
        String sql = "SELECT COUNT(*) FROM PRESENSI WHERE MATA_KULIAH = ? AND NIM = ? AND STATUS IN ('Tidak Hadir', 'Alpha')";
        int count = 0;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mk);
            stmt.setString(2, currentNIM);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Math.max(0, 3 - count);
    }

    private void updateSisaJatahAlpha(String mk) {
        int sisa = getSisaJatahAlpha(mk);
        lblSisaJatah.setText("Sisa Jatah Alpha: " + sisa);
        lblSisaJatah.setForeground(sisa > 0 ? SUCCESS_COLOR : DANGER_COLOR);
        rbAlpha.setEnabled(sisa > 0);
    }

    private int getNextPertemuan(String mk) {
        // Ambil tanggal sekarang sebagai String dalam format yyyy-MM-dd
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        int pertemuanHariIni = 0;

        String sqlToday = "SELECT MAX(PERTEMUAN_KE) AS MAX_PERTEMUAN FROM PRESENSI WHERE MATA_KULIAH = ? AND TANGGAL = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlToday)) {

            stmt.setString(1, mk);
            // Ganti dari:
            // stmt.setDate(2, Date.valueOf(today));
            // Jadi:
            stmt.setDate(2, java.sql.Date.valueOf(today));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pertemuanHariIni = rs.getInt("MAX_PERTEMUAN");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (pertemuanHariIni == 0) {
            int last = getLastPertemuanFromDatabase(mk);
            pertemuanHariIni = last + 1;
            updateLastPertemuanInDatabase(mk, pertemuanHariIni);
        }

        return pertemuanHariIni;
    }

    private int getLastPertemuanFromDatabase(String mk) {
        String sql = "SELECT LAST_PERTEMUAN FROM MATAKULIAH WHERE NAMA_MK = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("LAST_PERTEMUAN");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateLastPertemuanInDatabase(String mk, int newPertemuan) {
        String sql = "UPDATE MATAKULIAH SET LAST_PERTEMUAN = ? WHERE NAMA_MK = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newPertemuan);
            stmt.setString(2, mk);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal update pertemuan di database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTabelByMataKuliah(String mk) {
        model.setRowCount(0);
        String sql = "SELECT NIM, NAMA, MATA_KULIAH, DOSEN, PERTEMUAN_KE, TANGGAL, STATUS FROM PRESENSI WHERE MATA_KULIAH = ? ORDER BY TANGGAL DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mk);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tanggal = rs.getDate("TANGGAL").toString();
                    model.addRow(new Object[]{
                            rs.getString("NIM"),
                            rs.getString("NAMA"),
                            rs.getString("MATA_KULIAH"),
                            rs.getString("DOSEN"),
                            rs.getInt("PERTEMUAN_KE"),
                            tanggal,
                            rs.getString("STATUS")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method lainnya tetap sama
    private void handleSimpanPresensi() {
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Anda belum login!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = getStatusFromRadio();
        if (status == null) {
            JOptionPane.showMessageDialog(this, "Pilih status presensi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String mk = comboMataKuliah.getSelectedItem().toString();
        int sisaAlpha = getSisaJatahAlpha(mk);
        if ("Alpha".equals(status) && sisaAlpha <= 0) {
            JOptionPane.showMessageDialog(this, "Jatah Alpha sudah habis!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ambil tanggal sekarang sebagai String
        String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        String dosen = getDosenPengampu(mk);
        int pertemuanKe = getNextPertemuan(mk);

        boolean wasUpdated = removeExistingPresensi(currentNIM, mk, tanggal);
        simpanPresensiKeDatabase(currentNIM, currentNama, mk, dosen, pertemuanKe, tanggal, status);

        updateTabelPresensi(wasUpdated, currentNIM, currentNama, mk, dosen, pertemuanKe, tanggal, status);

        String action = wasUpdated ? "diperbarui" : "disimpan";
        String title = wasUpdated ? "Presensi Diperbarui" : " Presensi Disimpan";
        String message = String.format(
                "<html><b>Presensi berhasil %s!</b><br><br>" +
                        "• Nama: %s<br>" +
                        "• Mata Kuliah: %s<br>" +
                        "• Pertemuan Ke-%d<br>" +
                        "• Tanggal: %s<br>" +
                        "• Status: <b>%s</b></html>",
                action, currentNama, mk, pertemuanKe, tanggal, status
        );
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);

        updateSisaJatahAlpha(mk);
        JOptionPane.showMessageDialog(this, "Anda akan logout otomatis.", "Info", JOptionPane.INFORMATION_MESSAGE);
        doLogout();
    }

    private String getStatusFromRadio() {
        if (rbHadir.isSelected()) return "Hadir";
        if (rbTidakHadir.isSelected()) return "Tidak Hadir";
        if (rbIzin.isSelected()) return "Izin";
        if (rbAlpha.isSelected()) return "Alpha";
        return null;
    }

    private void updateTabelPresensi(boolean wasUpdated, String nim, String nama, String mk, String dosen, int pertemuan, String tanggal, String status) {
        if (wasUpdated) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                if (model.getValueAt(i, 0).equals(nim) &&
                        model.getValueAt(i, 2).equals(mk) &&
                        model.getValueAt(i, 5).equals(tanggal)) {
                    model.removeRow(i);
                }
            }
        }
        model.addRow(new Object[]{nim, nama, mk, dosen, pertemuan, tanggal, status});
    }

    private void enablePresensiControls(boolean enable) {
        rbHadir.setEnabled(enable);
        rbTidakHadir.setEnabled(enable);
        rbIzin.setEnabled(enable);
        rbAlpha.setEnabled(enable);
        btnSimpan.setEnabled(enable);
    }

    private void startViewTimer() {
        JOptionPane.showMessageDialog(this, "Mode lihat saja aktif. Logout otomatis dalam 5 detik.");
        viewTimer = new Timer();
        viewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "⏰ Waktu habis. Anda telah logout otomatis.");
                    doLogout();
                });
            }
        }, 5000);
    }

    private void doLogout() {
        isLoggedIn = false;
        currentNIM = ""; currentNama = "";
        lblNama.setText("👋 Selamat datang.");
        lblStatus.setText("Status: Belum login");
        lblStatus.setForeground(DANGER_COLOR);
        enablePresensiControls(false);
        if (viewTimer != null) { viewTimer.cancel(); viewTimer = null; }
    }

    private void styleButton(JButton btn, Color fg, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PresensiMahasiswaGUI().setVisible(true);
        });
    }
}