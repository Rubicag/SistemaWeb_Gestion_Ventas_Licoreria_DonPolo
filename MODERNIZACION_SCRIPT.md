# Script de Modernización Masiva

## Páginas Completadas ✅
1. home.html - Dashboard con Chart.js
2. login.html - Diseño centrado
3. productos.html - COMPLETO (navbar fragment, modern-card, table-modern, badges, icons)
4. clientes.html - COMPLETO (navbar fragment, modern-card, cliente-icon, badges)
5. **ventas.html** - COMPLETO (navbar fragment, modern-card, table-modern, badges, forms)
6. **usuarios.html** - COMPLETO (navbar fragment, modern-card, usuario-icon, badges, forms)

## Páginas Pendientes (Estructura Idéntica)
- **proveedores.html** (350 líneas)
- **promociones.html** (444 líneas)
- **pedidos.html**
- **reportes.html**

## Patrón de Modernización Aplicado

### 1. HEAD (Líneas 1-13)
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
```

**Acción**: Eliminar todo `<style>...</style>` entre líneas 8-120 aproximadamente.

### 2. BODY + NAVBAR (Líneas 14-20)
```html
<body>
    <!-- Navbar Moderno -->
    <div th:replace="~{fragments/navbar :: navbar('[pagina]')}"></div>

    <div class="container-main" style="max-width: 1600px; margin: 2rem auto; padding: 0 1.5rem;">
```

**Reemplaza**:
```html
<body>
    <nav class="navbar navbar-expand-lg navbar-custom">
        <div class="container-fluid">
            <a class="navbar-brand" href="/home">
                <i class="fas fa-wine-bottle"></i> Licorería Don Polo
            </a>
            <div class="navbar-nav ms-auto">
                ... (15-20 líneas de navegación)
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <h1><i class="..."></i> Gestión de [MODULO]</h1>
```

### 3. ALERTAS (Líneas 21-28)
```html
        <!-- Alertas Modernas -->
        <div th:if="${mensaje}" class="alert alert-modern alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i><span th:text="${mensaje}"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <div th:if="${error}" class="alert alert-modern alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i><span th:text="${error}"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
```

**Cambio**: Agregar clase `.alert-modern` a todos los alerts.

### 4. CARD PRINCIPAL (Líneas 30-50)
```html
        <!-- Card Principal Moderna -->
        <div class="modern-card">
            <div class="modern-card-header">
                <h3><i class="fas fa-[icono] me-2"></i>[Título]</h3>
                <button onclick="nuevo[Entidad]()" class="btn btn-modern-primary">
                    <i class="fas fa-plus-circle me-2"></i>Nuevo [Entidad]
                </button>
            </div>

            <div class="modern-card-body">
                <!-- Búsqueda Moderna -->
                <div class="search-box-modern mb-4">
                    <i class="fas fa-search"></i>
                    <input type="text" id="buscar[Entidad]" class="form-control" 
                           placeholder="Buscar..." onkeyup="filtrarTabla()">
                </div>

                <!-- Tabla Moderna -->
                <div class="table-responsive">
                    <table class="table table-modern" id="tabla[Entidades]">
```

**Reemplaza**:
- Eliminar `<div class="row mb-4">` con botones separados
- Envolver todo en `.modern-card` con `.modern-card-header` y `.modern-card-body`
- Cambiar `.search-box` por `.search-box-modern`
- Cambiar `class="table table-hover"` por `class="table table-modern"`

### 5. FILAS DE TABLA (Líneas 60-90)
```html
                        <tr th:each="item : ${items}">
                            <td><span class="badge badge-modern-secondary" th:text="${item.id}">1</span></td>
                            <td>
                                <div class="[entidad]-icon">
                                    <i class="fas fa-[icono]"></i>
                                </div>
                                <span th:text="${item.nombre}">Nombre</span>
                            </td>
                            <td><i class="fas fa-[icono] me-2" style="color: var(--secondary-color);"></i><span th:text="${item.campo}">Dato</span></td>
                            <td><span class="badge badge-modern-[tipo]" th:text="${item.badge}">Estado</span></td>
                            <td>
                                <div class="btn-group" role="group">
                                    <button class="btn btn-modern-warning btn-sm"><i class="fas fa-edit"></i></button>
                                    <button class="btn btn-modern-danger btn-sm"><i class="fas fa-trash"></i></button>
                                </div>
                            </td>
                        </tr>
