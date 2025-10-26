package com.mycompany.repository;

import com.mycompany.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    // Métodos personalizados si se requieren
}
