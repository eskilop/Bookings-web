angular.
  module('history').
  component('history', {
    templateUrl: "core/history/history.template.html",
    controller: ['$cookies', '$http', '$mdDialog', function($cookies, $http, $mdDialog) {
      var self = this;

      this.loggedUser = $cookies.getObject("loggedUser");

      this.invalidateSession = function() {
        $cookies.remove("loggedUser");
        self.loggedUser = {id:undefined, username:"Anonymous"};
      };
  
      this.showLoginDialog = function() {
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
              .title('You must be logged in to continue')
              .textContent('To book a course and use other functions of the app you must be logged in')
              .ariaLabel('You aren\'t logged in')
              .ok('Log in')
              .cancel('Cancel');
    
        $mdDialog.show(confirm).then(function() {
          $cookies.put("oldRoute", "/#!/history");
          window.location.href = "/#!/login";
        }, function() {});
      };
    
      self.userLogged = function() {
        return !(self.loggedUser === undefined || (self.loggedUser.id === undefined && self.loggedUser.username === 'Anonymous'));
      };

      this.backHome = function () {
        window.location.href = "/#!/home";
      }

      this.getIncomingBookings = function () {
        if (self.incomingBookings === undefined && this.userLogged()) {
          $http.get("http://localhost:8080/api?method=getIncomingBookings&id="+self.loggedUser.id).then(
            function (response) {
              self.incomingBookings = response.data.value;
            },
            function (response) {
              self.incomingBookings = undefined;
              // Show error msg
            }
          );
        }
      }

      this.getPastBookings = function () {
        if (self.pastBookings === undefined && this.userLogged()) {
          $http.get("http://localhost:8080/api?method=getPastBookings&id="+self.loggedUser.id).then(
            function (response) {
              self.pastBookings = response.data.value;
            },
            function (response) {
              self.pastBookings = undefined;
              // Show error msg
            }
          );
        }
      };

      this.unbook = function (booking_id) {
        $http.get("http://localhost:8080/api?method=unbook&booking_id="+booking_id+"&by_user="+this.loggedUser.id).then(
          function (response) {
            self.incomingBookings = self.incomingBookings.filter(function (bkng) {return !(bkng.booking_id === booking_id)});
            if (response.data.key) {
              self.pastBookings.push(response.data.value);
            }
          },
          function (response) {
            // error mesg
          }
        );
      };

      if (!(this.userLogged())) {
        this.showLoginDialog();
      }
      this.getIncomingBookings();
      this.getPastBookings();
     
    }]
  })