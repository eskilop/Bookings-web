package st169656;

import com.google.gson.Gson;
import javafx.util.Pair;
import st169656.model.Booking;
import st169656.model.Model;
import st169656.model.User;
import st169656.model.wrappers.UserCredential;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ApiServlet extends HttpServlet
  {

    /*
     * ApiServlet, it's a servlet with the only task to manage
     * client model requests. Its purpose is to get and forward
     * data from the RDBMS.
     */

    Gson gson = null;
    Model m = Model.getInstance ();

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.setHeader ("Access-Control-Allow-Origin", "*");
        String method = req.getParameter ("method");
        gson = new Gson ();

        switch (method)
          {
            case "getBookings":
              resp.getWriter ().write (gson.toJson (Booking.search ("SELECT * FROM `prenotazioni`.`bookings`;")));
              break;
          }
      }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.addHeader ("Access-Control-Allow-Origin", "*");

        gson = new Gson ();

        String raw_data = getDataFromRequest (req);

        switch (req.getParameter ("method"))
          {
            case "login":

              UserCredential u = gson.fromJson (raw_data, UserCredential.class);

              ArrayList <User> res = User.search ("SELECT * FROM `prenotazioni`.`users` WHERE " +
                  "user_name=\"" + u.getUsername () + "\" and user_password=\"" + u.getUserpass () + "\";");

              if (res.size () == 1)
                {
                  if (res.get (0).getUsername ().equals (u.getUsername ()) && res.get (0).getPassword ().equals (u.getUserpass ()))
                    resp.getWriter ().write (gson.toJson (res.get (0)));
                  else
                    resp.getWriter ().write (gson.toJson (new Pair <Boolean, String> (false, "Not equal")));
                }
              else
                resp.getWriter ().write (gson.toJson (new Pair <Boolean, String> (false, "Size mismatch")));
              break;

            default:
              System.out.println ("CRISTO");
              break;
          }
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
  }
