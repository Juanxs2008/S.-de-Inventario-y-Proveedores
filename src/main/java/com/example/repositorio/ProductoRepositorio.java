package com.example.repositorio;

import com.example.entidad.producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepositorio
        extends JpaRepository<producto, Integer> {
    List<producto> findByNombreArticuloContainingIgnoreCase(String texto);
}

