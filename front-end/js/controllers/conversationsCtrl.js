(function() {
	angular.module('tediApp')
	.controller('conversationsCtrl', function($scope, globalFunctions, user) {
		$scope.changeActiveLink("conversations-link");

		$scope.tempUser = angular.copy(user);
		$scope.displayedConversation = null;

		globalFunctions.getAllMessages().then(function(response) {
			$scope.allConversations = response.data.chats;
			console.log($scope.allConversations);
		});

		$scope.getMessages = function(userId) {
			$scope.displayedConversation = $scope.allConversations.find(function(conversation) {
				return conversation.chattingUser.id === userId;
			});
			console.log($scope.displayedConversation);
		}

		// globalFunctions.sendMessage("Hello Ioulia!", 45).then(function(response) {
		// 	console.log(response.data);
		// });
	});
})();
