(function() {
	angular.module('tediApp')
	.controller('viewProfileCtrl', function($scope, user) {
        $scope.tempUser = angular.copy(user);
        console.log($scope.tempUser);
	});
})();
