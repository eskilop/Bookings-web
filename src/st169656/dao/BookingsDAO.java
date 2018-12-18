package st169656.dao;

import java.sql.*;
import java.util.ArrayList;

interface BookingsDAO <T>
  {
    void create ();

    void destroy ();

    /**
     * @param target_id id of the target
     * @return an object of T's class
     */
    T get (int target_id);

    ArrayList <T> search (String condition);

    void save ();

    boolean exists();

    void delete ();

    /**
     * @param set resultset from a query
     * @return an object of T's class.
     */
    T createObj (ResultSet set) throws SQLException;
  }