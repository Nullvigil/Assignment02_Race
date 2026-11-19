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
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
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
    @FXML private Label speedValueLabel;
    @FXML private Label messageLabel;
    @FXML private Label finalResultsListLabel;

    private List<CarRaceModel> carModels;
    private List<Pane> carNodes;
    private List<TranslateTransition> translateTransitions;

    private PauseTransition slideshowTimeline;
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
            speedSlider.setShowTickMarks(true);
            speedSlider.setMajorTickUnit(0.5);
            speedSlider.setMinorTickCount(4);
            speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSpeed(newVal.doubleValue()));
            updateSpeed(speedSlider.getValue());
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
            slideshowTimeline = pause;
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
            // New random speed between 3.9 and 5.1 every time the cars are set up (start and each reset)
            model.setSpeed(3.9 + Math.random() * (5.1 - 3.9));
            Pane carGroup = createJavaFXCar(model);
            carGroup.setLayoutX(model.getXPosition());
            carGroup.setLayoutY(model.getYPosition());

            raceTrackPane.getChildren().add(carGroup);
            carNodes.add(carGroup);

            double distance = 640.0;
            double durationMillis = (7.0 / model.getSpeed()) * 1000.0;
            
            TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(durationMillis), carGroup);
            tt.setToX(distance);
            tt.setRate(speedSlider != null ? speedSlider.getValue() : 1.0);
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
        Color base = model.getColor();

        // Body: long rounded rectangle
        Rectangle body = new Rectangle(0, 14, 100, 20);
        body.setArcWidth(12);
        body.setArcHeight(12);
        body.setFill(base);

        // Cabin on top of the body
        Polygon cabin = new Polygon(26, 14, 36, 0, 66, 0, 78, 14);
        cabin.setFill(base.darker());

        // Two windows
        Polygon rearWindow = new Polygon(31, 14, 38, 3, 48, 3, 48, 14);
        Polygon frontWindow = new Polygon(52, 14, 52, 3, 64, 3, 72, 14);
        rearWindow.setFill(Color.LIGHTBLUE);
        frontWindow.setFill(Color.LIGHTBLUE);

        // Headlight
        Rectangle headlight = new Rectangle(94, 19, 6, 5);
        headlight.setFill(Color.YELLOW);

        // Wheels
        Circle wheel1 = new Circle(22, 34, 8, Color.BLACK);
        Circle wheel2 = new Circle(78, 34, 8, Color.BLACK);
        Circle hub1 = new Circle(22, 34, 3, Color.LIGHTGRAY);
        Circle hub2 = new Circle(78, 34, 3, Color.LIGHTGRAY);

        // Car number
        Text numberText = new Text(String.valueOf(model.getCarNumber()));
        numberText.setFill(Color.WHITE);
        numberText.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        numberText.setX(44);
        numberText.setY(29);

        pane.getChildren().addAll(cabin, rearWindow, frontWindow, body, headlight,
                wheel1, wheel2, hub1, hub2, numberText);
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
            if (slideshowTimeline != null) slideshowTimeline.playFromStart();
        }
    }

    /**
     * This will be the simple method for updating the new speed when the slider is activated
     */
    private void updateSpeed(double rate) {
        if (speedValueLabel != null) {
            speedValueLabel.setText(String.format("%.1fx", rate));
        }
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