(function() {
	angular.module('tediApp')
	.controller('homeCtrl', function($scope, user) {
        $scope.tempUser = angular.copy(user);
        console.log($scope.tempUser);
	});
})();
