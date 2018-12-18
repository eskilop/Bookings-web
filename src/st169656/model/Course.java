package st169656.model;

import st169656.dao.BookingsImplementation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Course extends BookingsImplementation<Course>
  {
    private int id;
    private String courseTitle;

    public Course(int _id)
      {
        Course tmp = get(_id);
        if (tmp == null) throw new IllegalStateException ("No such course found");
        this.id = tmp.getId ();
        this.courseTitle = tmp.courseTitle;
      }

    public Course(int _id, String title)
      {
        this.id = _id;
        this.courseTitle = title;
      }

    public Course(String title)
      {
        this.courseTitle = title;
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
    public void create ()
      {
        exec ("CREATE TABLE " +
            "`" + DB_NAME + "`.`courses` ( " +
            "`course_id` INT NOT NULL AUTO_INCREMENT , " +
            "`course_title` INT(24) NOT NULL , " +
            "PRIMARY KEY (`course_id`)) ENGINE = InnoDB;");
      }

    @Override
    public void destroy ()
      {
        exec ("DROP TABLE `" + DB_NAME + "`.`courses`;");
      }

    @Override
    public Course get (int target_id)
      {
        return search ("SELECT * FROM `" + DB_NAME + "`.`courses` WHERE course_id = "+target_id+";").get (0);
      }

    @Override
    public void save ()
      {
        if (this.exists ())
          save ("UPDATE `" + DB_NAME + "`.`courses` SET course_title=\"" + courseTitle + "\" WHERE course_id="+id+";");
        else
          save ("INSERT INTO `"+DB_NAME+"`.`courses` (title) VALUES "+courseTitle+";");
      }

    @Override
    public boolean exists ()
      {
        return get (id) != null;
      }

    @Override
    public void delete ()
      {
        exec ("DELETE FROM `" + DB_NAME + "`.`courses` WHERE course_id = "+id+";");
      }

    @Override
    public Course createObj (ResultSet set) throws SQLException
      {
        return new Course (set.getString ("course_title"));
      }
  }
