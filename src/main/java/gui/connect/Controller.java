package gui.connect;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private ComboBox comboIp;

    @FXML
    private Label lblStatusEquipment;

    @FXML
    private Label lblStatusConnect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 5;
        comboIp.setEditable(true);
        lblStatusConnect.setText("Не подключено");
        lblStatusEquipment.setText("Не доступно");
    }
}
