package com.orbitmessenger.Controllers;

import com.google.gson.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

class FxUtil{
    private final String PATH_TO_FXML = "/com/orbitmessenger/FXML/";
    final public String LOGIN_FXML = PATH_TO_FXML + "loginView.fxml";
    final public String MAIN_FXML = PATH_TO_FXML + "mainView.fxml";
    final public String PREF_FXML = PATH_TO_FXML + "preferences.fxml";
    final public String CROOM_FXML = PATH_TO_FXML + "createRoom.fxml";

    final public String PREF_LOC = "src/java/com/orbitmessenger/preferences/preferences.json";
    final public String PROFILE_PIC_LOC = "/com/orbitmessenger/images/profilePics/";
    final public String VERSION_LOC = "version.txt";


    public Integer messageNumber;
    public Integer fontSize;
    public String theme;
    public Boolean groupMessages;
    public JsonObject preferencesObject;
    public String version;

    //+++++++++ Stage functions ++++++++++

    /**
     * gets the current stage from a control
     */
    public Stage getStageFromControl(Control control){
        try{
            return (Stage) control.getScene().getWindow();
        } catch (Exception e){
            System.out.println(e);
        }
        return new Stage();
    }

    /**
     * Changes the javaFX scene by using an FXML file
     */
    public void changeSceneTo(String fxmlFile, Object controller, Stage stage){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setController(controller);
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(new Scene(parent));
        stage.show();
    }

    /**
     * Changes the scenes of the entire program
     */
    public void changeSceneTo(String fxmlFile, Object controller, Control control){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setController(controller);
        try {
            Parent parent = loader.load();
            Stage stage = this.getStageFromControl(control);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("couldn't change scene" + "\n" + e);
        }
    }

    public void loadPreferences() {
        Object returnedPreferences = readPreferencesFile();
        preferencesObject = (JsonObject) new JsonParser().parse(returnedPreferences.toString());
    }

    public void loadVersion() {
        String localVersion = readVersionFile();
        version = localVersion.trim();
    }

    private String readVersionFile() {
        String content = "";
        try {
            content = new String (Files.readAllBytes(Paths.get(this.VERSION_LOC)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private Object readPreferencesFile() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.PREF_LOC));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Object json = gson.fromJson(bufferedReader, Object.class);

        return json;
    }

    public void writePreferencesToFile() {
        try (Writer writer = new FileWriter(this.PREF_LOC)) {
            Gson gson = new GsonBuilder().create();

            String json = gson.toJson(preferencesObject);
            gson.toJson(json, writer);
        } catch (IOException e) {
            System.out.println("Error writing JSON Preferences file.");
            e.printStackTrace();
        }
    }

    public JsonObject getPreferences() {
        return preferencesObject;
    }

    public void setPreferences() {
        preferencesObject.addProperty("messageNumber", messageNumber);
        preferencesObject.addProperty("theme", theme);
        preferencesObject.addProperty("groupMessages", groupMessages);
        preferencesObject.addProperty("fontSize", fontSize);
    }
}

public class ControllerUtil<T> extends FxUtil{

    //+++++++++ control functions ++++++++++

    /**
     *
     */
    public String getTextFieldText(TextField textField){
        try{
            String usertext = textField.getText();
            return usertext;
        } catch (Exception e){
           return "";
        }
    }

    /**
     *
     */
    public int getTextFieldInt(TextField textField){
        return Integer.parseInt(textField.getText());
    }

    /**
     *
     */
    public double getTextFieldDouble(TextField textField){
        return Double.parseDouble(textField.getText());
    }

    /**
     *
     */
    public void setTextField(TextField textField, Object object){
        if(object instanceof Integer || object instanceof Double){
            textField.setText(String.valueOf(object));
        }else{
            textField.setText((String) object);
        }
    }

    /**
     *
     */
    public void fillOutChoiceBox(ChoiceBox choiceBox , ArrayList<String> arrayList){
        choiceBox.getItems().setAll(arrayList);
//        for(String stuff : arrayList){
//        }
    }

    /**
     *
     */
    public void fillOutChoiceBox(ChoiceBox choiceBox , String[] array){
        choiceBox.getItems().setAll(Arrays.asList(array));
    }

    /**
     *
     */
    public int getIndexInChoiceBox(ChoiceBox choiceBox){
       return choiceBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     */
    public void fillOutListView(ArrayList arrayList, ListView listView){
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getItems().setAll(arrayList);
    }

    /**
     *
     */
    public int getIndexInListView(ListView listView){
        int test = listView.getSelectionModel().getSelectedIndex();
        return test;
    }

}
