package com.example.repositorio;


import com.example.entidad.categoria;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface CategoriaRepositorio
            extends JpaRepository<categoria, Integer> {
    }