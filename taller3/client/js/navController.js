// create the controller and inject Angular's $scope
scotchApp.controller('navController', function($scope, $rootScope, $http, $log, $filter) {
    $scope.users = [];
    $scope.selectedUser;

    $rootScope.selectedUserId = 0;

    $scope.cities = [];
    $scope.selectedCity;

    $scope.loadUsers = function() {
        $log.log("loadUsers - navController");
        $http.get("http://example.com/testdb").then(function(response) {
            $scope.users = response.data.flurs_userids;

            $scope.itemSelected = $scope.users[0];

            $scope.$emit('onUserSelected', $scope.users[0]);
        });
    };

    $scope.onUserChange = function(item) {
        $log.log('onUserChange - navController ');
        //$log.log(item);
        $scope.itemSelected = item;
        $scope.$emit('onUserSelected', item);
    };

    $scope.init = function() {
        $log.log("init - navController");
        $scope.loadUsers();
    };

    $scope.init();


});
