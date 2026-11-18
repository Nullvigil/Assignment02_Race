//https://github.com/Nullvigil/Assignment02_Race
package lincz.matthew.assignment02_race;

import javafx.animation.PauseTransition;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*
 * @Author Matthew Lincz
*/
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
        // Correctly assign to class fields instead of creating local variables
        carModels = new ArrayList<>();
        carNodes = new ArrayList<>();
        translateTransitions = new ArrayList<>();
        finishingOrder = new ArrayList<>();

        // Initialize 4 cars matching the original visual style and colors
        carModels.add(new CarRaceModel("Sedan", 23, 70, 65, 4.0, Color.web("#e67e22"), "Orange")); 
        carModels.add(new CarRaceModel("Sports", 45, 70, 140, 4.5, Color.web("#00bcd4"), "Cyan")); 
        carModels.add(new CarRaceModel("Coupe", 78, 70, 215, 3.8, Color.web("#ab47bc"), "Purple")); 
        carModels.add(new CarRaceModel("Muscle", 88, 70, 290, 4.3, Color.web("#2196f3"), "Blue")); 

        setupCarsOnTrack();
        setupSlideshow();
        
        startButton.setOnAction(e -> startRace());
        pauseButton.setOnAction(e -> pauseRace());
        resetButton.setOnAction(e -> resetRace());

        if (speedSlider != null) {
            speedSlider.setMin(0.5);
            speedSlider.setMax(2.5);
            speedSlider.setValue(1.0);
            speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSpeed(newVal.doubleValue()));
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
            
            PauseTransition pause = new PauseTransition(javafx.util.Duration.millis(2000));
            pause.setOnFinished(event -> {
                currentSlideIndex = (currentSlideIndex + 1) % slideImages.size();
                slideshowImageView.setImage(slideImages.get(currentSlideIndex));
                if (slideshowPane != null && slideshowPane.isVisible()) {
                    pause.playFromStart();
                }
            });
            pause.play();
        }
    }

    private void setupCarsOnTrack() {
        if (raceTrackPane == null || carModels == null) return;
        
        // Remove old car nodes if present, keeping background elements
        carNodes.forEach(raceTrackPane.getChildren()::remove);
        carNodes.clear();
        translateTransitions.clear();

        for (CarRaceModel model : carModels) {
            Pane carGroup = createJavaFXCar(model);
            carGroup.setLayoutX(model.getXPosition());
            carGroup.setLayoutY(model.getYPosition());

            raceTrackPane.getChildren().add(carGroup);
            carNodes.add(carGroup);

            double distance = 640.0;
            double durationMillis = (7.0 / model.getSpeed()) * 1000.0;
            
            TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(durationMillis), carGroup);
            tt.setToX(distance);
            tt.setOnFinished(event -> handleCarFinished(model));
            translateTransitions.add(tt);
        }
        
        if (slideshowPane != null) {
            slideshowPane.toFront();
        }
    }

    /**
     * This is the method for making the car body and parts as requested in instructions
     * @param model
     * @return 
     */
    private Pane createJavaFXCar(CarRaceModel model) { 
        Pane pane = new Pane();

        // Car Body Polygon
        Polygon body = new Polygon(
            10, 18,
            30, 4,
            70, 4,
            90, 18,
            98, 30,
            2, 30
        );
        body.setFill(model.getColor());

        // Car Roof Polygon
        Polygon roof = new Polygon(
            35, 4,
            43, -10,
            67, -10,
            75, 4
        );
        roof.setFill(model.getColor().darker());

        // Windows
        Rectangle frontWindow = new Rectangle(48, -8, 14, 12);
        frontWindow.setFill(Color.web("#b2ebf2"));
        Rectangle rearWindow = new Rectangle(35, -8, 10, 12);
        rearWindow.setFill(Color.web("#b2ebf2"));

        // Wheels
        Circle wheel1 = new Circle(18, 30, 8, Color.DARKGRAY);
        wheel1.setStroke(Color.BLACK);
        wheel1.setStrokeWidth(1.5);
        Circle hub1 = new Circle(18, 30, 3, Color.WHITE);

        Circle wheel2 = new Circle(82, 30, 8, Color.DARKGRAY);
        wheel2.setStroke(Color.BLACK);
        wheel2.setStrokeWidth(1.5);
        Circle hub2 = new Circle(82, 30, 3, Color.WHITE);

        // Car Number
        Text numberText = new Text(String.valueOf(model.getCarNumber()));
        numberText.setFill(Color.WHITE);
        numberText.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        numberText.setX(43);
        numberText.setY(20);

        pane.getChildren().addAll(body, roof, frontWindow, rearWindow, wheel1, wheel2, hub1, hub2, numberText);
        return pane;
    }

    /**
     * This method will be the start function for the button called start
     */
    @FXML
    private void startRace() {
        if (slideshowTimeline != null) {
            slideshowTimeline.stop();
        }
        if (slideshowPane != null) {
            slideshowPane.setVisible(false);
            slideshowPane.setManaged(false);
        }

        raceFinished = false;
        finishingOrder.clear();
        if (messageLabel != null) {
            messageLabel.setText("Race in progress...");
        }
        if (finalResultsListLabel != null) {
            finalResultsListLabel.setText("");
        }

        for (TranslateTransition tt : translateTransitions) {
            if (tt.getStatus() == Animation.Status.PAUSED) {
                tt.play();
            } else if (tt.getStatus() != Animation.Status.RUNNING) {
                tt.playFromStart();
            }
        }
    }

    /**
     * This method will be the pause function for the button called pause
     */
    @FXML
    private void pauseRace() {
        for (TranslateTransition tt : translateTransitions) {
            if (tt.getStatus() == Animation.Status.RUNNING) {
                tt.pause();
            }
        }
        if (messageLabel != null) {
            messageLabel.setText("Race paused.");
        }
    }

    /**
     * This method will be the reset function for the button called reset
     */
    @FXML
    private void resetRace() {
        for (TranslateTransition tt : translateTransitions) {
            tt.stop();
        }
        setupCarsOnTrack();
        raceFinished = false;
        finishingOrder.clear();
        
        if (messageLabel != null) {
            messageLabel.setText("Click Start to begin race.");
        }
        if (finalResultsListLabel != null) {
            finalResultsListLabel.setText("");
        }
        if (slideshowPane != null) {
            slideshowPane.setVisible(true);
            slideshowPane.setManaged(true);
            if (slideshowTimeline != null) slideshowTimeline.play();
        }
    }

    /**
     * This will be the simple method for updating the new speed when the slider is activated
     */
    private void updateSpeed(double rate) {
        for (TranslateTransition tt : translateTransitions) {
            tt.setRate(rate);
        }
    }

    /**
     * handles the animation and change of all labels and results once a car reaches the end rectangle
     */
    private synchronized void handleCarFinished(CarRaceModel model) {
        String entry = "Car #" + model.getCarNumber() + " (" + model.getColorName() + ")";
        if (!finishingOrder.contains(entry)) {
            finishingOrder.add(entry);
        }

        if (!raceFinished && finishingOrder.size() == 1) {
            raceFinished = true;
            if (messageLabel != null) {
                messageLabel.setText("Race Finished!\nCongratulations! Car #" +
                        model.getCarNumber() + " (" + model.getColorName() +
                        ") is the winner!");
            }
        }

        if (finishingOrder.size() == carModels.size()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < finishingOrder.size(); i++) {
                sb.append((i + 1)).append(". ").append(finishingOrder.get(i)).append("\n");
            }
            if (finalResultsListLabel != null) {
                finalResultsListLabel.setText(sb.toString());
            }
        }
    }
}