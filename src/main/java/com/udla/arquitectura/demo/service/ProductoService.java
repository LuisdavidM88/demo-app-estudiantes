package com.udla.arquitectura.demo.service;

import com.udla.arquitectura.demo.model.Producto;
import com.udla.arquitectura.demo.repository.ProductoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio del catálogo.
 *
 * La caché se aplica de forma declarativa: las consultas se almacenan con
 * {@code @Cacheable} y las operaciones de escritura invalidan las entradas
 * afectadas con {@code @CacheEvict}, evitando así servir datos desactualizados.
 */
@Service
public class ProductoService {

    public static final String CACHE_PRODUCTO = "producto";
    public static final String CACHE_LISTA = "productos";

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Cacheable(CACHE_LISTA)
    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    @Cacheable(value = CACHE_PRODUCTO, key = "#id")
    public Producto buscarPorId(Long id) {
        return productoRepository.buscarPorId(id);
    }

    @CacheEvict(value = CACHE_LISTA, allEntries = true)
    public Producto crear(Producto producto) {
        return productoRepository.guardar(producto);
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_PRODUCTO, key = "#id"),
            @CacheEvict(value = CACHE_LISTA, allEntries = true)
    })
    public Producto actualizar(Long id, Producto producto) {
        return productoRepository.actualizar(id, producto);
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_PRODUCTO, key = "#id"),
            @CacheEvict(value = CACHE_LISTA, allEntries = true)
    })
    public void eliminar(Long id) {
        productoRepository.eliminar(id);
    }
}
