package org.example;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StudentPlan {
    private Set<Course> currentCourses;
    private Set<Course> completedCourses;
    private Student student;

    public StudentPlan(Student student){
        this.student = student;
        currentCourses = new HashSet<Course>();
        completedCourses = new HashSet<Course>();
    }

    /**
     * Adds the course to the students Set of currentCourses
     *
     * @param course  The course object to be added to the Set.
     * @return                true if the course is sucessfully aded, otherwise false.
     */
    public boolean addToCurrentCourses(Course course){
        boolean added = false;
        if (course != null){
            currentCourses.add(course);
            added = true;
        }
        return added;
    }

    /**
     * Adds the course to the students Set of completedCourses
     *
     * @param course  The course object to be added to the Set.
     * @return                true if the course is sucessfully aded, otherwise false.
     */
    public boolean addToCompletedCourses(Course course){
        boolean added = false;
        if (course != null){
            completedCourses.add(course);
            added = true;
        }
        return added;
    }
    /**
     * Checks if the current student has completed the course.
     *
     * @param name  A string which represents the name of the course to check
     * @return                True if the course has been completed by the student, false otherwise.
     */
    public boolean isCourseCompleted(String name){
        boolean completed = false;
        for (Course course : completedCourses){
            if (course.getCourseName().equals(name)){
                System.out.println("This course has been completed\n");
                completed = true;
            }
        }

        return completed;
    }

    /**
     * Checks if the student is currently taking the course.
     *
     * @param name  A string which represents the name of the course to check
     * @return                True if the student is taking the course currently, false otherwise.
     */
    public boolean aCurrentCourse(String name){
        boolean takingCurrently = false;
        for (Course course : currentCourses){
            if (course != null && course.getCourseName().equals(name)){
                takingCurrently = true;
            }
        }

        return takingCurrently;
    }

    /**
     * Removes the specified course a student is taking.
     *
     * @param courseName  A string which represents the name of the course to remove
     * @return                True if the course is removed, false otherwise.
     */
    public boolean removeCurrentCourse(String courseName){
        boolean successfulRemoval = false;
        Iterator<Course> currentCoursesIterator = currentCourses.iterator();

        while (currentCoursesIterator.hasNext()){
            Course course = currentCoursesIterator.next();
            if (course.getCourseName().equals(courseName)){
                currentCoursesIterator.remove();
                successfulRemoval = true;
            }
        }

        return successfulRemoval;
    }

    /**
     * Prints the student's current course information, which includes their list of current courses and completed courses.

     * @return                a String that represents a list of current courses of a student.
     */
    public String printStudentCurrentCourseInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Course> currentCoursesIterator = currentCourses.iterator();
        stringBuilder.append("Current Courses:\n");

        while (currentCoursesIterator.hasNext()){
            stringBuilder.append(currentCoursesIterator.next().getCourseName());
            if (currentCoursesIterator.hasNext()){
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Prints the student's completed course information

     * @return                a String that represents completed courses of a student.
     */
    public String printStudentCompletedCourseInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Course> completedCoursesIterator = completedCourses.iterator();
        stringBuilder.append("Completed Courses:\n");

        while (completedCoursesIterator.hasNext()){
            stringBuilder.append(completedCoursesIterator.next().getCourseName());
            if (completedCoursesIterator.hasNext()){
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public Set<Course> getCurrentCourses(){
        return currentCourses;
    }

    public Set<Course> getCompletedCourses(){
        return completedCourses;
    }
}
