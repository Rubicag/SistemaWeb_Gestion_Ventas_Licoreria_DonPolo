package com.mycompany.repository;

import com.mycompany.model.Promocion;
import com.mycompany.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Promocion
 */
@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    List<Promocion> findByProducto(Producto producto);
    
    List<Promocion> findByActivoTrue();

    @Query("SELECT p FROM Promocion p WHERE " +
           "p.activo = true AND " +
           "p.fechaInicio <= :fecha AND " +
           "p.fechaFin >= :fecha")
    List<Promocion> findPromocionesVigentes(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM Promocion p WHERE " +
           "p.producto.idProducto = :idProducto AND " +
           "p.activo = true AND " +
           "p.fechaInicio <= CURRENT_DATE AND " +
           "p.fechaFin >= CURRENT_DATE")
    Optional<Promocion> findPromocionVigenteParaProducto(@Param("idProducto") Integer idProducto);

    @Query("SELECT p FROM Promocion p WHERE " +
           "p.activo = true AND " +
           "p.fechaInicio <= CURRENT_DATE AND " +
           "p.fechaFin >= CURRENT_DATE " +
           "ORDER BY p.descuento DESC")
    List<Promocion> findPromocionesActuales();

    @Query("SELECT COUNT(p) FROM Promocion p WHERE " +
           "p.activo = true AND " +
           "p.fechaInicio <= CURRENT_DATE AND " +
           "p.fechaFin >= CURRENT_DATE")
    long contarPromocionesVigentes();

    @Query("SELECT p FROM Promocion p WHERE " +
           "p.fechaFin < CURRENT_DATE " +
           "ORDER BY p.fechaFin DESC")
    List<Promocion> findPromocionesExpiradas();
}
