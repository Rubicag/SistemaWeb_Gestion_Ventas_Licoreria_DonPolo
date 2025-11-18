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

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "destino", length = 200)
    private String destino;

    @Lob
    @Column(name = "payload")
    private String payload;

    @Column(name = "enviado")
    private Boolean enviado = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creadoAt;

    public Integer getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Integer idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public Boolean getEnviado() { return enviado; }
    public void setEnviado(Boolean enviado) { this.enviado = enviado; }

    public Date getCreadoAt() { return creadoAt; }
    public void setCreadoAt(Date creadoAt) { this.creadoAt = creadoAt; }
}
