package com.mycompany.repository;

import com.mycompany.model.Producto;
import com.mycompany.model.Categoria;
import com.mycompany.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(Categoria categoria);
    List<Producto> findByProveedor(Proveedor proveedor);
    List<Producto> findByActivoTrue();
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    boolean existsByCodigoBarras(String codigoBarras);

    @Query("SELECT p FROM Producto p WHERE " +
           "p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.marca) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarPorNombreOMarca(@Param("termino") String termino);

       @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stock <= COALESCE(p.stockMinimo, 0)")
       List<Producto> findProductosConBajoStock();

    @Query("SELECT p FROM Producto p WHERE p.stock > 0 AND p.activo = true")
    List<Producto> findProductosDisponibles();

    @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria AND p.activo = true")
    List<Producto> findByCategoriaId(@Param("idCategoria") Integer idCategoria);

    @Query("SELECT p FROM Producto p WHERE p.proveedor.idProveedor = :idProveedor AND p.activo = true")
    List<Producto> findByProveedorId(@Param("idProveedor") Integer idProveedor);

    @Query("SELECT p FROM Producto p WHERE " +
           "p.precio BETWEEN :precioMin AND :precioMax AND p.activo = true " +
           "ORDER BY p.precio ASC")
    List<Producto> findByRangoPrecio(@Param("precioMin") BigDecimal precioMin, 
                                     @Param("precioMax") BigDecimal precioMax);

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    long contarProductosActivos();
}
