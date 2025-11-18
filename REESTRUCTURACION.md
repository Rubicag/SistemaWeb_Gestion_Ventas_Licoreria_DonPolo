# Reestructuración del Sistema de Gestión de Ventas - Licorería Don Polo

## 📋 Resumen Ejecutivo

Se ha realizado una reestructuración completa del sistema para convertirlo en un verdadero **Sistema de Gestión y Ventas para Licorería**, con arquitectura limpia, validaciones robustas y lógica de negocio bien definida.

---

## 🏗️ Modelo de Dominio Reestructurado

### Entidades Core

#### 1. **Cliente** (NUEVO)
- Separado completamente de Usuario
- Campos: idCliente, nombre, apellido, dni, email, teléfono, dirección
- Validaciones: DNI (8 dígitos), email válido, teléfono (9 dígitos)
- Estado: ACTIVO/INACTIVO
- Relación: 1:N con Ventas

#### 2. **Usuario** (MEJORADO)
- Representa empleados y administradores
- Roles: ADMIN, VENDEDOR, SUPERVISOR
- Campos adicionales: activo, fechaCreación, ultimoAcceso
- Contraseña con BCrypt hash
- Relación: 1:N con Ventas (como vendedor)

#### 3. **Producto** (REESTRUCTURADO)
- Campos mejorados:
  - Precio y descuentos en BigDecimal
  - stock y stockMinimo (Integer)
  - codigoBarras único
  - marca, presentación (750ml, 1L, etc.)
  - gradoAlcoholico (BigDecimal)
  - descripción completa
- Métodos de negocio:
  - `hayStock(cantidad)`: Verificar disponibilidad
  - `reducirStock(cantidad)`: Descontar inventario
  - `aumentarStock(cantidad)`: Incrementar inventario
  - `necesitaReposicion()`: Alerta de stock mínimo
  - `calcularPrecioConDescuento(descuento)`: Precio con promoción
- Relaciones:
  - N:1 con Categoría
  - N:1 con Proveedor
  - 1:N con DetalleVenta
  - 1:N con Promocion

#### 4. **Categoría** (MEJORADO)
- Representa tipos de licores: Vinos, Cervezas, Whisky, Vodka, Ron, Pisco, etc.
- Campos: nombre, descripción, activo
- Relación: 1:N con Productos
- Método: `getCantidadProductos()`

#### 5. **Proveedor** (MEJORADO)
- Campos: nombre, RUC (11 dígitos), email, teléfono, dirección, contacto
- Validaciones: RUC único, email válido
- Estado: ACTIVO/INACTIVO
- Relación: 1:N con Productos
- Método: `getCantidadProductos()`

#### 6. **Venta** (COMPLETAMENTE REESTRUCTURADO)
- Campos:
  - idVenta, fecha (LocalDateTime)
  - Usuario (vendedor) - OBLIGATORIO
  - Cliente (puede ser null para venta sin registro)
  - metodoPago: EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA
  - total, descuento (BigDecimal)
  - comprobante (número de boleta/factura)
  - estado: COMPLETADA, ANULADA, PENDIENTE
  - observaciones
- Métodos de negocio:
  - `agregarDetalle(detalle)`: Agregar producto a venta
  - `eliminarDetalle(detalle)`: Quitar producto
  - `recalcularTotal()`: Calcular total automáticamente
  - `getSubtotal()`: Total antes de descuentos
  - `getCantidadItems()`: Número de líneas de detalle
  - `getCantidadTotalProductos()`: Suma de cantidades
  - `anular()`: Anular venta
- Relación: 1:N con DetalleVenta (cascade ALL, orphanRemoval)

#### 7. **DetalleVenta** (MEJORADO)
- Campos:
  - producto (EAGER fetch)
  - cantidad, precioUnitario, descuento, subtotal (BigDecimal)
- Métodos de negocio:
  - `calcularSubtotal()`: Cálculo automático
  - `aplicarDescuento(monto)`: Descuento fijo
  - `aplicarDescuentoPorcentaje(porcentaje)`: Descuento %
- Validaciones automáticas en @PrePersist/@PreUpdate

#### 8. **Promoción** (REESTRUCTURADO)
- Campos:
  - nombre, descripción
  - descuento (BigDecimal, porcentaje)
  - fechaInicio, fechaFin (LocalDate)
  - activo, fechaCreación
  - Producto (N:1 EAGER)
- Métodos de negocio:
  - `isVigente()`: Verificar si está activa
  - `getDiasRestantes()`: Días hasta expiración
  - `calcularPrecioConDescuento(precioOriginal)`: Precio con promoción
- Validación automática de fechas

---

## 🗃️ Cambios en Persistencia

