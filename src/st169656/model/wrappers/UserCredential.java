package st169656.model.wrappers;

public class UserCredential
  {
    private String username;
    private String userpass;

    public UserCredential (String username, String userpass)
      {
        this.username = username;
        this.userpass = userpass;
      }

    public String getUsername ()
      {
        return username;
      }

    public String getUserpass ()
      {
        return userpass;
      }
  }
