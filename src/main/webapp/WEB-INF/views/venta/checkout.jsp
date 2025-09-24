<%-- 
    Document   : checkout
    Created on : 23 set. 2025, 2:46:01 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
            text-align: center;
        }
        .resumen {
            display: inline-block;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            max-width: 600px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
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
    </style>
</head>
<body>
    <div class="resumen">
        <h2>Compra Realizada con Éxito</h2>
        <p>Gracias por su compra. Aquí está el resumen de su pedido:</p>

        <c:if test="${not empty carrito}">
            <table>
                <tr>
                    <th>ID Producto</th>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                </tr>
                <c:forEach var="item" items="${carrito}">
                    <tr>
                        <td>${item.producto.id}</td>
                        <td>${item.producto.nombre}</td>
                        <td>$${item.producto.precio}</td>
                        <td>${item.cantidad}</td>
                        <td>$${item.producto.precio * item.cantidad}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="4" class="total">Total</td>
                    <td class="total">$${total}</td>
                </tr>
            </table>
        </c:if>

        <p style="margin-top:20px;">
            <a href="/productos/listar">Seguir Comprando</a>
        </p>
    </div>
</body>
</html>

