package com.example.servicio;
import com.example.entidad.categoria;
import com.example.repositorio.CategoriaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }
    public List<categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }
    public Optional<categoria> buscarCategoria(Integer id) {
        return categoriaRepositorio.findById(id);
    }
    public categoria guardarCategoria(categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }
    public categoria actualizarCategoria(Integer id, categoria datosNuevos) {
        categoria categoriaExistente = categoriaRepositorio.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la categoría con ID: " + id));

        categoriaExistente.setNombreCategoria(
                datosNuevos.getNombreCategoria());

        return categoriaRepositorio.save(categoriaExistente);
    }

    public void eliminarCategoria(Integer id) {
        categoriaRepositorio.deleteById(id);
    }
}

