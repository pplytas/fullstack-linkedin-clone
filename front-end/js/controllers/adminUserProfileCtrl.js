(function() {
	angular.module('tediApp')
	.controller('adminUserProfileCtrl', function($scope, user) {
		$scope.tempUser = angular.copy(user);
	});
})();
