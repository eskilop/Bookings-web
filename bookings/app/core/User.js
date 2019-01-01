class User {
  constructor(cookies) {
      this._cookies = cookies;
      var tmp = cookies.getObject("loggedUser");
      if (tmp === undefined) {
        this._id = 0;
        this._username = "Anonymous";
      } else {
        this._id = tmp.id;
        this._username = tmp.username;
      }
    }

  set id(id) {
    this._id = id;
  }

  set username(username) {
    this._username = username;
  }

  get id() {
    return this._id;
  }

  get username() {
    return this._username;
  }

  isLogged() {
    return !(this.id === 0 && this.username === "Anonymous");
  }

  invalidateSession() {
    this._id = 0;
    this._username = "Anonymous";
    this._cookies.remove("loggedUser");
    this._cookies.putObject("loggedUser", {id: 0, username: "Anonymous"});
  }
}