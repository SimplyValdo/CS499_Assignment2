(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sport', {
            parent: 'entity',
            url: '/sport',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.sport.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sport/sports.html',
                    controller: 'SportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sport');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sport-detail', {
            parent: 'sport',
            url: '/sport/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.sport.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sport/sport-detail.html',
                    controller: 'SportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sport');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sport', function($stateParams, Sport) {
                    return Sport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sport',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sport-detail.edit', {
            parent: 'sport-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sport/sport-dialog.html',
                    controller: 'SportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sport', function(Sport) {
                            return Sport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sport.new', {
            parent: 'sport',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sport/sport-dialog.html',
                    controller: 'SportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sportName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sport', null, { reload: 'sport' });
                }, function() {
                    $state.go('sport');
                });
            }]
        })
        .state('sport.edit', {
            parent: 'sport',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sport/sport-dialog.html',
                    controller: 'SportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sport', function(Sport) {
                            return Sport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sport', null, { reload: 'sport' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sport.delete', {
            parent: 'sport',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sport/sport-delete-dialog.html',
                    controller: 'SportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sport', function(Sport) {
                            return Sport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sport', null, { reload: 'sport' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
