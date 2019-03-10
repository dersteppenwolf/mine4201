// create the controller and inject Angular's $scope
scotchApp.controller('homeController', function($scope, $rootScope, $http, $log, $filter) {

    $scope.isReady = false;
    $scope.hasText = false;
    $scope.hasUser = false;
    $scope.isPopular = false;

    $scope.textR = [];
    $scope.svdR = [];
    $scope.svdDistanceR
    $scope.contentR = [];
    $scope.contentDistanceR = [];
    $scope.popularReview = [];
    $scope.popularCheckin = [];
    $scope.popularByStars = [];

		/*
    $scope.layerTextR;
    $scope.layerSvdR;
    $scope.layerSvdDistanceR
    $scope.layerContentR;
    $scope.layerContentDistanceR;
    $scope.layerPopularReview;
    $scope.layerPopularCheckin;
    $scope.layerPopularByStars;
		*/

    $scope.selectedPlace;
    $scope.selectedPlaceMarker;
		$scope.recMarkers = [];

    $scope.$on('onUserChanged', function(event, item) {
        $log.log('onUserChanged - homeController ');
				$scope.loadPersonalizedRecs();
    });

    $scope.$on('onCityChanged', function(event, item) {
        $log.log('onCityChanged - homeController ');
        $scope.setSelectedPlace(item);
    });

    $scope.map = L.map('map', {
        center: [34.903952965596065, -113.593701171871],
        minZoom: 6,
        zoom: 6
    });
		$scope.map.scrollWheelZoom.disable();

		L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}.png', {
        maxZoom: 18, attribution: ''
      }).addTo($scope.map);


		L.tileLayer('https://api.mapbox.com/styles/v1/juanmendez/cj9ops0jd4m9o2rp47fo8r3kr/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoianVhbm1lbmRleiIsImEiOiJjaXY0aTZzZ2swMGE2MnRwOTkxNWZvZWtsIn0.DaronUtMutB3J-qCjFJYOw',
		{foo: 'bar'}).addTo($scope.map);



		////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////
		L.NumberedDivIcon = L.Icon.extend({
				options: {
			    iconUrl: 'js/marker_hole.png',
			    number: '',
			    shadowUrl: null,
			    iconSize: new L.Point(25, 41),
					iconAnchor: new L.Point(13, 41),
					popupAnchor: new L.Point(0, -33),
					/*
					iconAnchor: (Point)
					popupAnchor: (Point)
					*/
					className: 'leaflet-div-icon'
				},

				createIcon: function () {
					var div = document.createElement('div');
					var img = this._createImg(this.options['iconUrl']);
					var numdiv = document.createElement('div');
					numdiv.setAttribute ( "class", "number" );
					numdiv.innerHTML = this.options['number'] || '';
					div.appendChild ( img );
					div.appendChild ( numdiv );
					this._setIconStyles(div, 'icon');
					return div;
				},

				//you could change this to add a shadow like in the normal marker if you really wanted
				createShadow: function () {
					return null;
				}
			});

    ////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////
    $scope.loadNonPersonalizedRecs = function() {
        $scope.hasText = false;
        $scope.hasUser = false;
        $scope.isPopular = false;
        $scope.loadSearchRecs();
        $scope.loadPopularPlaces();
    };

    $scope.loadSearchRecs = function() {
				$scope.textR = [];
        // Recomendacion por Texto
        if ($scope.keyword) {
            var consultaq = $scope.keyword.replace(" ", "+");
            var url = "http://example.com/search" +
                '?lat=' + $scope.selectedPlace.latitude +
                '&lon=' + $scope.selectedPlace.longitude +
                '&q=' + consultaq;
            //$log.log(url);
            $scope.hasText = true;

            $http.get(url).then(function(response) {
                $scope.textR = response.data;
								for (var i = 0; i < $scope.textR.length; i++) {
									var item = $scope.textR[i];
									$log.log(item);

									if(item.highlight["categories.category"]){
										var catText = "";
										for (var j = 0; j < item.highlight["categories.category"].length; j++) {
											catText += item.highlight["categories.category"][j]+"  ,  " ;
										}
										item.highlight.categories = catText;
									}

									if(item.highlight["tips.text"]){
										var catText = "";
										for (var k = 0; k < item.highlight["tips.text"].length; k++) {
											catText += item.highlight["tips.text"][k]+"  <br/>  " ;
										}
										item.highlight.tips = catText;
									}

									if(item.highlight["reviews.text"]){
										var catText = "";
										for (var l = 0; l < item.highlight["reviews.text"].length; l++) {
											catText += item.highlight["reviews.text"][l]+"  <br/>  " ;
										}
										item.highlight.reviews = catText;
									}

								}
								$scope.addRecMarkers($scope.textR);
            });
        };
    }

    $scope.loadPersonalizedRecs = function() {
        /* Recomendacion basada en usuario
         *  - SVD
         *  - Contenido
         */
        if ($scope.itemSelected.id > 1) {
            var url = "http://example.com/user" +
                '?id=' + $scope.itemSelected.id +
                '&lat=' + $scope.selectedPlace.latitude +
                '&lon=' + $scope.selectedPlace.longitude;
            $log.log(url);

            $scope.hasUser = true;

            $scope.svdR = [];
            $scope.svdDistanceR = [];
            $scope.contentR = [];
            $scope.contentDistanceR = [];

            $http.get(url).then(function(response) {
                $scope.svdR = response.data.recs_by_svd;
                var svdicon = L.AwesomeMarkers.icon({
                    icon: 'user',
                    markerColor: 'purple',
                    iconColor: 'white'
                });
                //$scope.layerSvdR = L.layerGroup($scope.createMarks($scope.svdR, svdicon));
                //$log.log($scope.svdR);

                $scope.svdDistanceR = response.data.recs_by_svd_with_context;
                var svddicon = L.AwesomeMarkers.icon({
                    icon: 'user',
                    markerColor: 'darkpurple',
                    iconColor: 'white'
                });
                //$scope.layerSvdDistanceR = L.layerGroup($scope.createMarks($scope.svdDistanceR, svddicon));
                //$log.log($scope.svdDistanceR);

                $scope.contentR = response.data.recs_by_fm;
                var cicon = L.AwesomeMarkers.icon({
                    icon: 'user',
                    markerColor: 'red',
                    iconColor: 'white'
                });
                //$scope.layerContentR = L.layerGroup($scope.createMarks($scope.contentR, cicon));
                //$log.log($scope.contentR);

                $scope.contentDistanceR = response.data.recs_by_fm_with_context;
                var cdicon = L.AwesomeMarkers.icon({
                    icon: 'user',
                    markerColor: 'darkred',
                    iconColor: 'white'
                });
                //$scope.layerContentDistanceR = L.layerGroup($scope.createMarks($scope.contentDistanceR, cdicon));
                //$log.log($scope.contentDistanceR);
            });
        };

    }


    $scope.loadPopularPlaces = function() {
        var url = "http://example.com/popular" +
            '?lat=' + $scope.selectedPlace.latitude +
            '&lon=' + $scope.selectedPlace.longitude;
        $log.log(url);

        $scope.popularReview = [];
        $scope.popularCheckin = [];
        $scope.popularByStars = [];

        $scope.isPopular = true;

        $http.get(url).then(function(response) {
            $scope.popularReview = response.data.popular_by_review_count;
            var reviewicon = L.AwesomeMarkers.icon({
                icon: 'user',
                markerColor: 'green',
                iconColor: 'white'
            });
            //$scope.layerPopularReview = L.layerGroup($scope.createMarks($scope.popularReview, reviewicon));
            //$log.log($scope.popularReview);

            $scope.popularCheckin = response.data.popular_by_checkins;
            var checkicon = L.AwesomeMarkers.icon({
                icon: 'user',
                markerColor: 'red',
                iconColor: 'white'
            });
            //$scope.layerPopularCheckin = L.layerGroup($scope.createMarks($scope.popularCheckin, checkicon));
            //$log.log($scope.popularCheckin);

            $scope.popularByStars = response.data.popular_by_stars;
            var starsicon = L.AwesomeMarkers.icon({
                icon: 'user',
                markerColor: 'blue',
                iconColor: 'white'
            });
            //$scope.layerPopularByStars = L.layerGroup($scope.createMarks($scope.popularByStars, starsicon));
            //$log.log($scope.popularByStars);
            $scope.isReady = true;

        });


    }


		/*
    $scope.showInMap = function() {
        $scope.layerPopularByStars.addTo($scope.map);
        $scope.layerPopularCheckin.addTo($scope.map);
        $scope.layerPopularReview.addTo($scope.map);

        // Si es usuario anonimo
        if ($scope.itemSelected.id > 1) {
            $scope.layerContentDistanceR.addTo($scope.map);
            $scope.layerContentR.addTo($scope.map);
            $scope.layerSvdDistanceR.addTo($scope.map);
            $scope.layerSvdR.addTo($scope.map);

            if ($scope.layerTextR) {
                $scope.layerTextR.addTo($scope.map);

                $scope.overlayMaps = {
                    "Text": $scope.layerTextR,
                    "SVR": $scope.layerSvdR,
                    "SVR Distance": $scope.layerSvdDistanceR,
                    "Content": $scope.layerContentR,
                    "Content Distance": $scope.layerContentDistanceR,
                    "Popular Reviews": $scope.layerPopularReview,
                    "Popular Checkin": $scope.layerPopularCheckin,
                    "Popular Stars": $scope.layerPopularByStars
                };
            } else {
                $scope.overlayMaps = {
                    "SVR": $scope.layerSvdR,
                    "SVR Distance": $scope.layerSvdDistanceR,
                    "Content": $scope.layerContentR,
                    "Content Distance": $scope.layerContentDistanceR,
                    "Popular Reviews": $scope.layerPopularReview,
                    "Popular Checkin": $scope.layerPopularCheckin,
                    "Popular Stars": $scope.layerPopularByStars
                };
            };

        } else {

            if ($scope.layerTextR) {
                $scope.layerTextR.addTo($scope.map);

                $scope.overlayMaps = {
                    "Text": $scope.layerTextR,
                    "Popular Reviews": $scope.layerPopularReview,
                    "Popular Checkin": $scope.layerPopularCheckin,
                    "Popular Stars": $scope.layerPopularByStars
                };
            } else {
                $scope.overlayMaps = {
                    "Popular Reviews": $scope.layerPopularReview,
                    "Popular Checkin": $scope.layerPopularCheckin,
                    "Popular Stars": $scope.layerPopularByStars
                };
            };

        };

        L.control.layers($scope.overlayMaps).addTo($scope.map);
        $scope.isReady = false;

    };



    $scope.createMarks = function(business, myIcon) {
        $log.log("Creating Maps");
        var places = [];

        for (var i = 0; i < business.length; i++) {
            places[i] = L.marker([business[i].latitude, business[i].longitude], {
                icon: myIcon
            }).bindPopup(business[i].name);
        }
        return places;
    };
		*/

    /////////////////////////////////


    $scope.init = function() {
        $log.log("init - homeController");
        var item = $rootScope.getCurrentCity();
        if (item) {
            $scope.setSelectedPlace(item);
        }
    };

		/*+

		**/
		$scope.addRecMarkers = function(items){
			if ($scope.recMarkers.length > 0) {
				for (var i = 0; i < $scope.recMarkers.length; i++) {
					$scope.map.removeLayer($scope.recMarkers[i]);
				}
			}
			$scope.recMarkers = [];
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				$log.log(item);
				var marker = L.marker(
						[item.latitude, item.longitude], {
								icon:	new L.NumberedDivIcon({number: (item.rank_adjusted)? item.rank_adjusted:item.rank})
						}
				).addTo($scope.map).bindPopup(item.name);
				$scope.recMarkers.push(marker);
			}
			var group = new L.featureGroup($scope.recMarkers);
			$scope.map.fitBounds(group.getBounds());
		}

    $scope.setSelectedPlace = function(item) {
        $scope.selectedPlace = item;
        $scope.map.setView(new L.LatLng($scope.selectedPlace.latitude, $scope.selectedPlace.longitude), 13);
        if ($scope.selectedPlaceMarker) {
            $scope.map.removeLayer($scope.selectedPlaceMarker);
        }

        $scope.selectedPlaceMarker = L.marker(
            [$scope.selectedPlace.latitude, $scope.selectedPlace.longitude], {
                draggable: true,
								icon:	new L.NumberedDivIcon({number: ''})
            }
        ).addTo($scope.map);

        $scope.selectedPlaceMarker.on("dragend", function(e) {
            var marker = e.target;
            var position = marker.getLatLng();
            $scope.selectedPlace.latitude = position.lat;
            $scope.selectedPlace.longitude = position.lng;
            $scope.map.panTo(new L.LatLng(position.lat, position.lng));
						$scope.loadNonPersonalizedRecs();
						$scope.loadPersonalizedRecs();
        });

				$scope.loadNonPersonalizedRecs();
    }

    $scope.init();

});
