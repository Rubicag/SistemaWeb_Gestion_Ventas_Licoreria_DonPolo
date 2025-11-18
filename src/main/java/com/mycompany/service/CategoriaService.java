package com.mycompany.service;

import com.mycompany.model.Categoria;
import com.mycompany.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de Categorías
 */
@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarActivas() {
        return categoriaRepository.findByActivoTrue();
    }

    public List<Categoria> listarCategoriasConStock() {
        return categoriaRepository.findCategoriasConStock();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria guardar(Categoria categoria) {
        validarCategoria(categoria);
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        if (categoria.getIdCategoria() == null) {
            throw new IllegalArgumentException("El ID de la categoría es obligatorio para actualizar");
        }
        if (!categoriaRepository.existsById(categoria.getIdCategoria())) {
            throw new IllegalArgumentException("Categoría no encontrada con ID: " + categoria.getIdCategoria());
        }
        validarCategoria(categoria);
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        // Verificar si tiene productos asociados
        if (categoria.getCantidadProductos() > 0) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        
        categoriaRepository.deleteById(id);
    }

    public void desactivar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }

    public void activar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        categoria.setActivo(true);
        categoriaRepository.save(categoria);
    }

    public List<Object[]> obtenerEstadisticasProductosPorCategoria() {
        return categoriaRepository.contarProductosPorCategoria();
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        // Validar que no exista otra categoría con el mismo nombre
        Optional<Categoria> existente = categoriaRepository.findByNombre(categoria.getNombre());
        if (existente.isPresent() && !existente.get().getIdCategoria().equals(categoria.getIdCategoria())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
    }

    // Métodos legacy para compatibilidad
    @Deprecated
    public List<Categoria> listarCategorias() {
        return listarTodas();
    }

    @Deprecated
    public Categoria obtenerCategoriaPorId(Integer id) {
        return buscarPorId(id).orElse(null);
    }

    @Deprecated
    public void guardarCategoria(Categoria categoria) {
        guardar(categoria);
    }

    @Deprecated
    public void eliminarCategoria(Integer id) {
        eliminar(id);
    }
}
