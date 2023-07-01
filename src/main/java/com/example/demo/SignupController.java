package com.example.demo;

import Client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    TextField nameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField phoneOrEmail;

    @FXML
    Button back;

    @FXML
    TextField username;
    @FXML
    TextField pass;
    @FXML
    TextField passRepetition;
    @FXML
    ChoiceBox<String> country;
    @FXML
    DatePicker birthdate;
    @FXML
    private Parent root;
    @FXML
    private Label error;
    private Stage stage;
    private Scene scene;
    private boolean hasError = false;


    public void switchToMainMenu(ActionEvent event) { // exit button
        Button button = (Button) event.getSource();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("FirstPage.fxml"));
        Parent root = null;
        try {
            root=loader.load();
        }catch (IOException e){
            System.out.println("KOMAK!");
        }
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = null;
        if (root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent event) throws IOException, ClassNotFoundException {
        String firstname;
        String lastname;
        String numberOrEmail;
        String user;
        String password;
        String passRep;
        String country2;
        LocalDate birthday;
        String feedback = " ";
        firstname = nameTextField.getText();
        lastname = lastNameTextField.getText();
        numberOrEmail = phoneOrEmail.getText();
        user = username.getText();
        password = pass.getText();
        passRep = passRepetition.getText();
        country2 = country.getValue();
        birthday = birthdate.getValue();
        hasError = false;
            try {
                feedback = Client.signUp(firstname, lastname, numberOrEmail, user, password, passRep, country2, birthday);
            }
            catch (IllegalArgumentException e){
                hasError = true;
                error.setText(e.getMessage());
            }
            catch (ParseException | InterruptedException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        if (!hasError) {
            if (!feedback.equals("signed up successfully!")) {
                error.setText(feedback);
            }
            MainPageController.switchToMainPage(event);
        }
    }
        ObservableList<String> temp = FXCollections.observableArrayList("Afghanistan","Albania","Algeria","Andorra",
                "Angola","Antigua & Deps","Argentina","Armenia","Australia","Austria","Azerbaijan","Bahamas","Bahrain",
                "Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia Herzegovina",
                "Botswana","Brazil","Brunei","Bulgaria","Burkina","Burundi","Cambodia","Cameroon","Canada","Cape Verde",
                "Central African Rep","Chad","Chile","China","Colombia","Comoros","Congo","Congo {Democratic Rep}","" +
                        "Costa Rica","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica",
                "Dominican Republic","East Timor","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia"
                ,"Ethiopia","Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada",
                "Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hungary","Iceland","India","Indonesia",
                "Iran","Iraq","Ireland {Republic}","Israel","Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan"
                ,"Kenya","Kiribati","Korea North","Korea South","Kosovo","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon",
                "Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macedonia","Madagascar","Malakhestan"
                ,"Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Mauritania","Mauritius","Mexico",
                "Micronesia","Moldova","Monaco","Mongolia","Montenegro","Morocco","Mozambique","Myanmar, {Burma}","Namibia",
                "Nauru","Nepal","Netherlands","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan",
                "Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Qatar","Qom",
                "Romania","Russian Federation","Rwanda","St Kitts & Nevis","St Lucia","Saint Vincent & the Grenadines",
                "Samoa","San Marino","Sao Tome & Principe","Senegal","Serbia","Seychelles","Sierra Leone","Singapore",
                "Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","Spain","Sri Lanka","Sudan"
                ,"Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Togo",
                "Tonga","Trinidad & Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","Ukraine",
                "United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan","Vanuatu","Vatican City",
                "Venezuela","Vietnam","Yemen","Zambia","Zimbabwe");
           @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        country.setItems(temp);
    }
}
