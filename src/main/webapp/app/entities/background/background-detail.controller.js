(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('BackgroundDetailController', BackgroundDetailController);

    BackgroundDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Background', 'Coach', 'Player'];

    function BackgroundDetailController($scope, $rootScope, $stateParams, previousState, entity, Background, Coach, Player) {
        var vm = this;

        vm.background = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:backgroundUpdate', function(event, result) {
            vm.background = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
