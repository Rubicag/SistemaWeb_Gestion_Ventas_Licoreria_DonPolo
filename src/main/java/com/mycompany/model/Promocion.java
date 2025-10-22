	package com.mycompany.model;

	import jakarta.persistence.*;

	@Entity
	@Table(name = "promociones")
	public class Promocion {
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "id_producto")
		private Producto producto;

		// Método para compatibilidad con PromocionService
		public Producto getProducto() {
			return producto;
		}
		public void setProducto(Producto producto) {
			this.producto = producto;
		}

		// Constructor para compatibilidad con el controlador
		public Promocion(String nombre, String descripcion, double descuento, Producto producto, java.util.Date inicio, java.util.Date fin) {
			this.descripcion = descripcion;
			this.descuento = descuento;
			this.producto = producto;
			// inicio, fin: asignar si existen en el modelo real
		}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_promocion")
	private Integer idPromocion;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "descuento")
	private Double descuento;

	@Column(name = "fecha_inicio")
	private java.sql.Date fechaInicio;

	@Column(name = "fecha_fin")
	private java.sql.Date fechaFin;

	public Promocion() {}

	public Integer getIdPromocion() {
		return idPromocion;
	}
	public void setIdPromocion(Integer idPromocion) {
		this.idPromocion = idPromocion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getDescuento() {
		return descuento;
	}
	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}
	public java.sql.Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(java.sql.Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public java.sql.Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(java.sql.Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
