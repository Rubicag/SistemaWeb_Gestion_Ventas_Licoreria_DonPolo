package com.mycompany.repository;

import com.mycompany.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByActivoTrue();
    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo AND u.activo = true")
    Optional<Usuario> findByCorreoAndActivoTrue(@Param("correo") String correo);

    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombre ASC")
    List<Usuario> findUsuariosActivos();

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    List<Usuario> findByRolAndActivoTrue(@Param("rol") String rol);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long contarUsuariosActivos();

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> buscarPorNombre(@Param("nombre") String nombre);

    long countByActivoTrue();
}
