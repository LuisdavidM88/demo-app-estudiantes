package com.udla.arquitectura.demo;

import com.udla.arquitectura.demo.exception.ProductoNoEncontradoException;
import com.udla.arquitectura.demo.model.Producto;
import com.udla.arquitectura.demo.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductoCacheTest {

    @Autowired
    ProductoService service;

    private long medir(Runnable accion) {
        long inicio = System.nanoTime();
        accion.run();
        return (System.nanoTime() - inicio) / 1_000_000;
    }

    @Test
    void laSegundaConsultaSeResuelveDesdeCache() {
        long primera = medir(() -> service.buscarPorId(1L));
        long segunda = medir(() -> service.buscarPorId(1L));
        assertTrue(primera >= 1400, "La primera consulta debe llegar al repositorio: " + primera + " ms");
        assertTrue(segunda < 200, "La segunda consulta debe salir de cache: " + segunda + " ms");
    }

    @Test
    void crearUnProductoInvalidaLaListaEnCache() {
        int antes = service.listarTodos().size();
        service.crear(new Producto(null, "Webcam", "Perifericos", 25.0));
        assertEquals(antes + 1, service.listarTodos().size());
    }

    @Test
    void unIdInexistenteLanzaExcepcionDeDominio() {
        assertThrows(ProductoNoEncontradoException.class, () -> service.buscarPorId(999L));
    }
}
