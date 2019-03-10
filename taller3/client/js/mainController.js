// create the controller and inject Angular's $scope
scotchApp.controller('mainController', function($scope, $rootScope, $http, $log, $filter) {

    $scope.users = [];
    $scope.itemSelected;
    $scope.citySelected ;

    $rootScope.getCurrentUser = function(){
      $log.log('getCurrentUser - mainController');
      return $scope.itemSelected;
    }

    $rootScope.getCurrentCity = function(){
      $log.log('getCurrentCity - mainController');
      return $scope.citySelected ;
    }

    $scope.$on('onUserSelected', function(event, item) {
        event.stopPropagation();
        $log.log('onUserSelected - mainController');
        //$log.log(item);
        $scope.itemSelected = item;
        $rootScope.$broadcast('onUserChanged', item);
    });

    $scope.$on('onCitySelected', function(event, item) {
        //$log.log(event);
        event.stopPropagation();
        $log.log('onCitySelected - mainController');
        $log.log(item);
        $scope.citySelected = item;
        $rootScope.$broadcast('onCityChanged', item);
    });


    $scope.init = function() {
      $log.log("init - mainController");
    };

    $scope.init();




});
