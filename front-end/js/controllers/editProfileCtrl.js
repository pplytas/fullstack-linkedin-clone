(function() {
	angular.module('tediApp')
	.controller('editProfileCtrl', function($rootScope, $scope) {
        $('#_submit').click(function(e) {
               e.preventDefault();
               var email = $('#email').val();
               var password = $('#password').val();

               console.log(email);
               console.log(password);

               $rootScope.updateUser(email, password);
         });
	});
})();
