(function(){
    const proveedores = window.PROVEEDORES_DATA || [];

    function nuevoProveedor() {
        const modalTitulo = document.getElementById('modalTitulo'); if(modalTitulo) modalTitulo.textContent = 'Nuevo Proveedor';
        const formProveedor = document.getElementById('formProveedor'); if(formProveedor){ try { formProveedor.reset(); formProveedor.action = '/proveedores/guardar'; } catch(e) {} }
        const activoEl = document.getElementById('activo'); if(activoEl) activoEl.checked = true;
        const modalEl = document.getElementById('modalProveedor'); if(modalEl) new bootstrap.Modal(modalEl).show();
    }

    window.nuevoProveedor = nuevoProveedor;

    function editarProveedor(id) {
        const proveedor = proveedores.find(p => p.idProveedor === id);
        if (!proveedor) return;

        const modalTitulo = document.getElementById('modalTitulo'); if(modalTitulo) modalTitulo.textContent = 'Editar Proveedor';
        const formProveedor = document.getElementById('formProveedor'); if(formProveedor) formProveedor.action = '/proveedores/actualizar';
        const idProvEl = document.getElementById('idProveedor'); if(idProvEl) idProvEl.value = proveedor.idProveedor;
        const nombreEl = document.getElementById('nombre'); if(nombreEl) nombreEl.value = proveedor.nombre || '';
        const rucEl = document.getElementById('ruc'); if(rucEl) rucEl.value = proveedor.ruc || '';
        const telefonoEl = document.getElementById('telefono'); if(telefonoEl) telefonoEl.value = proveedor.telefono || '';
        const correoEl = document.getElementById('correo'); if(correoEl) correoEl.value = proveedor.correo || '';
        const direccionEl = document.getElementById('direccion'); if(direccionEl) direccionEl.value = proveedor.direccion || '';
        const activoEl = document.getElementById('activo'); if(activoEl) activoEl.checked = !!proveedor.activo;

        const modalEl = document.getElementById('modalProveedor'); if(modalEl) new bootstrap.Modal(modalEl).show();
    }

    function cambiarEstado(id, activar) {
        const accion = activar ? 'activar' : 'desactivar';
        if (typeof showConfirm === 'function') {
            showConfirm(`¿Está seguro de ${accion} este proveedor?`, function() {
                window.location.href = `/proveedores/${accion}/${id}`;
            });
        } else {
            if (confirm(`¿Está seguro de ${accion} este proveedor?`)) {
                window.location.href = `/proveedores/${accion}/${id}`;
            }
        }
    }

    function filtrarTabla() {
        const input = document.getElementById('buscarProveedor');
        if (!input) return;
        const value = input.value.toLowerCase();
        const rows = document.querySelectorAll('#tablaProveedores tbody tr');

        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(value) ? '' : 'none';
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        // Botones editar
        document.querySelectorAll('.btn-editar-proveedor').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.getAttribute('data-proveedor-id'));
                editarProveedor(id);
            });
        });

        // Botones cambiar estado
        document.querySelectorAll('.btn-cambiar-estado').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.getAttribute('data-proveedor-id'));
                const estado = this.getAttribute('data-estado') === 'true';
                cambiarEstado(id, estado);
            });
        });

        // Búsqueda en tiempo real
        const buscar = document.getElementById('buscarProveedor');
        if (buscar) {
            buscar.addEventListener('keyup', filtrarTabla);
        }

        // Auto-dismiss alerts
        setTimeout(() => {
            document.querySelectorAll('.alert').forEach(alert => {
                try {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                } catch (e) {
                    // ignore
                }
            });
        }, 5000);
    });

})();
