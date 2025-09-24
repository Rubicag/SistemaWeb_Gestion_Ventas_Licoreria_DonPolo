<%-- 
    Document   : carrito
    Created on : 23 set. 2025, 2:45:44 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Carrito de Compras</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
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
        .total {
            font-weight: bold;
        }
        a {
            text-decoration: none;
            color: #007bff;
        }
        a:hover {
            text-decoration: underline;
        }
        button {
            padding: 5px 10px;
            cursor: pointer;
        }
        .actions {
            display: flex;
            justify-content: center;
            gap: 10px;
        }
    </style>
</head>
<body>
<h2 style="text-align:center;">Carrito de Compras</h2>

<c:if test="${empty carrito}">
    <p style="text-align:center;">Tu carrito está vacío.</p>
</c:if>

<c:if test="${not empty carrito}">
    <form action="/ventas/checkout" method="post">
        <table>
            <tr>
                <th>ID Producto</th>
                <th>Nombre</th>
                <th>Precio</th>
                <th>Cantidad</th>
                <th>Subtotal</th>
                <th>Acciones</th>
            </tr>
            <c:forEach var="item" items="${carrito}">
                <tr>
                    <td>${item.producto.id}</td>
                    <td>${item.producto.nombre}</td>
                    <td>$${item.producto.precio}</td>
                    <td>
                        <input type="number" name="cantidades[${item.producto.id}]" value="${item.cantidad}" min="1"/>
                    </td>
                    <td>$${item.producto.precio * item.cantidad}</td>
                    <td class="actions">
                        <a href="/ventas/carrito/eliminar/${item.producto.id}">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4" class="total">Total</td>
                <td colspan="2" class="total">$${total}</td>
            </tr>
        </table>
        <div style="text-align:center; margin-top:15px;">
            <button type="submit">Realizar Compra</button>
        </div>
    </form>
</c:if>

<a href="/productos/listar" style="display:block; text-align:center; margin-top:20px;">Seguir Comprando</a>

</body>
</html>


