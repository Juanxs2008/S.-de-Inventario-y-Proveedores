package com.example.entidad;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre_articulo", nullable = false)
    private String nombreArticulo;

    @Column(name = "precio_producto", nullable = false)
    private BigDecimal precioProducto;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private proveedor proveedor;

    public producto() {
    }
}
