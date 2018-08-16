(function() {
	angular.module('tediApp')
	.controller('viewProfileCtrl', function($scope, user, globalFunctions) {
		window.scrollTo(0, 0);
		$("#profile-updated")
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
			globalFunctions.postEducation($scope.tempUser.education);
			globalFunctions.postExperience($scope.tempUser.experience);
			globalFunctions.postSkills($scope.tempUser.skills).then(function onSuccess() {
				$("#profile-updated").show();
				setTimeout(function() { $("#profile-updated").hide(); }, 6000);
			});
		}

        console.log($scope.tempUser);
	});
})();
