(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('CoachDetailController', CoachDetailController);

    CoachDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Coach', 'Sport'];

    function CoachDetailController($scope, $rootScope, $stateParams, previousState, entity, Coach, Sport) {
        var vm = this;

        vm.coach = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:coachUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
