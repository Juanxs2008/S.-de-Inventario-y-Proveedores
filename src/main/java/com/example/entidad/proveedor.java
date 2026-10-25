package com.example.entidad;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "nombre_proveedores", nullable = false)
    private String nombreProveedores;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    public proveedor() {
    }
}
