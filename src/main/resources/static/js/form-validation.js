// ValidaciÃ³n simple de formulario en el cliente
document.addEventListener('DOMContentLoaded', function(){
  const regForm = document.querySelector('form[th\:action="@{/registro}"]') || document.querySelector('form[action="/registro"]');
  if(regForm){
    regForm.addEventListener('submit', function(e){
      const pwd = regForm.querySelector('input[name="password"]');
      const pwd2 = regForm.querySelector('input[name="confirmPassword"]');
      if(pwd && pwd2 && pwd.value !== pwd2.value){
        e.preventDefault();
        let err = regForm.querySelector('.error.client');
        if(!err){ err = document.createElement('div'); err.className='error client'; regForm.prepend(err); }
        err.textContent = 'Las contraseñas no coinciden.';
        pwd2.focus();
      }
    });
  }
});
