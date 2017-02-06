(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('StatisticsController', StatisticsController);

    StatisticsController.$inject = ['$scope', '$state', 'Statistics'];

    function StatisticsController ($scope, $state, Statistics) {
        var vm = this;

        vm.statistics = [];

        loadAll();

        function loadAll() {
            Statistics.query(function(result) {
                vm.statistics = result;
                vm.searchQuery = null;
            });
        }
    }
})();
