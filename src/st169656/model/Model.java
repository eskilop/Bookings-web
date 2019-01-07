package st169656.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;

public class Model
  {
    private static Model instance = new Model ();
    private static boolean set = false;
    private ArrayList <Integer> loggedUsers = new ArrayList <> ();

    private Model ()
      {
        if (! set)
          {
            try
              {
                DriverManager.registerDriver (new com.mysql.cj.jdbc.Driver ());
                set = true;
              }
            catch (SQLException e)
              {
                e.printStackTrace ();
              }
          }
      }

    public static Model getInstance ()
      {
        return instance;
      }

    public boolean isLogged (int user)
      {
        return loggedUsers.contains (user);
      }

    public void removeLogged (int user)
      {
        if (isLogged (user))
          loggedUsers.remove (loggedUsers.indexOf (user));
      }

    public void addLogged (int user)
      {
        if (! isLogged (user))
          loggedUsers.add (user);
      }

    public void build ()
      {
        Role.create ();
        State.create ();
        Course.create ();

        User.create ();
        Teacher.create ();

        Booking.create ();
        History.create ();
      }

    public void destroy ()
      {
        History.destroy ();
        Booking.destroy ();

        Teacher.destroy ();
        User.destroy ();

        Role.destroy ();
        Course.destroy ();
        State.destroy ();
      }

    public void setupForSimulation ()
      {
        build ();

        Role admin = new Role (Role.ADMINISTRATOR, "Administrator");
        admin.save ();
        Role client = new Role (Role.CLIENT, "Client");
        client.save ();

        // Demo purposes only, clearly unsecured
        String samplepwd = strengthen ("sample");

        User regusr1 = new User ("Jack", samplepwd, client.getId ());
        regusr1.save ();
        User regusr2 = new User ("John", samplepwd, client.getId ());
        regusr2.save ();
        User administrator = new User ("Officer", samplepwd, admin.getId ());
        administrator.save ();

        Course dm = new Course ("Discrete Math");
        dm.save ();
        Course psy = new Course ("Psychology");
        psy.save ();
        Course cri = new Course ("Criminology");
        cri.save ();
        Course neuro = new Course ("Neuroscience");
        neuro.save ();
        Course gene = new Course ("Genetics");
        gene.save ();

        State booked = new State (State.BOOKED, "Booked");
        State unavailable = new State (State.UNAVAILABLE, "Unavailable");
        State available = new State (State.AVAILABLE, "Available");
        State cancelled = new State (State.CANCELLED, "Cancelled");
        booked.save ();
        unavailable.save ();
        available.save ();
        cancelled.save ();


        ArrayList <Teacher> teachers = new ArrayList <> ();

        Teacher t1 = new Teacher ("Joseph", "Mundani", dm.getId ());
        t1.save ();
        teachers.add (t1);
        Teacher t2 = new Teacher ("Angelica", "Nash", psy.getId ());
        t2.save ();
        teachers.add (t2);
        Teacher t3 = new Teacher ("Arthur", "Manslaughter", cri.getId ());
        t3.save ();
        teachers.add (t3);
        Teacher t4 = new Teacher ("Clara", "Nicolelis", neuro.getId ());
        t4.save ();
        teachers.add (t4);
        Teacher t5 = new Teacher ("Leonard", "Mendel", gene.getId ());
        t5.save ();
        teachers.add (t5);


        Booking b;

        // Assume all teachers in all slots
        for (Teacher t : teachers)
          {
            int today = Calendar.getInstance ().get (Calendar.DAY_OF_MONTH);
            int todayM = Calendar.getInstance ().get (Calendar.MONTH) + 1;
            int todayY = Calendar.getInstance ().get (Calendar.YEAR);

            // Get the number of days in that month
            YearMonth yearMonthObject = YearMonth.of (todayY, todayM);
            int maxdays = yearMonthObject.lengthOfMonth ();
            for (int i = 0; i <= 5; i++)
              {
                if (today + 1 > maxdays)
                  {
                    today = (today + 1) % maxdays;
                    if (todayM + 1 > 12)
                      {
                        todayM = (todayM + 1) % 12;
                        todayY = todayY + 1;
                      }
                  }
                else today += 1;

                b = new Booking (t.getId (), Timestamp.valueOf (todayY + "-" + todayM + "-" + today + " " + "15:00:00"), State.AVAILABLE);
                System.out.println (b);
                b.save ();
                b = new Booking (t.getId (), Timestamp.valueOf (todayY + "-" + todayM + "-" + today + " " + "16:00:00"), State.AVAILABLE);
                System.out.println (b);
                b.save ();
                b = new Booking (t.getId (), Timestamp.valueOf (todayY + "-" + todayM + "-" + today + " " + "17:00:00"), State.AVAILABLE);
                System.out.println (b);
                b.save ();
                b = new Booking (t.getId (), Timestamp.valueOf (todayY + "-" + todayM + "-" + today + " " + "18:00:00"), State.AVAILABLE);
                System.out.println (b);
                b.save ();
                b = new Booking (t.getId (), Timestamp.valueOf (todayY + "-" + todayM + "-" + today + " " + "19:00:00"), State.AVAILABLE);
                System.out.println (b);
                b.save ();
              }
          }
      }

    public String strengthen (String password)
      {
        return hash (salt (password, "NaCl"));
      }

    private String salt (String word, String salt)
      {
        int oldcntr = 0;
        String saltedword = "";
        for (int i = 3; i < word.length (); i += 3, oldcntr += 3)
          {
            saltedword += word.substring (oldcntr, i);
            saltedword += salt;
          }
        return saltedword;
      }

    private String hash (String str)
      {
        MessageDigest md = null;
        try
          {
            md = MessageDigest.getInstance ("MD5");

          }
        catch (NoSuchAlgorithmException nsae)
          {
            System.err.println ("Cannot encode password securely");
            nsae.printStackTrace ();
            // we should change algorithm, but that's out of the scope
            // of the project, furthermore, it's a server problem
            // in which case we could just change this code to fit our needs
          }
        byte[] bts = md.digest (str.getBytes (StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer ();
        for (byte bt : bts)
          {
            sb.append (Integer.toHexString ((bt & 0xFF) | 0x100).substring (1, 3));
          }
        return sb.toString ();
      }
  }
