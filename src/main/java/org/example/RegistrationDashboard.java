package org.example;

import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDashboard {
    private static int NUMBER_OF_AVAILABLE_COURSES = 0;
    Image myImage = new Image(getClass().getResourceAsStream("cool.png"));
    //private static String USER_ACCOUNTS_FILENAME = "main/java/app/UserAccounts.txt";
    private static String USER_ACCOUNTS_FILENAME = "data/UserAccounts.txt";
    private static String AVAILABLE_COURSES_FILENAME = "data/Available-Courses.txt";
    //private static String AVAILABLE_COURSES_FILENAME = "main/java/app/Available-Courses.txt";
    private static ArrayList<String> AVAILABLE_COURSES_NAME_LIST = new ArrayList<String>();
    private static ArrayList<Course> AVAILABLE_COURSES_OBJECTS_LIST = new ArrayList<Course>();
    private Aurora aurora = Aurora.getInstance();

    /**
     * Ensures there is only one instance of the Registration Dashboard.
     *
     * @return   the registrationDashboard object
     */
    private RegistrationDashboard(){}

    public static int getNumAvailableCourses(){
        return NUMBER_OF_AVAILABLE_COURSES;
    }

    public static ArrayList<String> getAvailableCourseNameList(){
        return AVAILABLE_COURSES_NAME_LIST;
    }

    /**
     * Prints a list of avalilable courses the school offers.
     * @return              true if list was shown, false otherwise.
     */
    public static String showAvailableCourses(){
        String availableCoursesInfo = "";
        if (AVAILABLE_COURSES_NAME_LIST != null){
            for (int i = 0; i < AVAILABLE_COURSES_NAME_LIST.size(); i++){
                availableCoursesInfo += (i + 1) + ". " + AVAILABLE_COURSES_NAME_LIST.get(i) + "\n";
            }
        }
        return availableCoursesInfo;
    }

    /**
     * Helper method which parses through a string and creats a course object which is stored in a list of available courses.
     *
     * @param line  A line from the file we are processing
     */
    private static void createAvailableCourseObject(String line){
        String[] splitLine = line.split(",\\s*");

        //Add the course name to our list of available course names
        String courseName = splitLine[0];
        AVAILABLE_COURSES_NAME_LIST.add(courseName);
        int courseCredit = Integer.parseInt(splitLine[1]);
        String courseCode = splitLine[2];

        List<String> coursePreRequisites = new ArrayList<String>();

        for (int i = splitLine.length - 1; i > 2; i--){
            coursePreRequisites.add(splitLine[i]);
        }

        //Add the course to our course object list
        Course newCourse = new Course(courseName, courseCredit, courseCode, coursePreRequisites);
        AVAILABLE_COURSES_OBJECTS_LIST.add(newCourse);
    }

    /**
     * Loads all available courses that a student could register for.

     * @return                true if courses are shown, otherwise false.
     */
    public static boolean loadAvailableCourses(){
        boolean successful = false;

        try(BufferedReader br = new BufferedReader(new FileReader(AVAILABLE_COURSES_FILENAME))){
            String line = br.readLine();
            int order = 1;

            while (line != null){
                createAvailableCourseObject(line);
                order++;
                successful =  true;
                line = br.readLine();
            }

            NUMBER_OF_AVAILABLE_COURSES = order;

        } catch (FileNotFoundException fnfe){
            System.out.println("File Not Found: " + fnfe.getMessage());
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

        return successful;
    }

    /**
     * Searches for the course name in the Available Course List and returns the object if found.
     *
     * @param courseName  A string which represents the name of the course that is being searched for.
     * @return                A course object if found, null otherwise.
     */
    public static Course findCourseObject(String courseName){
        Course courseFound = null;

        for (int i = 0; i < AVAILABLE_COURSES_OBJECTS_LIST.size(); i++){
            Course currentCourse = AVAILABLE_COURSES_OBJECTS_LIST.get(i);
            if (currentCourse.getCourseName().equals(courseName)){
                courseFound = currentCourse;
            }
        }

        return courseFound;
    }

    /**
     * Reads the USERACCOUNTS.TXT file and signs up past users to store their information in the database.
     * Their student object is saved in a list, so future users do not have the same username as past users.
     */
    public static void loadInPrevStudentLogIns(){
        try(BufferedReader br = new BufferedReader(new FileReader(USER_ACCOUNTS_FILENAME))){
            String currLine = br.readLine();
            while(currLine != null){
                String[] splitLine = currLine.split("\\s+");

                //We need the username and password
                if (splitLine.length > 1){
                    String username = splitLine[0];
                    String password = splitLine[1];
                    if (!username.isBlank() && !password.isBlank()){
                        //second parameter is true because we are loading in previous student logins
                        Aurora.getInstance().signUpIntoAurora(currLine, true); //Fix This. This happened bcs signUpintoAurora method was static and we just made it an instance method
                    }
                }
                currLine = br.readLine();
            }
        } catch (FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage());
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }
}