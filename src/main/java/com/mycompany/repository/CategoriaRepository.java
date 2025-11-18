package com.mycompany.repository;

import com.mycompany.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Categoría con consultas personalizadas
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombre(String nombre);
    
    List<Categoria> findByActivoTrue();
    
    boolean existsByNombre(String nombre);

    @Query("SELECT DISTINCT c FROM Categoria c " +
           "INNER JOIN c.productos p " +
           "WHERE c.activo = true AND p.activo = true AND p.stock > 0")
    List<Categoria> findCategoriasConStock();

    @Query("SELECT c.nombre, COUNT(p) FROM Categoria c " +
           "LEFT JOIN c.productos p " +
           "WHERE c.activo = true " +
           "GROUP BY c.idCategoria, c.nombre " +
           "ORDER BY COUNT(p) DESC")
    List<Object[]> contarProductosPorCategoria();
}
