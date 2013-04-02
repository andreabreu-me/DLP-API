//angular.module('dlp-library', ['ngResource']);
var app = angular.module('app', ['ngSanitize', 'filters']);

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
            when('/wba/:threadId/:chapterId', {templateUrl: 'search.html', controller: 'StoryController'}).
            when('/tags', {templateUrl: 'tags.html', controller: 'TagController'}).
            when('/about', {templateUrl: 'about.html'}).
            otherwise({redirectTo: '/wba/'});
    //$locationProvider.html5Mode( true );
}]);

app.controller('StoryController', ['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {
    $scope.loaded = false;
    $scope.threadId =  parseInt($routeParams.threadId);
    $scope.chapterId = parseInt($routeParams.chapterId);


    if (!$scope.threadId || !$scope.chapterId) return;
    $http.get('http://api.darklordpotter.net/wba/' + $scope.threadId.toString() + '/' + $scope.chapterId.toString()).success(function(data, status) {
        $scope.story = data;
        $scope.loaded = true;

        // 204 -- doesn't exist
        if (status == "204") $scope.error = "Story doesn't exist.";
    }).error(function(data, status, headers, config) {
                $scope.loaded = true;
                $scope.error = status;
            });

    $scope.$watch('storyId + chapterId', function() {

        $location.url("/wba/" + $scope.threadId + "/" + $scope.chapterId);


    });

}]);

angular.module('filters', []).filter('nl2br', function() {
    return function(text) {
        if (text) {
            return text.replace(/\n/g, "<br />");
        } else {
            return text;
        }
    };
}).filter('bbcode', function() {
    return function(text) {
        if (text) {
            text = text.replace(/\[B\]/gim, "<b>");
            text = text.replace(/\[\/B\]/gim, "</b>");
            text = text.replace(/\[I\]/gim, "<i>");
            text = text.replace(/\[\/I\]/gim, "</i>");
            text = text.replace(/\[CENTER\]/gim, '<span class="center">');
            text = text.replace(/\[\/CENTER\]/gim, '</span>');
            return text;
        } else {
            return text;
        }
    };
});

