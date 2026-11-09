
package com.example.vista;

// Entidades
import com.example.entidad.producto;
import com.example.entidad.categoria;
import com.example.entidad.proveedor;

// Servicios
import com.example.servicio.ProductoServicio;
import com.example.servicio.CategoriaServicio;
import com.example.servicio.ProveedorServicio;

// Componentes Vaadin
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

// Navegación
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.RouteAlias;


// Java
import java.math.BigDecimal;

@Route(value = "productos", layout = com.example.base.ui.MainLayout.class)
@RouteAlias(value = "", layout = com.example.base.ui.MainLayout.class)
@PageTitle("Productos")
@Menu(order = 1, title = "Productos")


public class ProductosView extends VerticalLayout {

    private final ProductoServicio productoServicio;
    private final CategoriaServicio categoriaServicio;
    private final ProveedorServicio proveedorServicio;

    private final Grid<producto> tablaProductos;
    private final TextField campoBusqueda;

    public ProductosView(
            ProductoServicio productoServicio,
            CategoriaServicio categoriaServicio,
            ProveedorServicio proveedorServicio) {

        this.productoServicio = productoServicio;
        this.categoriaServicio = categoriaServicio;
        this.proveedorServicio = proveedorServicio;

        // Titulo
        H2 titulo = new H2("Gestión de productos");

        // Campo de búsqueda
        campoBusqueda = new TextField();
        campoBusqueda.setPlaceholder("Buscar producto...");
        campoBusqueda.setClearButtonVisible(true);

        Button botonBuscar = new Button("Buscar",
                event -> buscarProductos());

        campoBusqueda.addValueChangeListener(
                event -> buscarProductos());

        // Botón registrar
        Button botonNuevo = new Button("Nuevo producto",
                event -> abrirFormulario(null));

        botonNuevo.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout barraHerramientas =
                new HorizontalLayout(
                        campoBusqueda,
                        botonBuscar,
                        botonNuevo);

        barraHerramientas.setWidthFull();
        barraHerramientas.setAlignItems(
                Alignment.BASELINE);

        // Tabla
        tablaProductos = new Grid<>(producto.class, false);

        tablaProductos.addColumn(producto::getIdProducto)
                .setHeader("ID")
                .setAutoWidth(true);

        tablaProductos.addColumn(producto::getNombreArticulo)
                .setHeader("Producto")
                .setAutoWidth(true);

        tablaProductos.addColumn(producto::getPrecioProducto)
                .setHeader("Precio")
                .setAutoWidth(true);

        tablaProductos.addColumn(producto::getStock)
                .setHeader("Stock")
                .setAutoWidth(true);

        tablaProductos.addColumn(
                        p -> p.getCategoria().getNombreCategoria())
                .setHeader("Categoría")
                .setAutoWidth(true);

        tablaProductos.addColumn(
                        p -> p.getProveedor().getNombreProveedores())
                .setHeader("Proveedor")
                .setAutoWidth(true);

        // Columna de acciones
        tablaProductos.addComponentColumn(p -> {

            Button editar = new Button("Editar",
                    event -> abrirFormulario(p));

            Button eliminar = new Button("Eliminar",
                    event -> confirmarEliminacion(p));

            eliminar.addThemeVariants(
                    ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editar, eliminar);

        }).setHeader("Acciones");

        tablaProductos.setSizeFull();

        // Cargar productos
        cargarProductos();

        setSizeFull();

        add(titulo, barraHerramientas, tablaProductos);
    }

    // Consultar todos los productos
    private void cargarProductos() {
        tablaProductos.setItems(
                productoServicio.listars());
    }

    // Buscar productos por nombre
    private void buscarProductos() {

        String texto = campoBusqueda.getValue();

        if (texto == null || texto.isBlank()) {
            cargarProductos();
        } else {
            tablaProductos.setItems(
                    productoServicio.buscarPorNombre(texto));
        }
    }

