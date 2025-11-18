// dashboard.js: funciones para cargar datos del endpoint /api/dashboard y renderizar gráficos con Chart.js
async function loadDashboard() {
  try {
    const resp = await fetch('/api/dashboard', { credentials: 'same-origin' });
    if (!resp.ok) throw new Error('No se pudo obtener dashboard');
    const data = await resp.json();
    // Actualizar KPI cards
    document.querySelectorAll('[data-kpi-ventas-hoy]').forEach(el => el.textContent = data.ventasHoy);
    document.querySelectorAll('[data-kpi-ventas-30]').forEach(el => el.textContent = data.ventas30Dias);
    document.querySelectorAll('[data-kpi-clientes-activos]').forEach(el => el.textContent = data.clientesActivos);
    document.querySelectorAll('[data-kpi-stock-bajo]').forEach(el => el.textContent = data.stockBajo);
    // Chart grande
    if (window.Chart && data.chartData) {
      const ctx = document.getElementById('dashboardChart');
      if (ctx) {
        new Chart(ctx.getContext('2d'), {
          type: 'line',
          data: data.chartData,
          options: { responsive: true }
        });
      }
    }
  } catch (e) { console.error(e); showToast && showToast('Error cargando dashboard','danger'); }
}

// Guardar filtros en localStorage
function saveDashboardFilters(key, filters) {
  localStorage.setItem('dashboard.'+key, JSON.stringify(filters));
}
function loadDashboardFilters(key) {
  const v = localStorage.getItem('dashboard.'+key);
  return v ? JSON.parse(v) : null;
}
