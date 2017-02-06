'use strict';

describe('Controller Tests', function() {

    describe('Background Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBackground, MockCoach, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBackground = jasmine.createSpy('MockBackground');
            MockCoach = jasmine.createSpy('MockCoach');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Background': MockBackground,
                'Coach': MockCoach,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("BackgroundDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'assignment2App:backgroundUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
