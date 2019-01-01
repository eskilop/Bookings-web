package st169656;

import com.google.gson.Gson;
import javafx.util.Pair;
import st169656.model.*;
import st169656.model.wrappers.UserCredential;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ApiServlet extends HttpServlet
  {

    /*
     * ApiServlet, it's a servlet with the only task to manage
     * client model requests. Its purpose is to get and forward
     * data from the RDBMS.
     */

    private Model m = Model.getInstance ();

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.setHeader ("Access-Control-Allow-Origin", "*");

        String method = req.getParameter ("method");
        int userId = 0;

        switch (method)
          {
            case "getBookings": // Todo: set unavailable bookings once they become unavailable
              // remove same date bookings
              userId = Integer.valueOf (req.getParameter ("by_user"));
              writeJSON (resp, getBookingsForUser (userId));
              break;

            case "book":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              if (m.isLogged (userId))
                {
                  book (Integer.valueOf (req.getParameter ("booking_id")), userId);
                  writeJSON (resp, new Pair <> (true, "Booking booked correctly"));
                }
              else
                writeJSON (resp, new Pair <> (false, "User is not logged in"));
              break;

            case "unbook":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              if (m.isLogged (userId))
                {
                  writeJSON (resp, new Pair <> (true, unBook (Integer.valueOf (req.getParameter ("booking_id")), userId)));
                }
              else
                writeJSON (resp, new Pair <> (false, "User is not logged in"));
              break;

            case "getIncomingBookings":
              userId = Integer.valueOf (req.getParameter ("id"));
              if (m.isLogged (userId))
                writeJSON (resp, new Pair <> (true, getIncomingBookings (userId)));
              else
                writeJSON (resp, new Pair <> (false, "User is not logged in"));
              break;

            case "getPastBookings":
              userId = Integer.valueOf (req.getParameter ("id"));
              if (m.isLogged (userId))
                writeJSON (resp, new Pair <> (true, getPastBookings (userId)));
              else
                writeJSON (resp, new Pair <> (false, "User is not logged in"));
              break;
          }
      }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.addHeader ("Access-Control-Allow-Origin", "*");

        Gson gson = new Gson ();

        String raw_data = getDataFromRequest (req);

        switch (req.getParameter ("method"))
          {
            case "login":
              UserCredential u = gson.fromJson (raw_data, UserCredential.class);

              ArrayList <User> res = User.search ("SELECT * FROM `prenotazioni`.`users` WHERE " +
                  "user_name=\"" + u.getUsername () + "\" and user_password=\"" + u.getUserpass () + "\";");

              writeJSON (resp, getLoginResponse (res, u));
              break;

            default:
              System.out.println ("CRISTO");
              break;
          }
      }

    private ArrayList <Booking> getBookingsForUser (int user_id)
      {
        return Booking.search (
            "select * from bookings where bookings.booking_date >= CURRENT_DATE and bookings.booking_date not in\n" +
                "(\n" +
                "    select b.booking_date from bookings b where booking_id in\n" +
                "    (\n" +
                "        select h.booking_id from history h where h.booking_id not in\n" +
                "        (\n" +
                "           select distinct h1.booking_id\n" +
                "           from history h1 inner join history h2 on \n" +
                "            (h1.booking_id = h2.booking_id and h1.booking_state = 1 and h2.booking_state = 3 and h1.booked_by = " + user_id + ")\n" +
                "        )\n" +
                "    )\n" +
                ");"
        );
      }

    private void book (int booking_id, int user_id)
      {
        History newEntry = new History (booking_id, user_id, State.BOOKED, new Timestamp (new Date ().getTime ()));
        Booking booked = Booking.get (booking_id);
        booked.setState (State.BOOKED);
        booked.save ();
        newEntry.save ();
      }

    private History unBook (int booking_id, int user_id)
      {
        History h = new History (booking_id, user_id, State.CANCELLED, new Timestamp (new Date ().getTime ()));
        h.save ();
        Booking b = Booking.get (booking_id);
        b.setState (State.AVAILABLE);
        b.save ();
        return h;
      }

    private <T> void writeJSON (HttpServletResponse resp, T something) throws IOException
      {
        Gson gson = new Gson ();
        resp.getWriter ().write (gson.toJson (something));
      }

    private ArrayList <History> getPastBookings (int user_id)
      {
        ArrayList <History> hst = History.search ("SELECT * FROM `prenotazioni`.`history` WHERE booked_by = " + user_id + ";");
        return hst;
      }

    private ArrayList <Booking> getIncomingBookings (int user_id)
      {
        ArrayList <History> hst = History.search ("SELECT * FROM `prenotazioni`.`history` WHERE booked_by = " + user_id + " && booking_state = " + State.BOOKED + ";");
        ArrayList <Booking> incomingBookings = new ArrayList <> ();
        for (History h : hst)
          {
            Booking b = Booking.get (h.getBooking ().getId ());
            if (b.getDate ().after (new Timestamp (new Date ().getTime ())) &&
                b.getState ().getId () == State.BOOKED &&
                ! incomingBookings.contains (b))
              incomingBookings.add (b);
          }
        return incomingBookings;
      }

    private String getDataFromRequest (HttpServletRequest req) throws IOException
      {
        StringBuilder sb = new StringBuilder ();
        BufferedReader br = req.getReader ();
        String str = null;
        while ((str = br.readLine ()) != null)
          {
            sb.append (str);
          }
        return sb.toString ();
      }

    private Pair <Boolean, String> getLoginResponse (ArrayList <User> res, UserCredential u)
      {
        Pair <Boolean, String> loginResponse = null;
        Gson gson = new Gson ();
        if (res.size () == 0)
          {
            loginResponse = new Pair <> (false, "Credentials mismatch, check your inputs then try again");
          }

        if (res.size () == 1)
          {
            if (res.get (0).getUsername ().equals (u.getUsername ()) && res.get (0).getPassword ().equals (u.getUserpass ()))
              {
                loginResponse = new Pair <> (true, gson.toJson (res.get (0)));
                m.addLogged (res.get (0).getId ());
              }
            else
              loginResponse = new Pair <> (false, "Credentials mismatch, check your inputs then try again");
          }

        if (res.size () > 1)
          loginResponse = new Pair <> (false, "Multiple users found. We have an even worse problem now.");

        return loginResponse;
      }
  }
