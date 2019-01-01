class User {
  constructor(cookies) {
      this._cookies = cookies;
      var tmp = cookies.getObject("loggedUser");
      if (tmp === undefined) {
        this._id = 0;
        this._username = "Anonymous";
        this._role = {id: 2, title: "Client"};
      } else {
        this._id = tmp.id;
        this._username = tmp.username;
        this._role = tmp.role;
      }
    }

  set id(id) {
    this._id = id;
  }

  set username(username) {
    this._username = username;
  }

  set role (role) {
    this._role = role;
  }

  get id() {
    return this._id;
  }

  get username() {
    return this._username;
  }

  get role() {
    return this._role;
  }

  isLogged() {
    return !(this.id === 0 && this.username === "Anonymous");
  }

  isAdministrator() {
    return this.role.id === 1;
  }

  invalidateSession() {
    this._id = 0;
    this._username = "Anonymous";
    this._role = {id: 2, title: "Client"};
    this._cookies.remove("loggedUser");
    this._cookies.putObject("loggedUser", {id: 0, username: "Anonymous", role: {id: 2, title: "Client"}});
  }
}