-- ============================================
-- Fix: Agregar columna id_detalle a detalle_ventas
-- Fecha: 2025-11-16
-- Descripción: La tabla detalle_ventas necesita una clave primaria auto-incremental
-- ============================================

USE licoreria_donpolo;

-- Verificar estructura actual
DESCRIBE detalle_ventas;

-- Si la columna id_detalle no existe, ejecutar:
-- ALTER TABLE detalle_ventas 
-- ADD COLUMN id_detalle INT AUTO_INCREMENT PRIMARY KEY FIRST;

-- Si ya existe pero no es PRIMARY KEY, ejecutar:
-- ALTER TABLE detalle_ventas 
-- MODIFY COLUMN id_detalle INT AUTO_INCREMENT PRIMARY KEY;

-- Verificar que se aplicó correctamente
SHOW CREATE TABLE detalle_ventas;

-- Verificar índices
SHOW INDEX FROM detalle_ventas;
