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
				"email": inputEmail,
				"password": inputPassword
			};

			// $.ajax({
			// 	type: "POST",
			// 	url: url,
			// 	contentType: "application/json; charset=utf-8",
			// 	data: parameter,
			// 	success : function(data) {
			// 		localStorage.isjwt = data.token;
			// 		console.log("Login Successful");
			// 	}
			// });

			return $http({
				method: "POST",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                data: parameter,
			}).then(function onSuccess(response) {
				// Handle success
				console.log(response);
				console.log(response.headers());
				console.log(response.headers("Authorization"));
				console.log("Login Successful");
			}).catch(function onError(response) {
				// Handle error
				console.log(response);
				console.log(response.headers());
				console.log("Login Failed");
			});


			// $.ajax({
  			//    url: "https://localhost:8443/login?email="+inputEmail+"&password="+"toor",
  			//    data: {
  			//       format: 'json'
  			//    },
  			//    error: function(response) {
  			//       console.log(response);
  			//    },
  			//    dataType: 'json',
  			//    success: function(data) {
  			//       console.log(data);
  			//    },
  			//    type: 'POST'
  			// });
  			// var xhttp = new XMLHttpRequest();
  			// xhttp.open("POST", "https://localhost:8443/login", true);
  			// xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  			// xhttp.send("email="+inputEmail+"&password="+inputPassword);
  			//
  			// xhttp.onreadystatechange = function() {
  			// 	if (this.readyState == XMLHttpRequest.DONE && this.status == 200) {
  			// 		console.log("1");
  			// 		var headers = this.getAllResponseHeaders();
  			// 		console.log(headers);
  			// 		console.log(this);
  			// 	}
  			// };
		}

		return globalFunctionsFactory;
	})

})();
