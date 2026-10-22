/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.librarymanagement;
import database.DatabaseConnection;
import javax.swing.SwingUtilities;
import ui.LibraryDashboardFrame;

public class LibraryManagementSystem {

    public static void main(String[] args) {


        DatabaseConnection.createDatabaseAndTables();

        SwingUtilities.invokeLater(() -> {
            LibraryDashboardFrame frame =
                    new LibraryDashboardFrame();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
