
package com.mycompany.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_notificacion")
	private Integer idNotificacion;

	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "mensaje")
	private String mensaje;

	@Column(name = "fecha_envio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEnvio;

	@Column(name = "tipo")
	private String tipo;

	public Notificacion() {}

	// Constructor personalizado para compatibilidad
	public Notificacion(String mensaje, Date fechaEnvio, boolean leida) {
		this.mensaje = mensaje;
		this.fechaEnvio = fechaEnvio;
		this.tipo = leida ? "LEIDA" : "NO_LEIDA";
	}

	public Integer getIdNotificacion() {
		return idNotificacion;
	}
	public void setIdNotificacion(Integer idNotificacion) {
		this.idNotificacion = idNotificacion;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	// Método para compatibilidad con NotificacionService
	public void setLeida(boolean leida) {
		this.tipo = leida ? "LEIDA" : "NO_LEIDA";
	}
}
