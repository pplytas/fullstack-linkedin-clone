(function() {
	angular.module('tediApp')
	.controller('conversationsCtrl', function($scope, globalFunctions, user, allConversations) {
		$scope.changeActiveLink("conversations-link");
		$scope.tempUser = angular.copy(user);
		$scope.allConversations = angular.copy(allConversations);
		$scope.displayedConversation = null;
		$scope.newMessageText = null;

		var initDisplayedConversation = function() {
			globalFunctions.getLastChatOpenedUserId().then(function(response) {
				if (response.data.id) {
					$scope.getMessages(response.data.id);
				}
				else if ($scope.allConversations.length > 0) {
					$scope.getMessages($scope.allConversations[0].chattingUser.id);
				}
			});
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

		$scope.getMessages = function(userId) {
			$scope.newMessageText = null;
			$scope.displayedConversation = $scope.allConversations.find(function(conversation) {
				return conversation.chattingUser.id === userId;
			});
			scrollConversationToBottom();
			console.log($scope.displayedConversation);
		};

		$scope.sendMessage = function() {
			if ($scope.newMessageText) {
				console.log($scope.newMessageText);
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
