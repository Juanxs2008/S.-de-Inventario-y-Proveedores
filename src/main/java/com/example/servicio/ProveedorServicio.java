package com.example.servicio;
import com.example.entidad.proveedor;
import com.example.repositorio.ProveedorRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServicio {

    private final ProveedorRepositorio proveedorRepositorio;

    public ProveedorServicio(ProveedorRepositorio proveedorRepositorio) {
        this.proveedorRepositorio = proveedorRepositorio;
    }
    public List<proveedor> listarProveedores() {
        return proveedorRepositorio.findAll();
    }
    public Optional<proveedor> buscarProveedor(Integer id) {
        return proveedorRepositorio.findById(id);
    }
    public proveedor guardarProveedor(proveedor proveedor) {
        return proveedorRepositorio.save(proveedor);
    }
    public proveedor actualizarProveedor(Integer id, proveedor datosNuevos) {
        proveedor proveedorExistente = proveedorRepositorio.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el proveedor con ID: " + id));

        proveedorExistente.setNombreProveedores(
                datosNuevos.getNombreProveedores());

        proveedorExistente.setTelefonoContacto(
                datosNuevos.getTelefonoContacto());

        proveedorExistente.setCorreoElectronico(
                datosNuevos.getCorreoElectronico());

        return proveedorRepositorio.save(proveedorExistente);
    }
    public void eliminarProveedor(Integer id) {
        proveedorRepositorio.deleteById(id);
    }
}

