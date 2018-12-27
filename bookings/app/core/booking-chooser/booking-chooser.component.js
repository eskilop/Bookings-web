angular.
module('bookingChooser').
component('bookingChooser', {
  templateUrl: "core/booking-chooser/booking-chooser.template.html",
  controller: ['$http', function BookingChooserController($http) {
    var self = this;

    $http.get("http://localhost:8080/api?method=getBookings").then(function (response) {
      self.bookings = response.data;
      self.courses = new Array();
      self.teachers = new Array();


      // get all courses
      self.bookings.forEach(bkng => {
        var curr = bkng.booking_from.course.courseTitle;
        if (!(curr === self.courses[self.courses.length-1])) {
          self.courses.push(curr);
        }
      });

      // build completeNameProperty && get all teachers
      self.bookings.forEach(bkng => {
        var curr = bkng.booking_from.name + " " + bkng.booking_from.surname;
        bkng.completeName = curr;
        if (!(curr === self.teachers[self.teachers.length-1])) {
          self.teachers.push(curr);
        }
      });

      self.coursesFor = function (teacher) {
        var courses = new Array();
        var filteredBookings = self.bookings.filter(function(bkng) {
          return bkng.completeName === teacher;
        });

        if (teacher === undefined) {
          return self.courses;
        }
        else {
          filteredBookings.forEach(bkng => {
            if (!(bkng.booking_from.course.courseTitle === courses[courses.length-1])) {
              courses.push(bkng.booking_from.course.courseTitle);
            }
          });
          return courses;
        }
      }

      self.teachersFor = function (course) {
        var teachers = new Array();
        var filteredBookings = self.bookings.filter(function(bkng) {
          return bkng.booking_from.course.courseTitle === course;
        });

        if (course === undefined) {
          return self.teachers;
        }
        else {
          filteredBookings.forEach(bkng => {
            if (!(bkng.completeName === teachers[teachers.length-1])) {
              teachers.push(bkng.completeName);
            }
          });
          if (teachers.length === 1)
            self.qt = teachers[0];
          return teachers;
        }
      }

      self.getSelected = function() {
        return self.bookings.filter(bkng => bkng.selected);
      }

      self.filterSameDate = function ()
      {
        var selected = self.getSelected();

        if (selected === [])
          return self.bookings;
        else {

          var dates = selected.map(x => x.booking_date);

          return self.bookings.filter(function (bkng) {
            return !(dates.includes(bkng.booking_date) && !(selected.includes(bkng)));
          });
        }
      }
  
      self.bookSelected = function() {
        // postRequest
        // check if user is logged in
        var selected = self.getSelected();
        selected.forEach(bkng => {
          // remove added attributes, so server can build the corrispondant object
          delete bkng.completeName;
          delete bkng.selected;
        });
        //send all idS for bookings
        console.log(filterSelected);
      }

    });

    self.onDateChanged = function() {
      var options = { year: 'numeric', month: 'short', day: 'numeric' };
      self.qd = self.qd.toLocaleDateString("en-US", options);
    }
  }]
});