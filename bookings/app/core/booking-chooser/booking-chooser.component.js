angular.
module('bookingChooser').
component('bookingChooser', {
  templateUrl: "core/booking-chooser/booking-chooser.template.html",
  controller: ['$http', '$cookies', '$mdDialog', function BookingChooserController($http, $cookies, $mdDialog) {
    var self = this;

    this.user = new User($cookies);

    $cookies.remove("oldRoute");
    $cookies.put("oldRoute", "/#!/home");

    $http.get("http://localhost:8080/api?method=getBookings&by_user="+this.user.id).then(function (response) {
      self.bookings = response.data;
      self.courses = new Array();
      self.teachers = new Array();
    
      // get all courses
      self.bookings.forEach(bkng => {
        var curr = bkng.booking_from.course.courseTitle;
        if (!(self.courses.includes(curr))) {
          self.courses.push(curr);
        }
      });
    
      // build completeNameProperty && get all teachers
      self.bookings.forEach(bkng => {
        var curr = bkng.booking_from.name + " " + bkng.booking_from.surname;
        bkng.completeName = curr;
        if (!(self.teachers.includes(curr))) {
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
      };
    
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
      };
    
      self.getSelected = function() {
        return self.bookings.filter(bkng => bkng.selected);
      };
    
      self.filterSameDate = function () {
        var selected = self.getSelected();
      
        if (selected === [])
          return self.bookings;
        else {
          var dates = selected.map(x => x.booking_date);
          return self.bookings.filter(function (bkng) {
            return !(dates.includes(bkng.booking_date) && !(selected.includes(bkng)));
          });
        }
      };
    
      self.bookSelected = function() {
          var workedFlag = true;
          var selected = self.getSelected();
          var dates = selected.map(x => x.booking_date);
          selected.forEach(bkng => {
            // remove added attributes, so server can build the corrispondant object
            delete bkng.completeName;
            delete bkng.selected;

            $http.get("http://localhost:8080/api?method=book&booking_id="+bkng.booking_id+"&by_user="+this.user.id)
            .then(function (response) {
                    if (response.data.key) {
                      workedFlag = workedFlag && true;
                    }
                  },
                  function (response) {
                    workedFlag = workedFlag && false;
                  });
          });

          if (workedFlag)
            {
              self.bookings = self.bookings.filter(function (bkng) {
                return !dates.includes(bkng.booking_date);
              })
              self.showDialog('Booking request result', 'All the bookings were booked with success');
            } else {
              self.showDialog('Booking request result', "There was a problem with some bookings, they weren't booked");
            }

          self.bookings = self.bookings.filter(function (elem) {
            return !selected.includes(elem);
          });
      };
    });

    self.showDialog = function(title, text) {
      $mdDialog.show(
        $mdDialog.alert()
          .clickOutsideToClose(true)
          .title(title)
          .textContent(text)
          .ariaLabel(title)
          .ok('Ok')
      );
    };

    self.showLoginDialog = function(ev) {
      // Appending dialog to document.body to cover sidenav in docs app
      var confirm = $mdDialog.confirm()
            .title('You must be logged in to continue')
            .textContent('To book a course and use other functions of the app you must be logged in')
            .ariaLabel('You aren\'t logged in')
            .targetEvent(ev)
            .ok('Log in')
            .cancel('Cancel');
  
      $mdDialog.show(confirm).then(function() {
        window.location.href = "/#!/login";
      }, function() {});
    };

    self.invalidateSession = function() {
      this.user.invalidateSession();
      $http.get("http://localhost:8080/api?method=logout&by_user="+this.user.id).then(
        function (response) {
          if (response.data) {
            self.showDialog("All right!", "you logged out successfully");
          }
          else {
            self.showDialog(":(", "there was a problem logging out");
          }
        },
        function (response) {
          self.showDialog("Error", "Can't contact server");
        }
      );
    };

    this.toLogin = function () {
      window.location.href = "/#!/login";
    }

    this.toHistory = function () {
      window.location.href = "/#!/history";
    }

    this.toAdministration = function () {
      window.location.href = "/#!/admin";
    }

    self.onDateChanged = function() {
      var options = { year: 'numeric', month: 'short', day: 'numeric' };
      if (self.qd === null)
        self.qd = "";
      else
        self.qd = self.qd.toLocaleDateString("en-US", options);
    };
  }]
});