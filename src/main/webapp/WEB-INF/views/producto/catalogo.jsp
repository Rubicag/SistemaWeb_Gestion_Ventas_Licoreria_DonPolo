<%-- 
    Document   : catalogo
    Created on : 23 set. 2025, 2:45:12?p. m.
    Author     : LUIGGI
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Catálogo de Productos</title>
</head>
<body>
<h2>Catálogo de Productos</h2>
<a href="/productos/agregar">Agregar Producto</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Precio</th>
        <th>Acciones</th>
    </tr>
    <c:forEach var="producto" items="${productos}">
        <tr>
            <td>${producto.id}</td>
            <td>${producto.nombre}</td>
            <td>${producto.precio}</td>
            <td>
                <a href="/productos/detalle/${producto.id}">Ver</a> |
                <a href="/productos/eliminar/${producto.id}">Eliminar</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
