package com.mycompany.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.mycompany.repository.UsuarioRepository;
import com.mycompany.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestión de Usuarios (empleados y administradores del sistema)
 * @author LUIGGI
 */
@Service
@Transactional
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Métodos de consulta
    
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        logger.debug("buscarUsuarioPorCorreo - buscando correo='{}'", correo);
        var usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            logger.debug("buscarUsuarioPorCorreo - usuario encontrado id={}", usuarioOpt.get().getIdUsuario());
            return usuarioOpt.get();
        }
        logger.debug("buscarUsuarioPorCorreo - usuario NO encontrado para correo='{}'", correo);
        return null;
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    public List<Usuario> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    // CRUD
    
    public String registrarUsuario(Usuario usuario) {
        // Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            return "El correo ya está registrado";
        }
        // Encriptar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return null; // null indica que no hubo error
    }

    public void guardarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        
        // Si es nuevo usuario, encriptar contraseña
        if (usuario.getIdUsuario() == null && usuario.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        
        usuarioRepository.save(usuario);
    }

    public void actualizarUsuario(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para actualizar");
        }
        
        if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuario.getIdUsuario());
        }
        
        validarUsuario(usuario);
        
        // No actualizar contraseña a menos que se proporcione una nueva
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario()).get();
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(usuarioExistente.getContrasena());
        } else {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // Validaciones
    
    private void validarUsuario(Usuario usuario) {
        if (usuario.getCorreo() != null) {
            var existente = usuarioRepository.findByCorreo(usuario.getCorreo());
            if (existente.isPresent() && !existente.get().getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuario.getCorreo());
            }
        }
    }

    // Ejemplo de validación segura con Apache Commons Lang
    public boolean esNombreValido(String nombre) {
        return StringUtils.isNotBlank(nombre);
    }

    public long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }
}
