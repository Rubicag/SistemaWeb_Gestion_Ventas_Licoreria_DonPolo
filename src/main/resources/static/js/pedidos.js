/* global bootstrap, showConfirm, showToast */
(function(){
  const pedidos = window.PEDIDOS_DATA || [];

  // Auto-dismiss alerts
  setTimeout(() => {
    document.querySelectorAll('.alert').forEach(alert => {
      try { new bootstrap.Alert(alert).close(); } catch(e) {}
    });
  }, 5000);

  window.nuevoPedido = function() {
    const titulo = document.getElementById('modalTitulo');
    if (titulo) titulo.innerHTML = '<i class="fas fa-truck"></i> Nuevo Pedido';
    const form = document.getElementById('formPedido');
    if (form) { form.reset(); form.action = '/pedidos/guardar'; }
    const idInput = document.getElementById('idPedido'); if (idInput) idInput.value = '';
    const now = new Date(); const dateStr = now.toISOString().slice(0,16); const fecha = document.getElementById('fecha'); if (fecha) fecha.value = dateStr;
    new bootstrap.Modal(document.getElementById('modalPedido')).show();
  };

  window.editarPedidoBtn = function(btn) {
    const id = btn.dataset.id; if (!id) return;
    const titulo = document.getElementById('modalTitulo'); if (titulo) titulo.innerHTML = '<i class="fas fa-edit"></i> Editar Pedido';
    const form = document.getElementById('formPedido'); if (form) form.action = '/pedidos/actualizar';
    const idInput = document.getElementById('idPedido'); if (idInput) idInput.value = id;
    const direccion = document.getElementById('direccionEntrega'); if (direccion) direccion.value = btn.dataset.direccion || '';
    const estado = document.getElementById('estado'); if (estado) estado.value = btn.dataset.estado || 'PENDIENTE';
    const obs = document.getElementById('observaciones'); if (obs) obs.value = btn.dataset.observaciones || '';
    const usuario = document.getElementById('usuario'); if (usuario && btn.dataset.usuarioId) usuario.value = btn.dataset.usuarioId;
    if (btn.dataset.fecha) { const dateStr = btn.dataset.fecha.slice(0,16); const fecha = document.getElementById('fecha'); if (fecha) fecha.value = dateStr; }
    new bootstrap.Modal(document.getElementById('modalPedido')).show();
  };

  window.verDetalleBtn = function(btn) {
    const id = btn.dataset.id; if (!id) return;
    const detId = document.getElementById('detIdPedido'); if (detId) detId.textContent = id;
    if (btn.dataset.fecha) {
      try { const fechaObj = new Date(btn.dataset.fecha); const detFecha = document.getElementById('detFecha'); if (detFecha) detFecha.textContent = fechaObj.toLocaleString('es-ES'); } catch(e) { const detFecha = document.getElementById('detFecha'); if (detFecha) detFecha.textContent = btn.dataset.fecha.replace('T',' '); }
    } else { const detFecha = document.getElementById('detFecha'); if (detFecha) detFecha.textContent = ''; }
    const detEstado = document.getElementById('detEstado'); if (detEstado) detEstado.innerHTML = `<span class="badge bg-info">${btn.dataset.estado || ''}</span>`;
    const detUsuario = document.getElementById('detUsuario'); if (detUsuario) detUsuario.textContent = btn.dataset.usuarioNombre || 'Sin usuario';
    const detDireccion = document.getElementById('detDireccion'); if (detDireccion) detDireccion.textContent = btn.dataset.direccion || '';
    const detObs = document.getElementById('detObservaciones'); if (detObs) detObs.textContent = btn.dataset.observaciones || 'Sin observaciones';
    new bootstrap.Modal(document.getElementById('modalDetalle')).show();
  };

  window.cambiarEstadoBtn = function(btn) {
    const id = btn.dataset.id; const estado = btn.dataset.estado; if (!id || !estado) return;
    const estados = ['PENDIENTE','EN_PROCESO','ENVIADO','ENTREGADO']; const currentIndex = estados.indexOf(estado);
    if (currentIndex < 0 || currentIndex >= estados.length -1) return; const nuevoEstado = estados[currentIndex+1];
    showConfirm(`¿Cambiar estado de ${estado} a ${nuevoEstado}?`, function(){ window.location.href = `/pedidos/cambiarEstado/${id}/${nuevoEstado}`; });
  };

  window.cancelarPedidoBtn = function(btn) {
    const id = btn.dataset.id; if (!id) return; showConfirm('¿Está seguro de cancelar este pedido?', function(){ window.location.href = `/pedidos/cancelar/${id}`; });
  };

  window.filtrarTabla = function(){
    const input = (document.getElementById('buscar')||{}).value || '';
    const tabla = document.getElementById('tablaPedidos'); if (!tabla) return;
    const filas = tabla.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
    for (let i=0;i<filas.length;i++){ const textoFila = (filas[i].textContent||'').toLowerCase(); filas[i].style.display = textoFila.includes(input.toLowerCase()) ? '' : 'none'; }
  };

  // fallback toast helper if fragments/toasts not present
  window.showToast = window.showToast || function(message, variant='info', delay=4000){ const container = document.getElementById('toastContainer'); if (!container) { window.alert(message); return;} const toast = document.createElement('div'); toast.className = `toast align-items-center text-bg-${variant} border-0`; toast.setAttribute('role','alert'); toast.setAttribute('aria-live','assertive'); toast.setAttribute('aria-atomic','true'); toast.innerHTML = `<div class="d-flex"><div class="toast-body">${message}</div><button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button></div>`; container.appendChild(toast); const bsToast = new bootstrap.Toast(toast,{delay:delay}); bsToast.show(); toast.addEventListener('hidden.bs.toast',()=>toast.remove()); };

})();
