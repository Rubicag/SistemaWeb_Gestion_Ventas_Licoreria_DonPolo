-- ==============================================
-- SCHEMA DE BASE DE DATOS REESTRUCTURADO
-- Sistema de Gestión y Ventas - Licorería Don Polo
-- Fecha: 15 de noviembre de 2025
-- ==============================================

-- Configuración inicial
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS detalle_ventas;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS promociones;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS proveedores;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================
-- TABLA: usuarios
-- Descripción: Empleados y administradores del sistema
-- ==============================================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del empleado',
    correo VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email para login',
    contrasena VARCHAR(255) NOT NULL COMMENT 'Hash BCrypt de la contraseña',
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'VENDEDOR', 'SUPERVISOR')) COMMENT 'Rol del usuario',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Usuario activo/inactivo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    ultimo_acceso DATETIME COMMENT 'Último login',
    INDEX idx_usuario_correo (correo),
    INDEX idx_usuario_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Usuarios del sistema (empleados)';

-- ==============================================
-- TABLA: clientes
-- Descripción: Clientes de la licorería
-- ==============================================
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del cliente',
    apellido VARCHAR(100) NOT NULL COMMENT 'Apellido del cliente',
    dni CHAR(8) UNIQUE COMMENT 'DNI del cliente (8 dígitos)',
    email VARCHAR(100) UNIQUE COMMENT 'Email del cliente',
    telefono CHAR(9) COMMENT 'Teléfono (9 dígitos)',
    direccion VARCHAR(255) COMMENT 'Dirección del cliente',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')) COMMENT 'Estado del cliente',
    INDEX idx_cliente_dni (dni),
    INDEX idx_cliente_email (email),
    INDEX idx_cliente_nombre (nombre, apellido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Clientes de la licorería';

-- ==============================================
-- TABLA: categorias
-- Descripción: Categorías de productos (tipos de licores)
-- ==============================================
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE COMMENT 'Nombre de la categoría',
    descripcion VARCHAR(255) COMMENT 'Descripción de la categoría',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Categoría activa/inactiva',
    INDEX idx_categoria_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Categorías de productos';

-- ==============================================
-- TABLA: proveedores
-- Descripción: Proveedores de productos
-- ==============================================
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre o razón social',
    ruc CHAR(11) UNIQUE COMMENT 'RUC del proveedor (11 dígitos)',
    email VARCHAR(100) COMMENT 'Email de contacto',
    telefono CHAR(9) COMMENT 'Teléfono de contacto (9 dígitos)',
    direccion VARCHAR(255) COMMENT 'Dirección del proveedor',
    contacto VARCHAR(100) COMMENT 'Nombre del contacto principal',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')) COMMENT 'Estado del proveedor',
    INDEX idx_proveedor_ruc (ruc),
    INDEX idx_proveedor_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Proveedores de productos';

-- ==============================================
-- TABLA: productos
-- Descripción: Productos de la licorería
-- ==============================================
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL COMMENT 'Nombre del producto',
    descripcion VARCHAR(255) COMMENT 'Descripción del producto',
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0.01) COMMENT 'Precio de venta',
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0) COMMENT 'Stock disponible',
    stock_minimo INT NOT NULL DEFAULT 10 CHECK (stock_minimo >= 0) COMMENT 'Stock mínimo para alerta',
    codigo_barras VARCHAR(50) UNIQUE COMMENT 'Código de barras EAN',
    marca VARCHAR(100) COMMENT 'Marca del producto',
    presentacion VARCHAR(50) COMMENT 'Presentación (750ml, 1L, etc.)',
    grado_alcoholico DECIMAL(4,1) CHECK (grado_alcoholico >= 0 AND grado_alcoholico <= 100) COMMENT 'Grado alcohólico',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Producto activo/inactivo',
    id_categoria INT NOT NULL COMMENT 'Categoría del producto',
    id_proveedor INT NOT NULL COMMENT 'Proveedor del producto',
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    INDEX idx_producto_nombre (nombre),
    INDEX idx_producto_categoria (id_categoria),
    INDEX idx_producto_proveedor (id_proveedor),
    INDEX idx_producto_codigo_barras (codigo_barras),
    INDEX idx_producto_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Productos de licorería';

