package com.mycompany.service;

import com.mycompany.model.Promocion;
import com.mycompany.repository.PromocionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PromocionService {
    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<Promocion> obtenerPromociones() {
        return promocionRepository.findAll();
    }

    public Promocion guardarPromocion(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    public Promocion obtenerPromocionPorId(Integer id) {
        return promocionRepository.findById(id).orElse(null);
    }

    public void eliminarPromocion(Integer id) {
        promocionRepository.deleteById(id);
    }
}
