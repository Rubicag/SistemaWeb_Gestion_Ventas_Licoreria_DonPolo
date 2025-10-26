package com.mycompany.repository;

import com.mycompany.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
}
