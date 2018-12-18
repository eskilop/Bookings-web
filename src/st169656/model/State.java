package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class State extends BookingsImplementation <State>
  {
    private int state_id;
    private String state_title;

    public State (int id)
      {
        State tmp = get (id);
        if (tmp == null) throw new IllegalStateException ("No such role");
        this.state_id = tmp.getId ();
        this.state_title = tmp.getTitle ();
      }

    public State (int id, String title)
      {
        this.state_id = id;
        this.state_title = title;
      }

    public State (String title)
      {
        this.state_title = title;
      }

    public int getId ()
      {
        return state_id;
      }

    public String getTitle ()
      {
        return state_title;
      }


    @Override
    public void create ()
      {
        exec ("CREATE TABLE `" + DB_NAME + "`.`states` ( " +
            "`state_id` INT NOT NULL AUTO_INCREMENT , " +
            "`state_title` TEXT NOT NULL , " +
            "PRIMARY KEY (`state_id`)) ENGINE = InnoDB;");
      }

    @Override
    public void destroy ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`states` WHERE state_id=" + state_id + ";");
      }

    @Override
    public State get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`states` WHERE state_id = " + target_id + ";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`states` SET " +
              "state_title=\"" + state_title + "\" WHERE state_id=" + state_id + ";");
        else
          save ("INSERT INTO `\" + DB_NAME + \"`.`states` (state_title) " +
              "VALUES (\"" + state_title + "\");");
      }

    @Override
    public boolean exists ()
      {
        return get (state_id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `\" + DB_NAME + \"`.`states` WHERE state_id = " + state_id + ";");
      }

    @Override
    public State createObj (ResultSet set) throws SQLException
      {
        return new State (
            set.getInt ("state_id"),
            set.getString ("state_title")
        );
      }
  }
