
package com.example.servicio;

import com.example.entidad.producto;
import com.example.repositorio.ProductoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    // 1. Listar todos los productos
    public List<producto> listarProductos() {
        return productoRepositorio.findAll();
    }

    // 2. Buscar un producto por su ID
    public Optional<producto> buscarProducto(Integer id) {
        return productoRepositorio.findById(id);
    }

    // 3. Registrar o guardar un producto
    public producto guardarProducto(producto producto) {
        return productoRepositorio.save(producto);
    }

    // 4. Actualizar un producto existente
    public producto actualizarProducto(Integer id, producto datosNuevos) {
        producto productoExistente = productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe el producto con ID: " + id));

        productoExistente.setNombreArticulo(
                datosNuevos.getNombreArticulo());

        productoExistente.setPrecioProducto(
                datosNuevos.getPrecioProducto());

        productoExistente.setStock(
                datosNuevos.getStock());

        productoExistente.setCategoria(
                datosNuevos.getCategoria());

        productoExistente.setProveedor(
                datosNuevos.getProveedor());

        return productoRepositorio.save(productoExistente);
    }

    // 5. Eliminar un producto por su ID
    public void eliminarProducto(Integer id) {
        productoRepositorio.deleteById(id);
    }
}
