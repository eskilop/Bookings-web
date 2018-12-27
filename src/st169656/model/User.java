package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class User extends BookingsImplementation
  {
    private int id;
    private String username;
    private String password;
    private Role role;

    public User (String username, String password, int role)
      {
        this.id = solveId ("user_id", "users");
        this.username = username;
        this.password = password;
        this.role = Role.get (role);
      }

    public User (int id, String username, String password, int role)
      {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = Role.get (role);
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`users`(\n" +
            "    `user_id` INT NOT NULL AUTO_INCREMENT,\n" +
            "    `user_name` VARCHAR(25) NOT NULL,\n" +
            "    `user_password` VARCHAR(32) NOT NULL,\n" +
            "    `user_role` INT NOT NULL,\n" +
            "    PRIMARY KEY(`user_id`, `user_name`),\n" +
            "    FOREIGN KEY(`user_role`) REFERENCES `roles`(`role_id`)\n" +
            ") ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`users`;");
      }

    public static User get (int target_id)
      {
        ArrayList <User> users = search ("SELECT * FROM `" + DB_NAME + "`.`users` WHERE user_id=" + target_id + ";");
        if (users.size () < 1)
          return null;
        else
          return users.get (0);
      }

    public static User fromResultSet (ResultSet set)
      {
        User ret = null;
        try
          {
            ret = new User (
                set.getInt ("user_id"),
                set.getString ("user_name"),
                set.getString ("user_password"),
                set.getInt ("user_role"));
          }
        catch (SQLException sqle)
          {
            System.err.println ("SQL Error, can't get parameters from resultset");
          }
        return ret;
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
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`users` SET " +
              "user_name=\"" + username + "\", " +
              "user_password=\"" + password + "\", " +
              "user_role="+role.getId ());
        else
          save ("INSERT INTO `" + DB_NAME + "`.`users` (user_id, user_name, user_password, user_role) " +
              "VALUES (" + id + ", \"" + username + "\", \"" + password + "\", " + role.getId () + ")");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    public static ArrayList<User> search(String search)
      {
        return search (search, User::fromResultSet);
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`users` WHERE user_id=" + id);
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof User)) return false;
        User user = (User) o;
        return getId () == user.getId () &&
            Objects.equals (getUsername (), user.getUsername ()) &&
            Objects.equals (getPassword (), user.getPassword ()) &&
            Objects.equals (getRole (), user.getRole ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getId (), getUsername (), getPassword (), getRole ());
      }

    @Override
    public String toString ()
      {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            '}';
      }
  }
