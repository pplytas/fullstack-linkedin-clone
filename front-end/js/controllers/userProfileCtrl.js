(function() {
	angular.module('tediApp')
	.controller('userProfileCtrl', function($scope, globalFunctions, user, isConnected, isSent, isPending) {
        window.scrollTo(0, 0);
        $scope.changeActiveLink("network-link");

		$scope.tempUser = angular.copy(user);
		$scope.isConnected = isConnected;
		$scope.isSent = isSent;
		$scope.isPending = isPending;

		$scope.connect = function() {
			globalFunctions.connect($scope.tempUser.email)
			.then(function(response) {
				$scope.isSent = true;
			});
		};

		$scope.accept = function() {
			globalFunctions.connect($scope.tempUser.email)
			.then(function(response) {
				$scope.isPending = false;
				$scope.isConnected = true;
			});
		};
	});
})();
