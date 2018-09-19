(function() {
	angular.module('tediApp')
	.controller('userProfileCtrl', function($scope, globalFunctions, user, isConnected, isSent, isPending) {
        window.scrollTo(0, 0);
        $scope.changeActiveLink("network-link");

		$scope.tempUser = angular.copy(user);
		$scope.isConnected = isConnected;
		$scope.isSent = isSent;
		$scope.isPending = isPending;

		console.log($scope.tempUser);
		console.log($scope.isConnected);
		console.log($scope.isSent);
		console.log($scope.isPending);

		$scope.connect = function() {
			globalFunctions.connect($scope.tempUser.id)
			.then(function(response) {
				$scope.isConnected = false;
				$scope.isSent = true;
				$scope.isPending = false;
			});
		};

		$scope.acceptConnection = function() {
			globalFunctions.connect($scope.tempUser.id)
			.then(function(response) {
				$scope.isPending = false;
				$scope.isSent = false;
				$scope.isConnected = true;
			});
		};

		$scope.deleteConnection = function() {
			globalFunctions.deleteConnection($scope.tempUser.id)
			.then(function(response) {
				$scope.isPending = false;
				$scope.isSent = false;
				$scope.isConnected = false;
			});
		};
	});
})();
