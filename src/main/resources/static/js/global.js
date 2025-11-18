// Placeholder global.js
// Contiene utilidades globales mínimas necesarias por las plantillas.
window.showToast = window.showToast || function(msg, level){
  try{ console.log('[toast]', level || 'info', msg); }catch(e){}
};
window.showConfirm = window.showConfirm || function(message, cb){ if(confirm(message)){ cb && cb(); } };

document.addEventListener('DOMContentLoaded', function(){
  // No-op placeholder
});
