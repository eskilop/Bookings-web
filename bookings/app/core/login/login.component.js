angular.
  module('login').
  component('login', {
    templateUrl: 'core/login/login.template.html',
    controller: ['$cookies', '$http', '$mdDialog', function ($cookies, $http, $mdDialog) {
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

        const prohibited = ['\u200b', '', ' ', undefined];
        
        if (prohibited.includes(self.user.name) || prohibited.includes(self.user.password)) {
          self.showDialog("Incorrect input, one or more fields were left blank")
        }
        else {
          var udata = {username: self.user.name, userpass: md5(self.salt(self.user.password))};
          $http.post("http://localhost:8080/api?method=login", udata, cfg).
          then(function (data) {
                if (data.data.key) {
                  var usr = JSON.parse(data.data.value);
                  $cookies.putObject('loggedUser', {id:usr.id, username:usr.username, role:usr.role});
                  self.continue();
                }
                else {
                  console.log(data.data);
                  self.showDialog(data.data.value);
                }
              }, 
              function (data) {
                console.log(data);
              }
            );
          }
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
      };

      self.showDialog = function(text) {
        $mdDialog.show(
          $mdDialog.alert()
            .clickOutsideToClose(true)
            .title('Wrong credentials')
            .textContent(text)
            .ariaLabel('Credentials Mismatch')
            .ok('Ok')
        );
      };

      this.guestLogin = function () {
        $cookies.putObject('loggedUser', {id:0, username:'Anonymous', role: {id: 2, title:"Client"}});
        this.continue();
      }

      this.continue = function() {
        var oldRoute = $cookies.get("oldRoute");
        window.location.href = oldRoute === undefined ? "/#!/home" : oldRoute;
      };
    }]
  });