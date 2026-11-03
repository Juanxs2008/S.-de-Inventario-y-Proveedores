package com.example.servicio;

import com.example.entidad.proveedor;
import com.example.exception.ReglaNegocioException;
import com.example.repositorio.ProveedorRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServicio {

    private static final Logger log =
            LoggerFactory.getLogger(ProveedorServicio.class);

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
        validar(proveedor);
        proveedor guardado = proveedorRepositorio.save(proveedor);
        log.info("Proveedor creado: {}", guardado);
        return guardado;
    }

    public proveedor actualizarProveedor(Integer id, proveedor datosNuevos) {
        proveedor existente = proveedorRepositorio.findById(id)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe el proveedor con ID: " + id));

        validar(datosNuevos);

        existente.setNombreProveedores(datosNuevos.getNombreProveedores().trim());
        existente.setTelefonoContacto(datosNuevos.getTelefonoContacto());
        existente.setCorreoElectronico(datosNuevos.getCorreoElectronico());

        proveedor actualizado = proveedorRepositorio.save(existente);
        log.info("Proveedor actualizado: {}", actualizado);
        return actualizado;
    }

    public void eliminarProveedor(Integer id) {
        if (!proveedorRepositorio.existsById(id)) {
            throw new ReglaNegocioException(
                    "No existe el proveedor con ID: " + id);
        }
        try {
            proveedorRepositorio.deleteById(id);
            log.info("Proveedor eliminado: id={}", id);
        } catch (DataIntegrityViolationException e) {
            log.warn("No se pudo eliminar el proveedor {}: tiene productos", id);
            throw new ReglaNegocioException(
                    "No se puede eliminar: el proveedor tiene productos asociados.");
        }
    }

    private void validar(proveedor p) {
        if (p.getNombreProveedores() == null
                || p.getNombreProveedores().isBlank()) {
            throw new ReglaNegocioException(
                    "El nombre del proveedor es obligatorio.");
        }
        String correo = p.getCorreoElectronico();
        if (correo != null && !correo.isBlank() && !correo.contains("@")) {
            throw new ReglaNegocioException(
                    "El correo electrónico no es válido.");
        }
    }
}