(function(){
    const promociones = window.PROMOCIONES_DATA || [];
    const productos = window.PRODUCTOS_DATA || [];

    function enableFormEditing() {
        const elements = document.querySelectorAll('#formPromocion input, #formPromocion select, #formPromocion textarea');
        elements.forEach(el => el.removeAttribute('disabled'));
        const btn = document.getElementById('btnGuardarPromocion');
        if (btn) btn.style.display = '';
    }

    function disableFormEditing() {
        const elements = document.querySelectorAll('#formPromocion input, #formPromocion select, #formPromocion textarea');
        elements.forEach(el => el.setAttribute('disabled', 'disabled'));
        const btn = document.getElementById('btnGuardarPromocion');
        if (btn) btn.style.display = 'none';
    }

    function nuevaPromocion() {
        const modalTitulo = document.getElementById('modalTitulo');
        if (modalTitulo) modalTitulo.textContent = 'Nueva Promoción';
        const form = document.getElementById('formPromocion');
        if (form) {
            form.reset();
            form.action = '/promociones/guardar';
        }
        enableFormEditing();
        toggleDescuento();
        const modalEl = document.getElementById('modalPromocion');
        if (modalEl) new bootstrap.Modal(modalEl).show();
    }

    window.nuevaPromocion = nuevaPromocion;

    function editarPromocion(id) {
        const promo = promociones.find(p => Number(p.idPromocion) === Number(id));
        if (!promo) return;
        const modalTitulo = document.getElementById('modalTitulo');
        if (modalTitulo) modalTitulo.textContent = 'Editar Promoción';
        const form = document.getElementById('formPromocion');
        if (form) form.action = '/promociones/actualizar';
        enableFormEditing();
        const setValue = (id, value) => { const el = document.getElementById(id); if (el) el.value = value || ''; };
        setValue('idPromocion', promo.idPromocion);
        setValue('nombre', promo.nombre);
        setValue('descripcion', promo.descripcion || '');
        setValue('tipoPromocion', promo.tipoPromocion);
        setValue('descuento', promo.descuento || '');
        setValue('fechaInicio', promo.fechaInicio);
        setValue('fechaFin', promo.fechaFin);

        // seleccionar productos aplicables
        const prodSelect = document.getElementById('productosPromocion');
        if (prodSelect) {
            Array.from(prodSelect.options).forEach(opt => opt.selected = false);
            const selectedIds = (promo.productos || []).map(p => Number(p.idProducto || p.id || p));
            Array.from(prodSelect.options).forEach(opt => {
                if (selectedIds.includes(Number(opt.value))) opt.selected = true;
            });
        }

        toggleDescuento();
        const modalEl = document.getElementById('modalPromocion');
        if (modalEl) new bootstrap.Modal(modalEl).show();
    }

    function verPromocion(id) {
        const promo = promociones.find(p => Number(p.idPromocion) === Number(id));
        if (!promo) return;
        const modalTitulo = document.getElementById('modalTitulo');
        if (modalTitulo) modalTitulo.textContent = 'Detalle Promoción';
        const setValue = (id, value) => { const el = document.getElementById(id); if (el) el.value = value || ''; };
        setValue('idPromocion', promo.idPromocion);
        setValue('nombre', promo.nombre || '');
        setValue('descripcion', promo.descripcion || '');
        setValue('tipoPromocion', promo.tipoPromocion || '');
        setValue('descuento', promo.descuento || '');
        setValue('fechaInicio', promo.fechaInicio || '');
        setValue('fechaFin', promo.fechaFin || '');

        // seleccionar productos aplicables
        const prodSelectView = document.getElementById('productosPromocion');
        if (prodSelectView) {
            Array.from(prodSelectView.options).forEach(opt => opt.selected = false);
            const selected = (promo.productos || []);
            const selectedIdsView = selected.map(p => Number(p.idProducto || p.id || p));
            Array.from(prodSelectView.options).forEach(opt => {
                if (selectedIdsView.includes(Number(opt.value))) opt.selected = true;
            });
        }

        // deshabilitar inputs para vista sólo lectura
        disableFormEditing();
        toggleDescuento();
        const modalEl = document.getElementById('modalPromocion');
        if (modalEl) new bootstrap.Modal(modalEl).show();
    }

    function finalizarPromocion(id) {
        if (typeof showConfirm === 'function') {
            showConfirm('¿Está seguro de finalizar esta promoción?', function() {
                window.location.href = `/promociones/finalizar/${id}`;
            });
        } else {
            if (confirm('¿Está seguro de finalizar esta promoción?')) {
                window.location.href = `/promociones/finalizar/${id}`;
            }
        }
    }

    function toggleDescuento() {
        const tipoEl = document.getElementById('tipoPromocion');
        const tipo = tipoEl ? tipoEl.value : '';
        const descuentoGroup = document.getElementById('descuentoGroup');
        const descuentoInput = document.getElementById('descuento');
        const descuentoHelp = document.getElementById('descuentoHelp');
        if (!descuentoGroup || !descuentoInput) return;

        if (tipo === '2X1' || tipo === '3X2') {
            descuentoGroup.style.display = 'none';
            descuentoInput.required = false;
        } else {
            descuentoGroup.style.display = 'block';
            descuentoInput.required = true;
            if (tipo === 'PORCENTAJE') {
                if (descuentoHelp) descuentoHelp.textContent = 'Porcentaje de descuento (0-100)';
                descuentoInput.max = 100;
            } else if (tipo === 'MONTO_FIJO') {
                if (descuentoHelp) descuentoHelp.textContent = 'Monto fijo en soles';
                descuentoInput.removeAttribute('max');
            }
        }
    }

    function filtrarPorEstado() {
        const filtro = document.getElementById('filtroEstado');
        const estado = filtro ? filtro.value.toLowerCase() : '';
        const rows = document.querySelectorAll('#tablaPromociones tbody tr');
        rows.forEach(row => {
            if (!estado) {
                row.style.display = '';
            } else {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(estado) ? '' : 'none';
            }
        });
    }

    document.addEventListener('click', function(e) {
        const btn = e.target.closest('button[data-promocion-id]');
        if (!btn) return;
        const id = btn.getAttribute('data-promocion-id');
        if (btn.classList.contains('btn-ver-promocion')) {
            verPromocion(Number(id));
        } else if (btn.classList.contains('btn-editar-promocion')) {
            editarPromocion(Number(id));
        } else if (btn.classList.contains('btn-finalizar-promocion')) {
            finalizarPromocion(Number(id));
        }
    });

    document.addEventListener('DOMContentLoaded', function() {
        const buscar = document.getElementById('filtroEstado');
        if (buscar) buscar.addEventListener('change', filtrarPorEstado);

        // Auto-dismiss alerts
        setTimeout(() => {
            document.querySelectorAll('.alert').forEach(alert => {
                try {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                } catch (e) {}
            });
        }, 5000);
    });

})();
