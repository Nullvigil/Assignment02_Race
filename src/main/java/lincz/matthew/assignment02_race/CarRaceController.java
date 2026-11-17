package lincz.matthew.assignment02_race;

import java.io.IOException;
import javafx.fxml.FXML;

public class CarRaceController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
