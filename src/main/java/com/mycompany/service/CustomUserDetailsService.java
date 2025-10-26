package com.mycompany.service;

import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        logger.info("Intentando autenticar usuario con correo: {}", correo);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        logger.info("Usuario encontrado: {} - Contraseña: {} - Rol: {}", usuario.getCorreo(), usuario.getContrasena(), usuario.getRol());
        // Si la contraseña no está encriptada, migrar automáticamente a BCrypt
        String contrasena = usuario.getContrasena();
        if (!contrasena.startsWith("$2a$") && !contrasena.startsWith("$2b$") && !contrasena.startsWith("$2y$")) {
            String hash = passwordEncoder.encode(contrasena);
            usuario.setContrasena(hash);
            usuarioRepository.save(usuario);
            contrasena = hash;
            logger.info("Contraseña migrada a BCrypt para usuario: {}", usuario.getCorreo());
        }
        // Convertir el rol a mayúsculas y sin acentos
        String rol = usuario.getRol()
            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
            .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
            .toUpperCase();
        return User.builder()
            .username(usuario.getCorreo())
            .password(contrasena)
            .roles(rol)
            .build();
    }
}
