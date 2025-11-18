
package com.mycompany.service;

import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

import com.mycompany.model.ReporteVentaProducto;
import java.util.List;
import java.util.Arrays;

@Service
public class ReporteService {
    // Ejemplo de generación segura de Excel
    public void exportarReporteExcel(String ruta) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Columna 1");
            row.createCell(1).setCellValue("Columna 2");
            try (FileOutputStream fos = new FileOutputStream(ruta)) {
                workbook.write(fos);
            }
        }
    }

    // Método para obtener datos de reporte de ventas por producto (simulado)
    public List<ReporteVentaProducto> obtenerReporteVentasPorProducto() {
        return Arrays.asList(
            new ReporteVentaProducto("Ron Cartavio", 120, 2400.0),
            new ReporteVentaProducto("Whisky Johnnie Walker", 80, 3200.0),
            new ReporteVentaProducto("Vodka Absolut", 60, 1500.0)
        );
    }
}