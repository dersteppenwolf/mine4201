// create the controller and inject Angular's $scope
scotchApp.controller('navController', function($scope, $rootScope, $http, $log, $filter) {
    $scope.users = [];
    $scope.selectedUser;
    $rootScope.selectedUserId = 0;

    $scope.onUserChange = function( item) {
        $log.log('onUserChange - navController ');
        $log.log(item);
        $scope.itemSelected = item;
        //$rootScope.selectedUserId  = item.id;
        //$rootScope.$broadcast('onUserChanged', item);
        $scope.$emit('onUserSelected',item);
    };

    $scope.loadUsers = function() {
        $log.log("loadUsers - navController");
        $http.get($rootScope.apiUrl + 'user/').then(function(response) {
            $scope.users = response.data;
            $scope.users = $filter('orderBy')($scope.users, 'name', false);
            $scope.itemSelected = $scope.users[0];
            //$rootScope.selectedUserId  = $scope.users[0].id;
            //$rootScope.$broadcast('onUserChanged', $scope.users[0]);
            $scope.$emit('onUserSelected', $scope.users[0]);
        });
    };


    $scope.init = function() {
      $log.log("init - navController");
      $scope.loadUsers();
    };

    $scope.init();


});
