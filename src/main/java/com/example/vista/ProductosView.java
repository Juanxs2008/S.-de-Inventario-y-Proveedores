package com.example.vista;

import com.example.entidad.producto;
import com.example.servicio.ProductoServicio;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H2;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Menu;

@Route("productos")
@PageTitle("Productos")
@Menu(order = 1, title = "Productos")

public class ProductosView extends VerticalLayout {

    private final ProductoServicio productoServicio;

    public ProductosView(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;


// el título de la pantalla
        H2 titulo = new H2("Gestión de productos");

//  la tabla
        Grid<producto> tablaProductos = new Grid<>(producto.class, false);

//  las columnas
        tablaProductos.addColumn(producto::getIdProducto)
                .setHeader("ID");
        tablaProductos.addColumn(producto::getNombreArticulo)
                .setHeader("Nombre del producto");
        tablaProductos.addColumn(producto::getPrecioProducto)
                .setHeader("Precio");
        tablaProductos.addColumn(producto::getStock)
                .setHeader("Stock");

// los productos del backend
        tablaProductos.setItems(productoServicio.listars());

// los componentes en la pantalla
        tablaProductos.setWidthFull();
        add(titulo, tablaProductos);

    }
}

