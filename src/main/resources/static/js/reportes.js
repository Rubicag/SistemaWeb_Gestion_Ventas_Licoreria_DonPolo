/* global bootstrap, html2pdf, showToast */
(function(){
  try{
    console.debug('[reportes.js] cargando...');
    let reporteActual = null;
  const reporteVentasData = window.REPORTE_VENTAS_DATA || [];
  const todasLasVentasData = window.TODAS_VENTAS_DATA || [];
  const todosLosClientesData = window.TODOS_CLIENTES_DATA || [];
  const todosLosProveedoresData = window.TODOS_PROVEEDORES_DATA || [];
  const todasLasPromocionesData = window.TODAS_PROMOCIONES_DATA || [];

  window.generarReporte = function(tipo){
    reporteActual = tipo;
    const titulos = { 'ventas': 'Reporte de Ventas', 'productos': 'Reporte de Productos', 'clientes': 'Reporte de Clientes', 'proveedores': 'Reporte de Proveedores', 'financiero': 'Reporte Financiero', 'promociones': 'Reporte de Promociones' };
    const modalTitulo = document.getElementById('modalTitulo'); if(modalTitulo) modalTitulo.textContent = titulos[tipo] || 'Generar Reporte';
    const tipoInput = document.getElementById('tipoReporte'); if(tipoInput) tipoInput.value = tipo;
    const form = document.getElementById('formReporte'); if(form) form.reset();
    const hoy = new Date().toISOString().split('T')[0];
    const hace30dias = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];
    const fechaInicio = document.getElementById('fechaInicio'); if(fechaInicio) fechaInicio.value = hace30dias;
    const fechaFin = document.getElementById('fechaFin'); if(fechaFin) fechaFin.value = hoy;
    const filtrosDiv = document.getElementById('filtrosAdicionales'); if(filtrosDiv) filtrosDiv.innerHTML = '';
    if(tipo === 'ventas'){
      if(filtrosDiv) filtrosDiv.innerHTML = `\n        <div class="mb-3">\n          <label class="form-label"><i class="fas fa-filter"></i> Filtrar por</label>\n          <select class="form-select" name="filtroVentas">\n            <option value="">Todas las ventas</option>\n            <option value="vendedor">Por vendedor</option>\n            <option value="metodoPago">Por método de pago</option>\n            <option value="estado">Por estado</option>\n          </select>\n        </div>`;
    } else if(tipo === 'productos'){
      if(filtrosDiv) filtrosDiv.innerHTML = `\n        <div class="mb-3">\n          <label class="form-label"><i class="fas fa-filter"></i> Filtrar por</label>\n          <select class="form-select" name="filtroProductos">\n            <option value="">Todos los productos</option>\n            <option value="categoria">Por categoría</option>\n            <option value="proveedor">Por proveedor</option>\n            <option value="stockBajo">Stock bajo</option>\n            <option value="masVendidos">Más vendidos</option>\n          </select>\n        </div>`;
    }
    const modalEl = document.getElementById('modalReporte'); if(modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.ejecutarReporte = function(){
    (async function(){
      const tipo = (document.getElementById('tipoReporte')||{}).value;
      const resultados = document.getElementById('resultadosReporte'); if(resultados){ resultados.classList.remove('d-none'); resultados.classList.add('d-block'); }
      try{ const modal = bootstrap.Modal.getInstance(document.getElementById('modalReporte')); if(modal) modal.hide(); }catch(e){}
      const resultadosEl = document.getElementById('resultadosReporte'); if(resultadosEl) resultadosEl.scrollIntoView({ behavior: 'smooth' });

      // Preparar params de fecha
      const fechaInicioVal = (document.getElementById('fechaInicio')||{}).value || '';
      const fechaFinVal = (document.getElementById('fechaFin')||{}).value || '';

      try{
        const url = `/reportes/api?tipo=${encodeURIComponent(tipo)}&fechaInicio=${encodeURIComponent(fechaInicioVal)}&fechaFin=${encodeURIComponent(fechaFinVal)}`;
        const resp = await fetch(url, { headers: { 'Accept': 'application/json' }, credentials: 'same-origin' });
        if(resp.ok){
          const data = await resp.json();
          generarTablaReporte(tipo, data);
          return;
        }
      }catch(err){ console.warn('Fallo al obtener reporte por AJAX, usando datos embebidos', err); }

      // Fallback a datos inyectados en la página
      generarTablaReporte(tipo);
    })();
  };

  window.generarTablaReporte = function(tipo, apiData){
    const head = document.getElementById('headReporte');
    const body = document.getElementById('bodyReporte');
    // Lógica de generación de tablas según tipo (puede ampliarse)
    if(!head || !body) return;
    // formateo dd/mm/yyyy
    const formatDateDDMMYYYY = (fechaVal) => {
      if(!fechaVal) return '';
      try{
        // si viene YYYY-MM-DD o ISO
        if(typeof fechaVal === 'string'){
          const s = fechaVal.split('T')[0];
          const parts = s.split('-');
          if(parts.length>=3) return `${parts[2]}/${parts[1]}/${parts[0]}`;
        }
        if(typeof fechaVal === 'object'){
          if(fechaVal.year !== undefined && fechaVal.month !== undefined && fechaVal.day !== undefined){
            const dd = String(fechaVal.day).padStart(2,'0');
            const mm = String(fechaVal.month).padStart(2,'0');
            return `${dd}/${mm}/${fechaVal.year}`;
          }
          // Date object
          if(fechaVal.getFullYear){
            const dd = String(fechaVal.getDate()).padStart(2,'0');
            const mm = String(fechaVal.getMonth()+1).padStart(2,'0');
            return `${dd}/${mm}/${fechaVal.getFullYear()}`;
          }
        }
      }catch(e){}
      return String(fechaVal);
    };
    // Helper: parsear posibles formatos de fecha provenientes del servidor
    const parseFechaObj = (fechaVal) => {
      if(!fechaVal) return null;
      try{
        if(typeof fechaVal === 'string'){
          // ISO string
          const d = new Date(fechaVal);
          if(!isNaN(d)) return d;
          // fallback: date-like string without timezone
          const s = fechaVal.split('T')[0];
          return new Date(s + 'T00:00:00');
        }
        if(typeof fechaVal === 'object'){
          // objeto serializado de LocalDateTime {year, month, day, hour...}
          if(fechaVal.year !== undefined && fechaVal.month !== undefined && fechaVal.day !== undefined){
            return new Date(Number(fechaVal.year), Number(fechaVal.month)-1, Number(fechaVal.day));
          }
          // si viene como {day, monthValue, year} o similares
          if(fechaVal.day !== undefined && fechaVal.month !== undefined && fechaVal.year !== undefined){
            return new Date(Number(fechaVal.year), Number(fechaVal.month)-1, Number(fechaVal.day));
          }
        }
        // última opción: convertir a string
        const d2 = new Date(String(fechaVal));
        if(!isNaN(d2)) return d2;
      }catch(e){}
      return null;
    };

    // Obtener rango de fechas desde el modal (si están presentes)
    const fechaInicioEl = document.getElementById('fechaInicio');
    const fechaFinEl = document.getElementById('fechaFin');
    const fechaInicioFilter = fechaInicioEl && fechaInicioEl.value ? new Date(fechaInicioEl.value + 'T00:00:00') : null;
    const fechaFinFilter = fechaFinEl && fechaFinEl.value ? new Date(fechaFinEl.value + 'T23:59:59') : null;
    const ventasFiltradasPorFecha = (Array.isArray(todasLasVentasData) ? todasLasVentasData.slice() : []).filter(v => {
      if(!fechaInicioFilter && !fechaFinFilter) return true;
      const f = parseFechaObj(v.fecha);
      if(!f) return false;
      if(fechaInicioFilter && f < fechaInicioFilter) return false;
      if(fechaFinFilter && f > fechaFinFilter) return false;
      return true;
    });
    head.innerHTML = '';
    body.innerHTML = '';
    if(tipo === 'ventas'){
      // Cabeceras
      head.innerHTML = '<tr>\n<th>ID</th>\n<th>Fecha</th>\n<th>Cliente</th>\n<th>Vendedor</th>\n<th>Método Pago</th>\n<th class="text-end">Total</th>\n<th>Estado</th>\n</tr>';
      // Filas
      let ventasData = null;
      if(apiData && Array.isArray(apiData)) ventasData = apiData;
      else if(Array.isArray(ventasFiltradasPorFecha) && ventasFiltradasPorFecha.length>0) ventasData = ventasFiltradasPorFecha;
      else ventasData = todasLasVentasData;

      if(Array.isArray(ventasData) && ventasData.length>0){
        const rows = ventasData.map(v => {
          const id = v.idVenta || v.id || '';
          const fecha = formatDateDDMMYYYY(v.fecha);
          const cliente = v.nombreCliente || v.cliente || '';
          const vendedor = v.nombreVendedor || v.usuario || '';
          const metodo = v.metodoPago || '';
          const total = v.total != null ? v.total : '';
          const estado = v.estado || '';
          return `<tr>\n<td>${id}</td>\n<td>${fecha}</td>\n<td>${cliente}</td>\n<td>${vendedor}</td>\n<td>${metodo}</td>\n<td class="text-end">${total}</td>\n<td>${estado}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        body.innerHTML = '<tr><td colspan="7">No se encontraron ventas para el período seleccionado.</td></tr>';
      }
      return;
    }
    // Reporte de productos (ventas por producto)
    if(tipo === 'productos'){
      head.innerHTML = '<tr>\n<th>Producto</th>\n<th>Categoría</th>\n<th>Unidades Vendidas</th>\n<th class="text-end">Total Vendido</th>\n</tr>';
      if(Array.isArray(reporteVentasData) && reporteVentasData.length>0){
        const rows = reporteVentasData.map(r => {
          const nombre = r.nombreProducto || r.producto || 'N/D';
          // Varias posibles claves para categoría - usar '-' si no existe
          const categoria = r.categoria || r.nombreCategoria || r.categoriaProducto || r.categoriaNombre || '-';
          // Varias posibles claves para cantidad/unidades vendidas
          const cantidad = (r.cantidadVendida != null) ? r.cantidadVendida : ((r.cantidad != null) ? r.cantidad : ((r.unidades != null) ? r.unidades : 0));
          // Varias posibles claves para total vendido
          let totalVal = (r.totalVendido != null) ? r.totalVendido : ((r.total != null) ? r.total : ((r.monto != null) ? r.monto : ''));
          const total = (totalVal !== '' && !isNaN(Number(totalVal))) ? Number(totalVal).toFixed(2) : totalVal;
          return `<tr>\n<td>${nombre}</td>\n<td>${categoria}</td>\n<td>${cantidad}</td>\n<td class="text-end">${total}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        body.innerHTML = '<tr><td colspan="4">No hay datos de productos para mostrar.</td></tr>';
      }
      return;
    }

    // Reporte de clientes
    if(tipo === 'clientes'){
      head.innerHTML = '<tr>\n<th>ID</th>\n<th>Cliente</th>\n<th>DNI/Documento</th>\n<th class="text-end">Compras</th>\n<th class="text-end">Total Gastado</th>\n</tr>';
      if(Array.isArray(todosLosClientesData) && todosLosClientesData.length>0){
        const rows = todosLosClientesData.map(c => {
          const id = c.idCliente || c.id || '';
          const nombre = `${c.nombre || ''} ${c.apellido || ''}`.trim();
          const documento = c.dni || c.documento || '';
          const compras = c.totalCompras != null ? c.totalCompras : (c.compras || 0);
          const monto = c.montoTotal != null ? c.montoTotal : c.totalGastado || '';
          return `<tr>\n<td>${id}</td>\n<td>${nombre}</td>\n<td>${documento}</td>\n<td class="text-end">${compras}</td>\n<td class="text-end">${monto}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        body.innerHTML = '<tr><td colspan="5">No hay datos de clientes para mostrar.</td></tr>';
      }
      return;
    }

    // Reporte de proveedores
    if(tipo === 'proveedores'){
      head.innerHTML = '<tr>\n<th>ID</th>\n<th>Proveedor</th>\n<th>RUC</th>\n<th class="text-end">Productos</th>\n<th>Contacto</th>\n</tr>';
      if(Array.isArray(todosLosProveedoresData) && todosLosProveedoresData.length>0){
        const rows = todosLosProveedoresData.map(p => {
          const id = p.idProveedor || p.id || '';
          const nombre = p.nombre || '';
          const ruc = p.ruc || p.rucProveedor || '';
          const cantidad = p.cantidadProductos != null ? p.cantidadProductos : p.productosCount || 0;
          const contacto = p.contacto || p.email || p.telefono || '';
          return `<tr>\n<td>${id}</td>\n<td>${nombre}</td>\n<td>${ruc}</td>\n<td class="text-end">${cantidad}</td>\n<td>${contacto}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        body.innerHTML = '<tr><td colspan="5">No hay datos de proveedores para mostrar.</td></tr>';
      }
      return;
    }

    // Reporte financiero: agrupa todas las ventas por fecha y muestra totales diarios
    if(tipo === 'financiero'){
      head.innerHTML = '<tr>\n<th>Fecha</th>\n<th class="text-end">Ventas</th>\n<th class="text-end">Total</th>\n</tr>';
      // Si vinieron datos de la API, se espera [{fecha, count, total},...]
      if(apiData && Array.isArray(apiData) && apiData.length>0){
        const rows = apiData.map(r => {
          const fecha = formatDateDDMMYYYY(r.fecha || r.fechaString || r.fechaStr || String(r.fecha));
          const count = r.count || r.ventas || 0;
          const total = (r.total != null) ? (Number(r.total).toFixed ? Number(r.total).toFixed(2) : r.total) : '0.00';
          return `<tr>\n<td>${fecha}</td>\n<td class="text-end">${count}</td>\n<td class="text-end">${total}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        // Fallback: construir desde ventasFiltradasPorFecha o todasLasVentasData
        const src = (Array.isArray(ventasFiltradasPorFecha) && ventasFiltradasPorFecha.length>0) ? ventasFiltradasPorFecha : todasLasVentasData;
        if(Array.isArray(src) && src.length>0){
          const agrupado = {};
          src.forEach(v => {
            let fecha = '';
            if(v.fecha){
              if(typeof v.fecha === 'string') fecha = v.fecha.split('T')[0];
              else if(v.fecha.year !== undefined) fecha = `${v.fecha.year}-${String(v.fecha.month).padStart(2,'0')}-${String(v.fecha.day).padStart(2,'0')}`;
              else fecha = String(v.fecha).split('T')[0];
            }
            if(!fecha) fecha = 'N/D';
            const total = Number(v.total) || 0;
            if(!agrupado[fecha]) agrupado[fecha] = { count:0, total:0 };
            agrupado[fecha].count += 1;
            agrupado[fecha].total += total;
          });
          const rows = Object.keys(agrupado).sort().map(f => {
            const t = agrupado[f];
            return `<tr>\n<td>${formatDateDDMMYYYY(f)}</td>\n<td class="text-end">${t.count}</td>\n<td class="text-end">${t.total.toFixed(2)}</td>\n</tr>`;
          }).join('\n');
          body.innerHTML = rows;
        } else {
          body.innerHTML = '<tr><td colspan="3">No hay datos financieros para el período seleccionado.</td></tr>';
        }
      }
      return;
    }

    // Reporte de promociones
    if(tipo === 'promociones'){
      head.innerHTML = '<tr>\n<th>ID</th>\n<th>Promoción</th>\n<th>Producto</th>\n<th>Descuento</th>\n<th>Vigente</th>\n</tr>';
      if(Array.isArray(todasLasPromocionesData) && todasLasPromocionesData.length>0){
        const rows = todasLasPromocionesData.map(p => {
          const id = p.idPromocion || p.id || '';
          const nombre = p.nombre || '';
          const producto = p.nombreProducto || p.producto || '';
          const descuento = p.descuento != null ? p.descuento : '';
          const vigente = (p.vigente === true || p.activo === true) ? 'Sí' : 'No';
          return `<tr>\n<td>${id}</td>\n<td>${nombre}</td>\n<td>${producto}</td>\n<td>${descuento}</td>\n<td>${vigente}</td>\n</tr>`;
        }).join('\n');
        body.innerHTML = rows;
      } else {
        body.innerHTML = '<tr><td colspan="5">No hay datos de promociones para mostrar.</td></tr>';
      }
      return;
    }

    // Default placeholder para tipos no manejados
    head.innerHTML = '<tr><th>Ejemplo</th><th>Valor</th></tr>';
    body.innerHTML = '<tr><td>Datos</td><td>N/A</td></tr>';
  };

  window.exportarPDF = function(){
    const resultados = document.getElementById('resultadosReporte');
    if(!resultados || resultados.style.display === 'none'){ showToast('Primero genere un reporte visible antes de exportar a PDF.','warning'); return; }
    const clone = resultados.cloneNode(true);
    clone.querySelectorAll('button, a.btn, .btn').forEach(el=>el.remove());
    const fecha = new Date().toISOString().slice(0,10);
    const opt = { margin: 0.5, filename: `Reporte_${fecha}.pdf`, image: { type: 'jpeg', quality: 0.98 }, html2canvas: { scale: 2, useCORS: true }, jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' } };
    const tempContainer = document.createElement('div'); tempContainer.style.position='fixed'; tempContainer.style.left='-9999px'; tempContainer.style.top='0'; tempContainer.appendChild(clone); document.body.appendChild(tempContainer);
    try{ html2pdf().set(opt).from(clone).save().finally(()=>{ setTimeout(()=>{ if(document.body.contains(tempContainer)) document.body.removeChild(tempContainer); },500); }); }catch(err){ console.error('Error generando PDF',err); showToast('No se pudo generar el PDF en el cliente.','danger'); if(document.body.contains(tempContainer)) document.body.removeChild(tempContainer); }
  };

  window.exportarExcel = function(){
    const anchor = document.querySelector('a[href*="/reportes/exportar/ventas-excel"]');
    if(anchor){ anchor.click(); return; }
    if(reporteActual === 'ventas'){ window.location.href = '/reportes/exportar/ventas-excel'; return; }
    if(!reporteActual){ showToast('Genere primero un reporte antes de exportar.','warning'); } else { showToast('Exportar Excel para este tipo de reporte aún no está implementado.','info'); }
  };

    setTimeout(()=>{ document.querySelectorAll('.alert').forEach(alert=>{ try{ new bootstrap.Alert(alert).close(); }catch(e){} }); },5000);
  }catch(err){
    try{ console.error('[reportes.js] error durante la inicialización', err); }catch(e){}
    if(window.showToast) window.showToast('Error cargando funcionalidades de Reportes: '+(err && err.message ? err.message : err),'danger');
  }

})();
