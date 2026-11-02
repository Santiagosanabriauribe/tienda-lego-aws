package com.tiendalego.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    private String usuarioNombre;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemPedido> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    private String metodoPago;

    @Column(nullable = false)
    private String estado = "CONFIRMADO";

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    private String direccionEnvio;
    private String ciudadEnvio;
    private String departamentoEnvio;
    private String telefonoEnvio;
    private String nombreDestinatario;

    public Pedido() {}

    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getCiudadEnvio() { return ciudadEnvio; }
    public void setCiudadEnvio(String ciudadEnvio) { this.ciudadEnvio = ciudadEnvio; }

    public String getDepartamentoEnvio() { return departamentoEnvio; }
    public void setDepartamentoEnvio(String departamentoEnvio) { this.departamentoEnvio = departamentoEnvio; }

    public String getTelefonoEnvio() { return telefonoEnvio; }
    public void setTelefonoEnvio(String telefonoEnvio) { this.telefonoEnvio = telefonoEnvio; }

    public String getNombreDestinatario() { return nombreDestinatario; }
    public void setNombreDestinatario(String nombreDestinatario) { this.nombreDestinatario = nombreDestinatario; }
}
