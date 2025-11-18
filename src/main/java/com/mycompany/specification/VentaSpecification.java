package com.mycompany.specification;

import com.mycompany.model.Venta;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class VentaSpecification {

    public static Specification<Venta> fechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return (root, query, cb) -> {
            if (inicio == null && fin == null) return null;
            if (inicio != null && fin != null) return cb.between(root.get("fecha"), inicio, fin);
            if (inicio != null) return cb.greaterThanOrEqualTo(root.get("fecha"), inicio);
            return cb.lessThanOrEqualTo(root.get("fecha"), fin);
        };
    }

    public static Specification<Venta> vendedorEquals(Integer vendedorId) {
        return (root, query, cb) -> {
            if (vendedorId == null) return null;
            return cb.equal(root.get("usuario").get("idUsuario"), vendedorId);
        };
    }

    public static Specification<Venta> textoEnClienteOMetodo(String text) {
        return (root, query, cb) -> {
            if (text == null || text.trim().isEmpty()) return null;
            String like = "%" + text.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("metodoPago")), like),
                cb.like(cb.lower(root.join("cliente", JoinType.LEFT).get("nombreCompleto")), like),
                cb.like(cb.lower(root.get("comprobante")), like)
            );
        };
    }
}
