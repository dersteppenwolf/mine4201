scotchApp.controller('userProfileController', function($scope, $rootScope, $http, $log) {

    $scope.isLoading = false;
    $scope.preferences = [];
    $scope.top5preferences = [];
    $scope.similarUsers = [];
    $scope.similitud = 'J';
    $scope.vecindario = '10';
    $scope.numRecs = 8;
    $scope.numPrefs = 0;

    $scope.$watch('similitud', function() {
        $scope.loadSimilarUsers();
    });


    $scope.$on('onUserChanged', function(event, item) {
        $log.log('onUserChanged....');
        $scope.selectedUser = item;
        $log.log($scope.selectedUser);
        $scope.init();
    });

    $scope.loadUserFromServer = function() {
        var url = $scope.address = $rootScope.apiUrl + 'user/' + $scope.selectedUser.id;
        //$log.log(url);
        $http.get($scope.address).then(function(response) {
            $log.log("user from server ");
            $log.log(response.data);
            $scope.numPrefs = response.data.totalRatings;
        });
    }

    $scope.loadUserPreferences = function() {
        $log.log('loadUserPreferences ');
        $scope.preferences = [];
        // http://localhost:8080/taller01/api/user/ratings/{id}
        var url = $scope.address = $rootScope.apiUrl + 'user/ratings/' + $scope.selectedUser.id;
        //$log.log(url);
        $http.get($scope.address).then(function(response) {
            //$log.log(response.data);
            angular.forEach(response.data, function(value, key) {
                var item = value.artist;
                item.rating = value.value;
                this.push(item);
            }, $scope.preferences);
        });
    };

    $scope.loadSimilarUsers = function() {
        $log.log('loadSimilarUsers ');
        if ($scope.selectedUser) {
            $scope.similarUsers = [];
            //Usuarios más similares a otro
            var url = $scope.address = $rootScope.apiUrl + 'user/similar?similarityAlgorithm=' +
                $scope.similitud + '&neighbors=' + $scope.vecindario + '&numberOfRecommendations=' +
                $scope.numRecs + '&userID=' + $scope.selectedUser.id;
            //$log.log(url);
            $http.get($scope.address).then(function(response) {
                $scope.similarUsers = response.data.similarUsers;
            });
        }
    };

    $scope.loadSimilarArtistsToTop5Preferences = function() {
        //  top 5 de los ratings  creadas por un usuario.  cada artista incluye lista de los 5 más similares
        // http://example.com/api/user/ratings/top5/2011
        var url = $rootScope.apiUrl + 'user/ratings/top5/' + $scope.selectedUser.id;
        $scope.top5preferences = [];
        $scope.isLoading = true;
        $http.get(url).then(function(response) {
            var result = response.data;
            angular.forEach(result, function(value, key) {
                var similar = [];
                angular.forEach(value.similarArtists.recommendations, function(v, k) {
                    similar.push(v.artist);
                });
                value.similar = similar;
            });
            $scope.top5preferences = result;
            $log.log("top5preferences");
            $log.log($scope.top5preferences);
            $scope.isLoading = false;
        });
    };

    $scope.init = function() {
        $log.log("init - userProfileController");
        $scope.selectedUser = $rootScope.getCurrentUser();
        if ($scope.selectedUser) {
            $scope.loadUserFromServer();
            $scope.loadUserPreferences();
            $scope.loadSimilarArtistsToTop5Preferences();
            $scope.loadSimilarUsers();
        }

    };

    $scope.init();

});
