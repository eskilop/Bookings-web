angular.
  module('history').
  component('history', {
    templateUrl: "core/history/history.template.html",
    controller: ['$cookies', '$http', '$mdDialog', function($cookies, $http, $mdDialog) {
      var self = this;

      this.user = new User($cookies);

      $cookies.remove("oldRoute");
      $cookies.put("oldRoute", "/#!/history");

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
        )
      };
  
      this.showLoginDialog = function() {
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
              .title('You must be logged in to continue')
              .textContent('To book a course and use other functions of the app you must be logged in')
              .ariaLabel('You aren\'t logged in')
              .ok('Log in');
    
        $mdDialog.show(confirm).then(function() {
          window.location.href = "/#!/login";
        }, function() {});
      };

      this.showDialog = function(title, text) {
        $mdDialog.show(
          $mdDialog.alert()
            .clickOutsideToClose(true)
            .title(title)
            .textContent(text)
            .ariaLabel('Server Sync error')
            .ok('Ok')
        );
      };
    
      this.backHome = function () {
        window.location.href = "/#!/home";
      }

      this.getIncomingBookings = function () {
        if (self.incomingBookings === undefined && this.user.isLogged()) {
          $http.get("http://localhost:8080/api?method=getIncomingBookings&id="+this.user.id).then(
            function (response) {
              if (response.data.key) {
                self.incomingBookings = response.data.value;
              } else {
                self.showDialog("Error while getting incoming bookings", response.data.value);
              }
            },
            function (response) {
              self.incomingBookings = undefined;
              self.showDialog("Error while getting incoming bookings", "");
            }
          );
        }
      }

      this.getPastBookings = function () {
        if (this.pastBookings === undefined && this.user.isLogged()) {
          $http.get("http://localhost:8080/api?method=getPastBookings&id="+self.user.id).then(
            function (response) {
              if (response.data.key) {
                self.pastBookings = response.data.value;
              } else {
                self.showDialog("Error while getting past bookings", response.data.value);
              }
            },
            function (response) {
              self.pastBookings = undefined;
              self.showDialog("Error while getting past bookings", "");
            }
          );
        }
      };

      this.unbook = function (booking_id) {
        $http.get("http://localhost:8080/api?method=unbook&booking_id="+booking_id+"&by_user="+this.user.id).then(
          function (response) {
            self.incomingBookings = self.incomingBookings.filter(function (bkng) {return !(bkng.booking_id === booking_id)});
            if (response.data.key) {
              self.pastBookings.push(response.data.value);
            }
          },
          function (response) {
            self.showDialog("Error while unbooking booking", response.data);
          }
        );
      };

      if (!(this.user.isLogged())) {
        this.showLoginDialog();
      }
      
      this.getIncomingBookings();
      this.getPastBookings();
     
    }]
  })