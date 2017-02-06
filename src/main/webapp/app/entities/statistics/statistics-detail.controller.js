(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('StatisticsDetailController', StatisticsDetailController);

    StatisticsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Statistics'];

    function StatisticsDetailController($scope, $rootScope, $stateParams, previousState, entity, Statistics) {
        var vm = this;

        vm.statistics = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:statisticsUpdate', function(event, result) {
            vm.statistics = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
