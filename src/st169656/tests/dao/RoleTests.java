package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.Role;

import static org.junit.jupiter.api.Assertions.*;

class RoleTests
  {
    @Test
    void RoleCreateTest ()
      {
        assertDoesNotThrow (Role::create);
      }

    @Test
    void RoleSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          Role r = new Role (1, "Admin");
          r.save ();
          Role.destroy ();
        });
      }

    @Test
    void RoleGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          Role r = new Role (1, "Client");
          r.save ();

          assertEquals (r, Role.get (1));
          Role.destroy ();
        });
      }

    @Test
    void RoleExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          new Role (1, "Client").save ();
          assertTrue (Role.get (1).exists ());
          Role.destroy ();
        });
      }

    @Test
    void RoleDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          Role r = new Role (1, "Admin");
          r.save ();
          r.delete ();
          assertFalse (r.exists ());
          Role.destroy ();
        });
      }

    @Test
    void RoleSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          assertEquals (0, Role.search ("SELECT * FROM `prenotazioni`.`roles`;").size ());
          Role.destroy ();
        });
      }

    @Test
    void RoleDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          Role.create ();
          Role.destroy ();
        });
      }
  }