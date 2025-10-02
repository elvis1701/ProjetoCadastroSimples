package com.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // ⚠️ AJUSTE A SENHA DO SEU MARIA DB AQUI!
    private static final String URL = "jdbc:mariadb://localhost:3306/sistema_cadastro";
    private static final String USER = "root";
    private static final String PASSWORD = "sua_senha";

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MariaDB não encontrado", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}