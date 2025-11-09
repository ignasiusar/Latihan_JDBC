import java.sql.*;

public class ProgramJDBC {

    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE"; // Ganti SESUAI SERVICE KAMU
    private static final String JDBC_USER = "akademik";                               // Ganti SESUAI USER KAMU
    private static final String JDBC_PASSWORD = "akademik";                           // Ganti SESUAI PASSWORD KAMU

    public static void main(String[] args) {
        boolean running = true;
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (running) {
            System.out.println("\n=== MENU PROGRAM JDBC ===");
            System.out.println("1. Tampilkan Data Mahasiswa");
            System.out.println("2. Tambah Data Mahasiswa");
            System.out.println("3. Hapus Data Mahasiswa");
            System.out.println("4. Update Data Mahasiswa");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showData();
                    break;
                case 2:
                    insertData(scanner);
                    break;
                case 3:
                    deleteData(scanner);
                    break;
                case 4:
                    updateData(scanner);
                    break;
                case 5:
                    running = false;
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Menu tidak valid.");
            }
        }
    }

    // Latihan 1: Koneksi ke Database
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Koneksi berhasil!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Oracle tidak ditemukan.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Koneksi gagal.");
            e.printStackTrace();
        }
        return conn;
    }

    // Latihan 2: Tampilkan Data
    public static void showData() {
        String sql = "SELECT NIM, NAMA, IPK FROM MAHASISWA ORDER BY NIM";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- Data Mahasiswa ---");
            System.out.printf("%-10s %-30s %-5s%n", "NIM", "NAMA", "IPK");
            System.out.println("----------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10s %-30s %-5.2f%n", rs.getString("NIM"), rs.getString("NAMA"), rs.getDouble("IPK"));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data.");
            e.printStackTrace();
        }
    }

    // Latihan 3: Tambah Data
    public static void insertData(java.util.Scanner scanner) {
        System.out.print("Masukkan NIM: ");
        String nim = scanner.nextLine();
        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan IPK: ");
        double ipk = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String sql = "INSERT INTO MAHASISWA (NIM, NAMA, IPK) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);
            stmt.setString(2, nama);
            stmt.setDouble(3, ipk);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data berhasil ditambahkan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan data.");
            e.printStackTrace();
        }
    }

    // Latihan 4: Hapus Data
    public static void deleteData(java.util.Scanner scanner) {
        System.out.print("Masukkan NIM yang akan dihapus: ");
        String nim = scanner.nextLine();

        String sql = "DELETE FROM MAHASISWA WHERE NIM = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data berhasil dihapus.");
            } else {
                System.out.println("NIM tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus data.");
            e.printStackTrace();
        }
    }

    // Latihan 5: Update Data
    public static void updateData(java.util.Scanner scanner) {
        System.out.print("Masukkan NIM yang akan diupdate: ");
        String nim = scanner.nextLine();
        System.out.print("Masukkan Nama baru: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan IPK baru: ");
        double ipk = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String sql = "UPDATE MAHASISWA SET NAMA = ?, IPK = ? WHERE NIM = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nama);
            stmt.setDouble(2, ipk);
            stmt.setString(3, nim);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data berhasil diupdate.");
            } else {
                System.out.println("NIM tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengupdate data.");
            e.printStackTrace();
        }
    }
}