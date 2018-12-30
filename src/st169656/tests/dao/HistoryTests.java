package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTests
  {
    @Test
    void HistoryCreateTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();
        });
      }

    @Test
    void HistorySaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          Course c = new Course ("History");
          c.save ();

          Teacher t = new Teacher ("Sample", "Sample", c.getId ());
          t.save ();

          Booking b = new Booking (t.getId (), new Timestamp (new Date ().getTime ()), State.AVAILABLE);
          b.save ();

          Role r = new Role ("myRole");
          r.save ();

          User u = new User("myUser", "myPassword", r.getId());
          u.save ();

          State s = new State ("Booked");
          s.save ();

          History h = new History (b.getId (), u.getId (), s.getId (), new Timestamp (new Date ().getTime ()));
          h.save ();

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void HistoryGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          Course c = new Course ("History");
          c.save ();

          Teacher t = new Teacher ("Sample", "Sample", c.getId ());
          t.save ();

          Booking b = new Booking (t.getId (), new Timestamp (new Date ().getTime ()), State.AVAILABLE);
          b.save ();

          Role r = new Role ("myRole");
          r.save ();

          User u = new User("myUser", "myPassword", r.getId());
          u.save ();

          State s = new State ("Booked");
          s.save ();

          History h = new History (b.getId (), u.getId (), s.getId (), new Timestamp (new Date ().getTime ()));
          h.save ();

          assertEquals (h, History.get (h.getId ()));

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void HistoryExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          Course c = new Course ("History");
          c.save ();

          Teacher t = new Teacher ("Sample", "Sample", c.getId ());
          t.save ();

          Booking b = new Booking (t.getId (), new Timestamp (new Date ().getTime ()), State.AVAILABLE);
          b.save ();

          Role r = new Role ("myRole");
          r.save ();

          User u = new User("myUser", "myPassword", r.getId());
          u.save ();

          State s = new State ("Booked");
          s.save ();

          History h = new History (b.getId (), u.getId (), s.getId (), new Timestamp (new Date ().getTime ()));
          h.save ();

          assertTrue (h.exists ());

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void HistoryDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          Course c = new Course ("History");
          c.save ();

          Teacher t = new Teacher ("Sample", "Sample", c.getId ());
          t.save ();

          Booking b = new Booking (t.getId (), new Timestamp (new Date ().getTime ()), State.AVAILABLE);
          b.save ();

          Role r = new Role ("myRole");
          r.save ();

          User u = new User("myUser", "myPassword", r.getId());
          u.save ();

          State s = new State ("Booked");
          s.save ();

          History h = new History (b.getId (), u.getId (), s.getId (), new Timestamp (new Date ().getTime ()));
          h.save ();

          h.delete ();
          assertFalse (h.exists ());

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void HistorySearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          assertEquals (0, History.search ("SELECT * FROM `prenotazioni`.`history`;").size ());

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void HistoryDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          State.create ();
          Role.create ();
          User.create ();
          Teacher.create ();
          Booking.create ();
          History.create ();

          History.destroy ();
          Booking.destroy ();
          Teacher.destroy ();
          User.destroy ();
          Role.destroy ();
          State.destroy ();
          Course.destroy ();
        });
      }
  }