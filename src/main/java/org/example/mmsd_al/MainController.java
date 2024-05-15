package org.example.mmsd_al;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;
//Еуые
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("СОТКА ММСД!");
    }
}