(function() {
    'use strict';

    angular
        .module('assignment2App')
        .controller('PlayerDialogController', PlayerDialogController);

    PlayerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Player', 'Sport', 'Statistics', 'Quote'];

    function PlayerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Player, Sport, Statistics, Quote) {
        var vm = this;

        vm.player = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sports = Sport.query();
        vm.stats = Statistics.query({filter: 'player-is-null'});
        $q.all([vm.player.$promise, vm.stats.$promise]).then(function() {
            if (!vm.player.stats || !vm.player.stats.id) {
                return $q.reject();
            }
            return Statistics.get({id : vm.player.stats.id}).$promise;
        }).then(function(stats) {
            vm.stats.push(stats);
        });
        vm.quotes = Quote.query({filter: 'player-is-null'});
        $q.all([vm.player.$promise, vm.quotes.$promise]).then(function() {
            if (!vm.player.quote || !vm.player.quote.id) {
                return $q.reject();
            }
            return Quote.get({id : vm.player.quote.id}).$promise;
        }).then(function(quote) {
            vm.quotes.push(quote);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.player.id !== null) {
                Player.update(vm.player, onSaveSuccess, onSaveError);
            } else {
                Player.save(vm.player, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('assignment2App:playerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
