//angular.module('dlp-library', ['ngResource']);
var app = angular.module('app', []);

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
            when('/search/:q', {templateUrl: 'search.html', controller: 'StoryController'}).
            when('/tags', {templateUrl: 'tags.html', controller: 'TagController'}).
            when('/about', {templateUrl: 'about.html'}).
            otherwise({redirectTo: '/search/'});
    //$locationProvider.html5Mode( true );
}]);

function StoryController($scope, $http, $routeParams) {
    $scope.loaded = false;
//    $scope.searchText = $routeParams.q;
//    $scope.q = $routeParams.q;
//    $scope.query = {};

    $http.get('http://api.darklordpotter.net/stories').success(function(data, status) {
         $scope.stories = data;
        $scope.loaded = true;
    }).error(function(data, status) {
        $scope.loaded = true;
        $scope.error = status;
    });

    $scope.setSearchTag = function(tag) {
        if ($scope.query) {
            $scope.query.tags = tag;
        }
    };

    $scope.specialTag = function specialTag(tag) {
        if ($.inArray(tag, ['completed', 'oneshot', 'incomplete', 'complete', 'abandoned']) > -1) {
            return "label-success";
        }
        if (tag.indexOf("/") !== -1) {
            return "label-warning"
        }
    };
}

function TagController($scope, $http, $routeParams) {
    $http.get('http://api.darklordpotter.net/tags').success(function(data) {
        $scope.tags = data;
        $scope.loaded = true;
    }).error(function(data, status, headers, config) {
                $scope.loaded = true;
                $scope.error = status;
            });

}



