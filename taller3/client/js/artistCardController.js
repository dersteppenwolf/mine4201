scotchApp.controller('ArtistCardController', ['$scope','$log', '$http',  function($scope, $log, $http) {

      $scope.count = 0;
	  $scope.explain="Me hace el favor y funciona ";

      $scope.increment = function() {
          $log.log("increment");
          $scope.count= $scope.count + 1;

      };

      $scope.setUserPreference = function(value) {
          $log.log("setUserPreference");
          $log.log(value);
          $scope.$emit('onSetUserPreference', value );
      };

	$scope.explain = function(userID, movieID) {
        $log.log(userID);
		$log.log(movieID);
//Recomendacion por director
            var url_2 = "http://example.com/user/recs/explain" +
                '?userid=' + userID +
				'&movieid=' + movieID;
            $log.log(url_2);

			var explicacion= [];

            $http.get(url_2).then(function(response) {
                explicacion = response.data.explanation.join(' -> ');
                $log.log($scope.directorR);
			          alert(explicacion);
            });

		//var y='esto es: ' + userID + ' esto: ' + movieID;


    };

}]);
