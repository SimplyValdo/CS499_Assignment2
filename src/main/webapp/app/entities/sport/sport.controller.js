(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SportController', SportController);

    SportController.$inject = ['$scope', '$state', 'Sport'];

    function SportController ($scope, $state, Sport) {
        var vm = this;

        vm.sports = [];

        loadAll();

        function loadAll() {
            Sport.query(function(result) {
                vm.sports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
