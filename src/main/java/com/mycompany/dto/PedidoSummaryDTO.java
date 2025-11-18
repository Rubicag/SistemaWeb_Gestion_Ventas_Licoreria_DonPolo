package com.mycompany.dto;

public class PedidoSummaryDTO {
    private Integer idPedido;
    private String fecha; // ISO datetime string
    private String direccionEntrega;
    private String estado;
    private String usuarioNombre;

    public PedidoSummaryDTO() {}

    public PedidoSummaryDTO(Integer idPedido, String fecha, String direccionEntrega, String estado, String usuarioNombre) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.direccionEntrega = direccionEntrega;
        this.estado = estado;
        this.usuarioNombre = usuarioNombre;
    }

    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
}
