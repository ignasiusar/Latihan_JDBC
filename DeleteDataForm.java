import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteDataForm extends JFrame {

    private JTextField jTextFieldDeleteID;
    private JButton jButtonDelete;

    public DeleteDataForm() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Delete Data Employee");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2));

        add(new JLabel("ID Employee:"));
        jTextFieldDeleteID = new JTextField();
        add(jTextFieldDeleteID);

        jButtonDelete = new JButton("Delete");
        JButton jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(e -> dispose());

        add(jButtonDelete);
        add(jButtonCancel);

        jButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });
    }

    private void deleteEmployee() {
        String idText = jTextFieldDeleteID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "DELETE FROM Employee WHERE ID = ?";

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data employee berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldDeleteID.setText(""); // Kosongkan field
            } else {
                JOptionPane.showMessageDialog(this, "ID employee tidak ditemukan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}