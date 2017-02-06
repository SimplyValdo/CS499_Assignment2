(function() {
    'use strict';
    angular
        .module('assignment2App')
        .factory('Sport', Sport);

    Sport.$inject = ['$resource'];

    function Sport ($resource) {
        var resourceUrl =  'api/sports/:id';

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
