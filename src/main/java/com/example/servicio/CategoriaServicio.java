package com.example.servicio;

import com.example.entidad.categoria;
import com.example.exception.ReglaNegocioException;
import com.example.repositorio.CategoriaRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicio {

    private static final Logger log =
            LoggerFactory.getLogger(CategoriaServicio.class);

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
        validarNombre(categoria.getNombreCategoria());

        String nombre = categoria.getNombreCategoria().trim();
        if (categoriaRepositorio.existsByNombreCategoriaIgnoreCase(nombre)) {
            throw new ReglaNegocioException(
                    "Ya existe una categoría con ese nombre.");
        }

        categoria.setNombreCategoria(nombre);
        categoria guardada = categoriaRepositorio.save(categoria);
        log.info("Categoría creada: {}", guardada);
        return guardada;
    }

    public categoria actualizarCategoria(Integer id, categoria datosNuevos) {
        categoria existente = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe la categoría con ID: " + id));

        validarNombre(datosNuevos.getNombreCategoria());

        String nombre = datosNuevos.getNombreCategoria().trim();
        boolean cambioElNombre =
                !existente.getNombreCategoria().equalsIgnoreCase(nombre);

        if (cambioElNombre
                && categoriaRepositorio.existsByNombreCategoriaIgnoreCase(nombre)) {
            throw new ReglaNegocioException(
                    "Ya existe una categoría con ese nombre.");
        }

        existente.setNombreCategoria(nombre);
        categoria actualizada = categoriaRepositorio.save(existente);
        log.info("Categoría actualizada: {}", actualizada);
        return actualizada;
    }

    public void eliminarCategoria(Integer id) {
        if (!categoriaRepositorio.existsById(id)) {
            throw new ReglaNegocioException(
                    "No existe la categoría con ID: " + id);
        }
        try {
            categoriaRepositorio.deleteById(id);
            log.info("Categoría eliminada: id={}", id);
        } catch (DataIntegrityViolationException e) {
            log.warn("No se pudo eliminar la categoría {}: tiene productos", id);
            throw new ReglaNegocioException(
                    "No se puede eliminar: la categoría tiene productos asociados.");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException(
                    "El nombre de la categoría es obligatorio.");
        }
    }
}