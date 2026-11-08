package com.example.vista;
import com.example.entidad.categoria;
import com.example.servicio.CategoriaServicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Menu;

@Route(value = "categorias", layout = com.example.base.ui.MainLayout.class)
@PageTitle("Categorías")
@Menu(order = 2, title = "Categorías")

public class CategoriasView extends VerticalLayout {

    private final CategoriaServicio categoriaServicio;

    private final Grid<categoria> tablaCategorias =
            new Grid<>(categoria.class, false);

    public CategoriasView(CategoriaServicio categoriaServicio) {

        this.categoriaServicio = categoriaServicio;

        H2 titulo = new H2("Gestión de categorías");

        // Columna del ID
        tablaCategorias.addColumn(categoria::getIdCategoria)
                .setHeader("ID");

        // Columna del nombre
        tablaCategorias.addColumn(categoria::getNombreCategoria)
                .setHeader("Nombre de la categoría");

        // Botón para editar
        tablaCategorias.addComponentColumn(categoria -> {

            Button editar = new Button("Editar", evento -> {
                abrirFormulario(categoria);
            });

            return editar;

        }).setHeader("Acciones");

        // Botón para registrar
        Button nuevaCategoria = new Button(
                "Nueva categoría",
                evento -> abrirFormulario(null)
        );

        tablaCategorias.setWidthFull();

        add(titulo, nuevaCategoria, tablaCategorias);

        cargarCategorias();
    }

    // Consultar categorías
    private void cargarCategorias() {

        tablaCategorias.setItems(
                categoriaServicio.listarCategorias()
        );
    }

    // Formulario para registrar o editar
    private void abrirFormulario(categoria categoriaActual) {

        Dialog dialogo = new Dialog();

        TextField nombre = new TextField("Nombre de la categoría");

        Button guardar = new Button("Guardar", evento -> {

            try {

                if (nombre.isEmpty()) {
                    Notification.show("Escribe el nombre de la categoría");
                    return;
                }

                categoria datos = new categoria();

                datos.setNombreCategoria(nombre.getValue());

                if (categoriaActual == null) {

                    categoriaServicio.guardarCategoria(datos);

                    Notification.show("Categoría registrada correctamente");

                } else {

                    categoriaServicio.actualizarCategoria(
                            categoriaActual.getIdCategoria(),
                            datos
                    );

                    Notification.show("Categoría actualizada correctamente");
                }

                dialogo.close();

                cargarCategorias();

            } catch (Exception e) {

                Notification.show(
                        "Error: " + e.getMessage(),
                        5000,
                        Notification.Position.MIDDLE
                );
            }
        });

        Button cancelar = new Button("Cancelar", evento -> {
            dialogo.close();
        });

        if (categoriaActual == null) {

            dialogo.setHeaderTitle("Registrar categoría");

        } else {

            dialogo.setHeaderTitle("Editar categoría");

            nombre.setValue(
                    categoriaActual.getNombreCategoria()
            );
        }

        HorizontalLayout botones =
                new HorizontalLayout(guardar, cancelar);

        VerticalLayout contenido =
                new VerticalLayout(nombre, botones);

        dialogo.add(contenido);

        dialogo.open();
    }
}
