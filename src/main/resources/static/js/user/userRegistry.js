$.validator.addMethod("institution", function (value, element) {
  return this.optional(element)
      || /^[A-Za-z0-9._%+-]+@alunos.utfpr.edu.br$/.test(value);
}, "Utilize o e-mail institucional, ex: ...@alunos.utfpr.edu.br");

$.validator.addMethod("uui", function (value, element) {
  return this.optional(element)
      || /^[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}$/i.test(
          value.toUpperCase());
}, "token com formato inválido verifique e tente novamente");

function renderError($) {
  $('#loading-plate').remove();
  $("div.plate").append(
      '<span id="loading-plate-error" class="text-danger">Placa Inválida, verifique a informação e tente novamente</span>')
}

const students = {}

jQuery(document).ready(function ($) {
  $.validator.setDefaults({
    errorElement: 'span',
    errorClass: 'validate-has-error',
    highlight: function (element) {
      $(element).closest('.form-group').addClass('validate-has-error');
    },
    unhighlight: function (element) {
      $(element).closest('.form-group').removeClass('validate-has-error');
    },
  })

  $("form#email_registry").validate({
    rules: {
      email: {
        required: true,
        institution: true,
      }
    },
    messages: {
      email: {
        required: "O campo deve ser preenchido"
      }
    },
  });

  $("form#token_validate").validate({
    rules: {
      token: {
        required: true,
        uui: true,
      }
    },
    messages: {
      token: {
        required: "O campo deve ser preenchido"
      }
    }
  });
})
