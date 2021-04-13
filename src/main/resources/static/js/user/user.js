const $body = $('body');
const $panel = $body.find('.panel')

$body.on('click', '.panel a[data-toggle="reload"]', function(ev) {
  ev.preventDefault();
  disablePanel();
  executeGetAllUser();
});

(function () {
  disablePanel();
  executeGetAllUser()
})();

function executeGetAllUser(params) {
  $.ajax({
    url: `/users/all${params ? params : ''}`,
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
  users.content.forEach(function (value) {
    $tbody.append(`
      <tr>
        <th>${value.id}</span></th>
        <td>${value.name}</td>
        <td>${value.accessCard.username}</td>
        <td>${value.car ? value.car.plate : 'Não Cadastrado'}</td>
        <td>${value.car ? value.car.model : 'Não Cadastrado'}</td>
        <td>${translateType(value.typeUser)}</td>
        <td>${value.accessCard.accountNonLocked ? 'Não' : 'Sim'}</td>
        <td>${value.accessCard.enabled ? 'Não' : 'Sim'}</td>
      </tr>
    `);
  });
}

function renderPagination() {

}

function translateType(type) {
  switch (type) {
    case "SERVICE": return 'Servidor';
    case "STUDENTS": return 'Estudante';
    case "SPEAKER": return 'Palestrante';
    default: return 'Não Informado'
  }
}



