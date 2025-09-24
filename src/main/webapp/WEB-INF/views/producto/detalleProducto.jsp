<%-- 
    Document   : detalleProducto
    Created on : 23 set. 2025, 2:45:23 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Detalle del Producto</title>
</head>
<body>
<h2>Detalle del Producto</h2>
<p>ID: ${producto.id}</p>
<p>Nombre: ${producto.nombre}</p>
<p>Precio: $${producto.precio}</p>
<a href="/productos/listar">Volver al catálogo</a>
</body>
</html>
