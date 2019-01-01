angular.
  module('admin').
    component('admin', {
      templateUrl: "core/admin/admin.template.html",
      controller: ['$http', '$cookies', '$mdDialog', function($http, $cookies, $mdDialog) {
        var self = this;

        this.user = new User($cookies);

        if (!this.user.isLogged() || !this.user.isAdministrator()) {
          window.location.href = "/#!/login";
        }
        else {
          this.getHistory();
        }

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
      }]
    });