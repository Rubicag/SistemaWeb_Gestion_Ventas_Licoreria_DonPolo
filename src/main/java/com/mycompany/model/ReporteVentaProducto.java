package com.mycompany.model;

public class ReporteVentaProducto {
    private String nombreProducto;
    private int cantidadVendida;
    private double totalVendido;

    public ReporteVentaProducto(String nombreProducto, int cantidadVendida, double totalVendido) {
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
        this.totalVendido = totalVendido;
    }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(int cantidadVendida) { this.cantidadVendida = cantidadVendida; }
    public double getTotalVendido() { return totalVendido; }
    public void setTotalVendido(double totalVendido) { this.totalVendido = totalVendido; }
}
