// create the controller and inject Angular's $scope
scotchApp.controller('navController', function($scope, $rootScope, $http, $log, $filter) {
    $scope.users = [];
    $scope.selectedUser;

    $rootScope.selectedUserId = 0;

    $scope.cities = [];
    $scope.selectedCity;

    $scope.loadUsers = function() {
        $log.log("loadUsers - navController");
        $http.get("http://example.com/user/popular?").then(function(response) {
            $scope.users = response.data;
            $scope.users = $filter('orderBy')($scope.users, 'name', false);

            var anonimo = {
                total_reviews: 0,
                id: -1,
                name: "Anonimo",
            };

            $scope.users.unshift(anonimo);
            $scope.itemSelected = $scope.users[0];
            //$rootScope.selectedUserId  = $scope.users[0].id;
            //$rootScope.$broadcast('onUserChanged', $scope.users[0]);
            $scope.$emit('onUserSelected', $scope.users[0]);
        });
    };


    $scope.loadCities = function() {
        $log.log("loadCities");
        $scope.topArtist = [];
        $http.get('http://example.com/places').
        then(function(response) {
            var data = response.data;
            //$log.log(data);
            $scope.cities = data;
            $scope.selectedCity = $scope.cities[0] ;
            $scope.onCityChange($scope.selectedCity );
        });
    };

    $scope.onUserChange = function(item) {
        $log.log('onUserChange - navController ');
        //$log.log(item);
        $scope.itemSelected = item;
        $scope.$emit('onUserSelected', item);
    };

    $scope.onCityChange = function(item) {
        $log.log('onCityChange - navController ');
        //$log.log(item);
        $scope.selectedCity = item;
        $scope.$emit('onCitySelected', item);
    };


    $scope.init = function() {
        $log.log("init - navController");
        $scope.loadUsers();
        $scope.loadCities();
    };

    $scope.init();


});
