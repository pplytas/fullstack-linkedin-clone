(function() {

	var app = angular.module("tediApp", [
	 	"ngRoute",
		"angular-jwt",
		"naif.base64",
		"angular-loading-bar"])
	.config(function($httpProvider, $routeProvider, jwtOptionsProvider, cfpLoadingBarProvider) {
		/* ================= Loading Spinner ================= */
		cfpLoadingBarProvider.includeSpinner = false;

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
			controller: function(globalFunctions) {
				delete localStorage.isjwt;
				globalFunctions.logout();
				window.location.href = "/login";
  			}
		})
		.when("/home", {
			templateUrl: '../templates/home.html',
			controller: 'homeCtrl',
			resolve: {
				user: function(globalFunctions) {
                    return globalFunctions.getUserDetails().then(function(response) {
                    	return response.data;
                    });
                }
			}
		})
		.when("/network", {
			templateUrl: '../templates/network.html',
			controller: 'networkCtrl',
			resolve: {
				user: function(globalFunctions) {
                    return globalFunctions.getUserDetails().then(function(response) {
                    	return response.data;
                    });
                }
			}
		})
		.when("/conversations/:ID?", {
			templateUrl: '../templates/conversations.html',
			controller: 'conversationsCtrl',
			resolve: {
				user: function(globalFunctions) {
					return globalFunctions.getUserDetails().then(function(response) {
						return response.data;
					});
				},
				allConversations: function(globalFunctions) {
					return globalFunctions.getAllMessages().then(function(response) {
						return response.data.chats;
					});
				}
			}
		})
		.when("/notifications", {
			templateUrl: '../templates/notifications.html',
			controller: 'notificationsCtrl',
			resolve: {
				notifications: function(globalFunctions) {
					return globalFunctions.getNotifications().then(function(response) {
						return response.data.notificationOutputModelList;
					});
				},
				pendingConnections: function(globalFunctions) {
					return globalFunctions.getConnections("pending").then(function(response) {
						return response.data.users;
					});
				}
			}
		})
		.when("/profile", {
			templateUrl: '../templates/view-profile.html',
			controller: 'viewProfileCtrl',
			resolve: {
				user: function(globalFunctions) {
                    return globalFunctions.getUserDetails().then(function(response) {
                    	return response.data;
                    });
                }
			}
        })
		.when("/profile/:ID", {
			templateUrl: '../templates/user-profile.html',
			controller: 'userProfileCtrl',
			resolve: {
				user: function(globalFunctions, $route) {
                    return globalFunctions.getUserDetails($route.current.params.ID).then(function(response) {
                    	return response.data;
                    });
                },
				isConnected: function(globalFunctions, $route) {
					return globalFunctions.getConnections().then(function(response) {
						console.log(response.data);
						return response.data.users.some(function(user) {
							return user.id == $route.current.params.ID;
						});
                    });
				},
				isSent: function(globalFunctions, $route) {
					return globalFunctions.getConnections("sent").then(function(response) {
						console.log(response.data);
						return response.data.users.some(function(user) {
							return user.id == $route.current.params.ID;
						});
                    });
				},
				isPending: function(globalFunctions, $route) {
					return globalFunctions.getConnections("pending").then(function(response) {
						console.log(response.data);
						return response.data.users.some(function(user) {
							return user.id == $route.current.params.ID;
						});
                    });
				}
			}
        })
		.when("/edit", {
			templateUrl: '../templates/edit-profile.html',
			controller: 'editProfileCtrl'
        })
        // .when("/posts", {
        //     templateUrl: '../templates/posts.html',
        //     controller: 'postsCtrl'
        // })
		.otherwise({
	        redirectTo: '/home'
	    });
		/* =========================================== */
	})
	.run(function ($rootScope, globalFunctions) {
		globalFunctions.init_app();
	});

})();
