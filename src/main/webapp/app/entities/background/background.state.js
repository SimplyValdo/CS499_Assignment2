(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('background', {
            parent: 'entity',
            url: '/background',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.background.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/background/backgrounds.html',
                    controller: 'BackgroundController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('background');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('background-detail', {
            parent: 'background',
            url: '/background/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.background.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/background/background-detail.html',
                    controller: 'BackgroundDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('background');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Background', function($stateParams, Background) {
                    return Background.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'background',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('background-detail.edit', {
            parent: 'background-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/background/background-dialog.html',
                    controller: 'BackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Background', function(Background) {
                            return Background.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('background.new', {
            parent: 'background',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/background/background-dialog.html',
                    controller: 'BackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                language: null,
                                hometown: null,
                                yearsInCurrentPosition: null,
                                totalChampionships: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('background', null, { reload: 'background' });
                }, function() {
                    $state.go('background');
                });
            }]
        })
        .state('background.edit', {
            parent: 'background',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/background/background-dialog.html',
                    controller: 'BackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Background', function(Background) {
                            return Background.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('background', null, { reload: 'background' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('background.delete', {
            parent: 'background',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/background/background-delete-dialog.html',
                    controller: 'BackgroundDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Background', function(Background) {
                            return Background.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('background', null, { reload: 'background' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
