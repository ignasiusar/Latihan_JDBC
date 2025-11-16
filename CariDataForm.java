import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CariDataForm extends JFrame {

    private JTextField jTextFieldCariID;
    private JTextField jTextFieldCariFirstName;
    private JTextField jTextFieldCariLastName;
    private JButton jButtonSearch;
    private JButton jButtonCancel;

    public CariDataForm() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Cari Data Employee");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("ID Employee:"));
        jTextFieldCariID = new JTextField();
        add(jTextFieldCariID);

        JButton jButtonSearch = new JButton("Search");
        add(new JLabel()); // Spacer
        add(jButtonSearch);

        add(new JLabel("First Name:"));
        jTextFieldCariFirstName = new JTextField();
        jTextFieldCariFirstName.setEditable(false); // Agar tidak bisa diedit, hanya untuk menampilkan hasil
        add(jTextFieldCariFirstName);

        add(new JLabel("Last Name:"));
        jTextFieldCariLastName = new JTextField();
        jTextFieldCariLastName.setEditable(false); // Agar tidak bisa diedit, hanya untuk menampilkan hasil
        add(jTextFieldCariLastName);

        jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(e -> dispose());

        add(new JLabel()); // Spacer
        add(jButtonCancel);

        jButtonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();
            }
        });
    }

    private void searchData() {
        String idText = jTextFieldCariID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Employee harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
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

        // Kosongkan field hasil sebelumnya
        jTextFieldCariFirstName.setText("");
        jTextFieldCariLastName.setText("");

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Jika data ditemukan, isi field dengan hasil
                jTextFieldCariFirstName.setText(rs.getString("First_Name"));
                jTextFieldCariLastName.setText(rs.getString("Last_Name"));
            } else {
                // Jika data tidak ditemukan
                JOptionPane.showMessageDialog(this, "Data Employee dengan ID " + id + " tidak ditemukan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}