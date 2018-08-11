(function() {

	var app = angular.module("tediApp", [
	 	"ngRoute",
		"angular-jwt"])
	.config(function($httpProvider, $routeProvider, jwtOptionsProvider) {
		/* ================= Authendication JWT ================= */
		jwtOptionsProvider.config({
			tokenGetter: function(options) {
                token = localStorage.isjwt;
                token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic3ViIjoiMTYiLCJleHAiOjE1MzQ4MDYyNjh9.Haa_Mta6R4iO9ThLcbimeFfJisba3a0qOWyQ2uh-qSR8Daftbeky0qcp_eudp9VLuD970IJ0pPF1DNTQFN3obw";
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
		.when("/profile", {
			templateUrl: '../templates/view-profile.html',
			controller: 'viewProfileCtrl',
			resolve: {
				user: function($rootScope) {
                    return $rootScope.getUserDetails();
                }
			}
        })
		.when("/edit", {
			templateUrl: '../templates/edit-profile.html',
			controller: 'editProfileCtrl'
        })
        .when("/post", {
            templateUrl: '../templates/post.html',
            controller: 'postCtrl'
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
		$rootScope.getUserList = globalFunctions.getUserList;
		$rootScope.getUserDetails = globalFunctions.getUserDetails;
		$rootScope.updateUser = globalFunctions.updateUser;
        $rootScope.logout = globalFunctions.logout;
        $rootScope.postArticle = globalFunctions.postArticle;

		// $rootScope.user = {};
		// $rootScope.getUserDetails().then(function(result) {
		// 	$rootScope.user.email = result.email;
		// 	$rootScope.user.firstName = result.name;
		// 	$rootScope.user.lastName = result.surname;
		// 	$rootScope.user.phoneNum = result.telNumber;
		// 	$rootScope.user.picture = result.picture;
        // });
	});

})();
