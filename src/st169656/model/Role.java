package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Role extends BookingsImplementation <Role>
  {
    private int id;
    private String title;

    public Role (int id)
      {
        Role tmp = get (id);
        if (tmp == null) throw new IllegalStateException ("No such role");
        this.id = tmp.getId ();
        this.title = tmp.getTitle ();
      }

    public Role (int id, String title)
      {
        this.id = id;
        this.title = title;
      }

    public Role (String title)
      {
        this.title = title;
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
    public void create ()
      {
        exec ("CREATE TABLE `" + DB_NAME + "`.`roles` ( " +
            "`role_id` INT NOT NULL AUTO_INCREMENT , " +
            "`role_title` TEXT NOT NULL , " +
            "PRIMARY KEY (`role_id`)) ENGINE = InnoDB;");
      }

    @Override
    public void destroy ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`roles` WHERE role_id=" + id + ";");
      }

    @Override
    public Role get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`roles` WHERE role_id = " + target_id + ";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`roles` SET " +
              "role_title=\"" + title + "\" WHERE role_id=" + id + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`roles` (role_title) " +
              "VALUES (\"" + title + "\");");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `\" + DB_NAME + \"`.`roles` WHERE role_id = "+id+";");
      }

    @Override
    public Role createObj (ResultSet set) throws SQLException
      {
        return new Role (
            set.getInt ("role_id"),
            set.getString ("role_title")
        );
      }
  }
