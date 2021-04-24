$(document).ready(function () {
  disablePanel();
  executeGetAllUser()
})

const $body = $('body');
const $panel = $body.find('.panel')

$body.on('click', '.panel a[data-toggle="reload"]', function (ev) {
  ev.preventDefault();
  disablePanel();
  executeGetAllUser();
});

function executeGetAllUser(params) {
  const queryParams = params ? params : '';
  $.ajax({
    async: true,
    url: '/users/all' + queryParams,
    dataType: 'json',
    success: function (result) {
      renderItems(result);
    },
    error: function (error) {
      console.error(error.responseText)
    },
    complete: function () {
      enablePanel();
    }
  })
}

function disablePanel() {
  $panel.append('<div class="panel-disabled"><div class="loader-1"></div></div>');
}

function enablePanel() {
  const $pd = $panel.find('.panel-disabled');
  $pd.fadeOut('fast', function() {
    $pd.remove();
  });
}

function renderItems(users) {
  console.log(users)
  const $tbody = $panel.find('tbody');
  let $thTag = '';
  users.content.forEach(function (value) {
    let $tr = '<tr class="user_item">';
    let plate = 'Não informado'
    let model = 'Não informado'

    if (value.car) {
      plate = value.car.plate;
      model = value.car.model;
    }

    $tr += '<th>' + value.id + '</th>';
    $tr += '<td>' + value.name + '</td>';
    $tr += '<td>' + value.accessCard.username + '</td>';
    $tr += '<td>' + plate + '</td>';
    $tr += '<td>' + model + '</td>';
    $tr += '<td>' + translateType(value.typeUser) + '</td>';
    $tr += '<td>' + value.accessCard.accountNonLocked ? 'Não' : 'Sim' + '</td>';
    $tr += '<td>' + value.accessCard.enabled ? 'Não' : 'Sim' + '</td>';
    $tr += '</tr>';

    $thTag += $tr;
  });

  $tbody.html($thTag)
}

function renderPagination() {

}

function translateType(type) {
  switch (type) {
    case 'SERVICE':
      return 'Servidor';
    case 'STUDENTS':
      return 'Estudante';
    case 'SPEAKER':
      return 'Palestrante';
    default:
      return 'Não Informado'
  }
}



