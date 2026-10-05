package com.udla.arquitectura.demo.repository;

import com.udla.arquitectura.demo.model.Producto;

import java.util.List;

/**
 * Contrato de acceso a datos del catálogo. Tanto el repositorio real como su
 * proxy lo implementan, lo que permite sustituir uno por otro sin que la capa
 * de servicio perciba la diferencia.
 */
public interface ProductoRepository {

    List<Producto> listarTodos();

    Producto buscarPorId(Long id);

    Producto guardar(Producto producto);

    Producto actualizar(Long id, Producto producto);

    void eliminar(Long id);
}
