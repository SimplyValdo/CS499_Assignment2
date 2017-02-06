(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('StatisticsDialogController', StatisticsDialogController);

    StatisticsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Statistics'];

    function StatisticsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Statistics) {
        var vm = this;

        vm.statistics = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.statistics.id !== null) {
                Statistics.update(vm.statistics, onSaveSuccess, onSaveError);
            } else {
                Statistics.save(vm.statistics, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:statisticsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
