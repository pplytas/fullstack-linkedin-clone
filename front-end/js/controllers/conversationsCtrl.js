(function() {
	angular.module('tediApp')
	.controller('conversationsCtrl', function($scope, $routeParams, globalFunctions, user, allConversations) {
		$scope.changeActiveLink("conversations-link");
		$scope.tempUser = angular.copy(user);
		$scope.allConversations = angular.copy(allConversations);
		$scope.displayedConversation = null;
		$scope.newMessageText = null;

		var initDisplayedConversation = function() {
			if ($routeParams.ID) {
				$scope.setDisplayedConversation(parseInt($routeParams.ID));
			}
			else {
				globalFunctions.getLastChatOpenedUserId().then(function(response) {
					if (response.data.id) {
						$scope.setDisplayedConversation(response.data.id);
					}
					else if ($scope.allConversations.length > 0) {
						$scope.setDisplayedConversation($scope.allConversations[0].chattingUser.id);
					}
				});
			}
		}

		var scrollConversationToBottom = function() {
			angular.element(document).ready(function() {
				var conversationArea = document.getElementById("conversation-area");
				conversationArea.scrollTop = conversationArea.scrollHeight;
			});
		};

		var addNewMessage = function(messageText) {
			var newMessage = {
				"message": messageText,
				"sender": $scope.tempUser.id,
				"receiver": $scope.displayedConversation.chattingUser.id
			};
			$scope.displayedConversation.messages.push(newMessage);
		};

		$scope.setDisplayedConversation = function(userId) {
			$scope.newMessageText = null;
			$scope.displayedConversation = $scope.allConversations.find(function(conversation) {
				return conversation.chattingUser.id === userId;
			});
			scrollConversationToBottom();
			globalFunctions.setLastChatOpenedUserId(userId);
		};

		$scope.sendMessage = function() {
			if ($scope.newMessageText && $scope.displayedConversation) {
				globalFunctions.sendMessage($scope.newMessageText, $scope.displayedConversation.chattingUser.id);
				scrollConversationToBottom();
				addNewMessage($scope.newMessageText);
				$scope.newMessageText = null;
			}
		};

		$scope.senderIsMe = function(senderId) {
			return senderId === $scope.tempUser.id;
		};

		/* ================= On start ================= */

		initDisplayedConversation();
	});
})();
