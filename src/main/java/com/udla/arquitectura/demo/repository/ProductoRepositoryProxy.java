package com.udla.arquitectura.demo.repository;

import com.udla.arquitectura.demo.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

/**
 * Implementación del patrón Proxy sobre el repositorio de productos.
 *
 * El proxy comparte la interfaz del sujeto real y se interpone en cada acceso:
 * valida los datos de entrada antes de delegar y registra el tiempo que toma
 * cada operación. Al estar marcado como {@code @Primary}, Spring lo inyecta en
 * el servicio en lugar del repositorio real, de modo que el servicio sigue
 * dependiendo únicamente de la abstracción.
 */
@Primary
@Repository
public class ProductoRepositoryProxy implements ProductoRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductoRepositoryProxy.class);

    private final ProductoRepository real;

    public ProductoRepositoryProxy(@Qualifier("repositorioReal") ProductoRepository real) {
        this.real = real;
    }

    @Override
    public List<Producto> listarTodos() {
        return medir("listarTodos", real::listarTodos);
    }

    @Override
    public Producto buscarPorId(Long id) {
        validarId(id);
        return medir("buscarPorId(" + id + ")", () -> real.buscarPorId(id));
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        return medir("guardar", () -> real.guardar(producto));
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        validarId(id);
        validarProducto(producto);
        return medir("actualizar(" + id + ")", () -> real.actualizar(id, producto));
    }

    @Override
    public void eliminar(Long id) {
        validarId(id);
        medir("eliminar(" + id + ")", () -> {
            real.eliminar(id);
            return null;
        });
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser un numero positivo");
        }
    }

    private void validarProducto(Producto p) {
        if (p == null || p.nombre() == null || p.nombre().isBlank()
                || p.categoria() == null || p.categoria().isBlank() || p.precio() < 0) {
            throw new IllegalArgumentException("Producto invalido: nombre, categoria y precio >= 0 son obligatorios");
        }
    }

    private <T> T medir(String operacion, Supplier<T> accion) {
        long inicio = System.nanoTime();
        try {
            return accion.get();
        } finally {
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            log.info("[PROXY] repositorio.{} -> {} ms", operacion, ms);
        }
    }
}
