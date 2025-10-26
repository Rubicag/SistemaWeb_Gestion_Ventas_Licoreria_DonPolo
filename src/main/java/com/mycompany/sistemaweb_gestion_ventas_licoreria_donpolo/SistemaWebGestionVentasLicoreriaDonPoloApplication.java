package com.mycompany.sistemaweb_gestion_ventas_licoreria_donpolo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.mycompany")
@EnableJpaRepositories(basePackages = "com.mycompany.repository")
@EntityScan(basePackages = "com.mycompany.model")
public class SistemaWebGestionVentasLicoreriaDonPoloApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaWebGestionVentasLicoreriaDonPoloApplication.class, args);
    }
}
