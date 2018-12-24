package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.Course;
import st169656.model.Role;

import static org.junit.jupiter.api.Assertions.*;

class CourseTests
  {
    @Test
    void CourseCreateTest ()
      {
        assertDoesNotThrow (Course::create);
      }

    @Test
    void CourseSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Course r = new Course (1, "myCourse");
          r.save ();
          Course.destroy ();
        });
      }

    @Test
    void CourseGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Course c = new Course (1, "myCourse");
          c.save ();

          assertEquals (c, Course.get (1));
          Course.destroy ();
        });
      }

    @Test
    void CourseExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          new Course (1, "myCourse").save ();
          assertTrue (Course.get (1).exists ());
          Course.destroy ();
        });
      }

    @Test
    void CourseDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Course r = new Course (1, "myCourse");
          r.save ();
          r.delete ();
          assertFalse (r.exists ());
          Course.destroy ();
        });
      }

    @Test
    void CourseSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          assertEquals (0, Course.search ("SELECT * FROM `prenotazioni`.`courses`;").size ());
          Course.destroy ();
        });
      }

    @Test
    void CourseDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Course.destroy ();
        });
      }
  }