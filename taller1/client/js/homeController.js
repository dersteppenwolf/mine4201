// create the controller and inject Angular's $scope
scotchApp.controller('homeController', function($scope, $rootScope, $http, $log, $filter) {
    $scope.topArtist = [];
    $scope.randomArtist = [];

    $scope.$on('onUserChanged', function(event, item) {
      $log.log('onUserChanged - homeController ');
      $scope.loadRandomArtists();
      $scope.loadArtists();
    });

    $scope.loadArtists = function() {
        $log.log("loadArtists");
        $scope.topArtist = [];
        $http.get($rootScope.apiUrl + 'artist/popular').
        then(function(response) {
            var data = response.data;
            data = $scope.shuffle(data);
            $scope.topArtist =data ;
        });
    };

    $scope.loadRandomArtists = function() {
      //$log.log('loadRandomArtists ');
      $scope.randomArtist = [];
      $http.get($rootScope.apiUrl + 'artist/random/?id=2').then(function(response) {
          $scope.randomArtist = response.data;
          //$log.log($scope.randomArtist);
      });

    };

    /**
    * https://stackoverflow.com/questions/2450954/how-to-randomize-shuffle-a-javascript-array
    **/
    $scope.shuffle = function (array) {
      var currentIndex = array.length, temporaryValue, randomIndex;
      // While there remain elements to shuffle...
      while (0 !== currentIndex) {
        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }
      return array;
    }

    $scope.init = function() {
      $log.log("init - homeController");
      $scope.loadArtists();
      $scope.loadRandomArtists();
    };

    $scope.init();

});
