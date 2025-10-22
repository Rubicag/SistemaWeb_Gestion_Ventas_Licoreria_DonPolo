

package com.mycompany.service;

import com.mycompany.model.Usuario;
import com.mycompany.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;


@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> buscarPorId(Integer id) {
		return usuarioRepository.findById(id);
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Usuario guardarUsuario(Usuario usuario) {
		// Encriptar la contraseña antes de guardar
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		return usuarioRepository.save(usuario);
	}

	public void eliminarUsuario(Integer id) {
		usuarioRepository.deleteById(id);
	}
}
