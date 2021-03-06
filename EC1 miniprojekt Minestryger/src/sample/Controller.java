package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;

import java.util.Optional;
import java.util.Random;

public class Controller {
    @FXML

    private int width = 20;
    private int height = 20;
    private int numBombs;
    private int numFlags;
    private int maxBombs = 30;
    private int maxFlags = maxBombs;
    private int fieldCount = 0;

    fields[][] field = new fields[width][height];

    public Pane mainPane;

    // Initialize er main metoden i controlleren
    public void initialize() {

        generateFields();
        countFields();
        generateButtons();
    }

    // Metode der generere fælter
    void generateFields() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                field[x][y] = new fields();
                field[x][y].setX(x);
                field[x][y].setY(y);
            }
        }
        while (numBombs < maxBombs) {
            Random random = new Random();
            int i = random.nextInt(width);
            int j = random.nextInt(height);
            if (!field[i][j].getBomb()) {
                field[i][j].setBomb(true);
                numBombs++;
                System.out.println("Placing bombs");
            }
        }
    } // generateFields end

    // Metoden binder knapper til vores array og ligger dem ind i pladen mainPane
    void generateButtons() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Button myButton = new Button();
                mainPane.getChildren().add(myButton);
                field[x][y].button = myButton;
                int finalX = x;
                int finalY = y;
                if (field[x][y].getBomb()) {
                    myButton.setText(".");
                }
                myButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent e) {
                        // Venstre klik
                        // Lavede en ekstra If statement for at holde recursiveFill ude af højreklik

                      //  if (e.getButton() == MouseButton.PRIMARY && !field[finalX][finalY].getPressed()) {
                      //      recursiveFill(finalX, finalY);
                      //  }
                        if (e.getButton() == MouseButton.PRIMARY && !field[finalX][finalY].getPressed()) {
                            recursiveFill(finalX,finalY);
                            for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {

                                    if (field[finalX][finalY].getBomb() && !field[finalX][finalY].getPressed()) {
                                        myButton.setText("BOOM");
                                        System.out.println("Bombe");
                                        triggerLoss();
                                    }
                                }
                            }
                        }
                        // Højre klik
                        if (e.getButton() == MouseButton.SECONDARY && !field[finalX][finalY].getPressed() && numFlags < maxFlags) {

                            field[finalX][finalY].button.setText("Flag");
                            field[finalX][finalY].setPressed(true);
                            field[finalX][finalY].setFlag(true);
                            System.out.println("Flag sat");
                            numFlags++;
                            fieldCount++;

                        }else if (e.getButton() == MouseButton.SECONDARY && field[finalX][finalY].getPressed() && field[finalX][finalY].getFlag()){

                            field[finalX][finalY].setPressed(false);
                            field[finalX][finalY].setFlag(false);
                            field[finalX][finalY].button.setText("");
                            numFlags--;
                            fieldCount--;
                        }  // Hvis højreklik og field = pressed og flag = true gør X
                        if( fieldCount == width*height){triggerWin();}
                    }
                });
                // Knappernes størrelse
                myButton.setPrefWidth(50);
                myButton.setPrefHeight(50);
                // Knapperne rykkes så de ikke ligger i en bunke ovenpå hinanden på pladen.
                myButton.setLayoutX(50 * x);
                myButton.setLayoutY(50 * y);
            }
        }
    } // generateButtons end

    void recursiveFill(int x, int y) {
        if (x < width && x >= 0 && y < height && y >= 0 && !field[x][y].getBomb() && !field[x][y].getPressed()) {
            field[x][y].setPressed(true);
            fieldCount++;
            System.out.println(fieldCount);
            if (field[x][y].numMines == 0) {
                recursiveFill(x + 1, y);
                recursiveFill(x - 1, y);
                recursiveFill(x, y + 1);
                recursiveFill(x, y - 1);
                recursiveFill(x - 1, y - 1);
                recursiveFill(x - 1, y + 1);
                recursiveFill(x + 1, y + 1);
                recursiveFill(x + 1, y - 1);
            }
            field[x][y].button.setText(String.valueOf(field[x][y].numMines));
        }
    }

    // Metoden countFields tæller miner, rundt om alle felterne, sætter feltets værdig til numMines
    void countFields() {
        // To løkker for at køre igennem alle felter
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // To løkker der tæller rundt om hvert felt og henter værdien.
                for (int xOff = -1; xOff <= 1; xOff++) {
                    for (int yOff = -1; yOff <= 1; yOff++) {
                        if (x + xOff < width && x + xOff >= 0 && y + yOff < height && y + yOff >= 0) {
                            if (field[x + xOff][y + yOff].getBomb()) {
                                field[x][y].numMines++;
                            }
                        }

                    }
                }
            }
        }
    }

    void triggerLoss() {
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("End Screen");
        alert2.setHeaderText("YOu hit a bomb and lost the game");
        alert2.setContentText("Press OK to replay, press cancel to exit");
        Optional<ButtonType> result = alert2.showAndWait();
        if (result.get() == ButtonType.OK) {
            resetBoard();
        }
    }

    void triggerWin() {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("End Screen");
        alert1.setHeaderText("You Cleared the field and won the game");
        alert1.setContentText("Press OK to replay, press cancel to exit");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            resetBoard();
            alert1.showAndWait();
        }
    }

    void resetBoard() {
        numBombs = 0;
        numFlags = 0;
        fieldCount = 0;
        mainPane.getChildren().clear();
        initialize();
    }
}
