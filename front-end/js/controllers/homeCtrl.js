(function() {
	angular.module('tediApp')
	.controller('homeCtrl', function($scope, globalFunctions, user) {
		$scope.changeActiveLink("home-link");

        $scope.tempUser = angular.copy(user);
		$scope.feedLoaded = false;
		$scope.feed = null;
		$scope.articleToComment = null;
		$scope.newCommentText = null;
		$scope.articlesShowUpvotes = [];
		$scope.articlesShowComments = [];
		$scope.newArticle = {
			"title": null,
			"text": null,
			"media": null
		};

		$scope.showUpvotes = function(articleId) {
			if ($scope.isShowUpvotes(articleId)) {
				var articleIndex = $scope.articlesShowUpvotes.indexOf(articleId);
				$scope.articlesShowUpvotes.splice(articleIndex, 1);
			} else {
				$scope.articlesShowUpvotes.push(articleId);
			}
		}

		$scope.showComments = function(articleId) {
			if ($scope.isShowComments(articleId)) {
				var articleIndex = $scope.articlesShowComments.indexOf(articleId);
				$scope.articlesShowComments.splice(articleIndex, 1);
			} else {
				$scope.articlesShowComments.push(articleId);
			}
		}

		$scope.isShowUpvotes = function(articleId) {
			return $scope.articlesShowUpvotes.some(function(id) { return id == articleId; });
		}

		$scope.isShowComments = function(articleId) {
			return $scope.articlesShowComments.some(function(id) { return id == articleId; });
		}

		$scope.isCommentClicked = function(articleId) {
			return $scope.articleToComment === articleId;
		}

		$scope.commentClicked = function(articleId) {
			$scope.newCommentText = null;
			if ($scope.isCommentClicked(articleId)) {
				$scope.articleToComment = null;
			} else {
				$scope.articleToComment = articleId;
			}
		}

		$scope.comment = function(articleId, articleIndex) {
			globalFunctions.postComment(articleId, $scope.newCommentText).then(function(response) {
				$scope.feed.articles[articleIndex].comments.unshift(response.data);
				$scope.newCommentText = null;
			});
		}

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
				});
			});
		}

		$scope.upvote = function(articleId, articleIndex) {
			globalFunctions.upvote(articleId).then(function(response) {
				$scope.feed.articles[articleIndex].upvotes.unshift(response.data);
			});
		}

		/* ================= On start ================= */

		globalFunctions.getFeed().then(function(response) {
			$scope.feedLoaded = true;
			$scope.feed = response.data;
			console.log($scope.feed);
		});
	});
})();
