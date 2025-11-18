(function(){
    'use strict';

    const clientes = window.CLIENTES_DATA || [];

    function initEventHandlers() {
        // Delegación: los botones se generan en servidor, añadimos listeners por clase
        document.querySelectorAll('.btn-editar-cliente').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.getAttribute('data-id'));
                editarCliente(id);
            });
        });

        document.querySelectorAll('.btn-eliminar-cliente').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.getAttribute('data-id'));
                const nombre = this.getAttribute('data-nombre');
                eliminarCliente(id, nombre);
            });
        });

        // Actualiza el contador inicial
        actualizarTotal();
    }

    function nuevoCliente() {
        const form = document.getElementById('formCliente');
        const modalTitulo = document.getElementById('modalTitulo');
        if (modalTitulo) modalTitulo.innerHTML = '<i class="fas fa-user-plus me-2"></i>Nuevo Cliente';
        if (form) {
            form.action = '/clientes/guardar';
            form.reset();
            const idEl = document.getElementById('idCliente');
            if (idEl) idEl.value = '';
            form.classList.remove('was-validated');
        }
    }

    function editarCliente(id) {
        const cliente = clientes.find(c => c.idCliente === id);
        if (!cliente) return;
        const modalTitulo = document.getElementById('modalTitulo');
        if (modalTitulo) modalTitulo.innerHTML = '<i class="fas fa-edit me-2"></i>Editar Cliente';
        const form = document.getElementById('formCliente');
        if (!form) return;
        form.action = '/clientes/actualizar';
        const setIf = (sel, value) => { const el = document.getElementById(sel); if (el) el.value = value || ''; };
        setIf('idCliente', cliente.idCliente);
        setIf('nombre', cliente.nombre);
        setIf('apellido', cliente.apellido);
        setIf('dni', cliente.dni);
        setIf('telefono', cliente.telefono);
        setIf('email', cliente.email);
        setIf('direccion', cliente.direccion);

        const modalEl = document.getElementById('modalCliente');
        if (modalEl) new bootstrap.Modal(modalEl).show();
    }

    function eliminarCliente(id, nombre) {
        if (typeof showConfirm === 'function') {
            showConfirm('¿Está seguro de eliminar al cliente ' + nombre + '?', function() {
                window.location.href = '/clientes/eliminar/' + id;
            });
        } else {
            if (confirm('¿Está seguro de eliminar al cliente ' + nombre + '?')) {
                window.location.href = '/clientes/eliminar/' + id;
            }
        }
    }

    function filtrarTabla() {
        const input = document.getElementById('searchInput');
        if (!input) return;
        const filter = input.value.toUpperCase();
        const table = document.getElementById('tablaClientes');
        if (!table) return;
        const tr = table.getElementsByTagName('tr');
        let count = 0;

        for (let i = 1; i < tr.length; i++) {
            const td = tr[i].getElementsByTagName('td');
            let found = false;
            for (let j = 0; j < td.length; j++) {
                const txtValue = td[j].textContent || td[j].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) { found = true; break; }
            }
            if (found) { tr[i].style.display = ''; count++; } else { tr[i].style.display = 'none'; }
        }

        const totalEl = document.getElementById('totalClientes');
        if (totalEl) totalEl.textContent = count;
    }

    function validarFormulario() {
        const form = document.getElementById('formCliente');
        if (!form) return true;
        if (!form.checkValidity()) { form.classList.add('was-validated'); return false; }
        return true;
    }

    function actualizarTotal() {
        const totalEl = document.getElementById('totalClientes');
        const count = (clientes && Array.isArray(clientes)) ? clientes.length : 0;
        if (totalEl) totalEl.textContent = count;
    }

    // Exponer funciones globales que usa el HTML (nuevoCliente, validarFormulario)
    window.nuevoCliente = nuevoCliente;
    window.validarFormulario = validarFormulario;
    window.filtrarTabla = filtrarTabla;
    window.editarCliente = editarCliente;
    window.eliminarCliente = eliminarCliente;

    document.addEventListener('DOMContentLoaded', function() {
        try { initEventHandlers(); } catch (e) { console.error('init clientes failed', e); }
    });

})();
