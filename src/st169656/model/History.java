package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class History extends BookingsImplementation
  {
    private int history_id;
    private Booking booking_id;
    private User booked_by;
    private State booking_state;
    private Timestamp action_date;

    public History (int history_id, int booking_id, int booked_by, int booking_state, Timestamp action_date)
      {
        this.history_id = history_id;
        this.booking_id = Booking.get (booking_id);
        this.booked_by = User.get (booked_by);
        this.booking_state = State.get (booking_state);
        this.action_date = new Timestamp (truncateMillis (action_date.getTime ()));
      }

    public History (int booking_id, int booked_by, int booking_state, Timestamp action_date)
      {
        this.history_id = solveId ("history_id", "history");
        this.booking_id = Booking.get (booking_id);
        this.booked_by = User.get (booked_by);
        this.booking_state = State.get (booking_state);
        this.action_date = new Timestamp (truncateMillis (action_date.getTime ()));
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`history` ( \n" +
            "    `history_id` INT NOT NULL AUTO_INCREMENT , \n" +
            "    `booking_id` INT, \n" +
            "    `booked_by` INT NOT NULL , \n" +
            "    `booking_state` INT NOT NULL , \n" +
            "    `action_date` TIMESTAMP NOT NULL,\n" +
            "    PRIMARY KEY(`history_id`),\n" +
            "    FOREIGN KEY(`booking_id`) REFERENCES `bookings`(`booking_id`) ON DELETE SET NULL,\n" +
            "    FOREIGN KEY(`booked_by`) REFERENCES `users`(`user_id`),\n" +
            "    FOREIGN KEY(`booking_state`) REFERENCES `states`(`state_id`)\n" +
            ") ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`history`;");
      }

    public static History get (int target_id)
      {
        ArrayList <History> histories = search ("SELECT * FROM `" + DB_NAME + "`.`history` WHERE history_id=" + target_id + ";");
        if (histories.size () < 1)
          return null;
        else
          return histories.get (0);
      }

    public static History fromResultSet (ResultSet set)
      {
        History ret = null;
        try
          {
            ret = new History (
                set.getInt ("history_id"),
                set.getInt ("booking_id"),
                set.getInt ("booked_by"),
                set.getInt ("booking_state"),
                set.getTimestamp ("action_date")
            );
          }
        catch (SQLException sqle)
          {
            System.err.println ("SQL Error, can't get parameters from resultset");
          }
        return ret;
      }

    private long truncateMillis (long datetime)
      {
        return 1000 * (datetime / 1000);
      }

    public int getId ()
      {
        return history_id;
      }

    public Booking getBooking ()
      {
        return booking_id;
      }

    public User getBookedBy ()
      {
        return booked_by;
      }

    public State getState ()
      {
        return booking_state;
      }

    public Timestamp getActionDate ()
      {
        return action_date;
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`history` SET " +
              "booking_id=" + booking_id.getId () + ", " +
              "booked_by=" + booked_by.getId () + ", " +
              "booking_state=" + booking_state.getId () + ", " +
              "action_date=\"" + action_date + "\" WHERE history_id = " + history_id + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`history` (booking_id, booked_by, booking_state, action_date) " +
              "VALUES (" + booking_id.getId () + ", " + booked_by.getId () + ", " + booking_state.getId () + ", \"" + action_date + "\");");
      }

    @Override
    public boolean exists ()
      {
        return get (history_id) != null;
      }

    public static ArrayList<History> search(String search)
      {
        return search (search, History::fromResultSet);
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`history` WHERE history_id=" + history_id + ";");
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof History)) return false;
        History history = (History) o;
        return history_id == history.history_id &&
            Objects.equals (booking_id, history.booking_id) &&
            Objects.equals (booked_by, history.booked_by) &&
            Objects.equals (booking_state, history.booking_state) &&
            Objects.equals (action_date, history.action_date);
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (history_id, booking_id, booked_by, booking_state, action_date);
      }

    @Override
    public String toString ()
      {
        return "History{" +
            "history_id=" + history_id +
            ", booking_id=" + booking_id +
            ", booked_by=" + booked_by +
            ", booking_state=" + booking_state +
            ", action_date=" + action_date +
            '}';
      }
  }
