(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('PlayerDetailController', PlayerDetailController);

    PlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Player', 'Sport', 'Statistics', 'Quote'];

    function PlayerDetailController($scope, $rootScope, $stateParams, previousState, entity, Player, Sport, Statistics, Quote) {
        var vm = this;

        vm.player = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
