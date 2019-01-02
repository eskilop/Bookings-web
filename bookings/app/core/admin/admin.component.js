angular.
  module('admin').
    component('admin', {
      templateUrl: "core/admin/admin.template.html",
      controller: ['$http', '$cookies', '$mdDialog', function($http, $cookies, $mdDialog) {
        var self = this;

        this.user = new User($cookies);
        this.tcassoc = [{teacher: "ASD", course:"MEMES"}, {teacher: "Jesus", course:"Religion"}, {teacher: "PLS", course:"IT"}, {teacher: "STAHP", course:"COM"}, {teacher: "Mundani", course:"Math"}];


        if (!this.user.isLogged() || !this.user.isAdministrator()) {
          window.location.href = "/#!/login";
        }

        this.backHome = function () {
          window.location.href = "/#!/home";
        }

        self.invalidateSession = function() {
          this.user.invalidateSession();
          $http.get("http://localhost:8080/api?method=logout&by_user="+this.user.id).then(
            function (response) {
              if (response.data) {
                window.location.href = "/#!/login";
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

        this.getHistory = function () {
          $http.get("http://localhost:8080/api?method=getHistory&by_user="+this.user.id).then(
            function (response) {
              if (response.data.key)
                self.history = response.data.value;
            },
            function (response) {
              console.log(response.data);
            }
          );
        }

        this.getTeachers = function () {
          $http.get("http://localhost:8080/api?method=getTeachers").then(
            function (response) {
              self.teachers = response.data;
              self.courses = self.teachers.map(teacher => teacher.course);
            },
            function (response) {
              console.log("error getting teachers");
            }
          );
        }

        this.deleteTeacher = function (teacher) {
          $http.get("http://localhost:8080/api?method=delTeacher&teacher_id="+teacher.id+"&by_user="+this.user.id).then(
            function (response) {
              if (response.data.key) {
                this.teachers.remove(teacher);
              }
            },
            function (response) {

            }
          );
        }

        this.getHistory();
        this.getTeachers();
      }]
    });