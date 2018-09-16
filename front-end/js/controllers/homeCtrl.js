(function() {
	angular.module('tediApp')
	.controller('homeCtrl', function($scope, user, globalFunctions) {
		$scope.changeActiveLink("home-link");
        $scope.tempUser = angular.copy(user);
        console.log($scope.tempUser);

		$scope.newArticle = {
			"title": null,
			"text": null,
			"media": null
		};

		globalFunctions.getFeed().then(function(response) {
			$scope.feedLoaded = true;
			$scope.feed = response.data;
			console.log($scope.feed.articles);
		});

		$scope.postNewArticle = function() {
			globalFunctions.postArticle($scope.newArticle.title, $scope.newArticle.text, $scope.newArticle.media).then(function() {
				globalFunctions.getFeed().then(function(response) {
					$scope.feed = response.data;
					$scope.newArticle.title = null;
					$scope.newArticle.text = null;
					$scope.newArticle.media = null;
					console.log($scope.feed.articles);
				});
			});
		}
	});
})();
