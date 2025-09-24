<%-- 
    Document   : registro
    Created on : 23 set. 2025, 4:47:55 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar Venta</title>
</head>
<body>
<h2>Registro de Venta</h2>
<form action="/ventas/registro" method="post">
    ID Cliente: <input type="number" name="idCliente" required><br>
    ID Producto: <input type="number" name="idProducto" required><br>
    Cantidad: <input type="number" name="cantidad" required><br>
    Total: <input type="number" name="total" step="0.01" required><br>
    Fecha: <input type="date" name="fecha" required><br>
    <button type="submit">Registrar Venta</button>
</form>
</body>
</html>