    // Abrir formulario de registro o edición
    private void abrirFormulario(producto productoActual) {

        boolean esNuevo = productoActual == null;

        Dialog dialogo = new Dialog();

        dialogo.setHeaderTitle(
                esNuevo ? "Registrar producto" : "Editar producto");

        // Campos
        TextField nombre = new TextField("Nombre del producto");

        BigDecimalField precio =
                new BigDecimalField("Precio");

        IntegerField stock =
                new IntegerField("Stock");

        ComboBox<categoria> categoriaCombo =
                new ComboBox<>("Categoría");

        ComboBox<proveedor> proveedorCombo =
                new ComboBox<>("Proveedor");

        // Cargar categorías y proveedores
        categoriaCombo.setItems(
                categoriaServicio.listarCategorias());

        categoriaCombo.setItemLabelGenerator(
                categoria::getNombreCategoria);

        proveedorCombo.setItems(
                proveedorServicio.listarProveedores());

        proveedorCombo.setItemLabelGenerator(
                proveedor::getNombreProveedores);

        // Configuración
        nombre.setRequired(true);
        precio.setRequired(true);
        stock.setRequired(true);

        stock.setMin(0);

        nombre.setWidthFull();
        precio.setWidthFull();
        stock.setWidthFull();
        categoriaCombo.setWidthFull();
        proveedorCombo.setWidthFull();

        // Si estamos editando, cargar datos actuales
        if (!esNuevo) {

            nombre.setValue(
                    productoActual.getNombreArticulo());

            precio.setValue(
                    productoActual.getPrecioProducto());

            stock.setValue(
                    productoActual.getStock());

            categoriaCombo.setValue(
                    productoActual.getCategoria());

            proveedorCombo.setValue(
                    productoActual.getProveedor());
        }

        // Botón guardar
        Button guardar = new Button("Guardar", event -> {

            try {

                // Validar campos obligatorios
                if (nombre.isEmpty()
                        || precio.isEmpty()
                        || stock.isEmpty()
                        || categoriaCombo.isEmpty()
                        || proveedorCombo.isEmpty()) {

                    Notification.show(
                            "Completa todos los campos obligatorios.");

                    return;
                }

                // Validar que el precio no sea negativo
                if (precio.getValue().compareTo(BigDecimal.ZERO) < 0) {

                    Notification.show(
                            "El precio no puede ser negativo.");

                    return;
                }

                producto datos = new producto();

                datos.setNombreArticulo(
                        nombre.getValue().trim());

                datos.setPrecioProducto(
                        precio.getValue());

                datos.setStock(stock.getValue());

                datos.setCategoria(
                        categoriaCombo.getValue());

                datos.setProveedor(
                        proveedorCombo.getValue());

                if (esNuevo) {

                    productoServicio.guardar(datos);

                    Notification.show(
                            "Producto registrado correctamente.");

                } else {

                    productoServicio.actualizar(
                            productoActual.getIdProducto(),
                            datos);

                    Notification.show(
                            "Producto actualizado correctamente.");
                }

                cargarProductos();
                buscarProductos();

                dialogo.close();

            } catch (Exception e) {

                Notification.show(
                        "Error: " + e.getMessage(),
                        5000,
                        Notification.Position.BOTTOM_START);
            }

        });

        guardar.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar",
                event -> dialogo.close());

        HorizontalLayout botones =
                new HorizontalLayout(guardar, cancelar);

        VerticalLayout contenido =
                new VerticalLayout(
                        nombre,
                        precio,
                        stock,
                        categoriaCombo,
                        proveedorCombo,
                        botones);

        contenido.setWidth("400px");

        dialogo.add(contenido);
        dialogo.open();
    }

    // Confirmar eliminación
    private void confirmarEliminacion(producto productoActual) {

        Dialog confirmacion = new Dialog();

        H2 mensaje = new H2(
                "¿Eliminar " + productoActual.getNombreArticulo() + "?");

        Button aceptar = new Button("Sí, eliminar", event -> {

            try {

                productoServicio.eliminar(
                        productoActual.getIdProducto());

                Notification.show(
                        "Producto eliminado correctamente.");

                cargarProductos();
                buscarProductos();

                confirmacion.close();

            } catch (Exception e) {

                Notification.show(
                        "Error: " + e.getMessage());
            }
        });

        aceptar.addThemeVariants(
                ButtonVariant.LUMO_ERROR);

        Button cancelar = new Button("Cancelar",
                event -> confirmacion.close());

        confirmacion.add(
                new VerticalLayout(
                        mensaje,
                        new HorizontalLayout(aceptar, cancelar)));

        confirmacion.open();
    }
}
