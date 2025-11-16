import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertDataForm extends JFrame {

    private JTextField jTextFieldID;
    private JTextField jTextFieldFirstName;
    private JTextField jTextFieldLastName;
    private JButton jButtonInsert;

    public InsertDataForm() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Insert Data Employee");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2));

        add(new JLabel("ID:"));
        jTextFieldID = new JTextField();
        add(jTextFieldID);

        add(new JLabel("First Name:"));
        jTextFieldFirstName = new JTextField();
        add(jTextFieldFirstName);

        add(new JLabel("Last Name:"));
        jTextFieldLastName = new JTextField();
        add(jTextFieldLastName);

        jButtonInsert = new JButton("Insert");
        JButton jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(e -> dispose()); // Tutup form

        add(jButtonInsert);
        add(jButtonCancel);

        jButtonInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertEmployee();
            }
        });
    }

    private void insertEmployee() {
        String idText = jTextFieldID.getText().trim();
        String firstName = jTextFieldFirstName.getText().trim();
        String lastName = jTextFieldLastName.getText().trim();

        if (idText.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO Employee (ID, First_Name, Last_Name) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnections.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data employee berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                // Kosongkan field setelah insert
                jTextFieldID.setText("");
                jTextFieldFirstName.setText("");
                jTextFieldLastName.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}