package org.example;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationDisplay extends Application {
    @FXML
    private Label informLabel;
    @FXML
    private TextField userInput;
    @FXML
    private Button defaultPageEnterButton;
    private Aurora aurora = Aurora.getInstance();
    private String reasonForInput = "";
    private static String gettingUserCredentials = "UserCredentials";
    private static String exitInputingUserAndPassword = "ExitEnteringUserAndPassword";
    private static String askIfUserHasAccAlready = "Do you already have an account? (Y/N)";
    private String incorrectUserCredentials =  "Retry. Type in your username and password, seperated by a blank space(s) or -1 to Exit";
    private String askUserCredentialsPromptString = "Type in your username and password, seperated by a blank space(s) or -1 to Exit";
    private String incorrectUserHasAccountResponse = "invalid input\nDo you already have an account? (Y/N)";;
    private String askIfUserHasAccount = "AskIfUserHasAccountAlready";
    private Stage theStage;
    private Parent auroraRoot;

    @Override
    public void start(Stage stage) throws IOException {
        if (stage != null){
            theStage = stage;
        }
        FXMLLoader applicationDisplayLoader = new FXMLLoader(getClass().getResource("/ApplicationDisplay.fxml"));
        FXMLLoader auroraDisplayLoader = new FXMLLoader(getClass().getResource("/Aurora.fxml"));

        ApplicationDisplay applicationDisplayController = applicationDisplayLoader.getController();
        Aurora auroraDisplayController = auroraDisplayLoader.getController();

        applicationDisplayLoader.setController(this);
        aurora.setApplicationDisplay(this);
        aurora.setAuroraDisplay(auroraDisplayController);

        Parent applicationRoot = applicationDisplayLoader.load();
        auroraRoot = auroraDisplayLoader.load();

        System.out.println("in start method");
        Scene scene = new Scene(applicationRoot);
        stage.setTitle("Aurora");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onEnterButtonClick() {
        if (userInput.getText().trim().equals("-1")){ //they pressed -1 which means exit entering username+password
            reasonForInput = exitInputingUserAndPassword;
        } else if (informLabel.getText().equals(askUserCredentialsPromptString)) {
            reasonForInput = "UserCredentials";
            System.out.println("incorr usercred");
        } else if (informLabel.getText().equals(incorrectUserCredentials)){
            reasonForInput = gettingUserCredentials;
        } else if (informLabel.getText().equals(incorrectUserHasAccountResponse)){
            reasonForInput = askIfUserHasAccAlready;
        }
        else if (informLabel.getText().equals(askIfUserHasAccAlready)) { //last option is that we are asking the user if they already have an account
            reasonForInput = askIfUserHasAccount;
        }
        aurora.handleInput(userInput.getText(), reasonForInput);
    }
    //Tips, you can override the handleInput method, you can also pass in a string or boolean as an argument
    //to see which step we are at in the inputing process.

    public void updateInformLabel(String textToUpdateWith){
        informLabel.setText(textToUpdateWith);
    }

    public String getUserInputInfo(){
        return userInput.getText();
    }

    public void showScreen() {
        theStage.getScene().setRoot(auroraRoot);
    }
}
