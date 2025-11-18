/* global bootstrap, ajaxTable, showConfirm, showToast */
(function(){
  let carrito = [];
  const ventas = window.VENTAS_DATA || [];
  const productos = window.PRODUCTOS_DATA || [];
  const clientes = window.CLIENTES_DATA || [];

  window.nuevaVenta = function() {
    const tituloEl = document.getElementById('modalTitulo'); if (tituloEl) tituloEl.textContent = 'Nueva Venta';
    const form = document.getElementById('formVenta'); if (form) { form.reset(); form.action = '/ventas/guardar'; }
    carrito = [];
    actualizarCarrito();
    new bootstrap.Modal(document.getElementById('modalVenta')).show();
  };

  window.agregarAlCarrito = function() {
    const select = document.getElementById('productoSeleccionado');
    const cantidad = parseInt((document.getElementById('cantidadProducto')||{}).value || 0);
    if (!select || !select.value || cantidad <= 0) { showToast('Seleccione un producto y cantidad válida', 'danger'); return; }
    const option = select.options[select.selectedIndex];
    const idProducto = parseInt(select.value);
    const precio = parseFloat(option.dataset.precio);
    const stock = parseInt(option.dataset.stock);
    const nombre = option.text.split(' - ')[0];
    if (cantidad > stock) { showToast(`Stock insuficiente. Disponible: ${stock}`, 'danger'); return; }
    const existe = carrito.find(item => item.idProducto === idProducto);
    if (existe) {
      if (existe.cantidad + cantidad > stock) { showToast(`Stock insuficiente. Disponible: ${stock}`, 'danger'); return; }
      existe.cantidad += cantidad;
    } else { carrito.push({ idProducto, nombre, precio, cantidad, stock }); }
    actualizarCarrito(); select.value = ''; if(document.getElementById('cantidadProducto')) document.getElementById('cantidadProducto').value = 1;
  };

  window.eliminarDelCarrito = function(index){ carrito.splice(index,1); actualizarCarrito(); };

  function actualizarCarrito(){
    const container = document.getElementById('carritoItems');
    const totalDiv = document.getElementById('totalVenta');
    const detallesHidden = document.getElementById('detallesHidden');
    if(!container){return}
    if(carrito.length===0){ container.innerHTML = '<p class="text-muted text-center">El carrito está vacío</p>'; if(totalDiv) totalDiv.innerHTML='Total: S/ 0.00'; if(detallesHidden) detallesHidden.innerHTML=''; return; }
    let html=''; let total=0; if(detallesHidden) detallesHidden.innerHTML='';
    carrito.forEach((item,index)=>{
      const subtotal = item.precio * item.cantidad; total += subtotal;
      html += `\n        <div class="carrito-item">\n          <div>\n            <strong>${item.nombre}</strong><br>\n            <small>Cantidad: ${item.cantidad} × S/ ${item.precio.toFixed(2)}</small>\n          </div>\n          <div>\n            <strong style="color: #a05a2c;">S/ ${subtotal.toFixed(2)}</strong>\n            <button type="button" class="btn btn-sm btn-danger ms-2" onclick="eliminarDelCarrito(${index})">\n              <i class="fas fa-trash"></i>\n            </button>\n          </div>\n        </div>`;
      if(detallesHidden) detallesHidden.innerHTML += `\n        <input type="hidden" name="detalles[${index}].idProducto" value="${item.idProducto}">\n        <input type="hidden" name="detalles[${index}].cantidad" value="${item.cantidad}">\n        <input type="hidden" name="detalles[${index}].precioUnitario" value="${item.precio}">\n      `;
    });
    container.innerHTML = html; if(totalDiv) totalDiv.innerHTML = `Total: S/ ${total.toFixed(2)}`;
  }

  window.validarVenta = function(){
    if(carrito.length===0){ showToast('Debe agregar al menos un producto al carrito','warning'); return false; }
    const metodoPago = (document.getElementById('metodoPago')||{}).value;
    if(!metodoPago){ showToast('Seleccione un método de pago','warning'); return false; }
    showConfirm('¿Confirmar venta?', function(){ document.getElementById('formVenta').submit(); });
    return false;
  };

  window.verDetalle = async function(idVenta){
    console.log('[ventas] verDetalle called with idVenta=', idVenta);
    try{
      if (idVenta == null || isNaN(Number(idVenta))) { console.warn('[ventas] verDetalle invalid idVenta:', idVenta); showToast && showToast('ID de venta inválido', 'warning'); return; }
      const resp = await fetch(`/api/ventas/${idVenta}`, { credentials: 'same-origin' });
      if(!resp.ok){ showToast('No se pudo obtener detalle de la venta','danger'); return; }
      const venta = await resp.json();
      console.log('[ventas] verDetalle -> venta received', venta);
      let html = `\n        <div class="row mb-3">\n          <div class="col-md-6">\n            <p><strong>ID Venta:</strong> ${venta.idVenta}</p>\n            <p><strong>Fecha:</strong> ${new Date(venta.fecha).toLocaleString('es-PE')}</p>\n            <p><strong>Cliente:</strong> ${venta.nombreCliente || 'Cliente General'}</p>\n          </div>\n          <div class="col-md-6">\n            <p><strong>Comprobante:</strong> ${venta.comprobante || '-'}</p>\n            <p><strong>Método Pago:</strong> <span class="badge bg-info">${venta.metodoPago}</span></p>\n            <p><strong>Estado:</strong> <span class="badge ${venta.estado === 'COMPLETADA' ? 'bg-success' : 'bg-danger'}">${venta.estado}</span></p>\n          </div>\n        </div>\n        <hr>\n        <h6>Detalle de Productos:</h6>\n        <table class="table table-sm">\n          <thead>\n            <tr>\n              <th>Producto</th>\n              <th>Cantidad</th>\n              <th>Precio Unit.</th>\n              <th>Subtotal</th>\n            </tr>\n          </thead>\n          <tbody>\n      `;
      if(venta.detalles && venta.detalles.length>0){ venta.detalles.forEach(det=>{ html += `\n            <tr>\n              <td>${det.nombreProducto || 'Producto'}</td>\n              <td>${det.cantidad}</td>\n              <td>S/ ${Number(det.precioUnitario).toFixed(2)}</td>\n              <td>S/ ${(det.cantidad * det.precioUnitario).toFixed(2)}</td>\n            </tr>\n          `; }); }
      html += `\n          </tbody>\n        </table>\n        <div class="total-venta">\n          Total: S/ ${Number(venta.total).toFixed(2)}\n        </div>\n      `;
      if(venta.observaciones) html += `<p class="mt-3"><strong>Observaciones:</strong> ${venta.observaciones}</p>`;
      const detalleBody = document.getElementById('detalleVentaBody'); if(detalleBody) detalleBody.innerHTML = html;
      new bootstrap.Modal(document.getElementById('modalDetalle')).show();
    }catch(e){ console.error(e); showToast('Error cargando detalle','danger'); }
  };

  window.anularVenta = function(idVenta){ showConfirm('¿Está seguro de anular esta venta?', function(){ window.location.href = `/ventas/anular/${idVenta}`; }); };

  window.filtrarTabla = function(){ const value = (document.getElementById('buscarVenta')||{}).value || ''; window.ventasTableConfig.extraParams = Object.assign({}, window.ventasTableConfig.extraParams || {}, { search: value }); ajaxTable.reload(); };

  document.addEventListener('DOMContentLoaded', function(){
    window.ventasTableConfig = {
      url: '/api/ventas/summary',
      tableBody: '#tablaVentas tbody',
      pageSize: 25,
      pagination: '#ventasPagination',
      renderRow: function(item){
        const tr = document.createElement('tr');
        const tdId = document.createElement('td'); tdId.innerHTML = `<span class="badge badge-modern-secondary">${item.idVenta}</span>`; tr.appendChild(tdId);
        const tdFecha = document.createElement('td'); tdFecha.textContent = new Date(item.fecha).toLocaleString('es-PE'); tr.appendChild(tdFecha);
        const tdCliente = document.createElement('td'); tdCliente.innerHTML = `<i class="fas fa-user-circle me-2" style="color: var(--secondary-color);"></i><span>${item.nombreCliente || 'Cliente General'}</span>`; tr.appendChild(tdCliente);
        const tdComp = document.createElement('td'); tdComp.textContent = item.comprobante || '-'; tr.appendChild(tdComp);
        const tdMetodo = document.createElement('td'); tdMetodo.innerHTML = `<span class="badge badge-modern-info">${item.metodoPago || ''}</span>`; tr.appendChild(tdMetodo);
        const tdTotal = document.createElement('td'); tdTotal.innerHTML = `<strong style="color: var(--primary-color); font-size: 1.05rem;">S/ ${Number(item.total || 0).toFixed(2)}</strong>`; tr.appendChild(tdTotal);
        const tdEstado = document.createElement('td'); const estadoBadge = document.createElement('span'); estadoBadge.className = 'badge ' + (item.estado === 'COMPLETADA' ? 'badge-modern-success' : 'badge-modern-danger'); estadoBadge.textContent = item.estado || ''; tdEstado.appendChild(estadoBadge); tr.appendChild(tdEstado);
        const tdAcc = document.createElement('td');
        tdAcc.innerHTML = `<div class="btn-group" role="group"><button class="btn btn-modern-info btn-sm btn-ver-detalle" data-venta-id="${item.idVenta}" title="Ver Detalle"><i class="fas fa-eye"></i></button>${(item.estado === 'COMPLETADA') ? `<button class="btn btn-modern-danger btn-sm btn-anular-venta" data-venta-id="${item.idVenta}" title="Anular"><i class="fas fa-ban"></i></button>` : ''}</div>`;
        // Attach direct listeners to buttons to avoid relying solely on delegation
        (function(){
          try{
            const btnVer = tdAcc.querySelector('.btn-ver-detalle');
            if(btnVer){ btnVer.addEventListener('click', function(ev){ ev.stopPropagation(); const raw = btnVer.dataset ? btnVer.dataset.ventaId : undefined; const id = raw ? parseInt(raw) : NaN; if(isNaN(id)){ showToast && showToast('ID de venta inválido','warning'); return; } verDetalle(id); }); }
            const btnAnular = tdAcc.querySelector('.btn-anular-venta');
            if(btnAnular){ btnAnular.addEventListener('click', function(ev){ ev.stopPropagation(); const raw = btnAnular.dataset ? btnAnular.dataset.ventaId : undefined; const id = raw ? parseInt(raw) : NaN; if(isNaN(id)){ showToast && showToast('ID de venta inválido','warning'); return; } anularVenta(id); }); }
          }catch(ex){ console.error('[ventas] error attaching direct button listeners', ex); }
        })();
        tr.appendChild(tdAcc);
        return tr;
      }
    };

    // Inicializar ajaxTable solo si la tabla de ventas está presente en el DOM
    if (document.querySelector('#tablaVentas')) {
      ajaxTable.init(window.ventasTableConfig);
    } else {
      console.warn('[ventas] selector #tablaVentas no encontrado — se omite ajaxTable.init');
    }

    const tbody = document.querySelector('#tablaVentas tbody');
    if(tbody){
      tbody.addEventListener('click', function(e){
        try{
          const btn = e.target.closest('.btn-ver-detalle, .btn-anular-venta');
          if(!btn) return;
          console.log('[ventas] table click, btn=', btn);
          const raw = btn.dataset ? btn.dataset.ventaId : undefined;
          const idVenta = raw ? parseInt(raw) : NaN;
          console.log('[ventas] button dataset.ventaId=', raw, 'parsed=', idVenta);
          if (isNaN(idVenta)) { showToast && showToast('ID de venta inválido', 'warning'); return; }
          if(btn.classList.contains('btn-ver-detalle')) verDetalle(idVenta);
          else if(btn.classList.contains('btn-anular-venta')) anularVenta(idVenta);
        }catch(err){ console.error('[ventas] error handling table click', err); }
      });
    }
  });

})();
