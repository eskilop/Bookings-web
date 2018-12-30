angular.
module('bookingChooser').
component('bookingChooser', {
  templateUrl: "core/booking-chooser/booking-chooser.template.html",
  controller: ['$http', '$cookies', '$mdDialog', function BookingChooserController($http, $cookies, $mdDialog) {
    var self = this;

    this.loggedUser = {
      id: $cookies.get("loggedUserId"), 
      username: $cookies.get("loggedUserUsername") === undefined ? "Anonymous" : $cookies.get("loggedUserUsername")
    };

    if (this.loggedUser.id === undefined) {
        $cookies.put("loggedUserId", undefined);
        $cookies.put("loggedUserUsername", "Anonymous");
      }

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
        // postRequest
        var selected = self.getSelected();
        if (!self.userLogged()) {
          // temp save selected bookings
          $cookies.putObject('selectedBookings', selected);
          self.showLoginDialog();
        }

        selected.forEach(bkng => {
          // remove added attributes, so server can build the corrispondant object
          delete bkng.completeName;
          delete bkng.selected;

          $http.get("http://localhost:8080/api?method=book&booking_id="+bkng.booking_id+"&by_user="+this.loggedUser.id).then(
            function (response) {
              console.log(response.data);
            },
            function (response) {
              // show err
              console.log(response.data);
            }
          );
        });
        //send all idS for bookings

        self.bookings = self.bookings.filter(function (elem) {
          return !selected.includes(elem);
        });

        console.log(selected);
      };

    });

    self.invalidateSession = function() {
      $cookies.remove("loggedUserId");
      $cookies.remove("loggedUserUsername");
      self.loggedUser = {id:undefined, username:"Anonymous"};
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
        $cookies.put("oldRoute", "/#!/home")
        window.location.href = "/#!/login";
      }, function() {});
    };
  
    self.userLogged = function() {
      return !(self.loggedUser.id === undefined && self.loggedUser.username === 'Anonymous');
    };

    self.toHistory = function () {
      window.location.href = "/#!/history";
    }

    self.onDateChanged = function() {
      var options = { year: 'numeric', month: 'short', day: 'numeric' };
      self.qd = self.qd.toLocaleDateString("en-US", options);
    };
  }]
});