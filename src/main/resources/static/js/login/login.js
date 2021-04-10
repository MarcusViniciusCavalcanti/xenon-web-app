jQuery(document).ready(function ($) {
    setTimeout(function () {
        $(".fade-in-effect").addClass('in');
    }, 1);

    $("form#login").validate({
        rules: {
            username: {required: true}, password: {required: true}
        },
        messages: {
            username: {required: 'Por favor digite username.'},
            password: {required: 'Por favor digite password.'}
        }
    });

    $("form#login .form-group:has(.form-control):first .form-control").focus();
});
