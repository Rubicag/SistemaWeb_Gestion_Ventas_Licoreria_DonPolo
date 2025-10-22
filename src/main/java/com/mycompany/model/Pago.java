	package com.mycompany.model;

	import jakarta.persistence.*;
	import java.util.Date;

	@Entity
	@Table(name = "pagos")
	public class Pago {
		// Métodos para compatibilidad con el controlador
		public void setPedido(Object pedido) {
			// Implementación vacía o lógica según tu modelo real
		}
		public void setCliente(Object cliente) {
			// Implementación vacía o lógica según tu modelo real
		}
		public void setMetodo(String metodo) {
			this.metodoPago = metodo;
		}
		public void setFecha(java.util.Date fecha) {
			this.fechaPago = fecha;
		}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pago")
	private Integer idPago;

	@Column(name = "id_venta")
	private Integer idVenta;

	@Column(name = "monto")
	private Double monto;

	@Column(name = "metodo_pago")
	private String metodoPago;

	@Column(name = "estado")
	private String estado;

	@Column(name = "fecha_pago")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaPago;

	public Pago() {}

	public Integer getIdPago() {
		return idPago;
	}
	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}
	public Integer getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
}
