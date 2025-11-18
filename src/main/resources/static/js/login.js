// login.js - comportamiento mínimo para la vista de login
(function(){
    'use strict';

    document.addEventListener('DOMContentLoaded', function(){
        var form = null;
        try {
            // Evitar selectores Thymeleaf inválidos que causarían excepción
            form = document.querySelector('form') || null;
        } catch(e) { form = document.querySelector('form') || null; }
        // Simple: marcar el primer input como autofocus si no lo está
        var firstInput = form ? form.querySelector('input[autofocus]') : null;
        if (!firstInput && form) {
            var inp = form.querySelector('input');
            if (inp) inp.focus();
        }

        // Soporte para mostrar/ocultar contraseña si se necesita (busca .toggle-password)
        document.querySelectorAll('.toggle-password').forEach(function(btn){
            btn.addEventListener('click', function(e){
                var targetSelector = btn.getAttribute('data-target');
                var target = targetSelector ? document.querySelector(targetSelector) : btn.previousElementSibling;
                if (!target) return;
                if (target.type === 'password') { target.type = 'text'; btn.classList.add('visible'); }
                else { target.type = 'password'; btn.classList.remove('visible'); }
            });
        });
    });
})();
