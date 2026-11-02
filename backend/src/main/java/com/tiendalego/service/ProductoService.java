package com.tiendalego.service;

import com.tiendalego.model.Categoria;
import com.tiendalego.model.Producto;
import com.tiendalego.repository.CategoriaRepository;
import com.tiendalego.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<Producto> listarActivos() {
        return productoRepo.findByActivoTrue();
    }

    public List<Producto> listarTodos() {
        return productoRepo.findAll();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepo.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepo.findById(id);
    }

    public Producto crear(Producto producto, Long categoriaId) {
        Categoria categoria = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("La categoria con id " + categoriaId + " no existe"));
        producto.setId(null);
        producto.setActivo(true);
        producto.setCategoria(categoria);
        return productoRepo.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto datos, Long categoriaId) {
        return productoRepo.findById(id).map(existente -> {
            if (categoriaId != null) {
                Categoria categoria = categoriaRepo.findById(categoriaId)
                        .orElseThrow(() -> new IllegalArgumentException("La categoria con id " + categoriaId + " no existe"));
                existente.setCategoria(categoria);
            }
            existente.setNombre(datos.getNombre());
            existente.setNumeroSet(datos.getNumeroSet());
            existente.setDescripcion(datos.getDescripcion());
            existente.setPrecio(datos.getPrecio());
            existente.setStock(datos.getStock());
            existente.setImagenUrl(datos.getImagenUrl());
            existente.setActivo(datos.isActivo());
            return productoRepo.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        return productoRepo.findById(id).map(prod -> {
            productoRepo.delete(prod);
            return true;
        }).orElse(false);
    }
}
