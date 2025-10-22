	package com.mycompany.model;

	import jakarta.persistence.*;
	import java.util.Date;

	@Entity
	@Table(name = "pedidos")
	public class Pedido {
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "id_cliente")
		private Cliente cliente;

		// Métodos para compatibilidad con ReporteController
		public Cliente getCliente() {
			return cliente;
		}
		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}
	public double getTotal() {
		return 0.0; // Ajusta según tu modelo real
	}
		// Métodos para compatibilidad con el controlador
		public void setCliente(Object cliente) {
			// Implementación vacía o lógica según tu modelo real
		}
		public void setTotal(double total) {
			// Implementación vacía o lógica según tu modelo real
		}
		public void setDetalles(java.util.List<?> detalles) {
			// Implementación vacía o lógica según tu modelo real
		}
		public Integer getId() {
			return idPedido;
		}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido")
	private Integer idPedido;

	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "fecha")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	@Column(name = "direccion_entrega")
	private String direccionEntrega;

	@Column(name = "estado")
	private String estado;

	public Pedido() {}

	public Pedido(Integer idPedido, Integer idUsuario, Date fecha, String direccionEntrega, String estado) {
		this.idPedido = idPedido;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.direccionEntrega = direccionEntrega;
		this.estado = estado;
	}

	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
