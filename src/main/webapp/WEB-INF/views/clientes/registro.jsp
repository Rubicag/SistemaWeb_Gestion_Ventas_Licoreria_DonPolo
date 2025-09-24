<%-- 
    Document   : registro
    Created on : 23 set. 2025, 4:47:16 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar Cliente</title>
</head>
<body>
<h2>Registro de Cliente</h2>
<form action="/clientes/registro" method="post">
    Nombre: <input type="text" name="nombre" required><br>
    Email: <input type="email" name="email" required><br>
    Teléfono: <input type="text" name="telefono" required><br>
    <button type="submit">Registrar</button>
</form>
</body>
</html>
