<%-- 
    Document   : index
    Created on : 23 set. 2025, 2:46:53 p. m.
    Author     : LUIGGI
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio - Licorería Don Polo</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #007bff;
            color: white;
            padding: 20px;
        }
        nav {
            margin: 20px 0;
        }
        nav a {
            margin: 0 15px;
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }
        nav a:hover {
            text-decoration: underline;
        }
        main {
            padding: 20px;
        }
        h1 {
            color: #333;
        }
    </style>
</head>
<body>
<header>
    <h1>Bienvenido a Licorería Don Polo</h1>
</header>

<nav>
    <a href="/productos/listar">Catálogo de Productos</a>
    <a href="/ventas/carrito">Carrito</a>
    <a href="/ventas/historial">Historial de Compras</a>
    <a href="/login">Iniciar Sesión</a>
    <a href="/registro">Registrarse</a>
</nav>

<main>
    <h2>Disfruta de nuestras ofertas y productos</h2>
    <p>Selecciona una opción en el menú para empezar.</p>
</main>

</body>
</html>

