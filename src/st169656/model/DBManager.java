package st169656.model;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager
  {
    public DBManager()
      {
        try
          {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
          } catch (SQLException sqle)
          {
            // todo: why can't register driver?
          }
      }
  }
