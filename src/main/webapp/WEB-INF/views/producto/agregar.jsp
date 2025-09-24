<%-- 
    Document   : agregar
    Created on : 23 set. 2025, 5:11:54 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Agregar Producto</title>
</head>
<body>
<h2>Agregar Producto</h2>
<form action="/productos/agregar" method="post">
    Nombre: <input type="text" name="nombre" required><br>
    Precio: <input type="number" step="0.01" name="precio" required><br>
    <button type="submit">Agregar</button>
</form>
<a href="/productos/listar">Volver al catálogo</a>
</body>
</html>
