package com.mycompany.repository;

import com.mycompany.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Proveedor
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    Optional<Proveedor> findByRuc(String ruc);
    
    List<Proveedor> findByEstado(String estado);
    
    boolean existsByRuc(String ruc);

    @Query("SELECT p FROM Proveedor p WHERE p.estado = 'ACTIVO'")
    List<Proveedor> findProveedoresActivos();

    @Query("SELECT p FROM Proveedor p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Proveedor> buscarPorNombre(@Param("termino") String termino);

    @Query("SELECT COUNT(p) FROM Proveedor p WHERE p.estado = 'ACTIVO'")
    long contarProveedoresActivos();

    @Query("SELECT p FROM Proveedor p " +
           "LEFT JOIN p.productos prod " +
           "WHERE p.estado = 'ACTIVO' " +
           "GROUP BY p.idProveedor " +
           "ORDER BY COUNT(prod) DESC")
    List<Proveedor> findProveedoresConMasProductos();
}
