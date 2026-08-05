package org.example;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import jdk.jshell.spi.ExecutionControlProvider;

import javafx.event.ActionEvent;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Set;
import java.io.FileNotFoundException;

public class Aurora {
    private static int REGISTER_FOR_COURSES_OPTION = 1;
    private static int DISPLAY_CURRENT_COURSES_OPTION = 2;
    private static int DISPLAY_COMPLETED_COURSES_OPTION = 3;
    private static int MARK_COURSE_AS_COMPLETED_OPTION = 4;
    private static int EXIT_AURORA_OPTION  = 5;
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<String> allLogIns = new ArrayList<String>();
    private static String USER_ACCOUNTS_FILENAME = "data/UserAccounts.txt";
    private static String askIfUserHasAccAlready = "AskIfUserHasAccountAlready";
    private static String gettingUserCredentials = "UserCredentials";
    private static String exitInputingUserAndPassword = "ExitEnteringUserAndPassword";
    private String incorrectUserCredentials = "Retry. Type in your username and password, seperated by a blank space(s) or -1 to Exit";
    private boolean isInformLabelTransitioning = false;
    private static String goBackInput = "GoBack";
    private boolean userChoseLogIn = true;
    private ApplicationDisplay applicationDisplay;
    private Aurora auroraDisplay;
    private static Aurora instance;
    private static Student[] allStudents;
    private static String currentUserName;
    private static String currentPassword;

    @FXML
    private TextField userInput;
    @FXML
    private Button defaultPageEnterButton;
    @FXML
    private AnchorPane welcomePane;
    @FXML
    private AnchorPane registerCoursesPane;
    @FXML
    private Label coursesAvailable;
    @FXML
    private TextField userCourseChoice;
    @FXML
    private Button courseRegisterButton;
    @FXML
    private Button registerCourseBackButton;
    @FXML
    private Label registerInformLabel;


    public Aurora(){}

    public static Aurora getInstance(){
        if (instance == null){
            instance = new Aurora();
        }
        return instance;
    }
    /**
     * Combines the user's username and password into one string and searches the arraylist to check if it
     * contains the logIn info. If not, it informs the user to sign up with a new account instead.
     *
     * @param logInInfo  The user's log-in information that includes username and password
     * @param loadingInLogIns  a boolean value that informs whether or not we are loading in previous login information
     * @return                true if a successful login, otherwise false.
     */
    public boolean logIntoAurora(String logInInfo, boolean loadingInLogIns){
        boolean logInSuccessful = false;

        if (allLogIns != null && (logInInfo != null && !logInInfo.isBlank())){
            logInSuccessful = allLogIns.contains(logInInfo);
            String[] splitLogInInfo = logInInfo.split("\\s+");

            if ((splitLogInInfo.length == 2) && logInSuccessful){
                if (!loadingInLogIns){
                    currentUserName = splitLogInInfo[0];
                    currentPassword = splitLogInInfo[1];
                    applicationDisplay.updateInformLabel("Successful LogIn!");
                    applicationDisplay.showScreen();
                    welcomePane.setVisible(true);
                    welcomePane.setManaged(true);
                }
            } else {
                System.out.println("Incorrect username or password.");
                applicationDisplay.updateInformLabel("Incorrect username or password.");
            }

        }

        return logInSuccessful;
    }

    /**
     * Checks if a username already exists
     *
     * @param signUpInfo  The user's login information.
     * @return                true if the username already exists, otherwise false.
     */
    private static boolean userNameExists(String signUpInfo){
        boolean userExists = false;
        String givenUsername = signUpInfo.split("\\s+")[0];

        for(int i = 0; i < allLogIns.size(); i++){
            String usernameInList = allLogIns.get(i).split("\\s+")[0];
            if (givenUsername.equals(usernameInList)){
                userExists = true;
            }
        }
        return userExists;
    }

    /**
     * A helper method that aids the sign up process for a user by creating the current and completed course objects from their data in UserAccounts.
     *
     * @param signUpInfo  The user's login information.
     * @param newStudentsPlan  A student plan for the new student.
     */
    private static void signUpUser(String signUpInfo, StudentPlan newStudentsPlan){
        String[] splitCoursesLine = signUpInfo.split("\\[");
        String[] currCoursesList = splitCoursesLine[1].split(", ");
        String[] completedCoursesList = splitCoursesLine[2].split(", ");

        currCoursesToAdd(currCoursesList, newStudentsPlan);
        completedCoursesToAdd(completedCoursesList, newStudentsPlan);
    }

