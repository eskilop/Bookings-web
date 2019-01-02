package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class Booking extends BookingsImplementation
  {
    private int booking_id;
    private Teacher booking_from;
    private Timestamp booking_date;
    private State booking_state;

    public Booking (int booking_from, Timestamp booking_date, int state)
      {
        this.booking_id = solveId ("booking_id", "bookings");
        this.booking_from = Teacher.get (booking_from);
        this.booking_date = new Timestamp (truncateMillis (booking_date.getTime ()));
        this.booking_state = State.get (state);
      }

    public Booking (int booking_id, int booking_from, Timestamp booking_date, int state)
      {
        this.booking_id = booking_id;
        this.booking_from = Teacher.get (booking_from);
        this.booking_date = new Timestamp (truncateMillis (booking_date.getTime ()));
        this.booking_state = State.get (state);
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`bookings` (\n" +
            "    `booking_id` INT NOT NULL AUTO_INCREMENT,\n" +
            "    `booking_from` INT NOT NULL,\n" +
            "    `booking_date` TIMESTAMP NOT NULL,\n" +
            "    `booking_state` INT NOT NULL,\n" +
            "    PRIMARY KEY(`booking_id`),\n" +
            "    FOREIGN KEY(`booking_from`) REFERENCES `teachers`(`teacher_id`) ON DELETE CASCADE,\n" +
            "    FOREIGN KEY(`booking_state`) REFERENCES `states`(`state_id`)\n" +
            ") ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`bookings`");
      }

    public static Booking get (int target_id)
      {
        ArrayList <Booking> bookings = search ("SELECT * FROM `" + DB_NAME + "`.`bookings` WHERE booking_id=" + target_id + ";");
        if (bookings.size () < 1)
          return null;
        else
          return bookings.get (0);
      }

    public static ArrayList<Booking> search(String search)
      {
        return search (search, Booking::fromResultSet);
      }

    public static Booking fromResultSet (ResultSet set)
      {
        Booking ret = null;
        try
          {
            ret = new Booking (
                set.getInt ("booking_id"),
                set.getInt ("booking_from"),
                set.getTimestamp ("booking_date"),
                set.getInt ("booking_state")
            );
          }
        catch (SQLException sqle)
          {
            System.err.println ("SQL Error, can't get parameters from resultset");
          }
        return ret;
      }

    public int getId ()
      {
        return booking_id;
      }

    public Teacher getFrom ()
      {
        return booking_from;
      }

    public Timestamp getDate ()
      {
        return booking_date;
      }

    public void setState(int state_id)
      {
        this.booking_state = State.get (state_id);
      }

    public State getState ()
      {
        return booking_state;
      }

    private long truncateMillis (long datetime)
      {
        return 1000 * (datetime / 1000);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`bookings` SET " +
              "booking_from=" + booking_from.getId () + ", " +
              "booking_date=\"" + booking_date + "\", booking_state="+booking_state.getId ()+" WHERE booking_id=" + booking_id + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`bookings` (booking_id, booking_from, booking_date, booking_state) " +
              "VALUES (" + booking_id + ", " + booking_from.getId () + ", \"" + booking_date + "\", "+booking_state.getId ()+");");
      }

    @Override
    public boolean exists ()
      {
        return get (booking_id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`bookings` WHERE booking_id=" + booking_id + ";");
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return booking_id == booking.booking_id &&
            Objects.equals (booking_from, booking.booking_from) &&
            Objects.equals (booking_date, booking.booking_date) &&
            Objects.equals (booking_state, booking.booking_state);
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (booking_id, booking_from, booking_date);
      }

    @Override
    public String toString ()
      {
        return "Booking{" +
            "booking_id=" + booking_id +
            ", booking_from=" + booking_from +
            ", booking_date=" + booking_date +
            ", booking_state=" + booking_state +
            '}';
      }
  }
