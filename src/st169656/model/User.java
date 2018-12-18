package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends BookingsImplementation <User>
  {
    private int id;
    private String username;
    private String password;
    private Role role;

    public User (int id)
      {
        User tmp = get (id);
        if (tmp == null) throw new IllegalStateException ("No such user");
        this.id = tmp.getId ();
        this.username = tmp.getUsername ();
        this.password = tmp.getPassword ();
        this.role = tmp.getRole ();
      }

    public User (String username, String password, int role)
      {
        this.username = username;
        this.password = password;
        this.role = new Role (role);
      }

    public User (int id, String username, String password, int role)
      {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = new Role (role);
      }

    public int getId ()
      {
        return id;
      }

    public String getUsername ()
      {
        return username;
      }

    public String getPassword ()
      {
        return password;
      }

    public Role getRole ()
      {
        return role;
      }

    @Override
    public void create ()
      {
        exec ("CREATE TABLE `" + DB_NAME + "`.`users`(\n" +
            "    `user_id` INT NOT NULL AUTO_INCREMENT,\n" +
            "    `user_name` TEXT NOT NULL,\n" +
            "    `user_password` TEXT NOT NULL,\n" +
            "    `user_role` INT NOT NULL,\n" +
            "    PRIMARY KEY(`user_id`),\n" +
            "    FOREIGN KEY(`user_role`) REFERENCES `roles`(`role_id`)\n" +
            ") ENGINE = InnoDB;)");
      }

    @Override
    public void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`users`;");
      }

    @Override
    public User get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`users` WHERE user_id=" + target_id + ";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`users` SET " +
              "user_name=\"" + username + "\", " +
              "user_password=\"" + password + "\", " +
              "user_role=");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`users` (user_name, user_password, user_role) " +
              "VALUES (\"" + username + "\", \"" + password + "\", " + role.getId () + ")");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`users` WHERE user_id=" + id);
      }

    @Override
    public User createObj (ResultSet set) throws SQLException
      {
        return new User (
            set.getInt ("user_id"),
            set.getString ("user_name"),
            set.getString ("user_password"),
            set.getInt ("user_role"));
      }
  }
