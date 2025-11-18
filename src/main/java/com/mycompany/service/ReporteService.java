
package com.mycompany.service;

import org.springframework.stereotype.Service;
import com.mycompany.model.ReporteVentaProducto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

@Service
public class ReporteService {
    
    // Método para obtener datos de reporte de ventas por producto (simulado)
    public List<ReporteVentaProducto> obtenerReporteVentasPorProducto() {
        return Arrays.asList(
            new ReporteVentaProducto("Ron Cartavio", "Licores", 120, 2400.0),
            new ReporteVentaProducto("Whisky Johnnie Walker", "Licores - Whisky", 80, 3200.0),
            new ReporteVentaProducto("Vodka Absolut", "Licores - Vodka", 60, 1500.0)
        );
    }
    
    /**
     * Exporta el reporte de ventas por producto a formato Excel (.xlsx)
     * @param reportes Lista de reportes a exportar
     * @return Array de bytes con el archivo Excel generado
     * @throws IOException Si ocurre un error al generar el archivo
     */
    public byte[] exportarReporteVentasExcel(List<ReporteVentaProducto> reportes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            // Crear hoja
            Sheet sheet = workbook.createSheet("Reporte de Ventas");
            
            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Estilo para datos numéricos
            CellStyle numberStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,##0"));
            
            // Estilo para moneda
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(format.getFormat("S/. #,##0.00"));
            
            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Producto", "Categoría", "Cantidad Vendida", "Total Vendido (S/.)"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            double totalGeneral = 0.0;
            int cantidadTotal = 0;
            
            for (ReporteVentaProducto reporte : reportes) {
                Row row = sheet.createRow(rowNum++);
                
                // Producto
                row.createCell(0).setCellValue(reporte.getNombreProducto());

                // Categoría
                row.createCell(1).setCellValue(reporte.getCategoria() != null ? reporte.getCategoria() : "-");

                // Cantidad
                Cell cantidadCell = row.createCell(2);
                cantidadCell.setCellValue(reporte.getCantidadVendida());
                cantidadCell.setCellStyle(numberStyle);

                // Total
                Cell totalCell = row.createCell(3);
                totalCell.setCellValue(reporte.getTotalVendido());
                totalCell.setCellStyle(currencyStyle);

                totalGeneral += reporte.getTotalVendido();
                cantidadTotal += reporte.getCantidadVendida();
            }
            
            // Fila de totales
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("TOTALES:");
            
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            totalLabelCell.setCellStyle(boldStyle);
            
            Cell cantidadTotalCell = totalRow.createCell(1);
            cantidadTotalCell.setCellValue(cantidadTotal);
            CellStyle boldNumberStyle = workbook.createCellStyle();
            boldNumberStyle.cloneStyleFrom(numberStyle);
            boldNumberStyle.setFont(boldFont);
            cantidadTotalCell.setCellStyle(boldNumberStyle);
            
            Cell totalGeneralCell = totalRow.createCell(2);
            totalGeneralCell.setCellValue(totalGeneral);
            CellStyle boldCurrencyStyle = workbook.createCellStyle();
            boldCurrencyStyle.cloneStyleFrom(currencyStyle);
            boldCurrencyStyle.setFont(boldFont);
            totalGeneralCell.setCellStyle(boldCurrencyStyle);
            
            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                // Añadir un poco de espacio extra
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
}