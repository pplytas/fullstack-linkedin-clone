(function() {
	angular.module('tediApp')
	.controller('networkCtrl', function($scope, user, globalFunctions) {
		$scope.changeActiveLink("network-link");

        $scope.tempUser = angular.copy(user);
        console.log($scope.tempUser);
        $scope.query = null;
        $scope.displayingConnections = $scope.tempUser.connected;

        $scope.searchUsers = function(queryString) {
            if(queryString){
                globalFunctions.searchAccounts(queryString).then(function(response) {
                    console.log(response.data);
                    $scope.displayingConnections = response.data.users;
                });
            }
        }

        $scope.showMyConnections = function() {
            $scope.displayingConnections = $scope.tempUser.connected;
        }
	});
})();
