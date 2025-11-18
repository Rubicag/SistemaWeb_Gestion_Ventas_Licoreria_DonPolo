package com.mycompany.service;

import com.mycompany.model.Promocion;
import com.mycompany.repository.PromocionRepository;
import com.mycompany.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de Promociones
 */
@Service
@Transactional
public class PromocionService {
    private final PromocionRepository promocionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<Promocion> listarTodas() {
        return promocionRepository.findAll();
    }

    public List<Promocion> listarActivas() {
        return promocionRepository.findByActivoTrue();
    }

    public List<Promocion> listarVigentes() {
        return promocionRepository.findPromocionesVigentes(LocalDate.now());
    }

    public List<Promocion> listarActuales() {
        return promocionRepository.findPromocionesActuales();
    }

    public Optional<Promocion> buscarPorId(Integer id) {
        return promocionRepository.findById(id);
    }

    public Optional<Promocion> buscarPromocionVigenteParaProducto(Integer idProducto) {
        return promocionRepository.findPromocionVigenteParaProducto(idProducto);
    }

    public Promocion guardar(Promocion promocion) {
        validarPromocion(promocion);
        return promocionRepository.save(promocion);
    }

    public Promocion actualizar(Promocion promocion) {
        if (promocion.getIdPromocion() == null) {
            throw new IllegalArgumentException("El ID de la promoción es obligatorio para actualizar");
        }
        if (!promocionRepository.existsById(promocion.getIdPromocion())) {
            throw new IllegalArgumentException("Promoción no encontrada con ID: " + promocion.getIdPromocion());
        }
        validarPromocion(promocion);
        return promocionRepository.save(promocion);
    }

    public void eliminar(Integer id) {
        if (!promocionRepository.existsById(id)) {
            throw new IllegalArgumentException("Promoción no encontrada con ID: " + id);
        }
        promocionRepository.deleteById(id);
    }

    public void desactivar(Integer id) {
        Promocion promocion = promocionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada con ID: " + id));
        promocion.setActivo(false);
        promocionRepository.save(promocion);
    }

    public void activar(Integer id) {
        Promocion promocion = promocionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada con ID: " + id));
        promocion.setActivo(true);
        promocionRepository.save(promocion);
    }

    public long contarVigentes() {
        return promocionRepository.contarPromocionesVigentes();
    }

    public List<Promocion> obtenerExpiradas() {
        return promocionRepository.findPromocionesExpiradas();
    }

    private void validarPromocion(Promocion promocion) {
        if (promocion.getNombre() == null || promocion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la promoción es obligatorio");
        }

        if (promocion.getProducto() == null || promocion.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio para la promoción");
        }

        // Verificar que el producto existe
        if (!productoRepository.existsById(promocion.getProducto().getIdProducto())) {
            throw new IllegalArgumentException("El producto no existe");
        }

        if (promocion.getFechaInicio() == null || promocion.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }

        if (promocion.getFechaFin().isBefore(promocion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (promocion.getDescuento() == null || promocion.getDescuento().doubleValue() <= 0) {
            throw new IllegalArgumentException("El descuento debe ser mayor a 0");
        }

        if (promocion.getDescuento().doubleValue() > 100) {
            throw new IllegalArgumentException("El descuento no puede ser mayor a 100%");
        }
    }

    // Métodos legacy para compatibilidad
    @Deprecated
    public List<Promocion> obtenerPromociones() {
        return listarTodas();
    }

    @Deprecated
    public Promocion guardarPromocion(Promocion promocion) {
        return guardar(promocion);
    }

    @Deprecated
    public Promocion obtenerPromocionPorId(Integer id) {
        return buscarPorId(id).orElse(null);
    }

    @Deprecated
    public void eliminarPromocion(Integer id) {
        eliminar(id);
    }
}
