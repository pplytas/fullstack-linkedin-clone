(function() {
	angular.module('tediApp')
	.controller('editProfileCtrl', function($scope, globalFunctions) {
		$scope.changeActiveLink("settings-link");

        $('#_submit').click(function(e) {
               e.preventDefault();
               var email = $('#email').val();
               var password = $('#password').val();

               globalFunctions.updateUser(email, password);
         });
	});
})();
