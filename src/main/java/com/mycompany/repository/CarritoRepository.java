package com.mycompany.repository;

import com.mycompany.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    // Métodos personalizados si los necesitas
}
