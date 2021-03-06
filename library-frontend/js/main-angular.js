//angular.module('dlp-library', ['ngResource']);
var app = angular.module('app', ['filters']);
var base = "http://api.darklordpotter.net";

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
            when('/search/:q', {templateUrl: 'search.html', controller: 'StoryController'}).
            when('/tags', {templateUrl: 'tags.html', controller: 'TagController'}).
            when('/about', {templateUrl: 'about.html'}).
            otherwise({redirectTo: '/search/'});
    //$locationProvider.html5Mode( true );
}]);

app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});


function StoryController($scope, $http, $routeParams) {
    $scope.loaded = false;
//    $scope.searchText = $routeParams.q;
//    $scope.q = $routeParams.q;
//    $scope.query = {};


    $scope.currentPage = 0;
    $scope.pageSize = 50;
    $scope.stories = [];
    $scope.numberOfPages=function(){
        return Math.ceil($scope.stories.length/$scope.pageSize);
    };
    $scope.pageElementTop=function() {
        return Math.min($scope.stories.length, ($scope.currentPage+1)*$scope.pageSize)
    }

//    if(typeof(Storage)!=="undefined") {
//        if (localStorage.getItem("stories")) {
//            $scope.stories = JSON.parse(localStorage.getItem("stories"));
//        }
//    }
    NProgress.start();
    $http.get(base+'/stories').success(function(data, status) {
        if(typeof(Storage)!=="undefined") {
            localStorage.setItem("stories", JSON.stringify(data));
        }
         $scope.stories = data;
        $scope.loaded = true;
        NProgress.done();
    }).error(function(data, status) {
        $scope.loaded = true;
        $scope.error = status;
                NProgress.done();
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

angular.module('filters', []).filter('ratingTitle', function() {
    return function(text) {
        if (text) {
            return text.replace("PLUS", "+");
        } else {
            return text;
        }
    };
});