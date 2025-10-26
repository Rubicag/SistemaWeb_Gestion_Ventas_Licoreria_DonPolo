package com.mycompany.service;

import com.mycompany.model.Reporte;
import com.mycompany.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public Reporte obtenerReportePorId(Integer id) {
        return reporteRepository.findById(id).orElse(null);
    }

    public void eliminarReporte(Integer id) {
        reporteRepository.deleteById(id);
    }
}
