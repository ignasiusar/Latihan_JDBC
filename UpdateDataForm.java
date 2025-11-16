import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateDataForm extends JFrame {

    private JTextField jTextFieldUpdateID;
    private JTextField jTextFieldUpdateFirstName;
    private JTextField jTextFieldUpdateLastName;
    private JButton jButtonLoad;
    private JButton jButtonUpdate;
    private JButton jButtonCancel;

    public UpdateDataForm() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Update Data Employee");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(6, 2));

        add(new JLabel("Employee ID:"));
        jTextFieldUpdateID = new JTextField();
        add(jTextFieldUpdateID);

        JButton jButtonLoad = new JButton("Load Data");
        add(new JLabel()); // Spacer
        add(jButtonLoad);

        add(new JLabel("New First Name:"));
        jTextFieldUpdateFirstName = new JTextField();
        add(jTextFieldUpdateFirstName);

        add(new JLabel("New Last Name:"));
        jTextFieldUpdateLastName = new JTextField();
        add(jTextFieldUpdateLastName);

        jButtonUpdate = new JButton("Update");
        jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(e -> dispose());

        add(jButtonUpdate);
        add(jButtonCancel);

        jButtonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataForUpdate();
            }
        });

        jButtonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });
    }

    private void loadDataForUpdate() {
        String idText = jTextFieldUpdateID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID harus diisi untuk Load!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT First_Name, Last_Name FROM Employee WHERE ID = ?";

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                jTextFieldUpdateFirstName.setText(rs.getString("First_Name"));
                jTextFieldUpdateLastName.setText(rs.getString("Last_Name"));
            } else {
                JOptionPane.showMessageDialog(this, "ID employee tidak ditemukan.", "Info", JOptionPane.INFORMATION_MESSAGE);
                // Kosongkan field jika tidak ditemukan
                jTextFieldUpdateFirstName.setText("");
                jTextFieldUpdateLastName.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        String idText = jTextFieldUpdateID.getText().trim();
        String firstName = jTextFieldUpdateFirstName.getText().trim();
        String lastName = jTextFieldUpdateLastName.getText().trim();

        if (idText.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID dan data baru harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "UPDATE Employee SET First_Name = ?, Last_Name = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data employee berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                // Kosongkan field setelah update jika diperlukan
                jTextFieldUpdateID.setText("");
                jTextFieldUpdateFirstName.setText("");
                jTextFieldUpdateLastName.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "ID employee tidak ditemukan untuk diupdate.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}