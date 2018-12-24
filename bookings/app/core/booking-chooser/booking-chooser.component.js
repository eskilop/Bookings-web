angular.
module('bookingChooser').
component('bookingChooser', {
  templateUrl: "core/booking-chooser/booking-chooser.template.html",
  controller: ['$http', function BookingChooserController($http) {
    var self = this;

    $http.get("http://localhost:8080/api?method=getBookings").then(function (response) {
      self.bookings = response.data;
    });

    console.log(self.bookings);

  }]
});