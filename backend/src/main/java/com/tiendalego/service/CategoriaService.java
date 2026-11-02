package com.tiendalego.service;

import com.tiendalego.model.Categoria;
import com.tiendalego.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listarActivas() {
        return repo.findByActivaTrue();
    }

    public List<Categoria> listarTodas() {
        return repo.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        categoria.setId(null);
        categoria.setActiva(true);
        return repo.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, Categoria datos) {
        return repo.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setDescripcion(datos.getDescripcion());
            existente.setActiva(datos.isActiva());
            return repo.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        return repo.findById(id).map(cat -> {
            repo.delete(cat);
            return true;
        }).orElse(false);
    }
}
