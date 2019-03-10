scotchApp.controller('adminController', function($scope, $http, $rootScope) {
    // create a message to display in our view

    $scope.topArtist = '';
    $scope.isLoading = false;
    $scope.userName = '';
    $scope.userAge = '';
    $scope.userFmUser = '';
    $scope.artistName  = '';


    $scope.newUser = function() {
        $scope.isLoading = true;
        data = {
            "name": $scope.userName,
            "age": $scope.userAge,
            "lastfmUser": $scope.userFmUser
        };

        config = {  headers: {
                'Content-Type': 'application/json'
            }};
        $scope.address =  $rootScope.apiUrl+'user/';

        $http.post($scope.address, data, config)
            .then(function(data) {
                $scope.PostDataResponse = 'User Created Succesfully. Please rate some artist using the Home Page' ;
                $scope.isLoading = false;
                $scope.$emit('onUserCreated', data.data );
            }, function errorCallback(response) {
                $scope.PostDataResponse = response.data.errorMessage;
                $scope.isLoading = false;
            });
    };


    $scope.generateUUID = function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
          var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
          return v.toString(16);
        });
   };




    $scope.addItem = function() {
        $scope.isLoading = true;
        var uuid = $scope.generateUUID();

        data = {
            "name": $scope.artistName,
            "gid": uuid
        };

        config = {
            headers: {
                'Content-Type': 'application/json'
            }
        };
        $scope.address =  $rootScope.apiUrl+'artist/';

        $http.post($scope.address, data, config)
            .then(function(data) {
                $scope.PostDataResponse = 'Artist Created Succesfully.' ;
                $scope.isLoading = false;
            }, function errorCallback(response) {
                $scope.PostDataResponse = response.data.errorMessage;
                $scope.isLoading = false;
            });

    };


});