```

**Cambios**:
- ID: Envolver en `<span class="badge badge-modern-secondary">`
- Nombre: Agregar `<div class="[entidad]-icon"><i class="fas fa-[icono]"></i></div>` antes del texto
- Badges: Cambiar `bg-success/danger/info/warning/primary/secondary` por `badge-modern-*`
- Botones: Agrupar en `<div class="btn-group">` y cambiar `btn-warning/danger/info` por `btn-modern-*`

### 6. EMPTY STATE (Líneas 95-100)
```html
                        <tr th:if="${#lists.isEmpty(items)}">
                            <td colspan="[N]" class="text-center text-muted py-5">
                                <i class="fas fa-[icono] fa-3x mb-3 opacity-25"></i><br>
                                <span style="font-size: 1.1rem;">No hay registros</span><br>
                                <small class="text-muted">Texto motivacional</small>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
    </div>
```

**Cambios**:
- Agregar clase `py-5` al `<td>`
- Agregar `opacity-25` al icono
- Agregar mensaje motivacional con `<small>`
- Cerrar correctamente `.table-responsive`, `.modern-card-body`, `.modern-card`, `.container-main`

### 7. MODAL (Líneas 110-200)
```html
    <!-- Modal [Entidad] -->
    <div class="modal fade" id="modal[Entidad]" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content modal-modern">
                <div class="modal-header modal-header-modern">
                    <h5 class="modal-title"><i class="fas fa-[icono]"></i> <span id="modalTitulo">Nuevo</span></h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="form[Entidad]">
                        <input type="text" class="form-control form-modern" ... />
                        <select class="form-select form-modern" ... ></select>
                    </form>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-modern-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button class="btn btn-modern-primary" form="form[Entidad]">Guardar</button>
                </div>
            </div>
        </div>
    </div>
```

**Cambios**:
- Agregar `modal-dialog-centered`
- Cambiar `.modal-content` por `.modal-content modal-modern`
- Cambiar `.modal-header` por `.modal-header modal-header-modern`
- Agregar `.form-modern` a todos los `<input>` y `<select>`
- Cambiar `btn-secondary` por `btn-modern-secondary`
- Cambiar `btn-primary-custom` por `btn-modern-primary`

## Resumen de Clases a Reemplazar

| Antigua | Nueva |
|---------|-------|
| `.container-fluid` | `.container-main` (con max-width inline) |
| `.card-custom` | `.modern-card` |
| `.card-header-custom` | `.modern-card-header` |
| `.card-body` | `.modern-card-body` |
| `.table-hover` | `.table-modern` |
| `.search-box` | `.search-box-modern` |
| `.btn-primary-custom` | `.btn-modern-primary` |
| `.btn-secondary` | `.btn-modern-secondary` |
| `.btn-warning` | `.btn-modern-warning` |
| `.btn-danger` | `.btn-modern-danger` |
| `.btn-info` | `.btn-modern-info` |
| `.btn-success` | `.btn-modern-success` |
| `.bg-primary` | `.badge-modern-primary` |
| `.bg-secondary` | `.badge-modern-secondary` |
| `.bg-success` | `.badge-modern-success` |
| `.bg-danger` | `.badge-modern-danger` |
| `.bg-info` | `.badge-modern-info` |
| `.bg-warning` | `.badge-modern-warning` |
| `.form-control` | `.form-control form-modern` |
| `.form-select` | `.form-select form-modern` |
| `.modal-content` | `.modal-content modal-modern` |
| `.modal-header` | `.modal-header modal-header-modern` |

## Estado Actual
✅ **6 archivos modernizados y copiados a target**
🔄 **4 archivos pendientes** (proveedores, promociones, pedidos, reportes)

## Próximos Pasos
1. Modernizar proveedores.html
2. Modernizar promociones.html
3. Modernizar pedidos.html
4. Modernizar reportes.html
5. Copiar todos a target/classes/templates/
6. Reiniciar aplicación para verificar cambios
