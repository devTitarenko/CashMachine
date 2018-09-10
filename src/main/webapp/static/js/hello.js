var app = angular.module('application', ['ngRoute']);

app.config(function ($routeProvider, $locationProvider) {
    $routeProvider
        .when('/', {
            templateUrl: '/views/card_number.html',
            controller: 'NumController as numCtrl'
        })
        .when('/pin', {
            templateUrl: '/views/password.html',
            controller: 'PinController as pinCtrl'
        })
        .when('/op', {
            templateUrl: '/views/operations.html'
        })
        .when('/block', {
            templateUrl: '/views/block.html'
        })
        .when('/balance', {
            templateUrl: '/views/balance.html',
            controller: 'BalanceController'
        })
        .when('/withdrawal', {
            templateUrl: '/views/withdrawal.html',
            controller: 'CashController as cashCtrl'
        })
        .otherwise({
            template: '<h1>404</h1>'
        });
    $locationProvider.html5Mode(true);
});

app.controller('NumController', function ($scope, $filter, $http, $location, $rootScope) {
    $scope.pressBtn = function (num) {
        var length = $scope.number.length;
        if (length < 16) {
            $scope.number += num;
            $scope.formatedNumber = $filter("numberFilter")($scope.number, $scope.formatedNumber + num);
        }
    };

    this.clear = function () {
        $scope.number = "";
        $scope.formatedNumber = "";
    };

    this.isDisable = function () {
        return $scope.number === "" && $scope.formatedNumber === "";
    };

    $scope.isCardExist = function () {
        console.log($scope.number);
        $http.get('/card/exist/' + $scope.number)
            .then(function (response) {
                if (response.data) {
                    $rootScope.cardNumber = $scope.number;
                    console.log($scope.number);
                    $location.path('/pin');
                } else {
                    alert("Карта не существует или заблокирована.")
                }
            });
    };

    this.clear();
});

app.controller('PinController', function ($scope, $http, $location, $rootScope) {
    var failures = 4;

    $scope.pressBtn = function (num) {
        $scope.pin += num;
    };

    this.clear = function () {
        $scope.pin = "";
    };

    this.isDisable = function () {
        return $scope.pin === "";
    };

    $scope.isPasswordCorrect = function () {
        $http.get('/card/pass/', {
            params: {
                id: $rootScope.cardNumber,
                pass: $scope.pin
            }
        }).then(function (response) {
            if (response.data) {
                $rootScope.cardPin = $scope.pin;
                $location.path('/op');
            } else {
                failures--;
                if (failures === 0) {
                    blocCard();
                } else if (failures === 1) {
                    alert("Больше предупреждений не будет.")
                } else {
                    alert("Осталось " + failures + " попытки")
                }
            }
        });
    };

    function blocCard() {
        $http.get('/card/block/' + $rootScope.cardNumber)
            .then(function () {
                $location.path('/block');
            })
    }

    this.clear();
});

app.controller('CashController', function ($scope, $http, $rootScope) {
    $scope.cash = "";
    $scope.formatedNumber = "";
    $scope.pin = "";

    $scope.pressBtn = function (num) {
        $scope.cash += num;
    };

    this.clear = function () {
        $scope.cash = "";
    };

    this.isDisable = function () {
        return $scope.cash === "";
    };

    $scope.withdrawal = function () {
        $http.get('/card/cashOut/', {
            params: {
                id: $rootScope.cardNumber,
                amount: $scope.cash,
                pass: $rootScope.cardPin
            }
        }).then(function (response) {
            if (response.data >= 0) {
                alert("Деньги выданы. Текущий баланс: " + response.data);
            } else {
                alert("Недостаточно средств на счёте");
            }
        });
    };
});

app.controller('BalanceController', function ($scope, $http, $rootScope) {
    $scope.operations = function () {
        $http.get('/operations/getAll/', {
            params: {
                id: $rootScope.cardNumber,
                pass: $rootScope.cardPin
            }
        }).then(function (response) {
            console.log(":-"+response);
            if (response.data) {
                console.log(":*"+response.data);
                $scope.operationList = response.data;
            } else {
                alert("Операций нет");
            }
        });
    };
});

app.controller('Hello', function ($scope, $http) {
    $scope.number = "";
});

app.filter('numberFilter', function () {
    return function (number, formatedNumber) {
        var length = number.length;
        return formatedNumber + ((length % 4 === 0 && length < 16) ? "-" : "");
    }
});

app.directive('keyboard', function () {
    return {
        templateUrl: "/views/keyboard.html"
    }
});

app.directive('goClick', function ($location) {
    return function (scope, element, attrs) {
        var path;

        attrs.$observe('goClick', function (val) {
            path = val;
        });

        element.bind('click', function () {
            scope.$apply(function () {
                $location.path(path);
            });
        });
    };
});