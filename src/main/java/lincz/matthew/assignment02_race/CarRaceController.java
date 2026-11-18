package lincz.matthew.assignment02_race;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
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
    private List<TranslateTransition> translateTransitions;

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
        */
        startButton.setOnAction(e -> startRace());
        pauseButton.setOnAction(e -> pauseRace());
        resetButton.setOnAction(e -> resetRace());
        
        

        if (speedSlider != null) {
            speedSlider.setMin(0.5);
            speedSlider.setMax(2.5);
            speedSlider.setValue(1.0);
            //speedslider function will be added later
            //speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSpeed(newVal.doubleValue()));
        }
    }
    
    private void setupSlideshow() {
        List<Image> slideImages = new ArrayList<>();
        try {
            slideImages.add(new Image(getClass().getResourceAsStream("car1.png")));
            slideImages.add(new Image(getClass().getResourceAsStream("car2.png")));
            slideImages.add(new Image(getClass().getResourceAsStream("car3.png")));
            slideImages.add(new Image(getClass().getResourceAsStream("car4.png")));
        } catch (Exception ignored) {}

        if (slideshowGreetingLabel != null) {
            slideshowGreetingLabel.setText("Welcome to Traffic Simulation!\nEnjoy the car slideshow before the race begins.");
        }

        if (!slideImages.isEmpty() && slideshowImageView != null) {
            slideshowImageView.setImage(slideImages.get(0));
            slideshowTimeline = new Timeline(new KeyFrame(Duration.seconds(2.0), event -> {
                currentSlideIndex = (currentSlideIndex + 1) % slideImages.size();
                slideshowImageView.setImage(slideImages.get(currentSlideIndex));
            }));
            slideshowTimeline.setCycleCount(Timeline.INDEFINITE);
            slideshowTimeline.play();
        }
    }

    private void setupCarsOnTrack() {
        if (raceTrackPane == null) return;
        
        // Remove old car nodes if present, keeping background elements (lines, sidebars, slideshow pane)
        carNodes.forEach(raceTrackPane.getChildren()::remove);
        carNodes.clear();
        translateTransitions.clear();

        for (CarRaceModel model : carModels) {
            Pane carGroup = createJavaFXCar(model);
            carGroup.setLayoutX(model.getXPosition());
            carGroup.setLayoutY(model.getYPosition());

            // Ensure cars render beneath the slideshow pane if active
            raceTrackPane.getChildren().add(carGroup);
            carNodes.add(carGroup);

            // Translate distance to reach arrival bar (~710 px)
            double distance = 640.0;
            double duration = 7.0 / model.getSpeed();
            TranslateTransition tt = new TranslateTransition(Duration.seconds(duration), carGroup);
            tt.setToX(distance);
            tt.setOnFinished(event -> handleCarFinished(model));
            translateTransitions.add(tt);
        }
        
        // Ensure slideshow pane is brought to front
        if (slideshowPane != null) {
            slideshowPane.toFront();
        }
  
    }
    private void createJavaFXCar(CarRaceModel model) { //change to pane (return)
        
    }
    @FXML
    private void startRace() {
        
    }
    @FXML
    private void pauseRace() {
  
    }

    @FXML
    private void resetRace() {
        
    }
    private void updateSpeed(double rate) {

    }

    private synchronized void handleCarFinished(CarRaceModel model) {

    }

}
