(function() {

	var app = angular.module("tediApp", [
	 	"ngRoute",
		"angular-jwt"])
	.config(function($httpProvider, $routeProvider, jwtOptionsProvider) {
		/* ================= Authendication JWT ================= */
		jwtOptionsProvider.config({
			tokenGetter: function(options) {
				token = localStorage.isjwt;
				if (!token) {
					window.location.href = "/login";
				}
				return token;
			},
			whiteListedDomains: ["localhost"]
		});
		$httpProvider.interceptors.push("jwtInterceptor");

		/* ================= Routing ================= */
		$routeProvider
		.when("/login", {
			template: "",
			controller: function() {
  				window.location.href = "/login";
  			}
		})
		.when("/logout", {
			template: "",
			controller: function() {
				delete localStorage.isjwt;
				window.location.href = "/login";
  			}
		})
		.when("/home", {
			templateUrl: '../templates/home.html'
		})
		.when("/edit", {
			templateUrl: '../templates/edit-profile.html',
			controller: 'editProfileCtrl'
		})
		.otherwise({
	        templateUrl: '../templates/home.html'
	    });
		/* =========================================== */
	})
	.run(function ($rootScope, globalFunctions) {
		globalFunctions.init_app();
        $rootScope.login = globalFunctions.login;
		$rootScope.registerUser = globalFunctions.registerUser;
		$rootScope.getuserlist = globalFunctions.getuserlist;
		$rootScope.getUserDetails = globalFunctions.getUserDetails;
		$rootScope.updateUser = globalFunctions.updateUser;
		$rootScope.logout = globalFunctions.logout;
		console.log(!localStorage.isjwt);
	});

})();
