package com.example.repositorio;

import com.example.entidad.producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepositorio
        extends JpaRepository<producto, Integer> {
}

