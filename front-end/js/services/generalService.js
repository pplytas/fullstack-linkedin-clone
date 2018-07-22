(function() {

	angular.module('tediApp')
	.factory('globalFunctions', function($rootScope, $http) {
		var globalFunctionsFactory = {};

		globalFunctionsFactory.init_app = function(){
			$rootScope.tediAPI = 'https://localhost:8443';
		}

		globalFunctionsFactory.login = function(inputEmail, inputPassword) {
            var endpoint = '/login';
			var url = $rootScope.tediAPI + endpoint;

			var parameter = {
				email: inputEmail,
                password: inputPassword
			};

			return $http({
				method: "POST",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                params: parameter
			}).then(function onSuccess(response) {
                // Handle success
                console.log(response);
                console.log("Login Successful");
                $http({
    				method: "GET",
    				url: $rootScope.tediAPI + "/admin/userlist"
    			}).then(function onSuccess(response) {
                    console.log(response);
                })
              }).catch(function onError(response) {
                // Handle error
                console.log(response);
                console.log("Login Failed");
              });

		}

		return globalFunctionsFactory;
	})

})();
