package com.informatingconfig.spring.app1.springboot_applications.controllers;

import com.informatingconfig.spring.app1.springboot_applications.dto.ProductoRequestDTO;
import com.informatingconfig.spring.app1.springboot_applications.dto.ProductoResponseDTO;
import com.informatingconfig.spring.app1.springboot_applications.Models.Categoria;
import com.informatingconfig.spring.app1.springboot_applications.Models.Producto;
import com.informatingconfig.spring.app1.springboot_applications.services.InventarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class InventarioRestController {

    private final InventarioService inventarioService;

    public InventarioRestController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    //GET para listar con paginacion
    @GetMapping
    public ResponseEntity<?> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Producto> paginaProductos = inventarioService.listarProductosPaginados(page, size);
        List<ProductoResponseDTO> contenido = paginaProductos.getContent()
                .stream()
                .map(ProductoResponseDTO::fromEntity)
                .toList();

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("content", contenido);
        respuesta.put("paginaActual", paginaProductos.getNumber());
        respuesta.put("tamanoPagina", paginaProductos.getSize());
        respuesta.put("totalElementos", paginaProductos.getTotalElements());
        respuesta.put("totalPaginas", paginaProductos.getTotalPages());

        return ResponseEntity.ok(respuesta);
    }

    // GET para ver un registro individual por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<Producto> productoOpt = inventarioService.buscarProductoPorId(id);
        if (productoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "mensaje", "No existe un producto con el ID " + id
            ));
        }
        return ResponseEntity.ok(ProductoResponseDTO.fromEntity(productoOpt.get()));
    }

    //POST para crear un producto validando con @Valid y devolviendo 201 Created
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        Optional<Categoria> categoriaOpt = inventarioService.buscarCategoriaPorId(dto.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "mensaje", "La categoría con ID " + dto.getCategoriaId() + " no existe"
            ));
        }

        Producto nuevo = new Producto(
                dto.getNombre(),
                dto.getPrecio(),
                dto.getStock(),
                categoriaOpt.get()
        );

        Producto guardado = inventarioService.guardarProducto(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponseDTO.fromEntity(guardado));
    }

    //PUT para actualizar un producto existente validando con @Valid
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoRequestDTO dto) {
        Optional<Producto> existenteOpt = inventarioService.buscarProductoPorId(id);
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "mensaje", "No se encontró el producto con ID " + id + " para actualizar"
            ));
        }

        Optional<Categoria> categoriaOpt = inventarioService.buscarCategoriaPorId(dto.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "mensaje", "La categoría con ID " + dto.getCategoriaId() + " no existe"
            ));
        }

        Producto producto = existenteOpt.get();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoriaOpt.get());

        Producto actualizado = inventarioService.guardarProducto(producto);
        return ResponseEntity.ok(ProductoResponseDTO.fromEntity(actualizado));
    }

    //DELETE para eliminar un producto por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        Optional<Producto> existenteOpt = inventarioService.buscarProductoPorId(id);
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", 404,
                    "error", "Not Found",
                    "mensaje", "No existe el producto con ID " + id + " para eliminar"
            ));
        }

        inventarioService.eliminarProducto(id);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "eliminado", true,
                "mensaje", "Producto con ID " + id + " eliminado correctamente"
        ));
    }
}