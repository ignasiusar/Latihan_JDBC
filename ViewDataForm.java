import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDataForm extends JFrame {

    private JTable jTableEmployee;
    private DefaultTableModel tableModel;
    private JButton jButtonRefresh;

    public ViewDataForm() {
        initializeComponents();
        loadEmployeeData(); // Muat data saat form dibuka
    }

    private void initializeComponents() {
        setTitle("Tampilkan Data Employee");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Hanya tutup form ini
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "First Name", "Last Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };

        jTableEmployee = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(jTableEmployee);

        jButtonRefresh = new JButton("Refresh Data");
        jButtonRefresh.addActionListener(e -> loadEmployeeData());

        setLayout(new BorderLayout());
        add(jScrollPane, BorderLayout.CENTER);
        add(jButtonRefresh, BorderLayout.SOUTH);
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0); // Hapus semua baris lama
        String query = "SELECT ID, First_Name, Last_Name FROM Employee ORDER BY ID";

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {rs.getInt("ID"), rs.getString("First_Name"), rs.getString("Last_Name")};
                tableModel.addRow(row);
            }
            JOptionPane.showMessageDialog(this, "Data berhasil dimuat.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}