(function() {
	angular.module('tediApp')
	.controller('jobsCtrl', function($scope, globalFunctions, user) {
		$scope.changeActiveLink("jobs-link");
		$scope.tempUser = angular.copy(user);

		/* ================= On start ================= */

	});
})();
