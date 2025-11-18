(function(){
    // Código movido desde fragments/navbar.html para búsqueda global
    function initNavbarSearch(){
        const globalSearch = document.getElementById('globalSearch');
        const searchResults = document.getElementById('searchResults');
        if (!globalSearch || !searchResults) return;

        const searchData = [
            { type: 'Producto', name: 'Cerveza Cusqueña', url: '/productos', icon: 'fa-box' },
            { type: 'Producto', name: 'Vino Tacama', url: '/productos', icon: 'fa-box' },
            { type: 'Cliente', name: 'Juan Pérez', url: '/clientes', icon: 'fa-user' },
            { type: 'Cliente', name: 'María García', url: '/clientes', icon: 'fa-user' },
            { type: 'Venta', name: 'Venta #001', url: '/ventas', icon: 'fa-shopping-cart' },
            { type: 'Proveedor', name: 'Distribuidora Lima', url: '/proveedores', icon: 'fa-truck' },
            { type: 'Usuario', name: 'Administrador', url: '/usuarios', icon: 'fa-user-shield' },
            { type: 'Reporte', name: 'Ventas del Mes', url: '/reportes', icon: 'fa-chart-line' }
        ];

        globalSearch.addEventListener('input', function(e) {
            const query = e.target.value.toLowerCase().trim();
            if (query.length < 2) { searchResults.classList.remove('show'); searchResults.innerHTML = ''; return; }

            const filtered = searchData.filter(item => item.name.toLowerCase().includes(query) || item.type.toLowerCase().includes(query));
            if (filtered.length === 0) {
                searchResults.innerHTML = `<div class="search-result-item text-center text-muted"><i class="fas fa-search me-2"></i>No se encontraron resultados</div>`;
                searchResults.classList.add('show');
                return;
            }

            const grouped = {};
            filtered.forEach(item => { if (!grouped[item.type]) grouped[item.type] = []; grouped[item.type].push(item); });

            let html = '';
            Object.keys(grouped).forEach(type => {
                html += `<div class="search-result-category">${type}s</div>`;
                grouped[type].forEach(item => { html += `<a href="${item.url}" class="search-result-item"><i class="fas ${item.icon} me-2"></i><strong>${item.name}</strong></a>`; });
            });

            searchResults.innerHTML = html;
            searchResults.classList.add('show');
        });

        document.addEventListener('click', function(e) {
            if (!globalSearch.contains(e.target) && !searchResults.contains(e.target)) {
                searchResults.classList.remove('show');
            }
        });

        globalSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                const first = searchResults.querySelector('.search-result-item');
                if (first && first.href) window.location.href = first.href;
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initNavbarSearch);
    } else {
        initNavbarSearch();
    }
})();
