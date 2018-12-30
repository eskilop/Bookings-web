package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.Booking;
import st169656.model.Course;
import st169656.model.State;
import st169656.model.Teacher;

import java.awt.print.Book;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookingTests
  {
    @Test
    void BookingCreateTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();
        });
      }

    @Test
    void BookingSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();

          Course c = new Course (1, "mCourse");
          c.save ();

          Teacher t = new Teacher (1, "Sample", "Sample", 1);
          t.save ();

          Booking b = new Booking (1, t.getId (), new Timestamp (new Date ().getTime ()), State.AVAILABLE);
          b.save ();

          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void BookingGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();

          Course c = new Course (1, "mCourse");
          c.save ();

          Teacher t = new Teacher (1, "Sample", "Sample", c.getId ());
          t.save ();


          Booking b = new Booking (1, t.getId (), new Timestamp (Calendar.getInstance ().getTime ().getTime ()), State.AVAILABLE);
          b.save ();

          assertEquals (b, Booking.get (b.getId ()));

          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void BookingExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();

          Course c = new Course (1, "mCourse");
          c.save ();

          Teacher t = new Teacher (1, "Sample", "Sample", 1);
          t.save ();

          Booking b = new Booking (1, t.getId (), new Timestamp (Calendar.getInstance ().getTime ().getTime ()), State.AVAILABLE);
          b.save ();

          assertTrue (Booking.get (b.getId ()).exists ());

          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void BookingDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();

          Course c = new Course (1, "mCourse");
          c.save ();

          Teacher t = new Teacher (1, "Sample", "Sample", 1);
          t.save ();

          Booking b = new Booking (1, t.getId (), new Timestamp (Calendar.getInstance ().getTime ().getTime ()), State.AVAILABLE);
          b.save ();

          b.delete ();
          assertFalse (b.exists ());

          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void BookingSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();
          assertEquals (0, Booking.search ("SELECT * FROM `prenotazioni`.`bookings`;").size ());
          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void BookingDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Booking.create ();

          Booking.destroy ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }
  }