    /**
     * The current course objects that need to be created for the student and added to their student plan.
     *
     * @param currCoursesList A list of the names of the current courses.
     * @param newStudentsPlan  A student plan for the new student.
     */
    private static void currCoursesToAdd(String[] currCoursesList, StudentPlan newStudentsPlan){
        for (int i = 0; i < currCoursesList.length; i++){
            String courseToAdd = currCoursesList[i];
            //if the course is the last course then we need to make sure to not add any text after ]
            if (i == currCoursesList.length - 1){
                courseToAdd = currCoursesList[i].substring(0, currCoursesList[i].indexOf("]"));
            }
            if (!courseToAdd.isEmpty()){
                newStudentsPlan.addToCurrentCourses(RegistrationDashboard.findCourseObject(courseToAdd));
            }
        }
    }

    /**
     * The completed course objects that need to be created for the student and added to their student plan.
     *
     * @param completedCoursesList A list of the names of the completed courses.
     * @param newStudentsPlan  A student plan for the new student.
     */
    private static void completedCoursesToAdd(String[] completedCoursesList, StudentPlan newStudentsPlan){

        for (int i = 0; i < completedCoursesList.length; i++){
            String courseToAdd = completedCoursesList[i];
            //if the course is the last course then we need to make sure to not add any text after ]
            if (i == completedCoursesList.length - 1){
                courseToAdd = completedCoursesList[i].substring(0, completedCoursesList[i].indexOf("]"));
            }
            if (!courseToAdd.isEmpty()){
                newStudentsPlan.addToCompletedCourses(RegistrationDashboard.findCourseObject(courseToAdd));
            }
        }
    }

    /**
     * A helper method that makes the student object with the given parameters. It adds their login information to the list of all login informations.
     * Adds the new student object to the list of all student objects, and adds the student information as a new line in UserAccounts.txt.
     *
     * @param loadingInLogIns   true if loading in past logins. False otherwise.
     * @param splitSignUpInfo   The students sign up information including their username and password.
     * @param newStudent   the student object that is being updated.
     * @return                always returns true.
     */
    private static boolean makeStudentObject(boolean loadingInLogIns, String[] splitSignUpInfo, Student newStudent){
        boolean newUserCreated = true;

        String onlyLogInInfo = splitSignUpInfo[0] + " " + splitSignUpInfo[1];
        allLogIns.add(onlyLogInInfo);

        if (allStudents == null){
            allStudents = new Student[0];
        }

        Student[] tempAllStudentsArray = new Student[allStudents.length + 1];
        for (int i = 0; i < allStudents.length; i++){
            tempAllStudentsArray[i] = allStudents[i];
        }

        tempAllStudentsArray[tempAllStudentsArray.length - 1] = newStudent;
        allStudents = tempAllStudentsArray;
        newUserCreated = true;
        currentUserName = splitSignUpInfo[0];
        currentPassword = splitSignUpInfo[1];
        ArrayList<String> updatedLines = new ArrayList<String>();

        if (!loadingInLogIns) {
            //add the user to UserAccounts.txt
            readUserAccounts(updatedLines);
            updatedLines.add(currentUserName + " " + currentPassword + " CurrentCourses:[] CompletedCourses:[]");
            writeToUserAccounts(updatedLines);
        }
        System.out.println(); //just to add whitespace to make output cleaner.
        return newUserCreated;
    }