-- ==============================================
-- TABLA: promociones
-- Descripción: Promociones y descuentos
-- ==============================================
CREATE TABLE promociones (
    id_promocion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre de la promoción',
    descripcion VARCHAR(255) COMMENT 'Descripción de la promoción',
    descuento DECIMAL(5,2) NOT NULL CHECK (descuento > 0 AND descuento <= 100) COMMENT 'Porcentaje de descuento',
    fecha_inicio DATE NOT NULL COMMENT 'Fecha de inicio',
    fecha_fin DATE NOT NULL COMMENT 'Fecha de fin',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Promoción activa/inactiva',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    id_producto INT NOT NULL COMMENT 'Producto en promoción',
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    INDEX idx_promocion_producto (id_producto),
    INDEX idx_promocion_fechas (fecha_inicio, fecha_fin),
    INDEX idx_promocion_activo (activo),
    CHECK (fecha_fin >= fecha_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Promociones y descuentos';

-- ==============================================
-- TABLA: ventas
-- Descripción: Ventas realizadas
-- ==============================================
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL COMMENT 'Vendedor que realizó la venta',
    id_cliente INT COMMENT 'Cliente (NULL si es venta sin registro)',
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de la venta',
    metodo_pago VARCHAR(50) NOT NULL CHECK (metodo_pago IN ('EFECTIVO', 'TARJETA', 'YAPE', 'PLIN', 'TRANSFERENCIA')) COMMENT 'Método de pago',
    total DECIMAL(10,2) NOT NULL CHECK (total >= 0.01) COMMENT 'Total de la venta',
    descuento DECIMAL(10,2) DEFAULT 0.00 CHECK (descuento >= 0) COMMENT 'Descuento aplicado',
    comprobante VARCHAR(50) COMMENT 'Número de comprobante',
    estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA' CHECK (estado IN ('COMPLETADA', 'ANULADA', 'PENDIENTE')) COMMENT 'Estado de la venta',
    observaciones VARCHAR(500) COMMENT 'Observaciones adicionales',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    INDEX idx_venta_fecha (fecha),
    INDEX idx_venta_cliente (id_cliente),
    INDEX idx_venta_usuario (id_usuario),
    INDEX idx_venta_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Registro de ventas';

-- ==============================================
-- TABLA: detalle_ventas
-- Descripción: Detalle de productos en cada venta
-- ==============================================
CREATE TABLE detalle_ventas (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL COMMENT 'Venta a la que pertenece',
    id_producto INT NOT NULL COMMENT 'Producto vendido',
    cantidad INT NOT NULL CHECK (cantidad >= 1) COMMENT 'Cantidad vendida',
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0.01) COMMENT 'Precio al momento de la venta',
    descuento DECIMAL(10,2) DEFAULT 0.00 CHECK (descuento >= 0) COMMENT 'Descuento por item',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal del item',
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    INDEX idx_detalle_venta (id_venta),
    INDEX idx_detalle_producto (id_producto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Detalle de productos por venta';

-- ==============================================
-- DATOS INICIALES
-- ==============================================

-- Insertar categorías iniciales
INSERT INTO categorias (nombre, descripcion, activo) VALUES
('Vinos', 'Vinos tintos, blancos y rosados', TRUE),
('Cervezas', 'Cervezas nacionales e importadas', TRUE),
('Whisky', 'Whisky escocés, irlandés y americano', TRUE),
('Vodka', 'Vodka premium y estándar', TRUE),
('Ron', 'Ron blanco, dorado y añejo', TRUE),
('Pisco', 'Pisco peruano artesanal', TRUE),
('Tequila', 'Tequila blanco, reposado y añejo', TRUE),
('Licores', 'Licores y cremas', TRUE),
('Espumantes', 'Champagne y espumantes', TRUE),
('Aperitivos', 'Vermut y aperitivos', TRUE);

-- Insertar usuario administrador inicial (contraseña: admin123 - debe ser hasheada con BCrypt)
INSERT INTO usuarios (nombre, correo, contrasena, rol, activo) VALUES
('Administrador', 'admin@donpolo.com', '$2a$10$dummyHashBCryptPasswordExample', 'ADMIN', TRUE);

-- ==============================================
-- VISTAS ÚTILES
-- ==============================================

-- Vista de productos con bajo stock
CREATE OR REPLACE VIEW v_productos_bajo_stock AS
SELECT 
    p.id_producto,
    p.nombre,
    p.marca,
    p.stock,
    p.stock_minimo,
    c.nombre AS categoria,
    pr.nombre AS proveedor
FROM productos p
INNER JOIN categorias c ON p.id_categoria = c.id_categoria
INNER JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor
WHERE p.stock <= p.stock_minimo AND p.activo = TRUE;

-- Vista de ventas del día
CREATE OR REPLACE VIEW v_ventas_hoy AS
SELECT 
    v.id_venta,
    v.fecha,
    CONCAT(u.nombre) AS vendedor,
    COALESCE(CONCAT(c.nombre, ' ', c.apellido), 'Anónimo') AS cliente,
    v.metodo_pago,
    v.total,
    v.estado
FROM ventas v
INNER JOIN usuarios u ON v.id_usuario = u.id_usuario
LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
WHERE DATE(v.fecha) = CURDATE();

-- ==============================================
-- FIN DEL SCRIPT
-- ==============================================
