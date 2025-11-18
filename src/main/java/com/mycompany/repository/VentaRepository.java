package com.mycompany.repository;

import com.mycompany.model.Venta;
import com.mycompany.model.Cliente;
import com.mycompany.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VentaRepository extends JpaRepository<Venta, Integer>, JpaSpecificationExecutor<Venta>, VentaRepositoryCustom {

    List<Venta> findByUsuario(Usuario usuario);
    List<Venta> findByCliente(Cliente cliente);
    List<Venta> findByEstado(String estado);
    List<Venta> findByMetodoPago(String metodoPago);

    @Query("SELECT v FROM Venta v WHERE " +
           "v.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY v.fecha DESC")
    List<Venta> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio,
                                  @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT v FROM Venta v WHERE " +
           "v.estado = 'COMPLETADA' AND " +
           "v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findVentasCompletadasEnRango(@Param("fechaInicio") LocalDateTime fechaInicio,
                                             @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(v.total) FROM Venta v WHERE " +
           "v.estado = 'COMPLETADA' AND " +
           "v.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularTotalVentasEnRango(@Param("fechaInicio") LocalDateTime fechaInicio,
                                          @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(v) FROM Venta v WHERE " +
           "v.estado = 'COMPLETADA' AND " +
           "v.fecha BETWEEN :fechaInicio AND :fechaFin")
    long contarVentasEnRango(@Param("fechaInicio") LocalDateTime fechaInicio,
                             @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE " +
           "v.estado = 'COMPLETADA' AND " +
           "FUNCTION('DATE', v.fecha) = CURRENT_DATE")
    BigDecimal calcularVentasDelDia();

    @Query("SELECT v FROM Venta v WHERE v.estado = 'COMPLETADA' " +
           "ORDER BY v.fecha DESC")
    List<Venta> findUltimasVentas();

    // JPQL does not support LIMIT; provide a derived query to get top N
    List<Venta> findTop10ByEstadoOrderByFechaDesc(String estado);
}
