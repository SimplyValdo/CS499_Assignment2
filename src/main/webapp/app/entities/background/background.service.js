(function() {
    'use strict';
    angular
        .module('assignment2App')
        .factory('Background', Background);

    Background.$inject = ['$resource'];

    function Background ($resource) {
        var resourceUrl =  'api/backgrounds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
