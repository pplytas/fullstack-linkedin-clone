(function() {
	angular.module('tediApp')
	.controller('jobsCtrl', function($scope, globalFunctions, user, allMyApplications, allApplicationsToMyAds) {
		$scope.changeActiveLink("jobs-link");

		$scope.tempUser = angular.copy(user);
		$scope.allMyApplications = angular.copy(allMyApplications);
		$scope.allApplicationsToMyAds = angular.copy(allApplicationsToMyAds);
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
				$scope.allMyApplications.push(
				{ad: {
					id: jobId
				}});
			});
		}

		$scope.deleteApplication = function(jobId) {
			globalFunctions.deleteApplication(jobId).then(function(response) {
				var jobIds = $scope.allMyApplications.map(function(application) {
					return application.ad.id;
				});
				$scope.allMyApplications.splice(jobIds.indexOf(jobId), 1);
			});
		}

		$scope.hasApplied = function(jobId) {
			var jobIds = $scope.allMyApplications.map(function(application) {
				return application.ad.id;
			});
			return jobIds.indexOf(jobId) > -1;
		}

		$scope.acceptApplication = function(applicationId, applicationIndex) {
			globalFunctions.acceptApplication(applicationId).then(function(response) {
				$scope.allApplicationsToMyAds[applicationIndex].status = 1;
			});
		}

		$scope.rejectApplication = function(applicationId, applicationIndex) {
			globalFunctions.rejectApplication(applicationId).then(function(response) {
				$scope.allApplicationsToMyAds[applicationIndex].status = -1;
			});
		}

		/* ================= On start ================= */

		initSuggestedJobs();
	});
})();
