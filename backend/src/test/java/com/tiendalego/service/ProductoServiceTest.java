package com.tiendalego.service;

import com.tiendalego.model.Categoria;
import com.tiendalego.model.Producto;
import com.tiendalego.repository.CategoriaRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private ProductoService service;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Star Wars", "Sets de Star Wars");
        categoria.setId(1L);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("X-Wing");
        producto.setPrecio(BigDecimal.valueOf(239.99));
        producto.setStock(10);
        producto.setCategoria(categoria);
    }

    @Test
    void listarActivos_devuelveProductos() {
        when(productoRepo.findByActivoTrue()).thenReturn(List.of(producto));
        List<Producto> resultado = service.listarActivos();
        assertEquals(1, resultado.size());
    }

    @Test
    void crear_categoriaExistente_creaProducto() {
        when(categoriaRepo.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepo.save(any())).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            p.setId(2L);
            return p;
        });

        Producto nuevo = new Producto();
        nuevo.setNombre("AT-AT");
        nuevo.setPrecio(BigDecimal.valueOf(799.99));
        nuevo.setStock(3);

        Producto creado = service.crear(nuevo, 1L);
        assertTrue(creado.isActivo());
        assertEquals(categoria, creado.getCategoria());
    }

    @Test
    void crear_categoriaNoExistente_lanzaExcepcion() {
        when(categoriaRepo.findById(99L)).thenReturn(Optional.empty());
        Producto nuevo = new Producto();
        assertThrows(IllegalArgumentException.class, () -> service.crear(nuevo, 99L));
    }

    @Test
    void eliminar_existente_devuelveTrue() {
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        assertTrue(service.eliminar(1L));
        verify(productoRepo).delete(producto);
    }
}
