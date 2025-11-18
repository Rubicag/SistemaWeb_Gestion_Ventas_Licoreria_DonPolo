package com.mycompany.repository;

import com.mycompany.dto.VentaSummaryDTO;
import com.mycompany.model.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VentaRepositoryImpl implements VentaRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<VentaSummaryDTO> findSummary(String search, LocalDateTime inicio, LocalDateTime fin, Integer vendedorId, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VentaSummaryDTO> cq = cb.createQuery(VentaSummaryDTO.class);
        Root<Venta> root = cq.from(Venta.class);
        Join<Object,Object> clienteJoin = root.join("cliente", jakarta.persistence.criteria.JoinType.LEFT);

        // Selections
        // construir nombre completo del cliente usando CONCAT(nombre, ' ', apellido)
        Expression<String> nombreCompletoExpr = cb.concat(cb.concat(clienteJoin.get("nombre"), cb.literal(" ")), clienteJoin.get("apellido"));
        Selection<?>[] selections = new Selection[] {
            root.get("idVenta"),
            root.get("fecha"),
            root.get("total"),
            nombreCompletoExpr,
            root.get("estado"),
            root.get("metodoPago"),
            root.get("idCarrito"),
            root.get("comprobante")
        };
        cq.select(cb.construct(VentaSummaryDTO.class, selections));

        // Predicates
        List<Predicate> predicates = new ArrayList<>();
        if (inicio != null && fin != null) {
            predicates.add(cb.between(root.get("fecha"), inicio, fin));
        } else if (inicio != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), inicio));
        } else if (fin != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), fin));
        }
        if (vendedorId != null) {
            predicates.add(cb.equal(root.get("usuario").get("idUsuario"), vendedorId));
        }
        if (search != null && !search.trim().isEmpty()) {
            String like = "%" + search.toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("metodoPago")), like),
                cb.like(cb.lower(nombreCompletoExpr), like),
                cb.like(cb.lower(root.get("comprobante")), like)
            ));
        }
        if (!predicates.isEmpty()) cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("fecha")));

        TypedQuery<VentaSummaryDTO> query = em.createQuery(cq);
        // paging
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        List<VentaSummaryDTO> content = query.getResultList();

        // count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Venta> countRoot = countQuery.from(Venta.class);
        Join<Object,Object> countClienteJoin = countRoot.join("cliente", jakarta.persistence.criteria.JoinType.LEFT);
        List<Predicate> countPreds = new ArrayList<>();
        if (inicio != null && fin != null) {
            countPreds.add(cb.between(countRoot.get("fecha"), inicio, fin));
        } else if (inicio != null) {
            countPreds.add(cb.greaterThanOrEqualTo(countRoot.get("fecha"), inicio));
        } else if (fin != null) {
            countPreds.add(cb.lessThanOrEqualTo(countRoot.get("fecha"), fin));
        }
        if (vendedorId != null) {
            countPreds.add(cb.equal(countRoot.get("usuario").get("idUsuario"), vendedorId));
        }
        if (search != null && !search.trim().isEmpty()) {
            String like = "%" + search.toLowerCase() + "%";
            Expression<String> countNombreCompleto = cb.concat(cb.concat(countClienteJoin.get("nombre"), cb.literal(" ")), countClienteJoin.get("apellido"));
            countPreds.add(cb.or(
                cb.like(cb.lower(countRoot.get("metodoPago")), like),
                cb.like(cb.lower(countNombreCompleto), like),
                cb.like(cb.lower(countRoot.get("comprobante")), like)
            ));
        }
        if (!countPreds.isEmpty()) countQuery.where(countPreds.toArray(new Predicate[0]));
        countQuery.select(cb.count(countRoot));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
