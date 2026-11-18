package lincz.matthew.assignment02_race;

/**
 *
 * @author Matthew Lincz
 */
import javafx.scene.paint.Color;

public class CarRaceModel {
    private String type;
    private int carNumber;
    private double xPosition;
    private double yPosition;
    private double speed;
    private Color color;

    public CarRaceModel(String type, int carNumber, double xPosition, double yPosition, double speed, Color color) {
        this.type = type;
        this.carNumber = carNumber;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.speed = speed;
        this.color = color;
    }

   
}
