
// Ejemplo de servicio Spring Boot usando DataSource inyectado
package com.mycompany.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class DbInspectorService {

    @Autowired
    private DataSource dataSource;

    public void inspeccionarUsuarios() {
        try (Connection c = dataSource.getConnection()) {
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
        } catch (Exception ex) {
            System.err.println("Error al inspeccionar la BD:");
            ex.printStackTrace(System.err);
        }
    }
}
