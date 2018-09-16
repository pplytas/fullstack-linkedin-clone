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

		$scope.removeNewArticleImage = function(imageIndex) {
			$scope.newArticleImage = null;
		}

		$scope.postNewArticle = function() {
			$scope.feed = null;
			$scope.feedLoaded = false;
			if ($scope.newArticleImage) {
				$scope.newArticle.media = $scope.newArticleImage.base64;
			}
			globalFunctions.postArticle($scope.newArticle.title, $scope.newArticle.text, $scope.newArticle.media).then(function() {
				globalFunctions.getFeed().then(function(response) {
					$scope.feedLoaded = true;
					$scope.feed = response.data;
					$scope.newArticle.title = null;
					$scope.newArticle.text = null;
					$scope.newArticle.media = null;
					$scope.newArticleImage = null;
					console.log($scope.feed.articles);
				});
			});
		}

		$scope.upvote = function(articleId, articleIndex) {
			globalFunctions.upvote(articleId).then(function(response) {
				$scope.feed.articles[articleIndex].upvotes.unshift(response.data);
				console.log($scope.feed);
			});
		}
	});
})();
