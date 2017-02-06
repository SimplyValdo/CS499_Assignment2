(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SportDeleteController',SportDeleteController);

    SportDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sport'];

    function SportDeleteController($uibModalInstance, entity, Sport) {
        var vm = this;

        vm.sport = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
