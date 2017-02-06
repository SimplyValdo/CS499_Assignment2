(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('QuoteDetailController', QuoteDetailController);

    QuoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Quote'];

    function QuoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Quote) {
        var vm = this;

        vm.quote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('assignment2App:quoteUpdate', function(event, result) {
            vm.quote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
