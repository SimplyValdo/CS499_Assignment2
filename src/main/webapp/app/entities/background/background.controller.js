(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('BackgroundController', BackgroundController);

    BackgroundController.$inject = ['$scope', '$state', 'Background'];

    function BackgroundController ($scope, $state, Background) {
        var vm = this;

        vm.backgrounds = [];

        loadAll();

        function loadAll() {
            Background.query(function(result) {
                vm.backgrounds = result;
                vm.searchQuery = null;
            });
        }
    }
})();
