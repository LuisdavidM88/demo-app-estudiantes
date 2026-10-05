package com.udla.arquitectura.demo.repository;

import com.udla.arquitectura.demo.exception.ProductoNoEncontradoException;
import com.udla.arquitectura.demo.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sujeto real dentro del patrón Proxy. Simula una base de datos con una
 * latencia artificial que permite apreciar el efecto de la caché.
 */
@Repository("repositorioReal")
public class RepositorioProductoEnMemoria implements ProductoRepository {

    private static final long LATENCIA_SIMULADA_MS = 1500;

    private final Map<Long, Producto> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(5);

    public RepositorioProductoEnMemoria() {
        List.of(
                new Producto(1L, "Teclado mecanico", "Perifericos", 45.90),
                new Producto(2L, "Monitor 27\"", "Pantallas", 189.00),
                new Producto(3L, "Mouse inalambrico", "Perifericos", 19.50),
                new Producto(4L, "Laptop 14\"", "Computadores", 780.00),
                new Producto(5L, "Audifonos USB-C", "Audio", 32.75)
        ).forEach(p -> datos.put(p.id(), p));
    }

    @Override
    public List<Producto> listarTodos() {
        simularLatencia();
        return datos.values().stream()
                .sorted(Comparator.comparing(Producto::id))
                .toList();
    }

    @Override
    public Producto buscarPorId(Long id) {
        simularLatencia();
        Producto producto = datos.get(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException(id);
        }
        return producto;
    }

    @Override
    public Producto guardar(Producto producto) {
        simularLatencia();
        Long id = secuencia.incrementAndGet();
        Producto nuevo = new Producto(id, producto.nombre(), producto.categoria(), producto.precio());
        datos.put(id, nuevo);
        return nuevo;
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        simularLatencia();
        if (!datos.containsKey(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        Producto actualizado = new Producto(id, producto.nombre(), producto.categoria(), producto.precio());
        datos.put(id, actualizado);
        return actualizado;
    }

    @Override
    public void eliminar(Long id) {
        simularLatencia();
        if (datos.remove(id) == null) {
            throw new ProductoNoEncontradoException(id);
        }
    }

    private void simularLatencia() {
        try {
            Thread.sleep(LATENCIA_SIMULADA_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
