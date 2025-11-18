# Modernización de Páginas - Licorería Don Polo

## ✅ Completadas
1. **home.html** - Dashboard con gráficos Chart.js
2. **login.html** - Diseño centrado moderno
3. **productos.html** - Tabla moderna, badges, filtros
4. **clientes.html** - Iconos de cliente, badges modernos

## 🔄 En Proceso
Las siguientes páginas tienen la misma estructura y deben modernizarse con el patrón:

### Estructura Base a Aplicar:
```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>[TITULO] - Licorería Don Polo</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet" th:href="@{/css/global.css}">
</head>
<body>
    <!-- Navbar Moderno -->
    <div th:replace="~{fragments/navbar :: navbar('[PAGINA]')}"></div>

    <!-- Contenido Principal -->
    <div class="container-main" style="max-width: 1600px; margin: 2rem auto; padding: 0 1.5rem;">
        <!-- Alertas Modernas -->
        <div th:if="${mensaje}" class="alert alert-modern alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i><span th:text="${mensaje}"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        
        <!-- Card Principal Moderno -->
        <div class="modern-card">
            <div class="modern-card-header">
                <h3><i class="[ICONO]"></i> [TITULO]</h3>
                <button class="btn btn-modern-primary" data-bs-toggle="modal">
                    <i class="fas fa-plus-circle me-2"></i>Nuevo [ITEM]
                </button>
            </div>
            
            <div class="modern-card-body">
                <!-- Búsqueda Moderna -->
                <div class="search-box-modern">
                    <i class="fas fa-search"></i>
                    <input type="text" class="form-control" placeholder="Buscar...">
                </div>
                
                <!-- Tabla Moderna -->
                <table class="table table-modern">
                    <!-- Contenido tabla -->
                </table>
            </div>
        </div>
    </div>
    
    <!-- Modal Moderno -->
    <div class="modal fade">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content modal-modern">
                <div class="modal-header modal-header-modern">
                    <!-- Header -->
                </div>
                <form>
                    <div class="modal-body p-4">
                        <!-- Formularios con class="form-modern" -->
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-modern-secondary">Cancelar</button>
                        <button class="btn btn-modern-primary">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
```

### Páginas Pendientes:
- [ ] ventas.html
- [ ] usuarios.html  
- [ ] proveedores.html
- [ ] promociones.html
- [ ] pedidos.html
- [ ] reportes.html
- [ ] carritos.html (si es necesaria)

### Cambios Clave:
1. Agregar Google Fonts (Inter)
2. Reemplazar navbar con fragmento: `<div th:replace="~{fragments/navbar :: navbar('pagina')}"></div>`
3. Usar clases modernas:
   - `.modern-card` en lugar de `.card-custom`
   - `.modern-card-header` en lugar de `.card-header-custom`
   - `.modern-card-body` en lugar de `.card-body`
   - `.btn-modern-primary` en lugar de `.btn-primary-custom`
   - `.table-modern` en lugar de `.table-custom`
   - `.form-modern` en todos los inputs
   - `.badge-modern-*` en badges
   - `.search-box-modern` para búsqueda
4. Agregar iconos en tablas y formularios
5. Usar `badge` para IDs y estados
6. Botones agrupados con `.btn-group`
7. Modales centrados con `.modal-dialog-centered`

### Pasos para Cada Página:
1. Actualizar `<head>` con Google Fonts y global.css
2. Reemplazar navbar con fragmento
3. Cambiar `.container-fluid` por `.container-main` con max-width
4. Actualizar clases de alerts: `.alert-modern`
5. Cambiar `.card-custom` por `.modern-card`
6. Actualizar botones: `.btn-modern-primary`, `.btn-modern-secondary`, etc.
7. Modernizar tabla: `.table-modern`
8. Agregar iconos y badges en filas
9. Actualizar formularios: `.form-modern`
10. Actualizar modales: `.modal-modern`, `.modal-header-modern`
