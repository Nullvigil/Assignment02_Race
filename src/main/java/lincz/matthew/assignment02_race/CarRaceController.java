package lincz.matthew.assignment02_race;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CarRaceController {
@FXML private Pane rootPane;
    @FXML private Pane raceTrackPane;
    @FXML private Pane slideshowPane;
    @FXML private ImageView slideshowImageView;
    @FXML private Label slideshowGreetingLabel;
    
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resetButton;
    @FXML private Slider speedSlider;
    @FXML private Label messageLabel;
    @FXML private Label finalResultsListLabel;

    private List<CarRaceModel> carModels;
    private List<Pane> carNodes;

    private Timeline slideshowTimeline;
    private int currentSlideIndex = 0;
    private boolean raceFinished = false;
    private List<String> finishingOrder;

    @FXML
    public void initialize() {
        carModels = new ArrayList<>();
        carNodes = new ArrayList<>();
        finishingOrder = new ArrayList<>();

        // Initialize 4 cars matching the original visual style and colors
        carModels.add(new CarRaceModel("Sedan", 23, 70, 65, 4.0, Color.web("#e67e22"))); //orange
        carModels.add(new CarRaceModel("Sports", 45, 70, 140, 4.5, Color.web("#00bcd4"))); //cyan
        carModels.add(new CarRaceModel("Coupe", 78, 70, 215, 3.8, Color.web("#ab47bc"))); //purple
        carModels.add(new CarRaceModel("Muscle", 88, 70, 290, 4.3, Color.web("#2196f3"))); //blue

        /*
        // future methods
        setupCarsOnTrack();
        setupSlideshow();

        startButton.setOnAction(e -> startRace());
        pauseButton.setOnAction(e -> pauseRace());
        resetButton.setOnAction(e -> resetRace());
        */
        

        if (speedSlider != null) {
            speedSlider.setMin(0.5);
            speedSlider.setMax(2.5);
            speedSlider.setValue(1.0);
            //speedslider function will be added later
            //speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSpeed(newVal.doubleValue()));
        }
    }

}
