$(document).ready(function ($) {
  $('#avatar').on('change', function (event) {
    event.preventDefault();

    $('#avatar_upload').submit();
  });
});