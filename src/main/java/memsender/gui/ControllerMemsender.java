package memsender.gui;

import core.ServiceSettings;
import core.ServiceShare;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ControllerMemsender implements Initializable{

    private Preferences pref;

    static ControllerMemsender controller;
    final static String ADRESSES = "ADRESSES";

    @FXML
    ComboBox<String> adresses;

    @FXML
    ComboBox<String> comboCommands;

    void loadAdresses(){
        String[] arrAddr = pref.get(ADRESSES, "").split(" ");
        adresses.getItems().addAll("2", "6");
        comboCommands.getItems().addAll("2", "6");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = this;
        pref = ServiceSettings.pref(ControllerMemsender.class);
        System.out.println("init");
        loadAdresses();
    }
}
