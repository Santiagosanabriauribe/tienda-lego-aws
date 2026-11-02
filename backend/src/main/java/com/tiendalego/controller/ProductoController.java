package com.tiendalego.controller;

import com.tiendalego.model.Producto;
import com.tiendalego.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "CRUD de productos/sets LEGO")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar productos activos")
    public List<Producto> listar(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return service.listarPorCategoria(categoriaId);
        }
        return service.listarActivos();
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos los productos")
    public List<Producto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear producto")
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto,
                                           @RequestParam Long categoriaId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crear(producto, categoriaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody Producto producto,
                                                @RequestParam(required = false) Long categoriaId) {
        return service.actualizar(id, producto, categoriaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
