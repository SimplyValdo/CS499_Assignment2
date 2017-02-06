(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('CoachController', CoachController);

    CoachController.$inject = ['$scope', '$state', 'Coach'];

    function CoachController ($scope, $state, Coach) {
        var vm = this;

        vm.coaches = [];

        loadAll();

        function loadAll() {
            Coach.query(function(result) {
                vm.coaches = result;
                vm.searchQuery = null;
            });
        }
    }
})();
