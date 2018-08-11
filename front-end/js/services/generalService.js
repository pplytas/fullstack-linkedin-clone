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

			var data = {
				"email": inputEmail,
				"password": inputPassword
			};

			return $http({
				method: "POST",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                data: data
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				localStorage.isjwt = response.headers("Authorization").replace('Bearer ', '');
				console.log(localStorage.isjwt);
				console.log("Login Successful");
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log(response.headers("Authorization"));
				console.log("Login Failed");
			});
		}

		globalFunctionsFactory.getUserList = function() {
            var endpoint = '/admin/userlist';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' }
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				console.log("UserList Successful");
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log("UserList Failed");
			});
        }

		globalFunctionsFactory.registerUser = function(
			email,
			password,
			name = null,
			surname = null,
			telNumber = null,
			picture = null
		) {
			var endpoint = '/register';
			var url = $rootScope.tediAPI + endpoint;

			var newUser = {
				"email": email,
				"password": password,
				"name": name,
				"surname": surname,
				"telNumber": telNumber,
				"picture": picture
			};

			return $http({
				method: "POST",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				data: newUser
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				console.log("Register Successful");
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log("Register Failed");
			});
		}

		globalFunctionsFactory.updateUser = function(
			email = null,
			password = null,
			name = null,
			surname = null,
			telNumber = null,
			picture = null
		) {
			var endpoint = '/user/update';
			var url = $rootScope.tediAPI + endpoint;

			var newUser = {
				"email": email,
				"password": password,
				"name": name,
				"surname": surname,
				"telNumber": telNumber,
				"picture": picture
			};

			return $http({
				method: "PUT",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				data: newUser
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				console.log("Update Successful");
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log("Update Failed");
			});
		}

		globalFunctionsFactory.getUserDetails = function(email = null) {
			var endpoint = '/user/details';
			var url = $rootScope.tediAPI + endpoint;
			var responseData;

			data = {
				"email": email
			};

			return $http({
				method: "GET",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				data: data
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				console.log("Details Successful");
				return response.data;
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log("Details Failed");
				return response.data;
			});
		}

		globalFunctionsFactory.logout = function() {
			var endpoint = '/logout';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
			}).then(function onSuccess(response) {		// Handle success
				delete localStorage.isjwt;
				console.log("Logout Successful");
			}).catch(function onError(response) {		// Handle error
				console.log("Logout Failed");
			});
        }
        
        globalFunctionsFactory.postArticle = function(
            title,
            text,
            media = null
        ) {
            var endpoint = '/article/new';
            var url = $rootScope.tediAPI + endpoint;
            var newArticle = {
                "title": title,
                "text": text,
                "media": media
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
				data: newArticle
			}).then(function onSuccess(response) {		// Handle success
				console.log(response);
				console.log("Article posted Successful");
			}).catch(function onError(response) {		// Handle error
				console.log(response);
				console.log("Article post Failed");
			});
        }

		return globalFunctionsFactory;
	})

})();
