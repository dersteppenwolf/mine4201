// create the controller and inject Angular's $scope
scotchApp.controller('homeController', function($scope, $rootScope, $http, $log, $filter) {

    $scope.svdR = [];
    $scope.mixedR= [];
    $scope.contentR = [];

    $scope.pageRankR = [];
    $scope.directorR = [];
    $scope.actorR = [];


    $scope.$on('onUserChanged', function(event, item) {
        $log.log('onUserChanged - homeController ');
				$scope.loadPersonalizedRecs();
    });

    $scope.loadPersonalizedRecs = function() {
        /* Recomendacion basada en usuario
         *  - SVD
         *  - Contenido
         */

            $scope.svdR = [];
            $scope.mixedR = [];
            $scope.contentR = [];

            $scope.pageRankR = [];
            $scope.directorR = [];
            $scope.actorR = [];

// Recomendacion por factorizacion

            var url = "http://example.com/user/recs/flurs" +
                '?id=' + $scope.itemSelected;
            $log.log(url);

            $http.get(url).then(function(response) {
                $scope.svdR = response.data.svd;
            //$log.log($scope.svdR);
                $scope.mixedR = response.data.mixed;

                $scope.contentR = response.data.fm;
            });

//Recomendacion por PageRank
            var url_1 = "http://example.com/user/recs/pprank" +
                '?id=' + $scope.itemSelected;
            $log.log(url_1);

            $http.get(url_1).then(function(response) {
                $scope.pageRankR = response.data.recs;
            $log.log($scope.pageRankR);
            });

//Recomendacion por director
            var url_2 = " http://example.com/user/recs/graph/directors" +
                '?id=' + $scope.itemSelected;
            $log.log(url_2);

            $http.get(url_2).then(function(response) {
                $scope.directorR = response.data.recs;
            //$log.log($scope.directorR);
            });

//Recomendacion por Actor
            var url_3 = " http://example.com/user/recs/graph/actors" +
                '?id=' + $scope.itemSelected;
            $log.log(url_3);

            $http.get(url_3).then(function(response) {
                $scope.actorR = response.data.recs;
            //$log.log($scope.actorR);
            });

    }

    $scope.init = function() {
        $log.log("init - homeController");

    };
	
	
    $scope.init();

});
