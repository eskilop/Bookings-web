package st169656.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Function;

public abstract class BookingsImplementation implements BookingsDAO
  {
    protected static final String DB_NAME = "prenotazioni";
    private static final String DB_UNAME = "root";
    private static final String DB_PASSWD = "";
    private static final String URI = "jdbc:mysql://localhost:3306/" + DB_NAME;

    protected static void exec (String anyquery)
      {
        Statement statement;
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            statement.execute (anyquery);
          }
        catch (SQLException sqle)
          {
            System.err.println ("EXEC: Error while opening connection");
            sqle.printStackTrace ();
          }
        finally
          {
            try
              {
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("EXEC:CLOSE: Error while closing connection");
                sqle.printStackTrace ();
              }
          }
      }

    protected static <T> ArrayList <T> search (String condition, Function <ResultSet, T> function)
      {
        Statement statement;
        ResultSet s = null;
        ArrayList <T> objs = new ArrayList <> ();
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            s = statement.executeQuery (condition);

            if (! isSetEmpty (s))
              {
                do
                  {
                    objs.add (function.apply (s));
                  }
                while (s.next ());
              }
          }
        catch (SQLException sqle)
          {
            System.err.println ("SEARCH: Error while opening connection");
            sqle.printStackTrace ();
          }
        finally
          {
            try
              {
                s.close ();
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("SEARCH:CLOSE: Error while closing connection");
                sqle.printStackTrace ();
              }
          }
        return objs;
      }

    private static boolean isSetEmpty (ResultSet s) throws SQLException
      {
        return ! s.first ();
      }

    protected static Integer solveId (String what, String table)
      {
        Statement statement;
        ResultSet s = null;
        int id = 0;
        Connection connection = null;
        try
          {
            connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
            statement = connection.createStatement ();
            s = statement.executeQuery ("SELECT MAX(" + what + ") FROM `" + DB_NAME + "`.`" + table + "`;");

            if (! isSetEmpty (s))
              {
                id = s.getInt ("MAX("+what+")") + 1;
              }
          }
        catch (SQLException sqle)
          {
            System.err.println ("SolveID: Error while opening connection");
            sqle.printStackTrace ();
          }
        finally
          {
            try
              {
                s.close ();
                connection.close ();
              }
            catch (SQLException sqle)
              {
                System.err.println ("SolveID:CLOSE: Error while closing connection");
                sqle.printStackTrace ();
              }
          }
        return id;
      }

        protected void save (String update)
        {
          Statement statement;
          Connection connection = null;
          try
            {
              connection = DriverManager.getConnection (URI, DB_UNAME, DB_PASSWD);
              statement = connection.createStatement ();
              statement.executeUpdate (update);
            }
          catch (SQLException sqle)
            {
              System.err.println ("SAVE: Error while opening connection");
              sqle.printStackTrace ();
            }
          finally
            {
              try
                {
                  connection.close ();
                }
              catch (SQLException sqle)
                {
                  System.err.println ("SAVE:CLOSE: Error while closing connection");
                  sqle.printStackTrace ();
                }
            }

        }
      }