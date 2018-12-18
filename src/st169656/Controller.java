package st169656;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet
  {

    public void manage (HttpServletRequest request, HttpServletResponse response)
      {
        response.setContentType ("Text/HTML");
        try
          {
            ServletOutputStream out = response.getOutputStream ();
          }
        catch (IOException ioe)
          {

          }
      }

    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
      {
        manage (request, response);
      }

    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
      {
        manage (request, response);
      }
  }
