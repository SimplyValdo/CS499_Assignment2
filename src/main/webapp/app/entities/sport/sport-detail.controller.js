(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SportDetailController', SportDetailController);

    SportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sport', 'Coach', 'Player'];

    function SportDetailController($scope, $rootScope, $stateParams, previousState, entity, Sport, Coach, Player) {
        var vm = this;

        vm.sport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:sportUpdate', function(event, result) {
            vm.sport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
