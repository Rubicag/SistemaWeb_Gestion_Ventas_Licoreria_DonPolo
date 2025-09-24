/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import com.mycompany.dao.UsuarioDAO;
import com.mycompany.model.Usuario;
import org.springframework.stereotype.Service;

/**
 *
 * @author LUIGGI
 */
@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Registrar un nuevo usuario
    public String registrarUsuario(Usuario usuario) {
        // Verificar si el email ya existe
        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            return "El correo ya está registrado";
        }

        // Guardar el usuario
        usuarioDAO.guardarUsuario(usuario);
        return null; // null indica que no hubo error
    }
}
