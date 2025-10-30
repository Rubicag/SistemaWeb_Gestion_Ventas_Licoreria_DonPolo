package com.mycompany.service;

import com.mycompany.model.Reporte;
import com.mycompany.repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {
    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> obtenerReportes() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> obtenerPorId(Integer id) {
        return reporteRepository.findById(id);
    }

    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminarReporte(Integer id) {
        reporteRepository.deleteById(id);
    }
}
