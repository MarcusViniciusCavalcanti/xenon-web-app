let numberPage = 5;
const state = {
  page: 0,
  size: 5,
  sortDirection: '',
  sort: '',
}

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

function executeGetAllUser() {
  $.ajax({
    async: true,
    data: state,
    url: '/users/all',
    dataType: 'json',
    type: 'get',
    success: function (result) {
      state.page = result.pageable.pageNumber;
      state.size = result.pageable.pageSize;
      renderItems(result);
      pagination(result)
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

    const accountNonLocked = value.accessCard.accountNonLocked ? 'Não' : 'Sim';
    const enabled = value.accessCard.enabled ? 'Sim' : 'Não';
    const authorisedAccess = value.authorisedAcces ? 'Sim' : 'Não';

    $tr += '<th>' + value.id + '</th>';
    $tr += '<td>' + value.name + '</td>';
    $tr += '<td>' + value.accessCard.username + '</td>';
    $tr += '<td>' + plate + '</td>';
    $tr += '<td>' + model + '</td>';
    $tr += '<td>' + translateType(value.typeUser) + '</td>';
    $tr += '<td>' + accountNonLocked + '</td>';
    $tr += '<td>' + enabled + '</td>';
    $tr += '<td>' + authorisedAccess + '</td>';
    $tr += '<td>'
        + '<a href="/usuario/atualizar/' + value.id
        + '" class="btn btn-info btn-icon btn-icon-standalone btn-sm"><i class="fa-pencil"></i><span>Atualizar</span></a>'
        + '<a href="/usuario/desativar/' + value.id
        + '" class="btn btn-danger btn-icon btn-icon-standalone btn-sm"><i class="fa-close"></i><span>Desativar</span></a>'
        + '</td>';
    $tr += '</tr>';

    $thTag += $tr;
  });

  $tbody.html($thTag)
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

function pagination(page) {
  $pagination = $('ul.pagination');

  let totalPages = page.totalPages;
  const numberOfElements = page.numberOfElements;

  let maxLeft = (state.page - Math.floor(numberOfElements / 2));
  let maxRight = (state.page + Math.floor(numberOfElements / 2));

  if (maxLeft < 1) {
    maxLeft = 1
    maxRight = numberPage
  }

  if (maxRight > totalPages) {
    maxLeft = totalPages - (numberPage - 1)

    if (maxLeft < 1) {
      maxLeft = 1
    }
    maxRight = totalPages
  }

  let ligTag = '';

  if (state.page !== 0) {
    ligTag += '<li><a href="#" class="prev" onclick="nextPage(0)">Primeira</a></li>'
  }

  for (let i = maxLeft; i <= maxRight; i++) {
    let active = '';
    if (page.number === i - 1) {
      active = 'class="numb active"';
    } else {
      active = 'class="numb"';
    }

    ligTag += '<li ' + active + '><a href="#" onclick="nextPage(' + (i - 1)
        + ')">' + i + '</a></li>'
  }

  if (state.page !== totalPages) {
    ligTag += '<li><a href="#" class="next" onclick="nextPage(' + (totalPages
        - 1) + ')">Ultima</a></li>'
  }

  $pagination.html(ligTag);
}

function nextPage(page) {
  state.page = page;
  executeGetAllUser();
}

$("#size_element").change(function () {
  $("#size_element option:selected").each(function () {
    state.size = $(this).text();

    executeGetAllUser();
  });
})

$('#search').click(function () {
  state.name = $('#search_name').val();
  state.profile = $('#search_profile').val();
  state.type = $('#search_type_user').val();
  state.enabled = $('#user_enabled').is(':checked') ? true : null;

  console.log(state)
  executeGetAllUser();
});