package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Teacher extends BookingsImplementation
  {
    private int id;
    private String name;
    private String surname;
    private Course course;

    public Teacher (int _id)
      {
        Teacher tmp = get (_id);
        if (tmp == null) throw new IllegalStateException ("No such teacher");
        this.id = tmp.getId ();
        this.name = tmp.getName ();
        this.surname = tmp.getSurname ();
        this.course = tmp.getCourse ();

      }

    public Teacher (String name, String surname, int course_id)
      {
        this.name = name;
        this.surname = surname;
        this.course = new Course (course_id);
      }

    public Teacher (int id, String name, String surname, int course_id)
      {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.course = new Course (course_id);
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS `" + DB_NAME + "`.`teachers` ( \n" +
            "    `teacher_id` INT NOT NULL AUTO_INCREMENT , \n" +
            "    `teacher_name` TEXT NOT NULL , \n" +
            "    `teacher_surname` TEXT NOT NULL , \n" +
            "    `teacher_course` INT NOT NULL , \n" +
            "    PRIMARY KEY (`teacher_id`),\n" +
            "    FOREIGN KEY (`teacher_course`) REFERENCES `courses`(course_id)) ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`teachers`;");
      }

    public static Teacher get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`teachers` WHERE teacher_id = " + target_id + ";", Teacher::fromResultSet).get (0);
      }

    public static Teacher fromResultSet (ResultSet set)
      {
        Teacher ret = null;
        try
          {
            ret = new Teacher (
                set.getInt ("teacher_id"),
                set.getString ("teacher_name"),
                set.getString ("teacher_surname"),
                set.getInt ("teacher_course")
            );
          }
        catch (SQLException sqle)
          {
            System.err.println ("SQL Error, can't get parameters from resultset");
          }
        return ret;
      }

    public int getId ()
      {
        return id;
      }

    public String getName ()
      {
        return name;
      }

    public String getSurname ()
      {
        return surname;
      }

    public Course getCourse ()
      {
        return course;
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          exec ("UPDATE `" + DB_NAME + "`.`teachers` SET " +
              "teacher_name=\"" + name + "\"," +
              "teacher_surname=\"" + surname + "\"," +
              "teacher_course=" + getCourse ().getId () + " WHERE teacher_id = " + id + ";");
        else
          exec ("INSERT INTO `" + DB_NAME + "`.`teachers` (teacher_name, teacher_surname, teacher_course) " +
              "VALUES (\"" +
              getName () + "\", \"" +
              getSurname () + "\", \"" +
              getCourse ().getId () +
              ");");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`teachers` WHERE teacher_id=" + id + ";");
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return getId () == teacher.getId () &&
            Objects.equals (getName (), teacher.getName ()) &&
            Objects.equals (getSurname (), teacher.getSurname ()) &&
            Objects.equals (getCourse (), teacher.getCourse ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getId (), getName (), getSurname (), getCourse ());
      }
  }
