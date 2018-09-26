(function() {
	angular.module('tediApp')
	.controller('editProfileCtrl', function($scope, globalFunctions) {
		$scope.changeActiveLink("settings-link");

		$scope.newEmail = null;
		$scope.newPassword = null;
		$scope.newLoginInfoSaved = false;

		$scope.updateLoginInfo = function() {
			$scope.newLoginInfoSaved = false;
			if (($scope.newPassword && $scope.confirmPassword && ($scope.newPassword === $scope.confirmPassword)) ||
				($scope.newEmail && !$scope.newPassword && !$scope.confirmPassword)) {
				globalFunctions.updateUser($scope.newEmail, $scope.newPassword).then(function(response) {
					$scope.newLoginInfoSaved = true;
				});
			}
		}
	});
})();
