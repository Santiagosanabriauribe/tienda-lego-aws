package com.tiendalego.service;

import com.tiendalego.model.ItemPedido;
import com.tiendalego.model.Pedido;
import com.tiendalego.model.Producto;
import com.tiendalego.repository.PedidoRepository;
import com.tiendalego.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProductoRepository productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
    }

    @Transactional
    public Pedido crearDirecto(Long productoId, int cantidad, Long usuarioId,
                                String usuarioNombre, String metodoPago, String direccion,
                                String ciudad, String departamento, String telefono,
                                String destinatario) {
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepo.save(producto);

        ItemPedido item = new ItemPedido(producto.getId(), producto.getNombre(),
                cantidad, producto.getPrecio());

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setUsuarioNombre(usuarioNombre);
        pedido.agregarItem(item);
        pedido.setTotal(item.getSubtotal());
        pedido.setMetodoPago(metodoPago);
        pedido.setDireccionEnvio(direccion);
        pedido.setCiudadEnvio(ciudad);
        pedido.setDepartamentoEnvio(departamento);
        pedido.setTelefonoEnvio(telefono);
        pedido.setNombreDestinatario(destinatario);

        return pedidoRepo.save(pedido);
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepo.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepo.findAll();
    }

    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        if (!List.of("CONFIRMADO", "ENTREGADO", "CANCELADO").contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no valido: " + nuevoEstado);
        }
        Pedido pedido = pedidoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepo.save(pedido);
    }
}
