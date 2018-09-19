(function() {
	angular.module('tediApp')
	.controller('notificationsCtrl', function($scope, globalFunctions, notifications, pendingConnections) {
		$scope.changeActiveLink("notifications-link");
        $scope.pendingConnections = pendingConnections;
        $scope.notifications = notifications;
		for (i in $scope.notifications) {
			var notification = $scope.notifications[i];
			var messageWords = notification.message.split(" ");
			if (notification.message.startsWith("Connection request")) {
				var sentenceParts = notification.message.split("from", 1);
				sentenceParts[0] += "from";
				console.log(sentenceParts);
			}
		}

        console.log($scope.notifications);
        console.log($scope.pendingConnections);

		$scope.acceptPendingRequest = function(connectionIndex) {
			console.log($scope.pendingConnections[connectionIndex].id);
			globalFunctions.connect($scope.pendingConnections[connectionIndex].id).then(function() {
				$scope.pendingConnections.splice(connectionIndex, 1);
			})
		};

		$scope.deletePendingRequest = function(connectionIndex) {
			console.log($scope.pendingConnections[connectionIndex].id);
			globalFunctions.deleteConnection($scope.pendingConnections[connectionIndex].id).then(function() {
				$scope.pendingConnections.splice(connectionIndex, 1);
			})
		};
	});
})();
