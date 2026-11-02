package com.tiendalego.service;

import com.tiendalego.model.Pedido;
import com.tiendalego.model.Producto;
import com.tiendalego.repository.PedidoRepository;
import com.tiendalego.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepo;

    @Mock
    private ProductoRepository productoRepo;

    @InjectMocks
    private PedidoService service;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("X-Wing Starfighter");
        producto.setPrecio(BigDecimal.valueOf(239.99));
        producto.setStock(10);
    }

    @Test
    void crearDirecto_productoExistente_creaPedido() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any())).thenReturn(producto);
        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pedido pedido = service.crearDirecto(1L, 2, 10L, "Santiago",
                "TARJETA", "Calle 48", "Bucaramanga", "Santander",
                "3001234567", "Santiago Admin");

        assertNotNull(pedido);
        assertEquals(10L, pedido.getUsuarioId());
        assertEquals("Santiago", pedido.getUsuarioNombre());
        assertEquals("TARJETA", pedido.getMetodoPago());
        assertEquals("CONFIRMADO", pedido.getEstado());
        assertEquals("Calle 48", pedido.getDireccionEnvio());
        assertEquals("Bucaramanga", pedido.getCiudadEnvio());
        assertEquals(1, pedido.getItems().size());
        assertEquals(8, producto.getStock());
    }

    @Test
    void crearDirecto_productoNoExistente_lanzaExcepcion() {
        when(productoRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                service.crearDirecto(99L, 1, 10L, "Test", "EFECTIVO",
                        "Dir", "Ciudad", "Depto", "123", "Dest"));
    }

    @Test
    void crearDirecto_stockInsuficiente_lanzaExcepcion() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.crearDirecto(1L, 100, 10L, "Test", "EFECTIVO",
                        "Dir", "Ciudad", "Depto", "123", "Dest"));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void crearDirecto_descuentaStock() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any())).thenReturn(producto);
        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        service.crearDirecto(1L, 3, 10L, "Test", "EFECTIVO",
                "Dir", "Ciudad", "Depto", "123", "Dest");
        assertEquals(7, producto.getStock());
        verify(productoRepo).save(producto);
    }

    @Test
    void listarPorUsuario_devuelvePedidos() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioId(10L);
        when(pedidoRepo.findByUsuarioIdOrderByFechaDesc(10L)).thenReturn(List.of(pedido));

        List<Pedido> resultado = service.listarPorUsuario(10L);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
    }

    @Test
    void listarPorUsuario_sinPedidos_devuelveLista() {
        when(pedidoRepo.findByUsuarioIdOrderByFechaDesc(99L)).thenReturn(List.of());
        List<Pedido> resultado = service.listarPorUsuario(99L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarTodos_devuelveTodos() {
        Pedido p1 = new Pedido();
        p1.setId(1L);
        Pedido p2 = new Pedido();
        p2.setId(2L);
        when(pedidoRepo.findAll()).thenReturn(List.of(p1, p2));

        List<Pedido> resultado = service.listarTodos();
        assertEquals(2, resultado.size());
    }

    @Test
    void actualizarEstado_entregado_actualizaCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("CONFIRMADO");
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = service.actualizarEstado(1L, "ENTREGADO");
        assertEquals("ENTREGADO", resultado.getEstado());
    }

    @Test
    void actualizarEstado_cancelado_actualizaCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("CONFIRMADO");
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = service.actualizarEstado(1L, "CANCELADO");
        assertEquals("CANCELADO", resultado.getEstado());
    }

    @Test
    void actualizarEstado_estadoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                service.actualizarEstado(1L, "INVALIDO"));
    }

    @Test
    void actualizarEstado_pedidoNoExistente_lanzaExcepcion() {
        when(pedidoRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                service.actualizarEstado(99L, "ENTREGADO"));
    }
}
