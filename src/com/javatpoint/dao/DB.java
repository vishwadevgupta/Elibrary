package com.javatpoint.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private DB() { }
    public static Connection getCon() throws SQLException {
        String url = env("ELIBRARY_DB_URL", "jdbc:oracle:thin:@localhost:1521:xe");
        String user = System.getenv("ELIBRARY_DB_USER");
        String password = System.getenv("ELIBRARY_DB_PASSWORD");
        if (user == null || password == null) {
            throw new SQLException("Set ELIBRARY_DB_USER and ELIBRARY_DB_PASSWORD before starting Elibrary.");
        }
        try { Class.forName("oracle.jdbc.driver.OracleDriver"); }
        catch (ClassNotFoundException e) { throw new SQLException("Oracle JDBC driver not found.", e); }
        return DriverManager.getConnection(url, user, password);
    }
    private static String env(String key, String fallback) { String value=System.getenv(key); return value==null||value.trim().isEmpty()?fallback:value; }
}
