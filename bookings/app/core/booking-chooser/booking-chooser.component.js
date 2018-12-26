angular.
module('bookingChooser').
component('bookingChooser', {
  templateUrl: "core/booking-chooser/booking-chooser.template.html",
  controller: ['$http', function BookingChooserController($http) {
    var self = this;
    this.booked = new Array();

    $http.get("http://localhost:8080/api?method=getBookings").then(function (response) {
      self.bookings = response.data;
      self.courses = new Array();

      self.bookings.forEach(bkng => {
        var curr = bkng.booking_from.course.courseTitle;
        if (!(curr === self.courses[self.courses.length-1])) {
          self.courses.push(curr);
        }
      });

      console.log("Bookings Ready");
      console.log("undefined: ");
      console.log(self.bookings === undefined || self.availableCourses === undefined);
    });

    self.teachersFor = function (course) {
      var teachers = new Set();
      var filteredBookings = self.bookings.filter(bkng => {bkng.booking_from.course.courseTitle === course});

      filteredBookings.forEach(bkng => {
        teachers.add(bkng.booking_from.name + " " + bkng.booking_from.surname );
      });
      return teachers;
    }

    self.datesFor = function (teacher, course) {
      var dates = new Set();
      var filteredBookings = self.bookings.filter(bkng => {bkng.booking_from.name + " " + bkng.booking_from.surname === teacher && bkng.booking_from.course.courseTitle === course});

      filteredBookings.forEach(bkng => {
        dates.add(bkng.booking_date);
      });
      return dates;
    }

    self.searchBooking = function (teacher, course, date) {
      return self.bookings.filter(bkng => { // expecting 1, but can fail.
        bkng.booking_from.name === teacher.split(" ")[0] &&
        bkng.booking_from.surname === teacher.split(" ")[1] &&
        bkng.booking_from.course.courseTitle === course &&
        bkng.booking_date === date;
      });
    }

    self.onDateChanged = function() {
      var options = { year: 'numeric', month: 'short', day: 'numeric' };
      self.qd = self.qd.toLocaleDateString("en-US", options);
      console.log(self.qd);
    }
  }]
});