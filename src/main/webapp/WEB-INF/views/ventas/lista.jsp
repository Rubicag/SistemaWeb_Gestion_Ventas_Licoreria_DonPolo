<%-- 
    Document   : lista
    Created on : 23 set. 2025, 4:48:04 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lista de Ventas</title>
</head>
<body>
<h2>Ventas Registradas</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>ID Cliente</th>
        <th>ID Producto</th>
        <th>Cantidad</th>
        <th>Total</th>
        <th>Fecha</th>
    </tr>
    <c:forEach var="venta" items="${ventas}">
        <tr>
            <td>${venta.id}</td>
            <td>${venta.idCliente}</td>
            <td>${venta.idProducto}</td>
            <td>${venta.cantidad}</td>
            <td>${venta.total}</td>
            <td>${venta.fecha}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
