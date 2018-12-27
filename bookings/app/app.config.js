angular.
module('BookingsApp').
config(['$locationProvider', '$routeProvider',
  function config($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.
      when('/home', {
        template: '<booking-chooser></booking-chooser>'
      }).
      when('/login', {
        template: '<login></login>'
      }).
      when('/history', {
        template: '<history></history>'
      }).
      when('/admin', {
        template: '<admin></admin>'
      }).
      otherwise('/home');
  }
]);