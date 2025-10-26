package com.mycompany.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DbInspector {
    public static void main(String[] args) {
        try (InputStream is = DbInspector.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                System.err.println("No se encontró application.properties en el classpath");
                return;
            }
            Properties p = new Properties();
            p.load(is);
            String url = p.getProperty("spring.datasource.url");
            String user = p.getProperty("spring.datasource.username");
            String pass = p.getProperty("spring.datasource.password");

            System.out.println("Conectando a: " + url + " como " + user);
            try (Connection c = DriverManager.getConnection(url, user, pass)) {
                String sql = "SELECT correo, `contraseña` as contrasena, rol FROM usuarios LIMIT 50";
                try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                    System.out.println("Resultados:");
                    int count = 0;
                    while (rs.next()) {
                        String correo = rs.getString("correo");
                        String contr = rs.getString("contrasena");
                        String rol = rs.getString("rol");
                        System.out.printf("%d) correo=%s, contrasena=%s, rol=%s%n", ++count, correo, contr, rol);
                    }
                    if (count == 0) System.out.println("(No se encontraron filas en usuarios)");
                }
            }
        } catch (Exception ex) {
            System.err.println("Error al inspeccionar la BD:");
            ex.printStackTrace(System.err);
        }
    }
}