    /**
     * Combines the user's username and password into one string, seperated by a space and searches the arraylist to check if it
     * contains the signUp info. If so, it informs the user to log in with the information instead.
     *
     * @param signUpInfo  The user's sign-in information that includes username and password
     * @param loadingInLogIns  a boolean value that informs whether or not we are loading in previous login information
     * @return                true if a successful signup, otherwise false.
     */
    public boolean signUpIntoAurora(String signUpInfo,  boolean loadingInLogIns){
        boolean newUserCreated = false;

        if (allLogIns != null && (signUpInfo != null && (!signUpInfo.isBlank()) && !userNameExists(signUpInfo))){
            String[] splitSignUpInfo = signUpInfo.split("\\s+");

            if (splitSignUpInfo.length >= 2){
                Student newStudent = new Student(splitSignUpInfo[0], splitSignUpInfo[1]);
                StudentPlan newStudentsPlan = newStudent.getStudentPlan();

                if (!loadingInLogIns){
                    if (splitSignUpInfo.length == 2){
                        currentUserName = splitSignUpInfo[0];
                        currentPassword = splitSignUpInfo[1];
                    }
                } else {
                    signUpUser(signUpInfo, newStudentsPlan);
                }

                int allowedInfoCount = 2;
                //It should be two objects in the array. The first is the username, second is their password
                //If we're loading in logins, then there will be multiple spaces due to all the info in UserAccounts
                //However, if they are just signing up, there should only be 1 chunk of space in between text.
                if ((loadingInLogIns && splitSignUpInfo.length >= allowedInfoCount) || (!loadingInLogIns && splitSignUpInfo.length == 2)){
                    newUserCreated = makeStudentObject(loadingInLogIns, splitSignUpInfo, newStudent);
                    if (!loadingInLogIns) {
                        applicationDisplay.updateInformLabel("Successful Sign Up!");
                        applicationDisplay.showScreen();
                        welcomePane.setVisible(true);
                        welcomePane.setManaged(true);
                    }
                } else {
                    System.out.println("Incorrect username or password.");
                }
            }
        }
        return newUserCreated;
    }

    /**
     * A helper method that welcomes the user to Aurora and shows options to choose from.
     */
    private static void landingPage(){
        System.out.println("Welcome to Aurora, " + currentUserName + "!");
        System.out.println("1. Register For Courses");
        System.out.println("2. Display Current Courses");
        System.out.println("3. Display Completed Courses");
        System.out.println("4. Mark Course As Completed");
        System.out.println("5. Log Out\n");
    }

    /**
     * A helper method that updates the user current courses and completed courses to file
     * and takes the user back to the signup/login page.
     */
    private void logUserOut(){
        //Write to their currCourses and completedCourses in file
        ArrayList<String> updatedLines = new ArrayList<String>();
        readUserAccounts(updatedLines);
        writeToUserAccounts(updatedLines);

        boolean successfulLogInOrSignUp = askUserToSignUpOrLogIn();
        while (!successfulLogInOrSignUp) { //while they don't sign up/log in successfully
            successfulLogInOrSignUp = askUserToSignUpOrLogIn();
        }
        displayAuroraOptions();
    }

    /**
     * Displays the different actions that can occur on Aurora. Decisions such as registering for a course, displaying a user's current courses,
     * displaying completed courses, and logging out.
     */
    public void displayAuroraOptions(){
        landingPage();

        int chosenOption = getValidIntegerChoice(EXIT_AURORA_OPTION);
        System.out.println();

        if (chosenOption == REGISTER_FOR_COURSES_OPTION){
            String availableCoursesString = RegistrationDashboard.showAvailableCourses();
            if (availableCoursesString != null){
                //registerForCoursesUI();
            }
        } else if (chosenOption == DISPLAY_CURRENT_COURSES_OPTION){
            displayCurrentCourses(currentUserName, currentPassword);
        } else if (chosenOption == DISPLAY_COMPLETED_COURSES_OPTION){
            displayCompletedCourses(currentUserName, currentPassword);
        } else if (chosenOption == MARK_COURSE_AS_COMPLETED_OPTION){
            markCourseCompleted();
        }
        else if (chosenOption == EXIT_AURORA_OPTION){
            System.out.println("Logging off...");
            logUserOut();
        }

        if (chosenOption != EXIT_AURORA_OPTION){
            displayAuroraOptions();
        }
    }

