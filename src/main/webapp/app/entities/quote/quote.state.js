(function() {
    'use strict';

    angular
        .module('assignment2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('quote', {
            parent: 'entity',
            url: '/quote',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.quote.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote/quotes.html',
                    controller: 'QuoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('quote');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('quote-detail', {
            parent: 'quote',
            url: '/quote/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'assignment2App.quote.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/quote/quote-detail.html',
                    controller: 'QuoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('quote');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Quote', function($stateParams, Quote) {
                    return Quote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'quote',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('quote-detail.edit', {
            parent: 'quote-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote/quote-dialog.html',
                    controller: 'QuoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Quote', function(Quote) {
                            return Quote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote.new', {
            parent: 'quote',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote/quote-dialog.html',
                    controller: 'QuoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                phrase: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('quote', null, { reload: 'quote' });
                }, function() {
                    $state.go('quote');
                });
            }]
        })
        .state('quote.edit', {
            parent: 'quote',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote/quote-dialog.html',
                    controller: 'QuoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Quote', function(Quote) {
                            return Quote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote', null, { reload: 'quote' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('quote.delete', {
            parent: 'quote',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/quote/quote-delete-dialog.html',
                    controller: 'QuoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Quote', function(Quote) {
                            return Quote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('quote', null, { reload: 'quote' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
