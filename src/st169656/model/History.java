package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class History extends BookingsImplementation<History>
  {
    private int history_id;
    private int booking_id;
    private int booked_by;
    private State booking_state;
    private Timestamp action_date;

    public History (int history_id)
      {
        History tmp = get (history_id);
        if (tmp == null) throw new IllegalStateException ("No such history piece");
        this.history_id = tmp.getId();
        this.booking_id = tmp.getBookingId();
        this.booked_by = tmp.getBookedBy();
        this.booking_state = tmp.getState();
        this.action_date = tmp.getActionDate();
      }

    public History (int history_id, int booking_id, int booked_by, int booking_state, Timestamp action_date)
      {
        this.history_id = history_id;
        this.booking_id = booking_id;
        this.booked_by = booked_by;
        this.booking_state = new State (booking_state);
        this.action_date = action_date;
      }

    public History (int booking_id, int booked_by, int booking_state, Timestamp action_date)
      {
        this.booking_id = booking_id;
        this.booked_by = booked_by;
        this.booking_state = new State (booking_state);
        this.action_date = action_date;
      }

    public int getId ()
      {
        return history_id;
      }

    public int getBookingId ()
      {
        return booking_id;
      }

    public int getBookedBy ()
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
    public void create ()
      {
        exec ("CREATE TABLE `prenotazioni`.`history` ( \n" +
            "    `history_id` INT NOT NULL AUTO_INCREMENT , \n" +
            "    `booking_id` INT NOT NULL , \n" +
            "    `booked_by` INT NOT NULL , \n" +
            "    `booking_state` INT NOT NULL , \n" +
            "    `action_date` TIMESTAMP NOT NULL,\n" +
            "    PRIMARY KEY(`history_id`),\n" +
            "    FOREIGN KEY(`booking_id`) REFERENCES `bookings`(`booking_id`),\n" +
            "    FOREIGN KEY(`booked_by`) REFERENCES `users`(`user_id`),\n" +
            "    FOREIGN KEY(`booking_state`) REFERENCES `states`(`state_id`)\n" +
            ") ENGINE = InnoDB;");
      }

    @Override
    public void destroy ()
      {
        exec("DROP TABLE `" + DB_NAME + "`.`history`;");
      }

    @Override
    public History get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`history` WHERE history_id="+history_id+";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`history` SET " +
              "booking_id="+booking_id+", " +
              "booked_by="+booked_by+", " +
              "booking_state="+booking_state.getId ()+", " +
              "action_date="+action_date + "WHERE history_id = "+history_id+";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`history` (booking_id, booked_by, booking_state, action_date) " +
              "VALUES ("+booking_id+", "+booked_by+", "+booking_state.getId ()+", "+action_date+");");
      }

    @Override
    public boolean exists ()
      {
        return get (history_id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`history` WHERE history_id="+history_id+";");
      }

    @Override
    public History createObj (ResultSet set) throws SQLException
      {
        return new History (
            set.getInt ("history_id"),
            set.getInt ("booking_id"),
            set.getInt ("booked_by"),
            set.getInt ("booking_state"),
            set.getTimestamp ("action_date")
        );
      }
  }
