scotchApp.directive('artistCard', function() {
    return {
        scope: {
            title: '@',
			user: '@',
            items: '='
        },
        restrict : 'EA',
        controller: 'ArtistCardController',
        controllerAs: 'ctrl',
        transclude: true,
        bindToController: true,
        templateUrl: 'pages/artist-card.html'
    }
});
