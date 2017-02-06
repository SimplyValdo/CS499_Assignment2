(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('BackgroundDialogController', BackgroundDialogController);

    BackgroundDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Background', 'Coach', 'Player'];

    function BackgroundDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Background, Coach, Player) {
        var vm = this;

        vm.background = entity;
        vm.clear = clear;
        vm.save = save;
        vm.coaches = Coach.query({filter: 'background-is-null'});
        $q.all([vm.background.$promise, vm.coaches.$promise]).then(function() {
            if (!vm.background.coach || !vm.background.coach.id) {
                return $q.reject();
            }
            return Coach.get({id : vm.background.coach.id}).$promise;
        }).then(function(coach) {
            vm.coaches.push(coach);
        });
        vm.players = Player.query({filter: 'background-is-null'});
        $q.all([vm.background.$promise, vm.players.$promise]).then(function() {
            if (!vm.background.player || !vm.background.player.id) {
                return $q.reject();
            }
            return Player.get({id : vm.background.player.id}).$promise;
        }).then(function(player) {
            vm.players.push(player);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.background.id !== null) {
                Background.update(vm.background, onSaveSuccess, onSaveError);
            } else {
                Background.save(vm.background, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:backgroundUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
