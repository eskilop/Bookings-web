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

        this.checkWellFormed = function (obj) {
          const prohibited = ['\u200b', '', ' ', undefined];
          var wellformed = true;

          for (var key in obj) {
            if (!prohibited.includes(obj[key]))
              wellformed = wellformed && true;
            else
              wellformed = wellformed && false;
          }

          return wellformed;
        }

        this.newCourse = function(title, text) {
          var course
          
          var confirm = $mdDialog.prompt()
            .title(title)
            .textContent(text)
            .placeholder('course name')
            .ariaLabel('course name')
            .initialValue('')
            .required(true)
            .ok('Okay')
            .cancel('Cancel');
      
          $mdDialog.show(confirm).then(function(result) {
            course = {id: null, courseTitle: result};
            var cfg = {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
              }
            };

            $http.post("http://localhost:8080/api?method=newCourse&by_user="+self.user.id, course, cfg).
            then(function (data) {
                  if (data.data.key)
                    self.courses.push(JSON.parse(data.data.value));
                  else 
                    console.log(data.data);
                }, 
                function (data) {
                  console.log(data);
                }
              );
          }, function() {
          });
        };

        this.newTeacher = function() {
          $mdDialog.show({
            templateUrl: "core/admin/new_teacher.dialog.html",
            controller: function ($scope, $mdDialog) {
              $scope.teacher = {id: null};
              $scope.courses = self.courses;

              $scope.cancel = function () {
                $mdDialog.hide();
              };
              $scope.hide = function () {
                $mdDialog.hide();
              }
              $scope.answer = function (answer) {
                $mdDialog.hide(answer);
              };
            },
            parent: angular.element(document.body),
            clickOutsideToClose: true,
          }).then(
            function (result) {
              console.log(result);
              if (self.checkWellFormed(result)) {
                var cfg = {
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                  }
                };
    
                $http.post("http://localhost:8080/api?method=newTeacher&by_user="+self.user.id, result, cfg).
                then(function (data) {
                      if (data.data.key)
                        self.teachers.push(JSON.parse(data.data.value));
                      else 
                        console.log(data.data);
                    }, 
                    function (data) {
                      console.log(data);
                    }
                  );
              }
            },
            function () {
              console.log("Failed to get data from dialog");
            }
          );
        };

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
            },
            function (response) {
              console.log("error getting teachers");
            }
          );
        }

        this.getCourses = function () {
          $http.get("http://localhost:8080/api?method=getCourses").then(
            function (response) {
              self.courses = response.data;
            },
            function (response) {
              console.log("error getting courses");
            }
          );
        }

        this.deleteTeacher = function (teacher) {
          $http.get("http://localhost:8080/api?method=delTeacher&teacher_id="+teacher.id+"&by_user="+this.user.id).then(
            function (response) {
              if (response.data.key) {
                self.teachers = self.teachers.filter(function (teacherel) {
                  return !(teacherel === teacher);
                });
                self.courses = self.courses.filter(function (courseel) {
                  return !(teacher.course === courseel);
                });
              }
            },
            function (response) {

            }
          );
        }

        this.deleteCourse = function (course) {
          $http.get("http://localhost:8080/api?method=delCourse&course_id="+course.id+"&by_user="+this.user.id).then(
            function (response) {
              console.log(response);
              if (response.data.key) {
                self.teachers = self.teachers.filter(function (teacher) {
                  return !(teacher.course === course);
                });
                self.courses = self.courses.filter(function (courseel) {
                  return !(course === courseel);
                });
              }
            },
            function (response) {

            }
          );
        }

        this.getHistory();
        this.getTeachers();
        this.getCourses();
      }]
    });