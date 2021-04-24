$.validator.addMethod("institution", function (value, element) {
  return this.optional(element)
      || /^[A-Za-z0-9._%+-]+@alunos.utfpr.edu.br$/.test(value);
}, "Utilize o e-mail institucional, ex: ...@alunos.utfpr.edu.br");

$.validator.addMethod("uui", function (value, element) {
  return this.optional(element)
      || /^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i.test(
          value);
}, "token com formato inválido verifique e tente novamente");

function renderError($) {
  $('#loading-plate').remove();
  $("div.plate").append(
      '<span id="loading-plate-error" class="text-danger">Placa Inválida, verifique a informação e tente novamente</span>')
}

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

  const $formRegistry = $("form#registry_students");
  const $input = $formRegistry.find('#input_plate_car');

  $input.focusin(function () {
    $('#loading-plate').remove();
    $('#loading-plate-success').remove();
    $('#loading-plate-error').remove();
  });

  $input.focusout(function () {
    if ($input.val()) {

      $("div.plate").append(
          '<span id="loading-plate" class="text-info">Por favor aguarde estámos verificando a placa...</span>')

      const data = {value: $input.value}
      submit(
          '/validate-plate',
          data,
          function (response) {
            if (response) {
              $('#loading-plate').remove();
              $("div.plate").append(
                  '<span id="loading-plate-success" class="text-success">Placa válida</span>')
            } else {
              $input.val('');
              renderError($)
            }
          },
          function (error) {
            console.error(error)
            renderError($);
          }
      )
    }
  })

  $("form#email_registry").validate({
    rules: {
      input_email: {
        required: true,
        institution: true,
      }
    },
    messages: {
      input_email: {
        required: "O campo deve ser preenchido"
      }
    },

    submitHandler: function (form) {
      const value = $(form).find('#input_email').val();
      submit(
          '/registry',
          {value: value},
          function (response) {
            console.log('response', response)
            Slider.next();
            $('#email').val(value)
          },
          function (error) {
            console.log('error', error)
            $(".errors-container").html('<div class="alert alert-danger">\
			 									<button type="button" class="close" data-dismiss="alert">\
			 										<span aria-hidden="true">&times;</span>\
			 										<span class="sr-only">Fechar</span>\
			 									</button>\
			 									' + error.responseJSON.result.reason + '\
			 								</div>');

            $(".errors-container .alert").hide().slideDown();
            $(form).find('#input_email').select();
          },
      )
    }
  });

  $("form#token").validate({
    rules: {
      input_token: {
        required: true,
        uui: true
      },
    },
    messages: {
      input_token: {
        required: 'Campo obrigatório'
      }
    },
    submitHandler: function (form) {
      submit(
          '/validate-token',
          {token: $(form).find('#input_token').val()},
          function (response) {
            Slider.next();
          },
          function (error) {
            $(".errors-container").html('<div class="alert alert-danger">\
			 									<button type="button" class="close" data-dismiss="alert">\
			 										<span aria-hidden="true">&times;</span>\
			 										<span class="sr-only">Fechar</span>\
			 									</button>\
			 									' + error.responseJSON.msg + '\
			 								</div>');

            $(".errors-container .alert").hide().slideDown();
            $(form).find('#input_token').select();
          },
      )
    }
  })

  $formRegistry.validate({
    rules: {
      input_email: {
        required: true,
        institution: true,
      }
    },
    messages: {
      input_email: {
        required: "O campo deve ser preenchido"
      }
    },

    submitHandler: function (form) {
      const input = {
        name: $(form).find('#input_full_name').val(),
        email: $(form).find('#email').val(),
        password: $(form).find('#input_password').val(),
        confirmPassword: $(form).find('#input_password_confirm').val(),
        modelCar: $(form).find('#input_model_car').val(),
        plateCar: $(form).find('#input_plate_car').val(),
      }
      submit(
          '/registry',
          input,
          function (response) {
            console.log(response)
          },
          function (error) {
            $(".errors-container").html('<div class="alert alert-danger">\
			 									<button type="button" class="close" data-dismiss="alert">\
			 										<span aria-hidden="true">&times;</span>\
			 										<span class="sr-only">Fechar</span>\
			 									</button>\
			 									' + error.responseJSON.result.reason + '\
			 								</div>');

            $(".errors-container .alert").hide().slideDown();
            $(form).find('#input_email').select();
          },
      )
    }
  });
})

function submit(url, data, onSuccess, onError) {
  const headerName = $('meta[name="_csrf_header"]').attr('content');
  const token = $('meta[name="_csrf"]').attr('content');

  const headers = {};
  headers[headerName] = token;

  $.ajax({
    async: true,
    url,
    method: 'POST',
    dataType: 'json',
    contentType: 'application/json',
    headers,
    data: JSON.stringify(data),
    statusCode: {
      200: onSuccess,
      202: onSuccess,
      400: onError
    }
  })
}
