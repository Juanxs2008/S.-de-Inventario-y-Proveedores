package com.example.servicio;

import com.example.entidad.producto;
import com.example.exception.ReglaNegocioException;
import com.example.repositorio.ProductoRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    private static final Logger log =
            LoggerFactory.getLogger(ProductoServicio.class);

    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<producto> listars() {
        return productoRepositorio.findAll();
    }

    public List<producto> buscarPorNombre(String texto) {
        return productoRepositorio.findByNombreArticuloContainingIgnoreCase(texto);
    }

    public Optional<producto> buscar(Integer id) {
        return productoRepositorio.findById(id);
    }

    public producto guardar(producto producto) {
        validar(producto);
        producto guardado = productoRepositorio.save(producto);
        log.info(" creado: {}", guardado);
        return guardado;
    }

    public producto actualizar(Integer id, producto datosNuevos) {
        producto existente = productoRepositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe el con ID: " + id));

        validar(datosNuevos);

        existente.setNombreArticulo(datosNuevos.getNombreArticulo().trim());
        existente.setPrecioProducto(datosNuevos.getPrecioProducto());
        existente.setStock(datosNuevos.getStock());
        existente.setCategoria(datosNuevos.getCategoria());
        existente.setProveedor(datosNuevos.getProveedor());

        producto actualizado = productoRepositorio.save(existente);
        log.info(" actualizado: {}", actualizado);
        return actualizado;
    }

    public void eliminar(Integer id) {
        if (!productoRepositorio.existsById(id)) {
            throw new ReglaNegocioException(
                    "No existe el con ID: " + id);
        }
        productoRepositorio.deleteById(id);
        log.info(" eliminado: id={}", id);
    }

    public BigDecimal calcularValorInventario() {
        return productoRepositorio.findAll().stream()
                .map(p -> p.getPrecioProducto()
                        .multiply(BigDecimal.valueOf(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validar(producto p) {
        if (p.getNombreArticulo() == null || p.getNombreArticulo().isBlank()) {
            log.warn("Validación fallida: nombre vacío");
            throw new ReglaNegocioException(
                    "El nombre del Articulo es obligatorio.");
        }
        if (p.getPrecioProducto() == null
                || p.getPrecioProducto().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException(
                    "El precio debe ser mayor o igual a cero.");
        }
        if (p.getStock() == null || p.getStock() < 0) {
            throw new ReglaNegocioException(
                    "El stock debe ser mayor o igual a cero.");
        }
        if (p.getCategoria() == null) {
            throw new ReglaNegocioException(
                    "Debes seleccionar una categoría.");
        }
        if (p.getProveedor() == null) {
            throw new ReglaNegocioException(
                    "Debes seleccionar un proveedor.");
        }
    }
}
