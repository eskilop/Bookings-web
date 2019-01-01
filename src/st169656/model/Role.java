package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Role extends BookingsImplementation
  {
    private int id;
    private String title;

    public static final int ADMINISTRATOR = 1;
    public static final int CLIENT = 2;

    public Role (int id, String title)
      {
        this.id = id;
        this.title = title;
      }

    public Role (String title)
      {
        this.id = solveId ("role_id", "roles");
        this.title = title;
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`roles` ( " +
            "`role_id` INT NOT NULL AUTO_INCREMENT , " +
            "`role_title` TEXT NOT NULL , " +
            "PRIMARY KEY (`role_id`)) ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`roles`;");
      }

    public static Role get (int target_id)
      {
        ArrayList <Role> roles = search ("SELECT * FROM `" + DB_NAME + "`.`roles` WHERE role_id = " + target_id + ";");
        if (roles.size () < 1)
          return null;
        else
          return roles.get (0);
      }

    public static Role fromResultSet (ResultSet set)
      {
        Role ret = null;
        try
          {
            ret = new Role (
                set.getInt ("role_id"),
                set.getString ("role_title")
            );
          }
        catch (SQLException sqle)
          {
            System.err.println ("Role::fromResultSet: SQL Error, can't get parameters from resultset");
            sqle.printStackTrace ();
          }
        return ret;
      }

    public int getId ()
      {
        return id;
      }

    public String getTitle ()
      {
        return title;
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`roles` SET " +
              "role_title=\"" + title + "\" WHERE role_id=" + id + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`roles` (role_id, role_title) " +
              "VALUES (" + id + ", \"" + title + "\");");
      }

    public static ArrayList<Role> search(String search)
      {
        return search (search, Role::fromResultSet);
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`roles` WHERE role_id = " + id + ";");
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof Role)) return false;
        Role role = (Role) o;
        return getId () == role.getId () &&
            Objects.equals (getTitle (), role.getTitle ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getId (), getTitle ());
      }

    @Override
    public String toString ()
      {
        return "Role{" +
            "id=" + id +
            ", title='" + title + '\'' +
            '}';
      }
  }