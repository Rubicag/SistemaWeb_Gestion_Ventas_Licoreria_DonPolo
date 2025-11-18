package com.mycompany.repository;

import com.mycompany.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Cliente con consultas personalizadas
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /**
     * Buscar cliente por DNI
     */
    Optional<Cliente> findByDni(String dni);

    /**
     * Buscar cliente por email
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Buscar clientes por estado
     */
    List<Cliente> findByEstado(String estado);

    /**
     * Buscar clientes activos
     */
    @Query("SELECT c FROM Cliente c WHERE c.estado = 'ACTIVO'")
    List<Cliente> findClientesActivos();

    /**
     * Buscar clientes por nombre o apellido (búsqueda parcial)
     */
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Cliente> buscarPorNombreOApellido(@Param("termino") String termino);

    /**
     * Verificar si existe cliente con DNI
     */
    boolean existsByDni(String dni);

    /**
     * Verificar si existe cliente con email
     */
    boolean existsByEmail(String email);

    /**
     * Contar clientes activos
     */
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.estado = 'ACTIVO'")
    long contarClientesActivos();

    /**
     * Clientes con más compras
     */
    @Query("SELECT c FROM Cliente c LEFT JOIN c.ventas v " +
           "WHERE c.estado = 'ACTIVO' " +
           "GROUP BY c.idCliente " +
           "ORDER BY COUNT(v) DESC")
    List<Cliente> findClientesConMasCompras();
}
