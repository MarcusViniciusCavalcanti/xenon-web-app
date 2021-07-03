$(document).ready(function ($) {
  $('#avatar').on('change', function (event) {
    event.preventDefault();

    $('#avatar_upload').submit();
  });
  // const avatar = event.target.files[0];
  //
  // const formData = new FormData();
  // formData.append('file', avatar);

  //   $.ajax({
  //     url: '/user/avatar/upload',
  //     type: 'post',
  //     data: formData,
  //     enctype: 'multipart/form-data',
  //     contentType: "application/octet-stream",
  //     processData: false,
  //     success: function (data) {
  //       console.log(data)
  //       const reader = new FileReader();
  //       reader.onload = function (event) {
  //         $('#avatar_user').attr('src', event.target.result)
  //       }
  //
  //       reader.readAsDataURL(avatar);
  //     },
  //     error: function (error) {
  //       console.error(error)
  //     }
  //   })
  // })
});