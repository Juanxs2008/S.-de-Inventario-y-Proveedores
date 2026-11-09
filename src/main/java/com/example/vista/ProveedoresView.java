package com.example.vista;

import com.example.entidad.proveedor;
import com.example.servicio.ProveedorServicio;

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

@Route(value = "proveedores", layout = com.example.base.ui.MainLayout.class)
@PageTitle("Proveedores")
@Menu(order = 3, title = "Proveedores")

public class ProveedoresView extends VerticalLayout {

    private final ProveedorServicio proveedorServicio;

    private final Grid<proveedor> tablaProveedores =
            new Grid<>(proveedor.class, false);

    public ProveedoresView(ProveedorServicio proveedorServicio) {

        this.proveedorServicio = proveedorServicio;

        H2 titulo = new H2("Gestión de proveedores");

        // Columna del ID
        tablaProveedores.addColumn(proveedor::getIdProveedor)
                .setHeader("ID");

        // Columna del nombreS
        tablaProveedores.addColumn(proveedor::getNombreProveedores)
                .setHeader("Nombre del proveedor");

        // Columna del teléfono
        tablaProveedores.addColumn(proveedor::getTelefonoContacto)
                .setHeader("Teléfono");

        // Columna del correo
        tablaProveedores.addColumn(proveedor::getCorreoElectronico)
                .setHeader("Correo electrónico");

        // Botones de acciones
        tablaProveedores.addComponentColumn(proveedor -> {

            Button editar = new Button("Editar", evento -> {
                abrirFormulario(proveedor);
            });

            Button eliminar = new Button("Eliminar", evento -> {
                confirmarEliminacion(proveedor);
            });

            return new HorizontalLayout(editar, eliminar);

        }).setHeader("Acciones");

        // Botón para registrar un proveedor
        Button nuevoProveedor = new Button(
                "Nuevo proveedor",
                evento -> abrirFormulario(null)
        );

        tablaProveedores.setWidthFull();

        add(titulo, nuevoProveedor, tablaProveedores);

        cargarProveedores();
    }

    // Consultar proveedores
    private void cargarProveedores() {

        tablaProveedores.setItems(
                proveedorServicio.listarProveedores()
        );
    }

    // Formulario para registrar o editar
    private void abrirFormulario(proveedor proveedorActual) {

        Dialog dialogo = new Dialog();

        TextField nombre =
                new TextField("Nombre del proveedor");

        TextField telefono =
                new TextField("Teléfono de contacto");

        TextField correo =
                new TextField("Correo electrónico");

        Button guardar = new Button("Guardar", evento -> {

            try {

                if (nombre.isEmpty()) {

                    Notification.show(
                            "Escribe el nombre del proveedor"
                    );

                    return;
                }

                proveedor datos = new proveedor();

                datos.setNombreProveedores(nombre.getValue());

                datos.setTelefonoContacto(telefono.getValue());

                datos.setCorreoElectronico(correo.getValue());

                if (proveedorActual == null) {

                    proveedorServicio.guardarProveedor(datos);

                    Notification.show(
                            "Proveedor registrado correctamente"
                    );

                } else {

                    proveedorServicio.actualizarProveedor(
                            proveedorActual.getIdProveedor(),
                            datos
                    );

                    Notification.show(
                            "Proveedor actualizado correctamente"
                    );
                }

                dialogo.close();

                cargarProveedores();

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

        if (proveedorActual == null) {

            dialogo.setHeaderTitle("Registrar proveedor");

        } else {

            dialogo.setHeaderTitle("Editar proveedor");

            nombre.setValue(
                    proveedorActual.getNombreProveedores()
            );

            telefono.setValue(
                    proveedorActual.getTelefonoContacto() == null
                            ? ""
                            : proveedorActual.getTelefonoContacto()
            );

            correo.setValue(
                    proveedorActual.getCorreoElectronico() == null
                            ? ""
                            : proveedorActual.getCorreoElectronico()
            );
        }

        HorizontalLayout botones =
                new HorizontalLayout(guardar, cancelar);

        VerticalLayout contenido =
                new VerticalLayout(
                        nombre,
                        telefono,
                        correo,
                        botones
                );

        dialogo.add(contenido);

        dialogo.open();
    }

    // Confirmar eliminación
    private void confirmarEliminacion(proveedor proveedorActual) {

        Dialog confirmacion = new Dialog();

        H2 mensaje = new H2(
                "¿Eliminar este proveedor?"
        );

        Button eliminar = new Button("Sí, eliminar", evento -> {

            try {

                proveedorServicio.eliminarProveedor(
                        proveedorActual.getIdProveedor()
                );

                Notification.show(
                        "Proveedor eliminado correctamente"
                );

                confirmacion.close();

                cargarProveedores();

            } catch (Exception e) {

                Notification.show(
                        "No se pudo eliminar: " + e.getMessage(),
                        5000,
                        Notification.Position.MIDDLE
                );
            }
        });

        Button cancelar = new Button("Cancelar", evento -> {
            confirmacion.close();
        });

        confirmacion.add(
                mensaje,
                new HorizontalLayout(eliminar, cancelar)
        );

        confirmacion.open();
    }
}
