package com.example;

import com.example.servicio.CategoriaServicio;
import com.example.servicio.ProveedorServicio;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

import com.example.servicio.ProductoServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css") // Your custom styles
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner probarConsulta(
            ProductoServicio productoServicio,
            CategoriaServicio categoriaServicio,
            ProveedorServicio proveedorServicio) {

        return args -> {

            System.out.println("=== PRODUCTOS ===");
            productoServicio.listars()
                    .forEach(System.out::println);

            System.out.println("=== CATEGORÍAS ===");
            categoriaServicio.listarCategorias()
                    .forEach(categoria ->
                            System.out.println(
                                    categoria.getNombreCategoria()));

            System.out.println("=== PROVEEDORES ===");
            proveedorServicio.listarProveedores()
                    .forEach(proveedor ->
                            System.out.println(
                                    proveedor.getNombreProveedores()));
        };
    }

}




