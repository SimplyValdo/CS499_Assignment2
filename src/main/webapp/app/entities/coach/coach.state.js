(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coach', {
            parent: 'entity',
            url: '/coach',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.coach.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coach/coaches.html',
                    controller: 'CoachController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coach');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coach-detail', {
            parent: 'coach',
            url: '/coach/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.coach.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coach/coach-detail.html',
                    controller: 'CoachDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coach');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Coach', function($stateParams, Coach) {
                    return Coach.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'coach',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('coach-detail.edit', {
            parent: 'coach-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach/coach-dialog.html',
                    controller: 'CoachDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Coach', function(Coach) {
                            return Coach.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coach.new', {
            parent: 'coach',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach/coach-dialog.html',
                    controller: 'CoachDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                positionTitle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('coach', null, { reload: 'coach' });
                }, function() {
                    $state.go('coach');
                });
            }]
        })
        .state('coach.edit', {
            parent: 'coach',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach/coach-dialog.html',
                    controller: 'CoachDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Coach', function(Coach) {
                            return Coach.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coach', null, { reload: 'coach' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coach.delete', {
            parent: 'coach',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coach/coach-delete-dialog.html',
                    controller: 'CoachDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Coach', function(Coach) {
                            return Coach.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coach', null, { reload: 'coach' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
