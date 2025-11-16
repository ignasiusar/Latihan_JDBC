import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    private JMenuItem jMenuItemTampil;
    private JMenuItem jMenuItemCari;
    private JMenuItem jMenuItemInsert;
    private JMenuItem jMenuItemDelete;
    private JMenuItem jMenuItemUpdate;

    public Main() {
        initializeComponents();
        setupMenu();
    }

    private void initializeComponents() {
        setTitle("Menu Utama - GUI Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Pusatkan window

        // Membuat komponen menu (meskipun tidak selalu diperlukan secara visual di sini,
        // kita tetap buat untuk struktur)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuData = new JMenu("Data Management");

        jMenuItemTampil = new JMenuItem("Tampilkan Data");
        jMenuItemCari = new JMenuItem("Cari Data");
        jMenuItemInsert = new JMenuItem("Insert Data");
        jMenuItemDelete = new JMenuItem("Delete Data");
        jMenuItemUpdate = new JMenuItem("Update Data");

        menuData.add(jMenuItemTampil);
        menuData.add(jMenuItemCari);
        menuData.add(jMenuItemInsert);
        menuData.add(jMenuItemDelete);
        menuData.add(jMenuItemUpdate);

        menuBar.add(menuData);
        setJMenuBar(menuBar);
    }

    private void setupMenu() {
        jMenuItemTampil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewDataForm formTampil = new ViewDataForm();
                formTampil.setVisible(true);
                // Optional: Sembunyikan menu utama saat form dibuka
                // MainMenu.this.setVisible(false);
                // formTampil.addWindowListener(new java.awt.event.WindowAdapter() {
                //     @Override
                //     public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                //         MainMenu.this.setVisible(true); // Kembalikan menu utama
                //     }
                // });
            }
        });

        jMenuItemCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CariDataForm formCari = new CariDataForm();
                formCari.setVisible(true);
            }
        });

        jMenuItemInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertDataForm formInsert = new InsertDataForm();
                formInsert.setVisible(true);
            }
        });

        jMenuItemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteDataForm formDelete = new DeleteDataForm();
                formDelete.setVisible(true);
            }
        });

        jMenuItemUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateDataForm formUpdate = new UpdateDataForm();
                formUpdate.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}