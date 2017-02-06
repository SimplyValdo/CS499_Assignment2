(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('QuoteDeleteController',QuoteDeleteController);

    QuoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Quote'];

    function QuoteDeleteController($uibModalInstance, entity, Quote) {
        var vm = this;

        vm.quote = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Quote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
