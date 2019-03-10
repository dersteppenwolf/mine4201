// create the controller and inject Angular's $scope
scotchApp.controller('mainController', function($scope, $rootScope, $http, $log, $filter) {

    // prod
    $rootScope.apiUrl = "http://example.com/api/";
    // dev
    // $rootScope.apiUrl = "http://127.0.0.1:8080/taller01/api/";
    $scope.topArtist = '';
    $scope.users = [];
    $scope.itemSelected;

    $rootScope.getCurrentUser = function(){
      return $scope.itemSelected;
    }

    $scope.onUserChange = function( item) {
        $log.log('onUserChange  - mainController');
        $log.log(item);
        $scope.itemSelected = item;
        $rootScope.$broadcast('onUserChanged', item);
    };

    $scope.$on('onUserCreated', function(event, item) {
        event.stopPropagation();
        $log.log('onUserCreated - mainController');
        $log.log(item);
        $scope.itemSelected = item;
        $rootScope.$broadcast('onUserChanged', item);
    });

    $scope.$on('onUserSelected', function(event, item) {
        event.stopPropagation();
        $log.log('onUserSelected - mainController');
        $log.log(item);
        $scope.itemSelected = item;
        $rootScope.$broadcast('onUserChanged', item);
    });

    $scope.$on('onSetUserPreference', function(event, item) {
        event.stopPropagation();
        $log.log('onSetUserPreference ');
        $log.log(item);
        $log.log($scope.itemSelected);
        var url = $rootScope.apiUrl + 'recommendation/setPreference?userID='+$scope.itemSelected.id+
          "&itemID="+item.id+"&value="+item.rating;
        $log.log(url);
        // el servicio retorna un texto plano en lugar de un json, se debe hacer override del transformResponse
        var config = {
          method: 'GET',
          url: url,
          transformResponse: [function (data) {
                return data;
            }]
          };
        $http( config ).then(function(response) {
            $log.log(response);
        }).catch(function (data) {
            $log.error(data);
        });

    });

    $scope.init = function() {
      $log.log("init - mainController");
    };

    $scope.init();




});
