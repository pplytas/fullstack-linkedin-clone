(function() {
	angular.module('tediApp')
	.controller('postCtrl', function($rootScope, $scope) {
        $('#_submit').click(function(e) {
            e.preventDefault();
            var title = $('#title').val();
            var text = $('#text').val();

            console.log(title);
            console.log(text);

            //$rootScope.postArticle(title, text);
            $rootScope.getNotifications();
        });
	});
})();
