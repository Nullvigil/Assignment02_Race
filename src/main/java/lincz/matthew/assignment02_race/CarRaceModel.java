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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
       
}
