/**
 * Course.java
 * Represents a university course with its code, name and credit hours.
 */

package org.example;
import java.util.List;

public class Course {
    private String name;
    private int credits;
    private String courseCode;
    private List<String> prerequisites;

    public Course(String name, int credits, String courseCode, List<String> prerequisites){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (credits != 3 && credits != 6){
            throw new IllegalArgumentException("Course must be worth 3 or 6 credit hours");
        }
        if (courseCode == null || courseCode.isBlank()){
            throw new IllegalArgumentException("CourseCode cannot be null or empty");
        }

        if (prerequisites == null){
            throw new IllegalArgumentException("Prerequisites list cannot be empty");
        }

        this.name = name;
        this.credits = credits;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
    }

    public String getCourseName(){
        return name;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public int getCreditWorth(){
        return credits;
    }

    public List<String> getPreRequisites(){
        return prerequisites;
    }
}
