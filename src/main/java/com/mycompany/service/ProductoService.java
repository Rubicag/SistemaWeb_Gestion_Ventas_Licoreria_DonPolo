
package com.mycompany.service;

import com.mycompany.dto.ProductoDTO;
import com.mycompany.model.Producto;
import com.mycompany.model.Categoria;
import com.mycompany.model.Proveedor;
import com.mycompany.repository.ProductoRepository;
import com.mycompany.repository.CategoriaRepository;
import com.mycompany.repository.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de Productos de licorería
 * @author LUIGGI
 */
@Service
@Transactional
public class ProductoService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;

    // CRUD Básico
    
    public List<Producto> listarTodos() {
        logger.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        logger.info("Listando productos activos");
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarDisponibles() {
        logger.info("Listando productos con stock disponible");
        return productoRepository.findProductosDisponibles();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras);
    }

    public List<Producto> buscar(String termino) {
        return productoRepository.buscarPorNombreOMarca(termino);
    }

    // Gestión de Inventario
    
    public List<Producto> obtenerProductosConBajoStock() {
        logger.warn("Obteniendo productos con bajo stock");
        return productoRepository.findProductosConBajoStock();
    }

    public boolean verificarDisponibilidad(Integer idProducto, int cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(idProducto);
        if (productoOpt.isEmpty()) {
            return false;
        }
        return productoOpt.get().hayStock(cantidad);
    }

    public void reducirStock(Integer idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));
        
        producto.reducirStock(cantidad);
        productoRepository.save(producto);
        logger.info("Stock reducido: {} unidades del producto {}", cantidad, producto.getNombre());
        
        if (producto.necesitaReposicion()) {
            logger.warn("ALERTA: Producto {} necesita reposición. Stock actual: {}", 
                producto.getNombre(), producto.getStock());
        }
    }

    public void aumentarStock(Integer idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + idProducto));
        
        producto.aumentarStock(cantidad);
        productoRepository.save(producto);
        logger.info("Stock aumentado: {} unidades del producto {}", cantidad, producto.getNombre());
    }

    // CRUD con Validaciones
    
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        logger.info("Guardando producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    public Producto actualizar(Producto producto) {
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio para actualizar");
        }
        if (!productoRepository.existsById(producto.getIdProducto())) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + producto.getIdProducto());
        }
        validarProducto(producto);
        logger.info("Actualizando producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
        logger.info("Producto desactivado: {}", producto.getNombre());
    }

    public void activar(Integer id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        producto.setActivo(true);
        productoRepository.save(producto);
        logger.info("Producto activado: {}", producto.getNombre());
    }

    // Consultas por Categoría y Proveedor
    
    public List<Producto> listarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoriaId(idCategoria);
    }

    public List<Producto> listarPorProveedor(Integer idProveedor) {
        return productoRepository.findByProveedorId(idProveedor);
    }

    public List<Producto> listarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByRangoPrecio(precioMin, precioMax);
    }

    // Estadísticas
    
    public long contarActivos() {
        return productoRepository.contarProductosActivos();
    }

    // Productos destacados (lista inmutable)
    public List<String> obtenerProductosDestacados() {
        return List.of("Ron Cartavio", "Whisky Johnnie Walker", "Vodka Absolut", 
                       "Pisco Queirolo", "Cerveza Cusqueña");
    }

    // Conversión DTO
    
    public ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setCodigoBarras(producto.getCodigoBarras());
        dto.setMarca(producto.getMarca());
        dto.setPresentacion(producto.getPresentacion());
        dto.setGradoAlcoholico(producto.getGradoAlcoholico());
        dto.setActivo(producto.getActivo());
        
        if (producto.getCategoria() != null) {
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }
        
        if (producto.getProveedor() != null) {
            dto.setIdProveedor(producto.getProveedor().getIdProveedor());
            dto.setNombreProveedor(producto.getProveedor().getNombre());
        }
        
        return dto;
    }

    public Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 10);
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setMarca(dto.getMarca());
        producto.setPresentacion(dto.getPresentacion());
        producto.setGradoAlcoholico(dto.getGradoAlcoholico());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + dto.getIdCategoria()));
            producto.setCategoria(categoria);
        }
        
        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + dto.getIdProveedor()));
            producto.setProveedor(proveedor);
        }
        
        return producto;
    }

    public List<ProductoDTO> convertirListaADTO(List<Producto> productos) {
        return productos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    // Validaciones
    
    private void validarProducto(Producto producto) {
        if (producto.getCodigoBarras() != null) {
            Optional<Producto> existente = productoRepository.findByCodigoBarras(producto.getCodigoBarras());
            if (existente.isPresent() && !existente.get().getIdProducto().equals(producto.getIdProducto())) {
                throw new IllegalArgumentException("Ya existe un producto con código de barras: " + producto.getCodigoBarras());
            }
        }
        
        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        
        if (producto.getProveedor() == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio");
        }
    }
    
    // Métodos legacy (compatibilidad)
    @Deprecated
    public List<Producto> obtenerProductos() {
        return listarTodos();
    }
    
    @Deprecated
    public void agregarProducto(Producto producto) {
        guardar(producto);
    }
    
    @Deprecated
    public void eliminarProducto(int productoId) {
        eliminar(productoId);
    }
    
    @Deprecated
    public Producto buscarProductoPorId(int id) {
        return buscarPorId(id).orElse(null);
    }
}