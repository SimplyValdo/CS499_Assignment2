(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('QuoteController', QuoteController);

    QuoteController.$inject = ['$scope', '$state', 'Quote'];

    function QuoteController ($scope, $state, Quote) {
        var vm = this;

        vm.quotes = [];

        loadAll();

        function loadAll() {
            Quote.query(function(result) {
                vm.quotes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
