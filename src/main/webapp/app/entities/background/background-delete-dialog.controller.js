(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('BackgroundDeleteController',BackgroundDeleteController);

    BackgroundDeleteController.$inject = ['$uibModalInstance', 'entity', 'Background'];

    function BackgroundDeleteController($uibModalInstance, entity, Background) {
        var vm = this;

        vm.background = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Background.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
