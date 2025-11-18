package com.mycompany.service;

import com.mycompany.model.Categoria;
import java.util.List;

public interface CategoriaService {
    List<Categoria> listarCategorias();
    Categoria obtenerCategoriaPorId(Integer id);
    void guardarCategoria(Categoria categoria);
    void eliminarCategoria(Integer id);
}
