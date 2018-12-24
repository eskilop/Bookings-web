package st169656;

import com.google.gson.Gson;
import st169656.dao.BookingsImplementation;
import st169656.model.Booking;
import st169656.model.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApiServlet extends HttpServlet
  {

    /*
     * ApiServlet, it's a servlet with the only task to manage
     * client model requests. Its purpose is to get and forward
     * data from the RDBMS.
     */

    Gson gson = null;
    Model m = Model.getInstance();

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
      {
        resp.setContentType ("application/json");
        resp.setHeader("Access-Control-Allow-Origin","*");
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
        // manages logins / registrations.
        // Username must go into session. managed through angular on frontend.
      }
  }
