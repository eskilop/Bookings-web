package st169656.tests;

import org.junit.jupiter.api.Test;
import st169656.model.DBManager;
import st169656.model.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DAOTests
  {

    @Test
    void BookingsImplementationSaveTest ()
      {
        Role.create ();

        Role r = new Role (1, "Client");
        r.save (); // <= usa la get() <= search <= exists <= save;

        Role s = Role.get (r.getId ());
        r.delete ();

        Role.destroy ();

        assertEquals (r, s);
      }

    @Test
    void BookingsImplementationSearchTest ()
      {
        Role.create ();

        Role r = new Role (1, "Client");
        r.save ();

        Role s = Role.search ("SELECT * FROM `prenotazioni`.`roles`;", Role::fromResultSet).get (0);
        r.delete ();

        Role.destroy ();

        assertEquals (r, s);
      }
  }