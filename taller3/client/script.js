	// create the module and name it scotchApp
	var scotchApp = angular.module('scotchApp', [
	'ngRoute',
	'angular-loading-bar',
	'ui.bootstrap', 'angular-input-stars',
	'chart.js']);

	// configure our routes
	scotchApp.config(function($routeProvider) {
		$routeProvider

			.when('/', {
				templateUrl : 'pages/home.html',
				controller  : 'homeController'
			})

			.when('/admin', {
				templateUrl : 'pages/admin.html',
				controller  : 'adminController'
			})
/*
			.when('/recommendations', {
				templateUrl : 'pages/recommendations.html',
				controller  : 'recommendationsController'
			})

			.when('/profile', {
				templateUrl : 'pages/user-profile.html',
				controller  : 'userProfileController'
			})
*/
			.otherwise({
				redirectTo: 'pages/home.html'
			});;
	});
