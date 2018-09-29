(function() {
	angular.module('tediApp')
	.controller('viewProfileCtrl', function($scope, user, globalFunctions) {
		window.scrollTo(0, 0);
		$scope.changeActiveLink("profile-link");
        $scope.tempUser = angular.copy(user);

		$scope.addEducation = function() {
			var newEducation = {
				"organization": null,
				"start": null,
				"finish": null
			};
			$scope.proInfo.$dirty = true;
			$scope.tempUser.education.push(newEducation);
		}

		$scope.addExperience = function() {
			var newExperience = {
				"company": null,
				"position": null,
				"start": null,
				"finish": null
			};
			$scope.proInfo.$dirty = true;
			$scope.tempUser.experience.push(newExperience);
		}

		$scope.addSkill = function() {
			var newSkill = {
				"name": null
			};
			$scope.proInfo.$dirty = true;
			$scope.tempUser.skills.push(newSkill);
		}

		$scope.deleteEducation = function(educationIndex) {
			$scope.proInfo.$dirty = true;
			$scope.tempUser.education.splice(educationIndex, 1);
		}

		$scope.deleteExperience = function(experienceIndex) {
			$scope.proInfo.$dirty = true;
			$scope.tempUser.experience.splice(experienceIndex, 1);
		}

		$scope.deleteSkill = function(skillIndex) {
			$scope.proInfo.$dirty = true;
			$scope.tempUser.skills.splice(skillIndex, 1);
		}

		$scope.updateProInfo = function() {
			$("#profile-update-error").hide();
			$("#profile-updated").hide();
			globalFunctions.postEducation(angular.copy($scope.tempUser.education))
			.then(function() {
				return globalFunctions.postExperience(angular.copy($scope.tempUser.experience));
			})
			.then(function() {
				return globalFunctions.postSkills($scope.tempUser.skills);
			})
			.then(function() {
				$("#profile-updated").show();
				//setTimeout(function() { $("#profile-updated").hide(); }, 6000);
				globalFunctions.getUserDetails().then(function(response) {
					user = response.data;
					$scope.tempUser = angular.copy(user);
				});
			})
			.catch(function(response) {		// Handle error
				globalFunctions.getUserDetails().then(function(response) {
					user = response.data;
					$scope.tempUser = angular.copy(user);
				});
				$("#profile-update-error").show();
			});
		}
	});
})();
