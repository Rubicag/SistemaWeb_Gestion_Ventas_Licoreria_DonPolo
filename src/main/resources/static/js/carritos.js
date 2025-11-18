/* global bootstrap, showConfirm, showToast */
(function(){
  const carritos = window.CARRITOS_DATA || [];

  // Auto-dismiss alerts
  setTimeout(() => {
    document.querySelectorAll('.alert').forEach(alert => {
      try { new bootstrap.Alert(alert).close(); } catch(e) {}
    });
  }, 5000);

  window.nuevoCarrito = function() {
    const form = document.getElementById('formCarrito');
    if (form) form.reset();
    const modalEl = document.getElementById('modalCarrito');
    if (modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.verDetalle = function(id) {
    const carrito = carritos.find(c => c.idCarrito === id) || carritos.find(c => c.idCarrito == id);
    if (!carrito) return;
    const detIdEl = document.getElementById('detIdCarrito'); if (detIdEl) detIdEl.textContent = carrito.idCarrito;
    const detUsuarioEl = document.getElementById('detUsuario'); if (detUsuarioEl) detUsuarioEl.textContent = carrito.usuario ? carrito.usuario.nombre : 'Sin usuario';
    const detCorreoEl = document.getElementById('detCorreo'); if (detCorreoEl) detCorreoEl.textContent = carrito.usuario ? carrito.usuario.correo : 'Sin correo';
    const detFechaEl = document.getElementById('detFechaCreacion'); if (detFechaEl) detFechaEl.textContent = carrito.fechaCreacion ? new Date(carrito.fechaCreacion).toLocaleString('es-ES') : '';
    const detEstadoEl = document.getElementById('detEstado'); if (detEstadoEl) detEstadoEl.innerHTML = `<span class="badge ${carrito.estado === 'ACTIVO' ? 'bg-success' : 'bg-secondary'}">${carrito.estado}</span>`;

    const detallesContainer = document.getElementById('detallesCarrito'); if (detallesContainer) detallesContainer.innerHTML = '';

    if (carrito.detalles && carrito.detalles.length > 0) {
      carrito.detalles.forEach(detalle => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'carrito-item';
        itemDiv.innerHTML = `\n          <div>\n            <strong>${detalle.producto ? detalle.producto.nombre : 'Producto'}</strong><br>\n            <small>Cantidad: ${detalle.cantidad} | Precio: S/ ${detalle.precioUnitario ? detalle.precioUnitario.toFixed(2) : '0.00'}</small>\n          </div>\n          <div>\n            <strong>S/ ${detalle.subtotal ? detalle.subtotal.toFixed(2) : '0.00'}</strong>\n          </div>`;
        if (detallesContainer) detallesContainer.appendChild(itemDiv);
      });
      const totalItemsEl = document.getElementById('totalItems'); if (totalItemsEl) totalItemsEl.textContent = carrito.detalles.length;
    } else {
      if (detallesContainer) detallesContainer.innerHTML = '<p class="text-muted text-center">Carrito vacío</p>';
      const totalItemsEl2 = document.getElementById('totalItems'); if (totalItemsEl2) totalItemsEl2.textContent = '0';
    }
    const modalEl = document.getElementById('modalDetalle'); if (modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.agregarProducto = function(id) {
    const form = document.getElementById('formAgregarProducto');
    if (form) form.reset();
    const hid = document.getElementById('idCarritoProducto');
    if (hid) hid.value = id;
    const modalEl = document.getElementById('modalAgregarProducto'); if (modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.convertirAVenta = function(id) {
    showConfirm('¿Convertir este carrito en una venta?', function() {
      window.location.href = `/carritos/convertirAVenta/${id}`;
    });
  };

  window.cambiarEstado = function(id) {
    const carrito = carritos.find(c => c.idCarrito === id) || carritos.find(c => c.idCarrito == id);
    if (!carrito) return;
    const nuevoEstado = carrito.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    showConfirm(`¿Cambiar estado a ${nuevoEstado}?`, function() {
      window.location.href = `/carritos/cambiarEstado/${id}/${nuevoEstado}`;
    });
  };

  window.vaciarCarrito = function(id) {
    showConfirm('¿Está seguro de vaciar este carrito? Se eliminarán todos los productos.', function() {
      window.location.href = `/carritos/vaciar/${id}`;
    });
  };

  window.filtrarTabla = function() {
    const input = (document.getElementById('buscar')||{}).value || '';
    const tabla = document.getElementById('tablaCarritos');
    if (!tabla) return;
    const filas = tabla.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (let i = 0; i < filas.length; i++) {
      const textoFila = (filas[i].textContent || '').toLowerCase();
      filas[i].style.display = textoFila.includes(input.toLowerCase()) ? '' : 'none';
    }
  };

})();
