package com.mycompany.controller.api;

import com.mycompany.dto.DashboardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class ApiDashboardController {

    @GetMapping
    public DashboardDto getDashboard() {
        DashboardDto d = new DashboardDto();
        // Datos de ejemplo; reemplazar con lógica real (servicio/repo)
        d.setVentasHoy(12);
        d.setVentas30Dias(345);
        d.setClientesActivos(128);
        d.setStockBajo(9);
        Map<String,Object> chart = new HashMap<>();
        chart.put("labels", new String[]{"D-6","D-5","D-4","D-3","D-2","D-1","Hoy"});
        Map<String,Object> dataset = new HashMap<>();
        dataset.put("label","Ventas");
        dataset.put("data", new int[]{3,5,2,6,4,7,12});
        chart.put("datasets", new Object[]{dataset});
        d.setChartData(chart);
        return d;
    }
}
