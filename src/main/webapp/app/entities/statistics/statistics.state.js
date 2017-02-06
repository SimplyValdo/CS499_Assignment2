(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statistics', {
            parent: 'entity',
            url: '/statistics',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.statistics.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statistics/statistics.html',
                    controller: 'StatisticsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statistics');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('statistics-detail', {
            parent: 'statistics',
            url: '/statistics/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.statistics.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/statistics/statistics-detail.html',
                    controller: 'StatisticsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('statistics');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Statistics', function($stateParams, Statistics) {
                    return Statistics.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'statistics',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('statistics-detail.edit', {
            parent: 'statistics-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statistics.new', {
            parent: 'statistics',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                wins: null,
                                losses: null,
                                draws: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('statistics');
                });
            }]
        })
        .state('statistics.edit', {
            parent: 'statistics',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-dialog.html',
                    controller: 'StatisticsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('statistics.delete', {
            parent: 'statistics',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/statistics/statistics-delete-dialog.html',
                    controller: 'StatisticsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Statistics', function(Statistics) {
                            return Statistics.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('statistics', null, { reload: 'statistics' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
