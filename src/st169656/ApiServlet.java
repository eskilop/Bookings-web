package st169656;

import com.google.gson.Gson;
import javafx.util.Pair;
import st169656.dao.BookingsImplementation;
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
    private boolean debug = true;

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.setHeader ("Access-Control-Allow-Origin", "*");

        String method = req.getParameter ("method");
        int userId;
        int objId;

        switch (method)
          {
            case "logout":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              m.removeLogged (userId);
              writeJSON (resp, !m.isLogged (userId));
              break;

            case "book":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              if (m.isLogged (userId))
                writeJSON (resp, new Pair <> (true, book (Integer.valueOf (req.getParameter ("booking_id")), userId)));
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

            case "getBookings": // Todo: set unavailable bookings once they become unavailable
              // remove same date bookings
              userId = Integer.valueOf (req.getParameter ("by_user"));
              writeJSON (resp, getBookingsForUser (userId));
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

            case "getHistory":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              if (User.get (userId).getRole ().getId () == Role.ADMINISTRATOR && m.isLogged (userId))
                writeJSON (resp, new Pair<> (true, History.search ("SELECT * FROM history;")));
              else writeJSON (resp, new Pair<> (false, "User not logged in or not an administrator."));
              break;

            case "getTeachers":
              writeJSON (resp, Teacher.search ("SELECT * FROM teachers;"));
              break;

            case "getCourses":
              writeJSON (resp, Course.search ("SELECT * FROM courses;"));
              break;

            case "updateTeacher":
              int teacher_id = Integer.valueOf (req.getParameter ("teacher_id"));
              objId = Integer.valueOf (req.getParameter ("course_id"));
              userId = Integer.valueOf (req.getParameter ("by_user"));
              if (User.get (userId).getRole ().getId () == Role.ADMINISTRATOR && m.isLogged (userId))
                {
                  Teacher toup = Teacher.get (teacher_id);
                  toup.setCourse (objId);
                  toup.save ();
                  writeJSON (resp, new Pair<> (true, "All right"));
                }
              else writeJSON (resp, new Pair<> (false, "User not logged in or not administrator"));
              break;

            case "delTeacher":
              objId = Integer.valueOf (req.getParameter ("teacher_id"));
              userId = Integer.valueOf (req.getParameter ("by_user"));
              Teacher togo = Teacher.get (objId);
              writeJSON (resp, checkAndDelete (userId, togo));
              break;

            case "delCourse":
              objId = Integer.valueOf (req.getParameter ("course_id"));
              userId = Integer.valueOf (req.getParameter ("by_user"));
              Course todel = Course.get (objId);
              writeJSON (resp, checkAndDelete (userId, todel));
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
        int userId;

        switch (req.getParameter ("method"))
          {
            case "login":
              UserCredential u = gson.fromJson (raw_data, UserCredential.class);
              ArrayList <User> res = User.search ("SELECT * FROM `prenotazioni`.`users` WHERE " +
                  "user_name=\"" + u.getUsername () + "\" and user_password=\"" + u.getUserpass () + "\";");
              writeJSON (resp, getLoginResponse (res, u));
              break;

            case "newTeacher":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              Teacher newComer = new Gson ().fromJson (raw_data, Teacher.class);
              writeJSON (resp, checkAndSave (userId, newComer));
              break;

            case "newCourse":
              userId = Integer.valueOf (req.getParameter ("by_user"));
              Course newCourse = new Course (new Gson ().fromJson (raw_data, Course.class).getCourseTitle ());
              writeJSON (resp, checkAndSave (userId, newCourse));
              break;
          }
      }

    private <T extends BookingsImplementation> Pair<Boolean, String> checkAndDelete(int user_id, T obj)
      {
        if (User.get (user_id).getRole ().getId () == Role.ADMINISTRATOR && m.isLogged (user_id))
          {
            obj.delete ();
            return new Pair<> (true, "All right");
          }
        else
          return new Pair<> (false, "User not logged in or not an administrator.");
      }

    private <T extends BookingsImplementation> Pair<Boolean, String> checkAndSave(int user_id, T obj)
      {
        if (User.get (user_id).getRole ().getId () == Role.ADMINISTRATOR && m.isLogged (user_id))
          {
            obj.save ();
            return new Pair<> (true, new Gson ().toJson (obj));
          }
        else
          return new Pair<> (false, "User not logged in or not an administrator.");
      }

    private ArrayList <Booking> getBookingsForUser (int user_id)
      {
        return Booking.search (
            "-- tutte quelle libere\n" +
                "select b.* from bookings b\n" +
                "where \n" +
                "\tb.booking_state = 4 and \n" +
                "    b.booking_date > CURRENT_TIMESTAMP and\n" +
                "    b.booking_date not in (\n" +
                "        -- ma non mostrare le stesse date dove io ho già prenotato\n" +
                "        select b1.booking_date\n" +
                "        from bookings b1 join history h on (\n" +
                "            b1.booking_id = h.booking_id and -- cerchiamo una prenotazione\n" +
                "            h.booked_by = " + user_id + " and -- da \"me\"\n" +
                "            h.booking_state = 1 and -- che sia prenotata\n" +
                "            b1.booking_state = 1\n" +
                "        )\n" +
                "\t)"
        );
      }

    private History book (int booking_id, int user_id)
      {
        History newEntry = new History (booking_id, user_id, State.BOOKED, new Timestamp (new Date ().getTime ()));
        Booking booked = Booking.get (booking_id);
        booked.setState (State.BOOKED);
        booked.save ();
        newEntry.save ();
        return newEntry;
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
        String result = gson.toJson (something);
        if (debug) System.out.println (result);
        resp.getWriter ().write (result);
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
