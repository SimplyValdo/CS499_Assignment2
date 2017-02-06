(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('QuoteDialogController', QuoteDialogController);

    QuoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Quote'];

    function QuoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Quote) {
        var vm = this;

        vm.quote = entity;
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
            if (vm.quote.id !== null) {
                Quote.update(vm.quote, onSaveSuccess, onSaveError);
            } else {
                Quote.save(vm.quote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:quoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
