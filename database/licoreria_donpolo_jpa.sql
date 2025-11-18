-- ====================================
-- BASE DE DATOS LICORERÍA DON POLO
-- Sincronizada con Modelo JPA Spring Boot
-- Versión: 2.0
-- Fecha: 16-11-2025
-- ====================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- ====================================
-- Crear base de datos
-- ====================================
DROP DATABASE IF EXISTS `licoreria_donpolo`;
CREATE DATABASE `licoreria_donpolo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `licoreria_donpolo`;

-- ====================================
-- TABLA: usuarios
-- ====================================
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) UNIQUE NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `rol` enum('ADMINISTRADOR','VENDEDOR','CLIENTE') NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ultimo_acceso` datetime DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  INDEX `idx_correo_usuario` (`correo`),
  INDEX `idx_usuario_rol` (`rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: clientes
-- ====================================
CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni` varchar(8) UNIQUE DEFAULT NULL,
  `email` varchar(100) UNIQUE DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime NOT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_cliente`),
  INDEX `idx_cliente_dni` (`dni`),
  INDEX `idx_cliente_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: categorias
-- ====================================
CREATE TABLE `categorias` (
  `id_categoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) UNIQUE NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_categoria`),
  INDEX `idx_categoria_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: proveedores
-- ====================================
CREATE TABLE `proveedores` (
  `id_proveedor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `ruc` varchar(11) UNIQUE DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `contacto` varchar(100) DEFAULT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_proveedor`),
  INDEX `idx_proveedor_ruc` (`ruc`),
  INDEX `idx_proveedor_nombre` (`nombre`),
  INDEX `idx_proveedor_estado` (`estado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: productos
-- ====================================
CREATE TABLE `productos` (
  `id_producto` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `stock_minimo` int(11) NOT NULL DEFAULT 10,
  `codigo_barras` varchar(50) UNIQUE DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `presentacion` varchar(50) DEFAULT NULL,
  `grado_alcoholico` decimal(4,1) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `id_categoria` int(11) NOT NULL,
  `id_proveedor` int(11) NOT NULL,
  PRIMARY KEY (`id_producto`),
  INDEX `idx_producto_nombre` (`nombre`),
  INDEX `idx_producto_categoria` (`id_categoria`),
  INDEX `idx_producto_proveedor` (`id_proveedor`),
  INDEX `idx_producto_activo` (`activo`),
  INDEX `idx_producto_marca` (`marca`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`),
  CONSTRAINT `fk_producto_proveedor` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: promociones
-- ====================================
CREATE TABLE `promociones` (
  `id_promocion` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `descuento` decimal(5,2) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_producto` int(11) NOT NULL,
  PRIMARY KEY (`id_promocion`),
  INDEX `idx_promocion_producto` (`id_producto`),
  INDEX `idx_promocion_fechas` (`fecha_inicio`, `fecha_fin`),
  INDEX `idx_promocion_activo` (`activo`),
  CONSTRAINT `fk_promocion_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: carrito
-- ====================================
CREATE TABLE `carrito` (
  `id_carrito` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` varchar(20) NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_carrito`),
  INDEX `idx_carrito_usuario` (`id_usuario`),
  INDEX `idx_carrito_estado` (`estado`),
  CONSTRAINT `fk_carrito_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: carrito_detalle
-- ====================================
CREATE TABLE `carrito_detalle` (
  `id_carrito_detalle` int(11) NOT NULL AUTO_INCREMENT,
  `id_carrito` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_carrito_detalle`),
  INDEX `idx_carrito_detalle_carrito` (`id_carrito`),
  INDEX `idx_carrito_detalle_producto` (`id_producto`),
  CONSTRAINT `fk_carrito_detalle_carrito` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`) ON DELETE CASCADE,
  CONSTRAINT `fk_carrito_detalle_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: ventas
-- ====================================
CREATE TABLE `ventas` (
  `id_venta` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `id_cliente` int(11) DEFAULT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `metodo_pago` varchar(50) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) DEFAULT 0.00,
  `estado` varchar(20) NOT NULL DEFAULT 'COMPLETADA',
  `observaciones` varchar(255) DEFAULT NULL,
  `id_carrito` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_venta`),
  INDEX `idx_venta_fecha` (`fecha`),
  INDEX `idx_venta_usuario` (`id_usuario`),
  INDEX `idx_venta_cliente` (`id_cliente`),
  INDEX `idx_venta_estado` (`estado`),
  CONSTRAINT `fk_venta_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  CONSTRAINT `fk_venta_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE SET NULL,
  CONSTRAINT `fk_venta_carrito` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: detalle_ventas
-- ====================================
CREATE TABLE `detalle_ventas` (
  `id_detalle_venta` int(11) NOT NULL AUTO_INCREMENT,
  `id_venta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle_venta`),
  INDEX `idx_detalle_venta_venta` (`id_venta`),
  INDEX `idx_detalle_venta_producto` (`id_producto`),
  CONSTRAINT `fk_detalle_venta_venta` FOREIGN KEY (`id_venta`) REFERENCES `ventas` (`id_venta`) ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_venta_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: pedidos
-- ====================================
CREATE TABLE `pedidos` (
  `id_pedido` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `direccion_entrega` varchar(200) DEFAULT NULL,
  `estado` enum('PENDIENTE','EN_PROCESO','ENVIADO','ENTREGADO','CANCELADO') DEFAULT 'PENDIENTE',
  `observaciones` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_pedido`),
  INDEX `idx_pedido_usuario` (`id_usuario`),
  INDEX `idx_pedido_estado` (`estado`),
  CONSTRAINT `fk_pedido_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: detalle_pedidos
-- ====================================
CREATE TABLE `detalle_pedidos` (
  `id_detalle` int(11) NOT NULL AUTO_INCREMENT,
  `id_pedido` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  INDEX `idx_detalle_pedido_pedido` (`id_pedido`),
  INDEX `idx_detalle_pedido_producto` (`id_producto`),
  CONSTRAINT `fk_detalle_pedido_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_pedido_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: pagos
-- ====================================
CREATE TABLE `pagos` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `id_venta` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` enum('Efectivo','Tarjeta','Transferencia','Yape','Plin') NOT NULL,
  `estado` enum('Pendiente','Completado','Fallido','Rechazado','Autorizado') DEFAULT 'Pendiente',
  `fecha_pago` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pago`),
  INDEX `idx_pago_venta` (`id_venta`),
  CONSTRAINT `fk_pago_venta` FOREIGN KEY (`id_venta`) REFERENCES `ventas` (`id_venta`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: movimientos_stock
-- ====================================
CREATE TABLE `movimientos_stock` (
  `id_movimiento` int(11) NOT NULL AUTO_INCREMENT,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `tipo` enum('Entrada','Salida') NOT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_movimiento`),
  INDEX `idx_movimiento_producto` (`id_producto`),
  INDEX `idx_movimiento_fecha` (`fecha`),
  CONSTRAINT `fk_movimiento_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: notificaciones
-- ====================================
CREATE TABLE `notificaciones` (
  `id_notificacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `mensaje` varchar(250) DEFAULT NULL,
  `fecha_envio` datetime DEFAULT CURRENT_TIMESTAMP,
  `tipo` enum('Correo','WhatsApp') DEFAULT NULL,
  PRIMARY KEY (`id_notificacion`),
  INDEX `idx_notificacion_usuario` (`id_usuario`),
  CONSTRAINT `fk_notificacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- TABLA: reportes
-- ====================================
CREATE TABLE `reportes` (
  `id_reporte` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id_reporte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================
-- DATOS INICIALES
-- ====================================

-- Usuarios
INSERT INTO `usuarios` (`nombre`, `correo`, `contrasena`, `rol`, `activo`) VALUES
('Admin Principal', 'admin@donpolo.com', '$2a$10$80tMaIxUyRFR0qKn9Dkp4eFyiwvHkH0FaYTomCSwmejjDRF5P7c3G', 'ADMINISTRADOR', 1),
('Vendedor 1', 'vendedor1@donpolo.com', '$2a$10$tRCKbbuOCx2rcGSWnXRek.89KT7RwQ52T88x98Nqny/fuZk2TXSVe', 'VENDEDOR', 1),
('Cliente Juan', 'juan@gmail.com', '$2a$10$tRCKbbuOCx2rcGSWnXRek.89KT7RwQ52T88x98Nqny/fuZk2TXSVe', 'CLIENTE', 1),
('Cliente Maria', 'maria@gmail.com', '$2a$10$tRCKbbuOCx2rcGSWnXRek.89KT7RwQ52T88x98Nqny/fuZk2TXSVe', 'CLIENTE', 1);

-- Clientes
INSERT INTO `clientes` (`nombre`, `apellido`, `dni`, `email`, `telefono`, `direccion`, `estado`) VALUES
('Juan', 'Pérez García', '12345678', 'juan@gmail.com', '987654321', 'Av. Siempre Viva 123 - Lima', 'ACTIVO'),
('María', 'López Rodríguez', '87654321', 'maria@gmail.com', '999888777', 'Jr. Los Olivos 456 - Lima', 'ACTIVO'),
('Carlos', 'Sánchez Torres', '11223344', 'carlos@gmail.com', '966554433', 'Av. Colonial 789 - Lima', 'ACTIVO');

-- Categorías
INSERT INTO `categorias` (`nombre`, `descripcion`) VALUES
('Vinos', 'Vinos tintos, blancos y rosados'),
('Cervezas', 'Cervezas nacionales e importadas'),
('Whisky', 'Whisky de diferentes marcas'),
('Ron', 'Ron blanco y añejo'),
('Vodka', 'Vodka premium y estándar'),
('Pisco', 'Pisco peruano tradicional');

-- Proveedores
INSERT INTO `proveedores` (`nombre`, `ruc`, `email`, `telefono`, `direccion`, `contacto`, `estado`) VALUES
('Proveedor Bebidas S.A.', '20123456789', 'ventas@bebidas.com', '987654321', 'Av. Principal 123 - Lima', 'Jorge Mendoza', 'ACTIVO'),
('Distribuidora Don Pepe', '20987654321', 'contacto@donpepe.com', '999888777', 'Jr. Los Pinos 456 - Lima', 'Pedro Ruiz', 'ACTIVO'),
('Licores Premium SAC', '20555666777', 'info@licorespremium.com', '955443322', 'Av. Industrial 200 - Callao', 'Ana Torres', 'ACTIVO');

-- Productos
INSERT INTO `productos` (`nombre`, `descripcion`, `precio`, `stock`, `stock_minimo`, `codigo_barras`, `marca`, `presentacion`, `grado_alcoholico`, `activo`, `id_categoria`, `id_proveedor`) VALUES
('Cerveza Cristal 620ml', 'Cerveza rubia tipo pilsen', 6.00, 100, 20, '7750182000116', 'Backus', '620ml', 5.0, 1, 2, 1),
('Cerveza Pilsen 620ml', 'Cerveza premium peruana', 7.00, 80, 15, '7750182000215', 'Backus', '620ml', 5.0, 1, 2, 1),
('Whisky Johnnie Walker Red Label 750ml', 'Whisky escocés mezclado', 90.00, 20, 5, '5000267024516', 'Johnnie Walker', '750ml', 40.0, 1, 3, 2),
('Ron Cartavio 750ml', 'Ron añejo peruano', 45.00, 25, 10, '7751863000123', 'Cartavio', '750ml', 38.0, 1, 4, 2),
('Vino Tacama Gran Tinto 750ml', 'Vino tinto seco', 35.00, 15, 8, '7751234000456', 'Tacama', '750ml', 13.5, 1, 1, 1),
('Vodka Absolut 750ml', 'Vodka sueco premium', 75.00, 12, 5, '7312040017898', 'Absolut', '750ml', 40.0, 1, 5, 3),
('Pisco Queirolo Quebranta 750ml', 'Pisco puro quebranta', 42.00, 30, 10, '7751234001234', 'Queirolo', '750ml', 42.0, 1, 6, 1),
('Cerveza Cusqueña Dorada 650ml', 'Cerveza tipo lager', 8.00, 60, 15, '7750182001234', 'Cusqueña', '650ml', 5.0, 1, 2, 1);

-- Promociones
INSERT INTO `promociones` (`nombre`, `descripcion`, `descuento`, `fecha_inicio`, `fecha_fin`, `activo`, `id_producto`) VALUES
('Promo Fiestas Patrias - Vinos', '10% de descuento en vinos Tacama', 10.00, '2025-07-20', '2025-07-31', 0, 5),
('Promo Fin de Semana - Cervezas', '5% de descuento en cervezas', 5.00, '2025-11-15', '2025-11-30', 1, 1),
('Promo Black Friday - Whisky', '15% de descuento en Johnnie Walker', 15.00, '2025-11-25', '2025-11-30', 1, 3);

-- Carritos
INSERT INTO `carrito` (`id_usuario`, `fecha_creacion`, `estado`) VALUES
(3, '2025-11-15 10:00:00', 'ACTIVO'),
(4, '2025-11-15 11:30:00', 'FINALIZADO');

-- Carrito Detalle
INSERT INTO `carrito_detalle` (`id_carrito`, `id_producto`, `cantidad`) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 1);

-- Ventas
INSERT INTO `ventas` (`id_usuario`, `id_cliente`, `fecha`, `metodo_pago`, `total`, `descuento`, `estado`) VALUES
(2, 1, '2025-11-15 12:00:00', 'EFECTIVO', 19.00, 0.00, 'COMPLETADA'),
(2, 2, '2025-11-15 14:30:00', 'TARJETA', 90.00, 0.00, 'COMPLETADA'),
(2, 1, '2025-11-15 16:00:00', 'YAPE', 42.00, 2.10, 'COMPLETADA');

-- Detalle Ventas
INSERT INTO `detalle_ventas` (`id_venta`, `id_producto`, `cantidad`, `precio_unitario`, `subtotal`) VALUES
(1, 1, 2, 6.00, 12.00),
(1, 2, 1, 7.00, 7.00),
(2, 3, 1, 90.00, 90.00),
(3, 7, 1, 42.00, 42.00);

-- Pedidos
INSERT INTO `pedidos` (`id_usuario`, `fecha`, `direccion_entrega`, `estado`, `observaciones`) VALUES
(3, '2025-11-15 09:00:00', 'Av. Siempre Viva 123 - Lima', 'PENDIENTE', 'Entrega antes de las 6pm'),
(4, '2025-11-14 18:00:00', 'Jr. Los Olivos 456 - Lima', 'ENTREGADO', NULL);

-- Detalle Pedidos
INSERT INTO `detalle_pedidos` (`id_pedido`, `id_producto`, `cantidad`) VALUES
(1, 4, 2),
(1, 5, 1),
(2, 3, 1);

-- Pagos
INSERT INTO `pagos` (`id_venta`, `monto`, `metodo_pago`, `estado`) VALUES
(1, 19.00, 'Efectivo', 'Completado'),
(2, 90.00, 'Tarjeta', 'Completado'),
(3, 42.00, 'Yape', 'Completado');

-- Movimientos de Stock
INSERT INTO `movimientos_stock` (`id_producto`, `cantidad`, `tipo`, `fecha`) VALUES
(1, 2, 'Salida', '2025-11-15 12:00:00'),
(2, 1, 'Salida', '2025-11-15 12:00:00'),
(3, 1, 'Salida', '2025-11-15 14:30:00'),
(7, 1, 'Salida', '2025-11-15 16:00:00');

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- ====================================
-- FIN DEL SCRIPT
-- ====================================
-- Para usar esta base de datos, actualiza application.properties:
-- spring.datasource.url=jdbc:mysql://localhost:3306/licoreria_donpolo
-- spring.jpa.hibernate.ddl-auto=validate
-- ====================================
