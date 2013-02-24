//angular.module('dlp-library', ['ngResource']);
var app = angular.module('app', ['ngSanitize']);

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
            when('/ffn/:storyId/:chapterId', {templateUrl: 'search.html', controller: 'StoryController'}).
            when('/tags', {templateUrl: 'tags.html', controller: 'TagController'}).
            when('/about', {templateUrl: 'about.html'}).
            otherwise({redirectTo: '/ffn/'});
    //$locationProvider.html5Mode( true );
}]);

app.controller('StoryController', ['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {
    $scope.loaded = false;
    $scope.storyId =  parseInt($routeParams.storyId);
    $scope.chapterId = parseInt($routeParams.chapterId);


    if (!$scope.storyId || !$scope.chapterId) return;
    $http.get('http://api.darklordpotter.net/ffn/' + $scope.storyId.toString() + '/' + $scope.chapterId.toString()).success(function(data, status) {
        $scope.story = data;
        $scope.loaded = true;

        // 204 -- doesn't exist
        if (status == "204") $scope.error = "Story doesn't exist.";
    }).error(function(data, status, headers, config) {
                $scope.loaded = true;
                $scope.error = status;
            });

    $scope.$watch('storyId + chapterId', function() {

        $location.url("/ffn/" + $scope.storyId + "/" + $scope.chapterId);


    });

}]);

