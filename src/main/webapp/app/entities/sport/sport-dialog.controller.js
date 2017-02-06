(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('SportDialogController', SportDialogController);

    SportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sport', 'Coach', 'Player'];

    function SportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sport, Coach, Player) {
        var vm = this;

        vm.sport = entity;
        vm.clear = clear;
        vm.save = save;
        vm.coaches = Coach.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sport.id !== null) {
                Sport.update(vm.sport, onSaveSuccess, onSaveError);
            } else {
                Sport.save(vm.sport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:sportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
