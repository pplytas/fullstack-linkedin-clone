(function() {

    var app = angular.module("loginApp", ['naif.base64']).run(function ($rootScope) {

        $rootScope.picture = null;

        if (localStorage.isjwt) {
            window.location.href = (localStorage.isAdmin == 'true') ? '/#!/admin':'/#!/home';
        }

        function clear_messages() {
            $('#loading-spinner').hide();
            $('#invalid-msg').hide();
        }

        $('#sign-up-top').click(function(e) {
            e.preventDefault();
            $('#register').fadeIn(500);
        });

        $('#sign-in').click(function(e) {
            if(document.getElementById("sign-in-form").checkValidity()) {
                e.preventDefault();
                clear_messages();
                var url = 'https://localhost:8443/login'
                var email = $('#email').val();
                var password = $('#password').val();
                var data = {
                    "email": email,
                    "password": password
                };

                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    beforeSend: function() {
                        $('#loading-spinner').show();
                        $('#sign-in span').text('');
                    },
                    success: function(data, status, $xhr) {
                        localStorage.isjwt = $xhr.getResponseHeader("Authorization").replace('Bearer ', '');
                        if ($xhr.status === 200) {
                            localStorage.isAdmin = false;
                            window.location.href = "/#!/home";
                        }
                        else if ($xhr.status === 202) {
                            localStorage.isAdmin = true;
                            window.location.href = "/#!/admin";
                        }
                    },
                    error: function($xhr) {
                        clear_messages();
                        $('#sign-in span').text('Sign In');
                        delete localStorage.isjwt;
                        if ($xhr.status >= 400 && $xhr.status < 500) {	// Invalid credentials
                            $('#invalid-msg').show();
                        }
                    }
                });
            }
        });

        $('#register-btn').click(function(e) {
            if(document.getElementById("register-form").checkValidity()) {
                e.preventDefault();
               clear_messages();
               var url = 'https://localhost:8443/register'
               var email = ($('#reg-email').val() == "" ?  null : $('#reg-email').val())
               var password = ($('#reg-password').val() == "" ?  null : $('#reg-password').val())
               var firstname = ($('#firstname').val() == "" ?  null : $('#firstname').val())
               var lastname = ($('#lastname').val() == "" ?  null : $('#lastname').val())
               var phone = ($('#phone').val() == "" ?  null : $('#phone').val())
               var picture = (!$rootScope.picture ? null : $rootScope.picture.base64)

               var data = {
                   "email": email,
                   "password": password,
                   "name": firstname,
                   "surname": lastname,
                   "telNumber": phone,
                   "picture": picture
               };

               $.ajax({
                   type: "POST",
                   url: url,
                   contentType: "application/json",
                   data: JSON.stringify(data),
                   beforeSend: function() {
                       $('#wait').show()
                   },
                   success: function(data, status, $xhr) {
                       var url = 'https://localhost:8443/login'
                       var data = {
                           "email": email,
                           "password": password
                       };
                       $.ajax({
                           type: "POST",
                           url: url,
                           contentType: "application/json",
                           data: JSON.stringify(data),
                           success: function(data, status, $xhr) {
                               localStorage.isjwt = $xhr.getResponseHeader("Authorization").replace('Bearer ', '');
                               localStorage.isAdmin = false;
                               window.location.href = "/#!/home";
                           },
                           error: function($xhr) {
                               clear_messages();
                               delete localStorage.isjwt;
                               if ($xhr.status >= 400 && $xhr.status < 500) {	// Invalid credentials
                                   $('#loginmsg').show();
                               }
                           }
                       });
                   },
                   error: function($xhr) {
                       clear_messages();
                       if ($xhr.status >= 400 && $xhr.status < 500) {	// Invalid credentials
                           $('#loginmsg').show();
                       }
                   }
               });
           }
        });
    });

})();
