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
			});
		}

		globalFunctionsFactory.getUserList = function() {
            var endpoint = '/admin/userlist';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' }
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
			});
		}

		globalFunctionsFactory.getUserDetails = function(email = null) {
			var endpoint = '/user/details';
			var url = $rootScope.tediAPI + endpoint;
            var responseData;

            if (email == null) {
                email = "";
            }

			return $http({
				method: "GET",
				url: url,
				headers: { 'Content-Type': 'application/json' },
				params: {'email': email}
			});
		}

		globalFunctionsFactory.logout = function() {
			var endpoint = '/logout';
			var url = $rootScope.tediAPI + endpoint;

			return $http({
				method: "GET",
				url: url
			}).then(function onSuccess(response) {		// Handle success
				delete localStorage.isjwt;
			});
        }

        globalFunctionsFactory.getUserSimple = function(email = null) {
            var endpoint = '/user/simple';
            var url = $rootScope.tediAPI + endpoint;
            if (email == null) {
                email = "";
            }
            return $http({
				method: "GET",
				url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
			});
        }

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
        }

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
        }

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
        }

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
			});
        }

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
        }

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
        }

        globalFunctionsFactory.getArticles = function(
            email = null
        ) {
            var endpoint = '/articles';
            var url = $rootScope.tediAPI + endpoint;

            if (email == null) {
                email = "";
            }

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

        globalFunctionsFactory.getUpvoted = function(
            email = null
        ) {
            var endpoint = '/upvoted';
            var url = $rootScope.tediAPI + endpoint;

            if (email == null) {
                email = "";
            }

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

        globalFunctionsFactory.getFeed = function() {
            var endpoint = '/feed';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        }

        globalFunctionsFactory.sendMessage = function(
            message,
            email
        ) {
            var endpoint = '/message';
            var url = $rootScope.tediAPI + endpoint;
            var message = {
                "message": message,
                "email": email
            }

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                data: message
            });
        }

        globalFunctionsFactory.getMessages = function(
            email
        ) {
            var endpoint = '/messages';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

        globalFunctionsFactory.publishAd = function(
            title,
            description,
            skillList = null
            //skillList contains elements with a string field 'name'
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
        }

        globalFunctionsFactory.getAds = function(
            email
        ) {
            var endpoint = '/ads/ofuser';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

        globalFunctionsFactory.getSuggestedAds = function() {
            var endpoint = '/ads/suggested';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        }

        globalFunctionsFactory.connect = function(
            email
        ) {
            var endpoint = '/connect';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "POST",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

        globalFunctionsFactory.deleteConnection = function(
            email
        ) {
            var endpoint = '/connection/delete';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "DELETE",
                url: url,
                headers: { 'Content-Type': 'application/json' },
                params: {'email': email}
            });
        }

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
        }

        globalFunctionsFactory.getNotifications = function() {
            var endpoint = '/notifications';
            var url = $rootScope.tediAPI + endpoint;

            return $http({
                method: "GET",
                url: url,
                headers: { 'Content-Type': 'application/json' }
            });
        }

		return globalFunctionsFactory;
	})

})();
