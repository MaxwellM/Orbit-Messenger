package com.orbitmessenger.Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.io.*;
import java.lang.reflect.Type;

public class PreferencesController extends ControllerUtil {

    @FXML
    private TextField messageNumberTxtField;
    @FXML
    private Button savePrefBtn;
    @FXML
    private ToggleSwitch darkThemeToggleBtn;

    private Integer messageNum;
    private Boolean darkThm;

    public static class Preferences {
        Integer messageNumber = 100;
        Boolean darkTheme = false;
    }

    /**
     * Updates the clients from the preferences screen
     */
    @FXML
    public void savePreferences() {
        if (checkField(messageNumberTxtField.getText().trim())) {
            messageNum = convertToInteger(messageNumberTxtField.getText().trim());
            darkThm = darkThemeToggleBtn.isSelected();

            // Write JSON file
            writePreferencesToFile();

            System.out.println("Preferences saved!");

            closePreferences();
        }
    }

    public Object readPreferencesFile() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.PREF_LOC));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Object json = gson.fromJson(bufferedReader, Object.class);

        System.out.println(json.getClass());
        System.out.println(json.toString());

        return json;
    }

    private void writePreferencesToFile() {
        try (Writer writer = new FileWriter(this.PREF_LOC)) {
            Gson gson = new GsonBuilder().create();

            Preferences pref = new Preferences();
            pref.messageNumber = messageNum;
            pref.darkTheme = darkThm;

            String json = gson.toJson(pref);
            gson.toJson(json, writer);
        } catch (IOException e) {
            System.out.println("Error writing JSON Preferences file.");
            e.printStackTrace();
        }
    }

    private void closePreferences() {
        // get a handle to the stage
        Stage stage = (Stage) savePrefBtn.getScene().getWindow();

        // do what you have to do
        stage.close();
    }

    private boolean checkField(String messageNumTxt) {
        try {
            Integer.parseInt(messageNumTxt);
            return true;
        } catch (Exception e) {
            messageNumberTxtField.setStyle("-fx-control-inner-background: red");
            return false;
        }
    }

    private Integer convertToInteger(String messageNumTxt) {
        return Integer.parseInt(messageNumTxt);
    }
}
