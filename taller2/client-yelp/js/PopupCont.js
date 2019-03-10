 angular.module('PopupDemo').controller('PopupCont', ['$scope','$modalInstance',function ($scope, $modalInstance) {
	 
$scope.close = function () {
$modalInstance.dismiss('cancel');
};
}

]);