package com.informatingconfig.spring.app1.springboot_applications.services;

import com.informatingconfig.spring.app1.springboot_applications.Models.Categoria;
import com.informatingconfig.spring.app1.springboot_applications.Models.Producto;
import com.informatingconfig.spring.app1.springboot_applications.repositories.CategoriaRepository;
import com.informatingconfig.spring.app1.springboot_applications.repositories.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public InventarioService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @PostConstruct
    public void inicializarDatosBase() {
        if (categoriaRepository.count() == 0) {
            Categoria audio = categoriaRepository.save(new Categoria("Audio", "Micrófonos, interfaces y monitores"));
            Categoria hardware = categoriaRepository.save(new Categoria("Hardware", "Microcontroladores, pantallas y sensores"));
            Categoria redes = categoriaRepository.save(new Categoria("Redes", "Routers, switches y puntos de acceso"));
            categoriaRepository.save(new Categoria("Software", "Licencias e IDEs de desarrollo"));

            productoRepository.save(new Producto("Micrófono USB Blue Yeti", 2450.00, 5, audio));
            productoRepository.save(new Producto("Placa ESP32 WiFi + Bluetooth", 185.50, 15, hardware));
            productoRepository.save(new Producto("Router MikroTik hEX RB750Gr3", 1390.00, 8, redes));
        }
    }

    public Page<Producto> listarProductosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findAll(pageable);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Producto> buscarProductoPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Optional<Categoria> buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }
}