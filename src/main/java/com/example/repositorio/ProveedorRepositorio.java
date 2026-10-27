package com.example.repositorio;
import com.example.entidad.proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface
ProveedorRepositorio
        extends JpaRepository<proveedor, Integer> {
}
