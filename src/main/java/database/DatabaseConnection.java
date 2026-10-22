package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "library_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(
                    SERVER_URL + DATABASE_NAME,
                    USERNAME,
                    PASSWORD
            );

            System.out.println("Database connected successfully!");

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return con;
    }

    public static void createDatabaseAndTables() {

        try {
            Connection con = DriverManager.getConnection(
                    SERVER_URL,
                    USERNAME,
                    PASSWORD
            );

            Statement stmt = con.createStatement();

            String createDatabase = "CREATE DATABASE IF NOT EXISTS "
                    + DATABASE_NAME;

            stmt.executeUpdate(createDatabase);

            System.out.println("Database checked/created successfully.");

            stmt.close();
            con.close();

            createTables();

        } catch (SQLException e) {
            System.out.println("Database creation failed!");
            e.printStackTrace();
        }
    }

    private static void createTables() {

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();

            // Books table
            String booksTable = """
                CREATE TABLE IF NOT EXISTS books (
                    book_id VARCHAR(20) PRIMARY KEY,
                    title VARCHAR(150) NOT NULL,
                    author VARCHAR(100) NOT NULL,
                    category VARCHAR(100) NOT NULL,
                    status VARCHAR(20) NOT NULL DEFAULT 'Available'
                )
                """;

            stmt.executeUpdate(booksTable);

            // Members table
            String membersTable = """
                CREATE TABLE IF NOT EXISTS members (
                    member_id VARCHAR(20) PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    contact_number VARCHAR(20) NOT NULL
                )
                """;

            stmt.executeUpdate(membersTable);

            // Borrow records table
            String borrowRecordsTable = """
                CREATE TABLE IF NOT EXISTS borrow_records (
                    record_id INT AUTO_INCREMENT PRIMARY KEY,
                    book_id VARCHAR(20) NOT NULL,
                    member_id VARCHAR(20) NOT NULL,
                    borrow_date DATE NOT NULL,
                    return_date DATE NULL,
                    status VARCHAR(20) NOT NULL DEFAULT 'Borrowed',

                    FOREIGN KEY (book_id)
                        REFERENCES books(book_id),

                    FOREIGN KEY (member_id)
                        REFERENCES members(member_id)
                )
                """;

            stmt.executeUpdate(borrowRecordsTable);

            System.out.println("All tables checked/created successfully!");

            stmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Table creation failed!");
            e.printStackTrace();
        }
    }
}