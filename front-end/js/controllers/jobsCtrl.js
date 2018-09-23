(function() {
	angular.module('tediApp')
	.controller('jobsCtrl', function($scope, globalFunctions, user) {
		$scope.changeActiveLink("jobs-link");

		$scope.tempUser = angular.copy(user);
		$scope.suggestedJobs = null;
		$scope.newJobPublished = false;
		$scope.suggestedJobsLoaded = false;
		$scope.newJob = {
			"title": null,
			"description": null,
			"skillTags": []
		};

		var initSuggestedJobs = function() {
			globalFunctions.getSuggestedAds().then(function(response) {
				$scope.suggestedJobs = response.data.ads;
				$scope.suggestedJobsLoaded = true;
			});
		}

		$scope.addSkillTag = function() {
			var newSkillTag = {"name": null};
			$scope.newJob.skillTags.push(newSkillTag);
		}

		$scope.deleteSkillTag = function(skillTagIndex) {
			$scope.newJob.skillTags.splice(skillTagIndex, 1);
		}

		$scope.publishNewJob = function() {
			$scope.newJobPublished = false;
			globalFunctions.publishAd($scope.newJob.title, $scope.newJob.description, $scope.newJob.skillTags).then(function(response) {
				$scope.newJobPublished = true;
				$scope.newJob = {
					"title": null,
					"description": null,
					"skillTags": []
				};
			});
		}

		$scope.apply = function(jobId) {
			globalFunctions.applyForAd(jobId).then(function(response) {
				console.log(response.data);
			});
		}

		/* ================= On start ================= */

		initSuggestedJobs();

	});
})();
