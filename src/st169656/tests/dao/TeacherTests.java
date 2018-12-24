package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.Course;
import st169656.model.Role;
import st169656.model.Teacher;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTests
  {
    @Test
    void TeacherCreateTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
        });
      }

    @Test
    void TeacherSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Course r = new Course (1, "myCourse");
          r.save ();

          Teacher s = new Teacher (1, "Sample", "Sample", r.getId ());
          s.save ();

          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void TeacherGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Course r = new Course (1, "myCourse");
          r.save ();
          Teacher s = new Teacher (1, "Sample", "Sample", r.getId ());
          s.save ();

          assertEquals (s, Teacher.get (1));

          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void TeacherExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Course r = new Course (1, "myCourse");
          r.save ();
          Teacher s = new Teacher (1, "Sample", "Sample", r.getId ());
          s.save ();

          assertTrue (Teacher.get (1).exists ());

          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void TeacherDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Course r = new Course (1, "myCourse");
          r.save ();
          Teacher s = new Teacher (1, "Sample", "Sample", r.getId ());
          s.save ();
          s.delete ();

          assertFalse (s.exists ());

          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void TeacherSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();

          assertEquals (0, Role.search ("SELECT * FROM `prenotazioni`.`teachers`;").size ());

          Teacher.destroy ();
          Course.destroy ();
        });
      }

    @Test
    void TeacherDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Course.create ();
          Teacher.create ();
          Teacher.destroy ();
          Course.destroy ();
        });
      }
  }