import java.sql.*;

public class DatabaseConnections {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String USERNAME = "data_kantor";
    private static final String PASSWORD = "data_kantor";

public static Connection getConnection() throws SQLException{
    try{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        System.out.println("Driver JDBC Oracle ditemukan.");
    } catch (ClassNotFoundException e) {
        System.out.println("Driver JDBC Oracle tidak ditemukan: " + e.getMessage());
        throw new SQLException("Driver Oracle tidak ditemukan.", e);
    }try {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("Koneksi ke database Oracle berhasil.");
        return conn;
    } catch (SQLException e) {
        System.out.println("Gagal koneksi ke database Oracle: " + e.getMessage());
        throw e; // Melemparkan exception ke kelas yang memanggil
    }
}

}
