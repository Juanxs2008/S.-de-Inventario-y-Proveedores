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

    //Metodos get y set de los atributos de esta clase
    public Integer getIdProveedor() {
        return idProveedor;
    }
    public String getNombreProveedores() {
        return nombreProveedores;
    }
    public void setNombreProveedores(String nombreProveedores) {
        this.nombreProveedores = nombreProveedores;
    }
    public String getTelefonoContacto() {
        return telefonoContacto;
    }
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }




}
