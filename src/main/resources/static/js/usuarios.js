/* global bootstrap, showConfirm, showToast */
(function(){
  const usuarios = window.USUARIOS_DATA || [];

  window.nuevoUsuario = function(){
    const titulo = document.getElementById('modalTitulo'); if(titulo) titulo.textContent = 'Nuevo Usuario';
    const form = document.getElementById('formUsuario'); if(form){ form.reset(); form.action = '/usuarios/guardar'; }
    const pwd = document.getElementById('password'); if(pwd) pwd.required = true;
    const pwdReq = document.getElementById('passwordRequired'); if(pwdReq) pwdReq.textContent = '*';
    const activo = document.getElementById('activo'); if(activo) activo.checked = true;
    const modalEl = document.getElementById('modalUsuario'); if(modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.editarUsuario = function(id){
    const usuario = usuarios.find(u => u.idUsuario === id);
    if(!usuario) return;
    const titulo = document.getElementById('modalTitulo'); if(titulo) titulo.textContent = 'Editar Usuario';
    const form = document.getElementById('formUsuario'); if(form) form.action = '/usuarios/actualizar';
    const idInput = document.getElementById('idUsuario'); if(idInput) idInput.value = usuario.idUsuario;
    const nombre = document.getElementById('nombre'); if(nombre) nombre.value = usuario.nombre;
    const correo = document.getElementById('correo'); if(correo) correo.value = usuario.correo;
    const rol = document.getElementById('rol'); if(rol) rol.value = usuario.rol;
    const activo = document.getElementById('activo'); if(activo) activo.checked = usuario.activo;
    const password = document.getElementById('password'); if(password) { password.value = ''; password.required = false; }
    const pwdReq = document.getElementById('passwordRequired'); if(pwdReq) pwdReq.textContent = '';
    const modalEl = document.getElementById('modalUsuario'); if(modalEl) new bootstrap.Modal(modalEl).show();
  };

  window.cambiarEstado = function(id, activar){
    const accion = activar ? 'activar' : 'desactivar';
    showConfirm(`¿Está seguro de ${accion} este usuario?`, function(){ window.location.href = `/usuarios/${accion}/${id}`; });
  };

  window.filtrarTabla = function(){
    const input = (document.getElementById('buscarUsuario')||{}).value.toLowerCase();
    const rows = document.querySelectorAll('#tablaUsuarios tbody tr');
    rows.forEach(row => { const text = (row.textContent||'').toLowerCase(); row.style.display = text.includes(input) ? '' : 'none'; });
  };

  setTimeout(()=>{ document.querySelectorAll('.alert').forEach(alert=>{ try{ new bootstrap.Alert(alert).close(); }catch(e){} }); },5000);

  document.addEventListener('DOMContentLoaded', function(){
    document.querySelectorAll('.btn-editar-usuario').forEach(btn=>{ btn.addEventListener('click', function(){ const id = parseInt(this.getAttribute('data-usuario-id')); editarUsuario(id); }); });
    document.querySelectorAll('.btn-cambiar-estado').forEach(btn=>{ btn.addEventListener('click', function(){ const id = parseInt(this.getAttribute('data-usuario-id')); const estado = this.getAttribute('data-estado')==='true'; cambiarEstado(id, estado); }); });
  });
})();