    /**
     * A helper method that updates the user's line in UserAccount.txt
     * @param updatedLines  An arraylist that contains the user's information as strings.
     */
    private static void readUserAccounts(ArrayList<String> updatedLines){
        try(BufferedReader br = new BufferedReader(new FileReader(USER_ACCOUNTS_FILENAME))){
            String line = br.readLine();

            while(line != null){
                String[] splitLine = line.split("\\s+");
                //add the students username and password to the line
                String fullLine = line.substring(0, line.indexOf("[") + 1);
                String currCoursesStringHold = "";
                //CurrentCourses to add
                fullLine += currCoursesStringHold + addCurrCoursesToUserAccountsDoc(splitLine) + "] CompletedCourses:[";
                //Do the same for completed too
                fullLine += addCompletedCoursesToUserAccountsDoc(splitLine) + "]";
                updatedLines.add(fullLine);
                line = br.readLine();
            }
        } catch (FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * A helper method that loops through the user's course objects, and adds the course names to a string.
     *
     * @param splitLine a String array that splits the user's information from UserAccounts.txt
     * @return                a string of current courses to update the user's line in UserAccounts.txt
     */
    private static String addCurrCoursesToUserAccountsDoc(String[] splitLine){
        String fullLine = "";
        Set<Course> currStudentCurrentCourses = findStudentByName(splitLine[0], splitLine[1]).getStudentPlan().getCurrentCourses();
        int countSize = 0;
        for (Course course : currStudentCurrentCourses){
            fullLine += course.getCourseName();

            if (countSize < currStudentCurrentCourses.size() - 1){
                fullLine += ", ";
            }

            countSize++;
        }

        return fullLine;
    }

    /**
     * A helper method that loops through the user's course objects, and adds the course names to a string.
     *
     * @param splitLine a String array that splits the user's information from UserAccounts.txt
     * @return                a string of completed courses to update the user's line in UserAccounts.txt
     */
    private static String addCompletedCoursesToUserAccountsDoc(String[] splitLine){
        String fullLine = "";
        Set<Course> currStudentCompletedCourses = findStudentByName(splitLine[0], splitLine[1]).getStudentPlan().getCompletedCourses();
        int countSize = 0;
        for (Course course : currStudentCompletedCourses){
            fullLine += course.getCourseName();

            if (countSize < currStudentCompletedCourses.size() - 1){
                fullLine += ", ";
            }

            countSize++;
        }

        return fullLine;
    }

    /**
     * A helper method that updates the file UserAccounts.txt with the updated line.
     *
     * @param updatedLines a String arraylist to be used for updating the file.
     */
    private static void writeToUserAccounts(ArrayList<String> updatedLines){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(USER_ACCOUNTS_FILENAME))){
            for (String line : updatedLines){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }

    /**
     * When user enters "-1", exit the prompt that asks for their username.
     *
     * @param userInfo  The user's sign-in information that consists of their username and password
     * @return                true if user typed "-1", otherwise false.
     */
    public static boolean exitEnteringUserName(String userInfo){
        boolean exiting = false;
        try{
            int num = Integer.parseInt(userInfo);
            if (num == -1){
                exiting = true;
            }
        } catch (NumberFormatException nfe){}

        return exiting;
    }

    /**
     * A helper method which asks the user if they already own an account
     *
     * @param userChoice  The user's choice. If Y, then takes them to log in. If No, then takes user to sign up.
     */
    private static void askAccountOwnership(String userChoice){
        //System.out.println("invalid input\n");
        //System.out.println("Do you already have an account? (Y/N)");
    }

    public void setUpUI(){
        welcomePane.setVisible(false);
        welcomePane.setManaged(false);

        registerCoursesPane.setManaged(false);
        registerCoursesPane.setVisible(false);
    }

    public void registerForCoursesUI(){
        welcomePane.setVisible(false);
        welcomePane.setManaged(false);

        registerCoursesPane.setManaged(true);
        registerCoursesPane.setVisible(true);
    }

    public void showWelcomePane(){
        welcomePane.setVisible(true);
        welcomePane.setManaged(true);
    }
    @FXML
    public void courseRegisterButtonClick(){
        String courseName = userCourseChoice.getText();
        registerForCourses(courseName);
    }

    @FXML
    public void registerCourseButtonClick(){
        System.out.println("is it printed  yet?");
        String availableCoursesString = RegistrationDashboard.showAvailableCourses();
        if (availableCoursesString != null){
            coursesAvailable.setText("Enter the course name:\n" + availableCoursesString);
        } else {
            System.out.println("it's null");
        }
        registerForCoursesUI();
    }
    /**
     * A helper method that keeps askign the user for their username and password, unless they enter "-1".
     *
     * @param successfulLogIn   A boolean that represents if the user log in was successful.
     * @param goBack    True if user entered "-1", False otherwise.
     * @return                true if user entered "-1", False otherwise.
     */
    private boolean startTryingToLogIn(boolean successfulLogIn, boolean goBack){
        while(!successfulLogIn && !goBack){
            System.out.println("Invalid Log-In Information.\n");
            System.out.println("Type in your username and password, seperated by a blank space(s) or -1 to Exit");
            String userInfo = getValidStringInput();
            if (!exitEnteringUserName(userInfo)){
                successfulLogIn = logIntoAurora(userInfo, false);

            } else {
                goBack = true;
            }
        }
        return goBack;
    }

    /**
     * A helper method which informs the user of invalid sign up information
     */
    private static void informInvalidSignUpInfo(){
        System.out.println("Username already exists or invalid sign-up information.\n");
        System.out.println("Type in your username and password, seperated by a blank space(s) or -1 to Exit");
    }

    public void handleInput(String input, String reasonForInput) {
        if (reasonForInput.equals(askIfUserHasAccAlready)){
            if (input.equals("Y")){
                applicationDisplay.updateInformLabel("Type in your username and password, seperated by a blank space(s) or -1 to Exit");
                userChoseLogIn = true;
            } else if (input.equals("N")){
                applicationDisplay.updateInformLabel("Type in your username and password, seperated by a blank space(s) or -1 to Exit");
                userChoseLogIn = false;
            } else {
                applicationDisplay.updateInformLabel("invalid input\nDo you already have an account? (Y/N)");
            }
        } else if (reasonForInput.equals(gettingUserCredentials)){

            if (userChoseLogIn){
                if (!logIntoAurora(applicationDisplay.getUserInputInfo(), false)){
                    applicationDisplay.updateInformLabel("Retry. Type in your username and password, seperated by a blank space(s) or -1 to Exit");
                }
            } else {
                System.out.println("User tries to sign up");
                if (!signUpIntoAurora(applicationDisplay.getUserInputInfo(), false)){
                    applicationDisplay.updateInformLabel("Retry. Type in your username and password, seperated by a blank space(s) or -1 to Exit");
                }
            }
        }
        else if (input.trim().equals("-1") && (reasonForInput.equals(exitInputingUserAndPassword))){

            //They want to go back
            applicationDisplay.updateInformLabel("Do you already have an account? (Y/N)");
        }
    }

    public void setApplicationDisplay(ApplicationDisplay display){
        this.applicationDisplay = display;
    }

    public ApplicationDisplay getApplicationDisplay(){
        return applicationDisplay;
    }

    public void setAuroraDisplay(Aurora display){
        auroraDisplay = display;
    }

    public Aurora getAuroraDisplay(){
        return auroraDisplay;
    }

    public void uIDisplayAvailableCourses(){

    }
    /**
     * Asks the user to sign up or log into Aurora. Calls other methods to validate the user's information.
     * Accesses their data from previously using the site if applicable.
     */
    public boolean askUserToSignUpOrLogIn(){
        //System.out.println("Log In/Sign Up Page:\n" + "Do you already have an account? (Y/N)");
        String userChoice = getValidStringBoxInput();
        handleInput(userChoice, askIfUserHasAccAlready);
        boolean successfulLogIn = false;
        boolean successfulSignUp = false;

        while (!(userChoice.equals("Y")) && !(userChoice.equals("N"))){
            askAccountOwnership(userChoice);
            userChoice = getValidStringBoxInput();
        }

        //System.out.println("Type in your username and password, seperated by a blank space(s) or -1 to Exit");
        handleInput(userChoice, gettingUserCredentials);

        String userInfo = getValidStringBoxInput();
        boolean goBack = false;
        if (exitEnteringUserName(userInfo)){ //check if they want to go back to log in/signup page
            handleInput(userInfo, goBackInput);
        } else if (userChoice.equals("Y")){ //verify their log in
            successfulLogIn = logIntoAurora(userInfo, false);
            userChoseLogIn = true;

            if (!successfulLogIn){
                if (!startTryingToLogIn(successfulLogIn, goBack)){ //if they're not going back. (so they logged in)
                    successfulLogIn = true;
                    System.out.println("The logged in");
                }
            }

        } else if (userChoice.equals("N")){ //ask them to sign up
            userChoseLogIn = false;
            successfulSignUp = signUpIntoAurora(userInfo, false);
            while(!successfulSignUp && !goBack){
                informInvalidSignUpInfo();
                userInfo = getValidStringInput();
                if (!exitEnteringUserName(userInfo)){
                    successfulSignUp = signUpIntoAurora(userInfo, false);
                } else {
                    goBack = true;
                }
            }
        }

        return successfulLogIn || successfulSignUp;
    }

    /**
     * Returns the student object that matches the same student name. Does not account for same student names
     *
     * @param studentName  The name of the student to be found
     * @param studentPassword  The password of the student to be found
     * @return                the student object if found, otherwise null
     */
    public static Student findStudentByName(String studentName, String studentPassword){
        Student studentFound = null;

        for (int i = 0; i < allStudents.length; i++){
            String currStudentName = allStudents[i].getStudentName();
            String currStudentPassword = allStudents[i].getStudentPassword();

            if (currStudentName.equals(studentName) && currStudentPassword.equals(studentPassword)){
                studentFound = allStudents[i];
            }
        }

        return studentFound;
    }
    @FXML
    public void backButtonPressed(ActionEvent event){
        Button buttonClicked = (Button) event.getSource();

        if (buttonClicked == registerCourseBackButton){ //if the same object in memory reference
            hideRegisterCoursesPane();
        }
    }
    private void hideRegisterCoursesPane(){
        registerCoursesPane.setVisible(false);
        registerCoursesPane.setManaged(false);
        showWelcomePane();
    }

    private void labelDisappear(Label label){
        registerInformLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        courseRegisterButton.setDisable(true);
        pause.setOnFinished(event -> {
            label.setVisible(false);
            if (registerInformLabel.getText().contains("successful")){
                //switch back to welcome to aurora screen
                hideRegisterCoursesPane();
            }
            courseRegisterButton.setDisable(false);
        });

        pause.play();
    }
    /**
     * Registers the user into a course. Adds the course to their list of current courses, and takes the course
     * of the list of available courses for the user.
     */
    public void registerForCourses(String courseNameChosen){
        //registerForCoursesUI();
        int verifyCourseName = RegistrationDashboard.getAvailableCourseNameList().indexOf(courseNameChosen);

        if (verifyCourseName != -1) {
            boolean completed = findStudentByName(currentUserName, currentPassword).getStudentPlan().isCourseCompleted(courseNameChosen);
            boolean takingCurrently = findStudentByName(currentUserName, currentPassword).getStudentPlan().aCurrentCourse(courseNameChosen);

            //check if the student has completed this course already and not currently taking the course
            if (!completed && !takingCurrently) {
                StudentPlan studentPlan = findStudentByName(currentUserName, currentPassword).getStudentPlan();
                if (studentPlan.addToCurrentCourses(RegistrationDashboard.findCourseObject(courseNameChosen))) {
                    ArrayList<String> updatedLines = new ArrayList<String>();
                    readUserAccounts(updatedLines);
                    writeToUserAccounts(updatedLines);
                }
                registerInformLabel.setText("You have successfully registered for " + userCourseChoice.getText());
            } else if (takingCurrently) {
                registerInformLabel.setText("You are currently registered for " + userCourseChoice.getText());
            } else {
                registerInformLabel.setText("This course has been completed");
            }
        } else {
            //inform the user if we're not already telling them to try again.
            registerInformLabel.setText("That Course Is Currently Not Offered. Try Again.");
        }
        labelDisappear(registerInformLabel);
    }

    /**
     * Displays all the courses the student is taking in a list format.
     * @param studentName  The name of the student whose course list we want to access.
     * @param studentPassword  The password of the student whose course list we want to access.
     */
    public static void displayCurrentCourses(String studentName, String studentPassword){
        StudentPlan studentPlan = findStudentByName(studentName, studentPassword).getStudentPlan();
        System.out.println(studentPlan.printStudentCurrentCourseInfo());
    }

    /**
     * Displays all the courses the student has completed in a list format. This data is saved on the user's account, so they can logout
     * and still have access to their information.
     * @param studentName  The name of the student whose course list we want to access.
     * @param studentPassword  The password of the student whose course list we want to access.
     */
    public static void displayCompletedCourses(String studentName, String studentPassword){
        StudentPlan studentPlan = findStudentByName(studentName, studentPassword).getStudentPlan();
        System.out.println(studentPlan.printStudentCompletedCourseInfo());
    }

    /**
     * Calls other methods in StudentPlan to mark the course as completed, and informs the user.
     * @param currStudentPlan   The studentPlan object of the current student.
     * @param courseToUpdate    A string that represents the name of the course to be updated.
     */
    public static void markCourseCompletedHelper(StudentPlan currStudentPlan, String courseToUpdate){
        if(currStudentPlan.addToCompletedCourses(RegistrationDashboard.findCourseObject(courseToUpdate))){
            ArrayList<String> updatedLines = new ArrayList<String>();
            readUserAccounts(updatedLines);
            writeToUserAccounts(updatedLines);
        }
        if(currStudentPlan.removeCurrentCourse(courseToUpdate)){
            ArrayList<String> updatedLines = new ArrayList<String>();
            readUserAccounts(updatedLines);
            writeToUserAccounts(updatedLines);
        }
    }
    /**
     * Shows the user all the courses they are currently registered for. Allows them to select any course
     * they desire to mark as completed. Removes the course from their list of current courses. Adds the course
     * to their list of completed courses. This data is saved on the user's account, so they can logout
     * and still have access to their information.
     */
    public static void markCourseCompleted(){
        System.out.println("Here are your current courses:");
        displayCurrentCourses(currentUserName, currentPassword);
        System.out.println("Input completed course name: ");
        String courseToUpdate = getValidStringInput();

        StudentPlan currStudentPlan = findStudentByName(currentUserName, currentPassword).getStudentPlan();
        if (!currStudentPlan.aCurrentCourse(courseToUpdate)){
            System.out.println("You are not currently registered for that course.\n");
        } else {
            markCourseCompletedHelper(currStudentPlan, courseToUpdate);
            System.out.println(courseToUpdate + " has been marked as completed.\n");
        }

    }

    /**
     * Gets the valid integer choice from the user. It is a utility function
     * that is called to make sure the input is an integer between 1 and max (the parameter)
     *
     * @param max  The max choice the user can pick
     * @return                The option the user chooses
     */
    public static int getValidIntegerChoice(int max){
        boolean validChoice = false;;
        int chosenNum = 0;

        while (!validChoice){

            if (scanner.hasNextInt()){
                chosenNum = scanner.nextInt();
                scanner.nextLine(); // consume the newline character

                if (chosenNum >= 0 && chosenNum <= max){
                    //valid input
                    validChoice = true;
                } else {
                    System.out.println("invalid input");
                }
            } else {
                System.out.println("invalid input");
                scanner.nextLine(); // clear the invalid input
            }
        }

        return chosenNum;
    }

    /**
     * Describe the purpose of the method. Clearly.
     * It is a method to get a valid string input from the user. It is a utility
     * function that is called to make sure the input is only letters
     * @return                none
     */
    public String getValidStringBoxInput(){
        boolean validString = false;
        String givenInput = "";

        while (!validString){
            givenInput = userInput.getText();

            if (!givenInput.trim().equals("")){
                validString = true;
            }
            else{
                System.out.println("invalid input");
            }

        }

        return givenInput;
    }

    /**
     * Describe the purpose of the method. Clearly.
     * It is a method to get a valid string input from the user. It is a utility
     * function that is called to make sure the input is only letters
     * @return                none
     */
    public static String getValidStringInput(){
        boolean validString = false;
        String givenInput = "";

        while (!validString){
            givenInput = scanner.nextLine();

            if (givenInput.trim() != ""){
                validString = true;
            }
            else{
                System.out.println("invalid input");
            }

        }

        return givenInput;
    }
}