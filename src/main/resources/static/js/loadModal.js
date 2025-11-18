// loadModal(url, modalSelector): carga HTML parcial o JSON y lo inyecta en el modal
async function loadModal(url, modalSelector) {
  const resp = await fetch(url, { credentials: 'same-origin' });
  if (!resp.ok) {
    showToast && showToast('Error cargando modal','danger');
    return;
  }
  const text = await resp.text();
  const modalEl = document.querySelector(modalSelector);
  if (!modalEl) { console.warn('Modal no encontrado', modalSelector); return; }
  const modalBody = modalEl.querySelector('.modal-body');
  if (modalBody) modalBody.innerHTML = text;
  // Inicializar Parsley si está disponible
  if (window.jQuery && window.Parsley) {
    const $form = modalEl.querySelector('form');
    if ($form) {
      try { window.$($form).parsley(); } catch(e) { console.warn('Parsley init error', e); }
    }
  }

  // Manejar formularios que indiquen data-ajax="true"
  const form = modalEl.querySelector('form[data-ajax="true"]');
  if (form) {
    form.addEventListener('submit', async function(e){
      e.preventDefault();
      const fd = new FormData(form);
      const action = form.getAttribute('action') || url;
      const method = (form.getAttribute('method') || 'post').toUpperCase();
      const opts = { method: method, credentials: 'same-origin' };
      if (method === 'GET') {
        const q = new URLSearchParams(fd).toString();
        const r = await fetch(action + (q ? ('?' + q) : ''), opts);
        const t = await r.text();
        if (!r.ok) { modalBody.innerHTML = t; return; }
        // success: close modal and reload page or table
        const modalObj = new bootstrap.Modal(modalEl);
        modalObj.hide();
        if (window.ajaxTable) window.ajaxTable.reload && window.ajaxTable.reload();
        return;
      } else {
        opts.body = fd;
      }
      const res = await fetch(action, opts);
      if (res.status === 400) {
        // servidor devuelve partial con errores o JSON
        const ct = res.headers.get('content-type') || '';
        if (ct.indexOf('application/json') !== -1) {
          const json = await res.json();
          // mapear errores en campos si estructura conocida
          if (json.errors) {
            Object.keys(json.errors).forEach(field => {
              const f = form.querySelector('[name="'+field+'"]');
              if (f) {
                const help = document.createElement('div'); help.className='invalid-feedback d-block'; help.textContent = json.errors[field];
                f.classList.add('is-invalid');
                const existing = f.parentNode.querySelector('.invalid-feedback'); if (existing) existing.remove();
                f.parentNode.appendChild(help);
              }
            });
          }
        } else {
          const t = await res.text();
          modalBody.innerHTML = t;
        }
        return;
      }
      if (!res.ok) {
        showToast && showToast('Error al enviar el formulario','danger');
        return;
      }
      // éxito: cerrar modal y refrescar tabla si aplica
      const modalObj = new bootstrap.Modal(modalEl);
      modalObj.hide();
      if (window.ajaxTable) window.ajaxTable.reload && window.ajaxTable.reload();
    });
  }

  const modal = new bootstrap.Modal(modalEl);
  modal.show();
}
