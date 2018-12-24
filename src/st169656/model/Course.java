package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Course extends BookingsImplementation
  {
    private int id;
    private String courseTitle;

    public Course (int _id, String title)
      {
        this.id = _id;
        this.courseTitle = title;
      }

    public Course (String title)
      {
        this.id = solveId ("course_id", "courses");
        this.courseTitle = title;
      }

    public static Course fromResultSet (ResultSet set)
      {
        Course ret = null;
        try
          {
            ret = new Course (set.getInt ("course_id"), set.getString ("course_title"));
          }
        catch (SQLException sqle)
          {
            System.err.println ("SQL Error, can't get parameters from resultset");
          }
        return ret;
      }

    public static void create ()
      {
        exec ("CREATE TABLE IF NOT EXISTS " +
            "`" + DB_NAME + "`.`courses` ( " +
            "`course_id` INT NOT NULL AUTO_INCREMENT , " +
            "`course_title` VARCHAR(24) NOT NULL , " +
            "PRIMARY KEY (`course_id`)) ENGINE = InnoDB;");
      }

    public static void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`courses`;");
      }

    public static Course get (int target_id)
      {
        ArrayList <Course> courses = search ("SELECT * FROM `" + DB_NAME + "`.`courses` WHERE course_id = " + target_id + ";");
        if (courses.size () < 1)
          return null;
        else
          return courses.get (0);
      }

    public int getId ()
      {
        return id;
      }

    public String getCourseTitle ()
      {
        return courseTitle;
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`courses` SET course_title=\"" + courseTitle + "\" WHERE course_id=" + id + ";");
        else
          save ("INSERT INTO `" + DB_NAME + "`.`courses` (course_id, course_title) VALUES (" + id + ", \"" + courseTitle + "\");");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    public static ArrayList<Course> search(String search)
      {
        return search (search, Course::fromResultSet);
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`courses` WHERE course_id = " + id + ";");
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof Course)) return false;
        Course course = (Course) o;
        return getId () == course.getId () &&
            Objects.equals (getCourseTitle (), course.getCourseTitle ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getId (), getCourseTitle ());
      }

    @Override
    public String toString ()
      {
        return "Course{" +
            "id=" + id +
            ", courseTitle='" + courseTitle + '\'' +
            '}';
      }
  }
