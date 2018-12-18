package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Booking extends BookingsImplementation <Booking>
  {
    private int booking_id;
    private Teacher booking_from;
    private Timestamp booking_date;

    public Booking (int booking_id)
      {
        this.booking_id = booking_id;
      }

    public Booking (int booking_from, Timestamp booking_date)
      {
        this.booking_from = new Teacher (booking_from);
        this.booking_date = booking_date;
      }

    public Booking (int booking_id, int booking_from, Timestamp booking_date)
      {
        this.booking_id = booking_id;
        this.booking_from = new Teacher (booking_from);
        this.booking_date = booking_date;
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


    @Override
    public void create ()
      {
        exec ("CREATE TABLE `" + DB_NAME + " `.`bookings`(\n" +
            "    ` booking_id ` INT NOT NULL AUTO_INCREMENT,\n" +
            "    ` booking_from ` INT NOT NULL,\n" +
            "    ` booking_date ` TIMESTAMP NOT NULL,\n" +
            "    PRIMARY KEY(` booking_id `),\n" +
            "    FOREIGN KEY(` booking_from `) REFERENCES ` teachers `(` teacher_id `)\n" +
            ") ENGINE = InnoDB;");
      }

    @Override
    public void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + " `.`bookings`");
      }

    @Override
    public Booking get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`bookings` WHERE booking_id=" + target_id + ";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + " `.`bookings` SET " +
              "booking_from=" + booking_from.getId () + ", " +
              "booking_date=" + booking_date + " WHERE booking_id=" + booking_date + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`bookings` (booking_from, booking_date) " +
              "VALUES (" + booking_from.getId () + ", " + booking_date + ");");
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
    public Booking createObj (ResultSet set) throws SQLException
      {
        return new Booking (
            set.getInt ("booking_id"),
            set.getInt ("booking_from"),
            set.getTimestamp ("booking_date")
        );
      }
  }
