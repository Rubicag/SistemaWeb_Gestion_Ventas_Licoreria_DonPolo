(function(){
  'use strict';
  const productos = window.PRODUCTOS_DATA || [];

  function nuevoProducto(){
    const form = document.getElementById('formProducto');
    const titulo = document.getElementById('modalTitulo');
    if(titulo) titulo.innerHTML = '<i class="fas fa-plus-circle me-2"></i>Nuevo Producto';
    if(form){ form.action = '/productos/agregar'; form.reset(); const id = document.getElementById('idProducto'); if(id) id.value = ''; }
  }

  function editarProducto(id){
    const producto = productos.find(p => Number(p.idProducto) === Number(id));
    if(!producto) return;
    const titulo = document.getElementById('modalTitulo'); if(titulo) titulo.innerHTML = '<i class="fas fa-edit me-2"></i>Editar Producto';
    const form = document.getElementById('formProducto'); if(!form) return; form.action = '/productos/actualizar';
    const setIf = (sel,val) => { const el = document.getElementById(sel); if(el) el.value = val || ''; };
    setIf('idProducto', producto.idProducto);
    setIf('nombre', producto.nombre);
    setIf('descripcion', producto.descripcion);
    setIf('codigoBarras', producto.codigoBarras);
    setIf('marca', producto.marca);
    setIf('presentacion', producto.presentacion);
    setIf('gradoAlcoholico', producto.gradoAlcoholico);
    setIf('precio', producto.precio);
    setIf('stock', producto.stock);
    setIf('stockMinimo', producto.stockMinimo || 5);
    setIf('idCategoria', producto.idCategoria);
    setIf('idProveedor', producto.idProveedor);
    const modal = document.getElementById('modalProducto'); if(modal) new bootstrap.Modal(modal).show();
  }

  function eliminarProducto(id,nombre){
    if(typeof showConfirm === 'function'){
      showConfirm('¿Está seguro de eliminar el producto ' + nombre + '?', function(){ window.location.href = '/productos/eliminar/' + id; });
    } else if(confirm('¿Está seguro de eliminar el producto ' + nombre + '?')){
      window.location.href = '/productos/eliminar/' + id;
    }
  }

  function filtrarTabla(){
    const searchEl = document.getElementById('searchInput');
    const categoriaEl = document.getElementById('filtroCategoria');
    const proveedorEl = document.getElementById('filtroProveedor');
    const searchValue = (searchEl && searchEl.value != null) ? String(searchEl.value).toUpperCase() : '';
    const categoriaValue = (categoriaEl && categoriaEl.value != null) ? String(categoriaEl.value).toUpperCase() : '';
    const proveedorValue = (proveedorEl && proveedorEl.value != null) ? String(proveedorEl.value).toUpperCase() : '';
    const table = document.getElementById('tablaProductos'); if(!table) return; const tr = table.getElementsByTagName('tr'); let count=0;
    for(let i=1;i<tr.length;i++){ const tds = tr[i].getElementsByTagName('td'); if(tds.length===0) continue; const nombre = (tds[1].textContent||tds[1].innerText||''); const categoria = (tds[2].textContent||tds[2].innerText||''); const proveedor = (tds[3].textContent||tds[3].innerText||''); const matchSearch = searchValue === '' || nombre.toUpperCase().indexOf(searchValue) > -1; const matchCategoria = categoriaValue === '' || categoria.toUpperCase().indexOf(categoriaValue) > -1; const matchProveedor = proveedorValue === '' || proveedor.toUpperCase().indexOf(proveedorValue) > -1; if(matchSearch && matchCategoria && matchProveedor){ tr[i].style.display=''; count++; } else { tr[i].style.display='none'; }}
    const totalEl = document.getElementById('totalProductos'); if(totalEl) totalEl.textContent = count;
  }

  // Event delegation
  document.addEventListener('click', function(e){
    const editBtn = e.target.closest('.btn-editar-producto'); if(editBtn){ editarProducto(Number(editBtn.dataset.productoId)); return; }
    const delBtn = e.target.closest('.btn-eliminar-producto'); if(delBtn){ eliminarProducto(Number(delBtn.dataset.productoId), delBtn.dataset.productoNombre); return; }
  });

  // Expose global
  window.nuevoProducto = nuevoProducto;
  window.editarProducto = editarProducto;
  window.eliminarProducto = eliminarProducto;
  window.filtrarTabla = filtrarTabla;

  document.addEventListener('DOMContentLoaded', function(){ try{ filtrarTabla(); }catch(e){console.error(e);} });
})();
