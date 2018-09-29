(function() {

	angular.module('tediApp')
	.factory('globalFunctions', function($rootScope, $http) {
		var globalFunctionsFactory = {};

		globalFunctionsFactory.init_app = function(){
			$rootScope.tediAPI = 'https://localhost:8443';

			$rootScope.changeActiveLink = function(id) {
				if (document.getElementsByClassName("nav-item active")[0]) {
					document.getElementsByClassName("nav-item active")[0].classList.remove("active");
				}
				document.getElementById(id).classList.add("active");
			}

			$rootScope.isAdmin = localStorage.isAdmin == 'true';
		};

		globalFunctionsFactory.login = function(
			inputEmail,
			inputPassword
		) {
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
			});
		};

		globalFunctionsFactory.getUserList = function() {
            var endpoint = '/admin/userlist';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' }
			});
        };

		globalFunctionsFactory.getAccountDetails = function(
			id
		) {
			var endpoint = '/admin/userdetails';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				params: {'id': id}
			});
		};

		globalFunctionsFactory.adminSearchAccounts = function(
			queryString = null
		) {
			var endpoint = '/admin/usersearch';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				params: {'query': queryString}
			});
		};

		globalFunctionsFactory.export = function(
			idList
		) {
			var endpoint = '/admin/export';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "POST",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				data: {"ids": idList},
				responseType: 'blob'
			});
		};

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
			});
		};

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
				"email": ((email === "") ? (null) : (email)),
				"password": ((password === "") ? (null) : (password)),
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
			});
		};

		globalFunctionsFactory.getUserDetails = function(
			id = null
		) {
			var endpoint = '/user/details';
			var url = $rootScope.tediAPI + endpoint;

            if (id == null) {
                id = "";
            }

			return $http({
				method: "GET",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				params: {'id': id}
			});
		};

		globalFunctionsFactory.logout = function() {
			var endpoint = '/logout';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url
			}).then(function onSuccess(response) {		// Handle success
				delete localStorage.isjwt;
			});
        };

        globalFunctionsFactory.getUserSimple = function(
			id = null
		) {
            var endpoint = '/user/simple';
            var url = $rootScope.tediAPI + endpoint;
            if (id == null) {
                id = "";
            }
            return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
			});
        };

        globalFunctionsFactory.postEducation = function(
            educationList = null,
            //educationList contains elements with fields:
            //organization, start (time string in yyyy-MM-dd format), finish (time string in yyyy-MM-dd format)
            isPublic = true
        ) {
            var endpoint = '/user/education';
            var url = $rootScope.tediAPI + endpoint;
			for (var i in educationList) {
				if(educationList[i].finish == "Unknown" || educationList[i].finish == "Present") {
					educationList[i].finish = "";
				}
			}

            var educationWrapper = {
                "educations": educationList,
                "isPublic": isPublic
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: educationWrapper
            });
        };

        globalFunctionsFactory.postExperience = function(
            experienceList = null,
            //experienceList contains elements with fields:
            //company, position, start (time string in yyyy-MM-dd format), finish (time string in yyyy-MM-dd format)
            isPublic = true
        ) {
            var endpoint = '/user/experience';
            var url = $rootScope.tediAPI + endpoint;
			for (var i in experienceList) {
				if(experienceList[i].finish == "Unknown" || experienceList[i].finish == "Present" ) {
					experienceList[i].finish = "";
				}
			}

            var experienceWrapper = {
                "experiences": experienceList,
                "isPublic": isPublic
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: experienceWrapper
            });
        };

        globalFunctionsFactory.postSkills = function(
            skillList = null,
            //skillList contains elements with a string field 'name'
            isPublic = true
        ) {
            var endpoint = '/user/skills';
            var url = $rootScope.tediAPI + endpoint;
            var skillsWrapper = {
                "skills": skillList,
                "isPublic": isPublic
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: skillsWrapper
            });
        };

        globalFunctionsFactory.searchAccounts = function(
            queryString = null
        ) {
            var endpoint = '/user/search/simple';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'query': queryString}
			});
        };

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
			});
        };

        globalFunctionsFactory.postComment = function(
            articleId,
            text
        ) {
            var endpoint = '/article/comment';
            var url = $rootScope.tediAPI + endpoint;
            var newComment = {
                "articleId": articleId,
                "text": text
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: newComment
            });
        };

        globalFunctionsFactory.upvote = function(
            articleId
        ) {
            var endpoint = '/article/upvote';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'articleId': articleId}
            });
        };

        globalFunctionsFactory.getArticles = function(
            id = null
        ) {
            var endpoint = '/articles';
            var url = $rootScope.tediAPI + endpoint;

            if (id == null) {
                id = "";
            }

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
        };

        globalFunctionsFactory.getUpvoted = function(
            id = null
        ) {
            var endpoint = '/upvoted';
            var url = $rootScope.tediAPI + endpoint;

            if (id == null) {
                id = "";
            }

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
        };

        globalFunctionsFactory.getFeed = function() {
            var endpoint = '/feed';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        };

        globalFunctionsFactory.sendMessage = function(
            message,
            id
        ) {
            var endpoint = '/message';
            var url = $rootScope.tediAPI + endpoint;
            var message = {
                "message": message,
                "id": id
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: message
            });
        };

		globalFunctionsFactory.getLastChatOpenedUserId = function() {
            var endpoint = '/messages/lastuser';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        };

		globalFunctionsFactory.setLastChatOpenedUserId = function(
			id
		) {
			var endpoint = '/messages/lastuser';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "PUT",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
		};

        globalFunctionsFactory.getMessages = function(
            id
        ) {
            var endpoint = '/messages';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
        };

		globalFunctionsFactory.getAllMessages = function() {
            var endpoint = '/messages/all';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        };

		globalFunctionsFactory.publishAd = function(
		    title,
		    description,
		    skillList = null    //skillList contains elements with a string field 'name'
		) {
		    var endpoint = '/ads/add';
		    var url = $rootScope.tediAPI + endpoint;
		    var newAd = {
		        "title": title,
		        "description": description,
		        "skills": skillList
		    }

		    return $http({
		        method: "POST",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        data: newAd
		    });
		};

		globalFunctionsFactory.getAds = function(
		    id
		) {
		    var endpoint = '/ads/ofuser';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "GET",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'id': id}
		    });
		};

		globalFunctionsFactory.getSuggestedAds = function() {
		    var endpoint = '/ads/suggested';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "GET",
		        url: url,
		        headers: { 'Content-Type': 'application/json' }
		    });
		};

		globalFunctionsFactory.applyForAd = function(
		    adId
		) {
		    var endpoint = '/ads/apply';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "POST",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'id': adId}
		    });
		};

		globalFunctionsFactory.deleteApplication = function(
		    adId
		) {
		    var endpoint = '/ads/application';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "DELETE",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'id': adId}
		    });
		};

		globalFunctionsFactory.getAdApplications = function(
		    adId
		) {
		    var endpoint = '/ads/applications';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "GET",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'id': adId}
		    });
		};

		globalFunctionsFactory.getAllApplicationsToMyAds = function() {
		    var endpoint = '/ads/applications';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "GET",
		        url: url,
		        headers: { 'Content-Type': 'application/json' }
		    });
		};

		globalFunctionsFactory.getAllMyApplications = function() {
		    var endpoint = '/ads/applications/my';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "GET",
		        url: url,
		        headers: { 'Content-Type': 'application/json' }
		    });
		};

		globalFunctionsFactory.acceptApplication = function(
		    applicationId
		) {
		    var endpoint = '/ads/application/accept';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "PUT",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'applicationId': applicationId}
		    });
		};

		globalFunctionsFactory.rejectApplication = function(
		    applicationId
		) {
		    var endpoint = '/ads/application/reject';
		    var url = $rootScope.tediAPI + endpoint;

		    return $http({
		        method: "PUT",
		        url: url,
		        headers: { 'Content-Type': 'application/json' },
		        params: {'applicationId': applicationId}
		    });
		};

        globalFunctionsFactory.connect = function(
            id
        ) {
            var endpoint = '/connect';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
        };

        globalFunctionsFactory.deleteConnection = function(
            id
        ) {
            var endpoint = '/connection/delete';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "DELETE",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'id': id}
            });
        };

        globalFunctionsFactory.getConnections = function(
            type
        ) {
            var endpoint = '/connections';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'type': type}
            });
        };

        globalFunctionsFactory.getNotifications = function() {
            var endpoint = '/notifications';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        };

		return globalFunctionsFactory;
	})

})();
