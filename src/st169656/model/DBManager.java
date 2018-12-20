package st169656.model;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager
  {
    public DBManager()
      {
        try
          {
            Class.forName ("com.mysql.cj.jdbc.Driver").newInstance ();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
          } catch (SQLException sqle)
          {
            System.err.println ("Cannot register database driver");
          }
        catch (IllegalAccessException e)
          {
            e.printStackTrace ();
          }
        catch (InstantiationException e)
          {
            e.printStackTrace ();
          }
        catch (ClassNotFoundException e)
          {
            e.printStackTrace ();
          }
      }

    public void build()
      {
        Role.create ();
        State.create ();
        Course.create ();

        User.create();
        Teacher.create();
        Booking.create ();
        History.create ();
      }

    public void destroy()
      {
        History.destroy ();
        Booking.destroy ();
        Teacher.destroy ();
        User.destroy ();

        Course.destroy ();
        State.destroy ();
        Role.destroy ();
      }
  }
