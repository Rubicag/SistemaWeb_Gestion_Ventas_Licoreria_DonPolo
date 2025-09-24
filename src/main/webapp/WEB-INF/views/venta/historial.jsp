<%-- 
    Document   : historial
    Created on : 23 set. 2025, 2:46:14 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Historial de Compras</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
            text-align: center;
        }
        table {
            width: 80%;
            margin: auto;
            border-collapse: collapse;
            background-color: white;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        a {
            text-decoration: none;
            color: #007bff;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h2>Historial de Compras</h2>

<c:if test="${empty ventas}">
    <p>No hay compras registradas.</p>
</c:if>

<c:if test="${not empty ventas}">
    <table>
        <tr>
            <th>ID Venta</th>
            <th>ID Producto</th>
            <th>Cantidad</th>
            <th>Total</th>
            <th>Fecha</th>
        </tr>
        <c:forEach var="venta" items="${ventas}">
            <tr>
                <td>${venta.id}</td>
                <td>${venta.idProducto}</td>
                <td>${venta.cantidad}</td>
                <td>$${venta.total}</td>
                <td>${venta.fecha}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<p style="margin-top:20px;">
    <a href="/productos/listar">Volver al Catálogo</a>
</p>

</body>
</html>

