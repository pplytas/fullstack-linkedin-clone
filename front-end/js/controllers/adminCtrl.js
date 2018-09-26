(function() {
	angular.module('tediApp')
	.controller('adminCtrl', function($scope, globalFunctions, allUsers) {
		$scope.changeActiveLink("admin-link");

        $scope.allUsers = angular.copy(allUsers);
        $scope.displayingConnections = $scope.allUsers;
        $scope.selectedUsers = [];
        $scope.query = null;

        $scope.searchUsers = function(queryString) {
            if(queryString){
                globalFunctions.adminSearchAccounts(queryString).then(function(response) {
                    $scope.displayingConnections = response.data.users;
                });
            }
        };

        $scope.showAllConnections = function() {
            $scope.displayingConnections = $scope.allUsers;
        };

        $scope.selectUser = function(userId) {
            return $scope.selectedUsers.push({'id': userId});
        };

        $scope.deselectUser = function(userId) {
            var ids = $scope.selectedUsers.map(function(selectedUser) {
                return selectedUser.id;
            });
            return $scope.selectedUsers.splice(ids.indexOf(userId), 1);
        };

        $scope.isSelected = function(userId) {
            var ids = $scope.selectedUsers.map(function(selectedUser) {
                return selectedUser.id;
            });
            return ids.indexOf(userId) > -1;
        };

        $scope.selectOrDeselect = function(userId) {
            if ($scope.isSelected(userId)) {
                $scope.deselectUser(userId);
            }
            else {
                $scope.selectUser(userId);
            }
        };

        $scope.export = function() {
            var idList = [];
            if ($scope.selectedUsers.length > 0) {
                idList = $scope.selectedUsers;
            }
            else {
                idList = $scope.displayingConnections.map(function(connection) {
                    return {'id': connection.id};
                })
            }
            globalFunctions.export(idList).then(function(response) {
				saveAs(response.data, 'users.xml');
            })
            .catch(function(response) {
            });
        };

        /* ================= On start ================= */
	});
})();
