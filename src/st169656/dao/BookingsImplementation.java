package st169656.dao;

import java.sql.*;
import java.util.ArrayList;

public abstract class BookingsImplementation <T extends BookingsImplementation> implements BookingsDAO <T>
  {
    protected static final String DB_NAME = "prenotazioni";
    private static final String DB_UNAME = "root";
    private static final String DB_PASSWD = "";
    private static final String URI = "jdbc:mysql://localhost:3306/" + DB_NAME;

    private Statement statement;

    public void exec (String anyquery)
      {
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            statement.execute (anyquery);
          }
        catch (SQLException sqle)
          {
            System.err.println ("Error while opening connection");
          }
        finally
          {
            try
              {
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("Error while closing connection");
              }
          }
      }

    @Override
    public ArrayList <T> search (String condition)
      {
        ArrayList <T> result = new ArrayList <> ();
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            ResultSet s = statement.executeQuery (condition);

            if (! s.isFirst ())
              s.first ();

            do
              {
                result.add (createObj (s));
              } while (s.next ());
          }
        catch (SQLException sqle)
          {
            System.err.println ("Error while opening connection");
          }
        finally
          {
            try
              {
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("Error while closing connection");
              }
          }
        return result;
      }

    public void save (String update)
      {
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            statement.executeUpdate (update);
          }
        catch (SQLException sqle)
          {
            System.err.println ("Error while opening connection");
          }
        finally
          {
            try
              {
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("Error while closing connection");
              }
          }

      }
  }