$(document).ready(function ($) {
  $("#type_user").selectBoxIt({
    showEffect: 'fadeIn',
    hideEffect: 'fadeOut'
  });

  $("#user_roles").select2({
    placeholder: 'Selecione os perfils',
    allowClear: true
  }).on('select2-open', function () {
    $(this).data('select2').results.addClass(
        'overflow-hidden').perfectScrollbar();
  });

  $.validator.setDefaults({
    errorElement: 'span',
    errorClass: 'validate-has-error',
    highlight: function (element) {
      $(element).closest('.form-group').addClass('validate-has-error');
    },
    unhighlight: function (element) {
      $(element).closest('.form-group').removeClass('validate-has-error');
    },
  });

  $("form#form_new_user").validate(options);
  $("form#form_update_user").validate(options);
});

const options = {
  rules: {
    name: {
      required: true,
      minlength: 5,
      maxlength: 255,
    },
    username: {
      required: true,
      email: true,
    },
    authorities: {
      required: true,
    }
  },
  messages: {
    name: {
      required: "O campo deve ser preenchido",
      minlength: "O valor do campo de ser maior que 5 caracteres",
      maxlength: "O valor do campo de ser maior que 5 caracteres",
    },
    username: {
      required: "O campo deve ser preenchido",
      email: "Deve conter uma e-mail válido"
    },
    authorities: {
      required: "O campo deve ser preenchido",
    },
  },
};