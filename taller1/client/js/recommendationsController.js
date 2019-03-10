scotchApp.controller('recommendationsController', function($scope, $rootScope, $http, $log) {

    $scope.similitud = 'C';
    $scope.vecindario = '10';
    $scope.nrecomendaciones = '5';

    $scope.isLoading = false;

    $scope.selectedUser;
    $scope.result;
    $scope.artists = [];
    $scope.labels = [];
    $scope.data = [];
    $scope.options = {
        scale: {
            display: true,
            ticks: {
                beginAtZero: true
            }
        },
        elements: {
            point: {
                radius: 5
            },
            line: {
                tension: 0.0
            }
        }
    };

    $scope.$on('onUserChanged', function(event, item) {
        $log.log('onUserChanged - recommendationsController ');
        $scope.selectedUser = item;
        $log.log($scope.selectedUser);
        $scope.search();
    });


    $scope.search = function() {
        $scope.address = $rootScope.apiUrl + 'recommendation/user';
        $scope.isLoading = true;
        $scope.artists = [];
        $scope.labels = [];
        $scope.data = [];

        var url = $scope.address +
            '?similarityAlgorithm=' + $scope.similitud +
            '&neighbors=' + $scope.vecindario +
            '&userID=' + $scope.selectedUser.id +
            '&numberOfRecommendations=' + $scope.nrecomendaciones;
        //$http.get('http://example.com/api/recommendation/user?similarityAlgorithm=C&neighbors=5&userID=1&numberOfRecommendations=10').
        $log.log(url);
        $http.get(url).then(function(response) {
            $scope.result = response.data;
            $scope.artists = [];
            angular.forEach($scope.result.recommendations, function(value, key) {
                this.push(value.artist);
            }, $scope.artists);
            angular.forEach($scope.result.recommendations, function(value, key) {
                this.push(value.artist.name);
            }, $scope.labels);
            angular.forEach($scope.result.recommendations, function(value, key) {
                this.push(value.value);
            }, $scope.data);

            $log.log($scope.data);
            $log.log($scope.labels);

            $scope.isLoading = false;
        });
    };

    $scope.init = function() {
      $log.log("init - recommendationsController");
      $scope.selectedUser = $rootScope.getCurrentUser();
      if($scope.selectedUser){
        $scope.search();
      }
    };

    $scope.init();




});
