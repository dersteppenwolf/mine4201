scotchApp.controller('ArtistCardController', ['$scope','$log', function($scope, $log) {

      $scope.count = 0;


      $scope.increment = function() {
          $log.log("increment");
          $scope.count= $scope.count + 1;

      };

      $scope.setUserPreference = function(value) {
          $log.log("setUserPreference");
          $log.log(value);
          $scope.$emit('onSetUserPreference', value );
      };


}]);
