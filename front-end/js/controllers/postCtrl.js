(function() {
	angular.module('tediApp')
	.controller('postCtrl', function($rootScope, $scope) {
        $('#_media').click(function(e) {
            e.preventDefault();
            var title = $('#title').val();
            var text = $('#text').val();

            console.log(title);
            console.log(text);

            $rootScope.postArticle(title, text);
        });
	});
})();
