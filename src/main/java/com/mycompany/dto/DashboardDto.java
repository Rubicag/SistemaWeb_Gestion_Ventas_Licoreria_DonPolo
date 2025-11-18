package com.mycompany.dto;

import java.util.Map;

public class DashboardDto {
    private long ventasHoy;
    private long ventas30Dias;
    private long clientesActivos;
    private long stockBajo;
    private Map<String, Object> chartData;

    public long getVentasHoy() { return ventasHoy; }
    public void setVentasHoy(long ventasHoy) { this.ventasHoy = ventasHoy; }
    public long getVentas30Dias() { return ventas30Dias; }
    public void setVentas30Dias(long ventas30Dias) { this.ventas30Dias = ventas30Dias; }
    public long getClientesActivos() { return clientesActivos; }
    public void setClientesActivos(long clientesActivos) { this.clientesActivos = clientesActivos; }
    public long getStockBajo() { return stockBajo; }
    public void setStockBajo(long stockBajo) { this.stockBajo = stockBajo; }
    public Map<String, Object> getChartData() { return chartData; }
    public void setChartData(Map<String, Object> chartData) { this.chartData = chartData; }
}
