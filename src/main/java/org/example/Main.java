package org.example;
import javafx.application.Application;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

        Student mary = new Student("Mary", "yessir");
        StudentPlan marysPlan = new StudentPlan(mary);
        Aurora aurora = Aurora.getInstance();

        //Load in past students information
        RegistrationDashboard.loadAvailableCourses();
        RegistrationDashboard.loadInPrevStudentLogIns();

        System.out.println("Before");
        Application.launch(ApplicationDisplay.class, args);
        boolean successfulLogInOrSignUp = aurora.askUserToSignUpOrLogIn();
        while (!successfulLogInOrSignUp) { //while they don't sign up/log in successfully
            successfulLogInOrSignUp = aurora.askUserToSignUpOrLogIn();
        }
        System.out.println("User logged in done");
        //aurora.displayAuroraOptions();
    }
}