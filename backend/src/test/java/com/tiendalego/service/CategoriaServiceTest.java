package com.tiendalego.service;

import com.tiendalego.model.Categoria;
import com.tiendalego.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Star Wars", "Sets de Star Wars");
        categoria.setId(1L);
    }

    @Test
    void listarActivas_devuelveSoloActivas() {
        when(repo.findByActivaTrue()).thenReturn(List.of(categoria));
        List<Categoria> resultado = service.listarActivas();
        assertEquals(1, resultado.size());
        assertEquals("Star Wars", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_existente_devuelveCategoria() {
        when(repo.findById(1L)).thenReturn(Optional.of(categoria));
        Optional<Categoria> resultado = service.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Star Wars", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_noExistente_devuelveVacio() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        Optional<Categoria> resultado = service.buscarPorId(99L);
        assertFalse(resultado.isPresent());
    }

    @Test
    void crear_asignaActivaTrue() {
        when(repo.save(any(Categoria.class))).thenAnswer(inv -> {
            Categoria c = inv.getArgument(0);
            c.setId(2L);
            return c;
        });
        Categoria nueva = new Categoria("City", "Sets de City");
        Categoria creada = service.crear(nueva);
        assertTrue(creada.isActiva());
        assertNull(nueva.getId() == 2L ? null : "debe tener id");
    }

    @Test
    void eliminar_existente_devuelveTrue() {
        when(repo.findById(1L)).thenReturn(Optional.of(categoria));
        assertTrue(service.eliminar(1L));
        verify(repo).delete(categoria);
    }

    @Test
    void eliminar_noExistente_devuelveFalse() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertFalse(service.eliminar(99L));
        verify(repo, never()).delete(any());
    }
}
