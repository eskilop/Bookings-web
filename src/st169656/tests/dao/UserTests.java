package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.Course;
import st169656.model.Role;
import st169656.model.Teacher;
import st169656.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTests
  {
    @Test
    void UserCreateTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();
        });
      }

    @Test
    void UserSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();

          Role r = new Role (1, "myRole");
          r.save ();

          User s = new User (1, "Sample", "Sample", r.getId ());
          s.save ();

          User.destroy ();
          Role.destroy ();
        });
      }

    @Test
    void UserGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();

          Role r = new Role (1, "myRole");
          r.save ();
          User s = new User (1, "Sample", "Sample", r.getId ());
          s.save ();

          assertEquals (s, User.get (1));

          User.destroy ();
          Role.destroy ();
        });
      }

    @Test
    void UserExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();

          Role r = new Role (1, "myRole");
          r.save ();
          User s = new User (1, "Sample", "Sample", r.getId ());
          s.save ();

          assertTrue (User.get (1).exists ());

          User.destroy ();
          Role.destroy ();
        });
      }

    @Test
    void UserDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();

          Role r = new Role (1, "myRole");
          r.save ();
          User s = new User (1, "Sample", "Sample", r.getId ());
          s.save ();
          s.delete ();

          assertFalse (s.exists ());

          User.destroy ();
          Role.destroy ();
        });
      }

    @Test
    void UserSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();

          assertEquals (0, User.search ("SELECT * FROM `prenotazioni`.`users`;").size ());

          User.destroy ();
          Role.destroy ();
        });
      }

    @Test
    void UserDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          User.create ();
          User.destroy ();
          Role.destroy ();
        });
      }
  }