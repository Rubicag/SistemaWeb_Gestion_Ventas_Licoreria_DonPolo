package com.mycompany.sistemaweb_gestion_ventas_licoreria_donpolo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mycompany.model.Usuario;
import com.mycompany.model.Usuario.Rol;
import com.mycompany.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.mycompany", "com.mycompany.sistemaweb_gestion_ventas_licoreria_donpolo"})
public class SistemaWebGestionVentasLicoreriaDonPoloApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaWebGestionVentasLicoreriaDonPoloApplication.class, args);
    }

    // Crear usuario admin por defecto si no existe
    @Bean
    public CommandLineRunner crearAdminPorDefecto(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@admin.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setEmail("admin@admin.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(Rol.Administrador);
                usuarioRepository.save(admin);
                System.out.println("\nUSUARIO ADMIN CREADO: admin@admin.com / admin123\n");
            }
        };
    }
}
