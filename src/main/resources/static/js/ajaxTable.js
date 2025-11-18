// Helper ajaxTable: solicita /api endpoints con parámetros de paginación y renderiza filas
// Uso: ajaxTable.init({url:'/api/ventas', tableBody:'#tablaVentas tbody', pageSize:25, renderRow: fn, pagination:'#ventasPagination'});
const ajaxTable = (function(){
  let config = {};
  function buildQuery(params) {
    return Object.keys(params).map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k])).join('&');
  }
  async function load(page) {
    page = page || 0;
    const params = Object.assign({}, config.extraParams || {}, { page: page, size: config.pageSize || 25 });
    const q = buildQuery(params);
    const resp = await fetch(config.url + '?' + q, { credentials: 'same-origin' });
    if (!resp.ok) {
      // try to read body for debugging (may be HTML error page)
      let bodyText = '';
      try { bodyText = await resp.clone().text(); } catch(e){ bodyText = '<no body>'; }
      console.error('[ajaxTable] request failed', { url: config.url + '?' + q, status: resp.status, body: bodyText });
      throw new Error('Error al obtener datos: ' + resp.status);
    }
    let json;
    try {
      json = await resp.json();
    } catch (err) {
      // if JSON parse fails, log response text to help debugging
      let txt = '';
      try { txt = await resp.clone().text(); } catch(e){ txt = '<no body>'; }
      console.error('[ajaxTable] Error parsing JSON response for', config.url + '?' + q, err, 'responseText:', txt);
      throw new Error('Error parseando respuesta del servidor');
    }
    render(json);
  }
  function render(pageObj) {
    const tbody = document.querySelector(config.tableBody);
    if (!tbody) {
      console.error('[ajaxTable] tableBody selector not found:', config.tableBody);
      try { if (window.showToast) showToast('No se encontró la tabla para mostrar los datos', 'warning'); } catch(e){}
      return;
    }
    tbody.innerHTML = '';
    (pageObj.content || []).forEach(item => {
      const tr = config.renderRow(item);
      tbody.appendChild(tr);
    });
    renderPagination(pageObj);
  }
  function renderPagination(pageObj) {
    if (!config.pagination) return;
    const el = document.querySelector(config.pagination);
    if (!el) {
      console.error('[ajaxTable] pagination element not found:', config.pagination);
      return;
    }
    el.innerHTML = '';
    const totalPages = pageObj.totalPages || 1;
    for (let i=0;i<totalPages;i++){
      const btn = document.createElement('button');
      btn.className = 'btn btn-sm btn-outline-primary me-1';
      if (i === pageObj.number) btn.classList.add('active');
      btn.textContent = (i+1);
      btn.addEventListener('click', ()=> load(i));
      el.appendChild(btn);
    }
  }
  return {
    init: function(conf){ config = conf; load(conf.initialPage || 0).catch(e=>{console.error(e); showToast && showToast('Error cargando tabla','danger');}); },
    reload: function(){ load(0); },
    loadPage: load
  };
})();
