(function() {

	var app = angular.module('tediApp', [])
	.run(function ($rootScope, globalFunctions) {
		globalFunctions.init_app();
        globalFunctions.login('p@root.com', 'toor');
	});

})();
