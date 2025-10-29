package com.mycompany;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mycompany.sistemaweb_gestion_ventas_licoreria_donpolo.SistemaWebGestionVentasLicoreriaDonPoloApplication;

@SpringBootTest(classes = SistemaWebGestionVentasLicoreriaDonPoloApplication.class)
public class TestDB {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConexionYConsultaProductos() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM productos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id_producto") + " - " + rs.getString("nombre"));
            }
        }
    }
}
