angular.
  module('login').
  component('login', {
    templateUrl: 'core/login/login.template.html',
    controller: ['$cookies', '$http', function ($cookies, $http) {
      var self = this;

      this.user = {
        name: undefined,
        password: undefined
      }

      this.disableLogin = function() {
        return this.user.name === undefined || this.user.password === undefined;
      }

      this.login = function() {

        var cfg = {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
          }
        };

        var data = {username: self.user.name, userpass: md5(self.salt(self.user.password))};

        $http.post("http://localhost:8080/api?method=login", data, cfg).
        then(function (data) {
              $cookies.put('username', self.user.name);
              console.log(data);
          }, function (data) {
              console.log(data);
          });
      }

      this.salt = function (word) {
        var salt = "NaCl";
        var oldcntr = 0;
        var saltedword = "";

        for (var i = 3; i < self.user.password.length; i += 3) {
          saltedword += word.substring (oldcntr, i);
          saltedword += salt;
        }
        return saltedword;
      }

      this.continue = function() {
        window.location.href = "/#!/home";
      }
    }]
  });