### Dependencias Agregadas
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    <version>3.5.7</version>
</dependency>
```

### Validaciones Bean Validation
- `@NotNull`, `@NotBlank`: Campos obligatorios
- `@Email`: Validación de emails
- `@Pattern`: DNI, RUC, teléfonos
- `@Min`, `@Max`: Rangos numéricos
- `@DecimalMin`, `@DecimalMax`: Precios y descuentos
- `@Size`: Longitud de textos

### Índices de Base de Datos
- Clientes: dni, email
- Productos: nombre, categoría, proveedor, codigoBarras
- Ventas: fecha, cliente, usuario
- Categorías: nombre
- Proveedores: ruc, nombre
- Promociones: producto, fechas

---

## 🔄 Repositorios con Consultas Personalizadas

### ClienteRepository
- `findByDni(dni)`, `findByEmail(email)`
- `findClientesActivos()`
- `buscarPorNombreOApellido(termino)`
- `findClientesConMasCompras()`
- `contarClientesActivos()`

### ProductoRepository
- `findByCategoria(categoria)`, `findByProveedor(proveedor)`
- `buscarPorNombreOMarca(termino)`
- `findProductosConBajoStock()`
- `findProductosDisponibles()`
- `findByRangoPrecio(min, max)`
- `contarProductosActivos()`

### VentaRepository
- `findByRangoFechas(inicio, fin)`
- `findVentasCompletadasEnRango(inicio, fin)`
- `calcularTotalVentasEnRango(inicio, fin)`
- `contarVentasEnRango(inicio, fin)`
- `calcularVentasDelDia()`

### PromocionRepository
- `findPromocionesVigentes(fecha)`
- `findPromocionVigenteParaProducto(idProducto)`
- `findPromocionesActuales()`
- `contarPromocionesVigentes()`
- `findPromocionesExpiradas()`

### CategoriaRepository
- `findByActivoTrue()`
- `findCategoriasConStock()`
- `contarProductosPorCategoria()`

### ProveedorRepository
- `findProveedoresActivos()`
- `buscarPorNombre(termino)`
- `findProveedoresConMasProductos()`

### UsuarioRepository
- `findByCorreoAndActivoTrue(correo)`
- `findUsuariosActivos()`
- `findByRolAndActivoTrue(rol)`
- `contarUsuariosActivos()`

---

## 📦 DTOs (Data Transfer Objects)

### ClienteDTO
- Separación de capa de presentación
- Validaciones específicas para formularios
- Conversión bidireccional con entidad

### ProductoDTO
- Incluye nombres de categoría y proveedor
- Facilita vista en formularios

### VentaDTO
- Incluye nombres de usuario y cliente
- Lista de DetalleVentaDTO

### DetalleVentaDTO
- Incluye nombre de producto
- Cálculos de subtotal

---

## 🔧 Servicios Implementados

### ClienteService
- CRUD completo con validaciones
- Búsqueda por DNI, email, nombre
- Activar/desactivar clientes
- Conversión DTO ↔ Entidad
- Validación de duplicados

*(Pendiente crear servicios similares para: Producto, Venta, Categoría, Proveedor, Promoción)*

---

## ❌ Entidades Eliminadas

### Carrito / CarritoEntity
**Razón**: No es necesario para punto de venta físico. Las ventas se registran directamente.

### Pedido / DetallePedido
**Razón**: Para licorería, el flujo es: Cliente → Selección → Venta directa. No hay sistema de pedidos diferido.

---

## 📊 Lógica de Negocio Mejorada

### Control de Inventario
- Reducción automática de stock al vender
- Alerta de reposición (stock mínimo)
- Validación de disponibilidad antes de venta

### Gestión de Precios
- Uso de BigDecimal para precisión
- Soporte para descuentos por promoción
- Cálculo automático de subtotales

### Sistema de Promociones
- Vigencia automática por fechas
- Descuentos porcentuales
- Validación de fechas coherentes

### Seguridad y Auditoría
- Fechas de creación y modificación
- Estados activo/inactivo (soft delete)
- Registro de último acceso de usuarios
- Contraseñas hasheadas (BCrypt)

---

## 🎯 Próximos Pasos

### 1. Servicios Pendientes
- ProductoService con lógica de inventario
- VentaService con proceso completo de venta
- CategoriaService
- ProveedorService
- PromocionService
- UsuarioService con autenticación

### 2. Controladores
- Actualizar controladores existentes
- Implementar validación con @Valid
- Manejo de errores personalizado
- API REST endpoints

### 3. Vistas (Templates)
- Actualizar formularios con nuevos campos
- Dashboards con estadísticas
- Reportes de ventas
- Gestión de inventario

### 4. Funcionalidades Adicionales
- Reportes en PDF (Apache POI)
- Exportación a Excel
- Dashboard con gráficos
- Sistema de notificaciones (stock bajo, promociones)
- Historial de cambios

---

## 🔑 Conceptos Clave Implementados

1. **Separación de Responsabilidades**: Usuario ≠ Cliente
2. **Validaciones en Capas**: Entidad + DTO + Servicio
3. **Precisión Monetaria**: BigDecimal en lugar de double
4. **Soft Delete**: Estados en lugar de eliminar registros
5. **Auditoría**: Fechas de creación y modificación
6. **Relaciones JPA Correctas**: Lazy/Eager apropiados
7. **Cascade y Orphan Removal**: Gestión automática de detalles
8. **Índices de BD**: Optimización de consultas
9. **Consultas Personalizadas**: JPQL para reportes
10. **Patrón DTO**: Separación modelo/vista

---

## 📝 Notas Importantes

- ✅ Todas las entidades usan validaciones Jakarta Validation
- ✅ BigDecimal para todos los valores monetarios
- ✅ LocalDateTime/LocalDate para fechas
- ✅ Índices en campos de búsqueda frecuente
- ✅ Métodos de negocio en entidades (DDD)
- ✅ Repositorios con consultas específicas del negocio
- ✅ Nombres en español coherentes con el dominio

---

**Fecha de Reestructuración**: 15 de noviembre de 2025  
**Estado**: Modelo de dominio completo, repositorios listos, servicios en desarrollo
