package st169656.tests.dao;

import org.junit.jupiter.api.Test;
import st169656.model.State;

import static org.junit.jupiter.api.Assertions.*;

class StateTests
  {
    @Test
    void StateCreateTest ()
      {
        assertDoesNotThrow (State::create);
      }

    @Test
    void StateSaveTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          State r = new State (1, "Available");
          r.save ();
          State.destroy ();
        });
      }

    @Test
    void StateGetTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          State s = new State (1, "Available");
          s.save ();
          assertEquals (s, State.get (1));
          State.destroy ();
        });
      }

    @Test
    void StateExistsTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          new State (1, "Available").save ();
          assertTrue (State.get (1).exists ());
          State.destroy ();
        });
      }

    @Test
    void StateDeleteTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          State r = new State (1, "Available");
          r.save ();
          r.delete ();
          assertFalse (r.exists ());
          State.destroy ();
        });
      }

    @Test
    void StateSearchTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          assertEquals (0, State.search ("SELECT * FROM `prenotazioni`.`states`;").size ());
          State.destroy ();
        });
      }

    @Test
    void StateDestroyTest ()
      {
        assertDoesNotThrow (() ->
        {
          State.create ();
          State.destroy ();
        });
      }
  }