package com.informatingconfig.spring.app1.springboot_applications.dto;

import com.informatingconfig.spring.app1.springboot_applications.Models.Producto;

public class ProductoResponseDTO {

    private Integer id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private Integer categoriaId;
    private String categoriaNombre;
    private String categoriaDescripcion;

    public ProductoResponseDTO() {
    }

    public static ProductoResponseDTO fromEntity(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
            dto.setCategoriaNombre(p.getCategoria().getNombre());
            dto.setCategoriaDescripcion(p.getCategoria().getDescripcion());
        }
        return dto;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public String getCategoriaDescripcion() { return categoriaDescripcion; }
    public void setCategoriaDescripcion(String categoriaDescripcion) { this.categoriaDescripcion = categoriaDescripcion; }
}