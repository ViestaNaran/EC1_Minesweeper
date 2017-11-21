package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Optional;
import java.util.Random;


public class Controller {
    @FXML // Binder controlleren til FXML'en
    public int width = 10;
    public int height = 10;

    Field[][] fields = new Field[width + 1][height + 1];
    public Pane mainPane;

    public void initialize() {
        generateFields();
        countNfields();
        generateButtons();

    }

    void countNfields() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                // Her løber vi alle felter igennem, ikke bare de trykkede på
                for (int xOff = -1; xOff <= 1; xOff++)
                    for (int yOff = -1; yOff <= 1; yOff++) {

                        if (x + xOff < width && x + xOff >= 0 && y + yOff < height && y + yOff >= 0) {
                            if (fields[x + xOff][y + yOff].getBomb()) {
                                fields[x][y].numNmines++;
                            }
                        }
                    }
            }
    }
    void generateFields() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                fields[x][y] = new Field();
                fields[x][y].setX(x);
                fields[x][y].setY(y);
            }
            // fields[x][y].setBomb(Math.random() > 0.7f);
        }
        int numBombs = 0;
        while (numBombs < 20) {
            Random random = new Random();
            int i = random.nextInt(width);
            int j = random.nextInt(height);

            if (!fields[i][j].getBomb()) {
                fields[i][j].setBomb(true);
                numBombs++;
                System.out.println("Placing Bombs");
            }
        }
    }

    void recursiveFill(int x, int y) {

        if (x < width && x >= 0 && y < height && y >= 0 && !fields[x][y].getBomb() && !fields[x][y].getPressed()) {
            {
                fields[x][y].setPressed(true);
                fields[x][y].button.setText("Pressed");
                if (fields[x][y].numNmines == 0) {
                    recursiveFill(x + 1, y);
                    recursiveFill(x - 1, y);
                    recursiveFill(x, y + 1);
                    recursiveFill(x, y - 1);
                    recursiveFill(x + 1, y + 1);
                    recursiveFill(x + 1, y - 1);
                    recursiveFill(x, y - 1);
                    recursiveFill(x, y + 1);
                }
                fields[x][y].button.setText(String.valueOf(fields[x][y].numNmines));
            }
        }
    }

    void generateButtons() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                Button myButton = new Button();
                mainPane.getChildren().add(myButton);
                fields[x][y].button = myButton;
                int finalX = x;
                int finalY = y;
                // For test reasons. Sætter prikker på bomberne
                if (fields[x][y].getBomb()) {
                    myButton.setText(".");
                }
                myButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent e) {

                        if (e.getButton() == MouseButton.PRIMARY && !fields[finalX][finalY].getPressed()) {
                            System.out.println("Venstre");

                            System.out.println(finalX + "," + finalY);
                            //  fields[finalX][finalY].setPressed(true);
                            //   recursiveFill(finalX, finalY);

                            if (fields[finalX][finalY].getBomb() && !fields[finalX][finalY].getPressed()) {
                                myButton.setText("BOOM");
                                System.out.println("JOOHHN CEENA");

                                for (int x = 0; x < width; x++) {
                                    for (int y = 0; y < width; y++) {
                                        if (fields[x][y].getBomb()){
                                            fields[x][y].button.setText("BOOM");
                                        }
                                    }
                                }
                                triggerLoss();

                            } else {
                                recursiveFill(finalX, finalY);
                            }

                            System.out.println(fields[finalX][finalY].numNmines);

                        } else if (e.getButton() == MouseButton.SECONDARY && !fields[finalX][finalY].getPressed()) {

                            fields[finalX][finalY].button.setText("Flag");
                            fields[finalX][finalY].setPressed(true);
                        }
                    }

                        /* else {
                            myButton.setText("Negativ");
                            System.out.println("No boom");
                        }
                        */
                        /*
                      for (int x = finalX - 1; x <= finalX + 1; x++) {
                            for (int y = finalY - 1; y <= finalY + 1; y++) {
                            fields[finalX - 1][finalY - 1].button.setText("true");
                            fields[finalX][finalY - 1].button.setText("true");
                            fields[finalX + 1][finalY - 1].button.setText("true");
                            fields[finalX - 1][finalY].button.setText("true");
                            fields[finalX + 1][finalY].button.setText("true");
                            fields[finalX - 1][finalY + 1].button.setText("true");
                            fields[finalX + 1][finalY + 1].button.setText("true");
                            fields[finalX][finalY + 1].button.setText("true");
                            fields[finalX + 1][finalY + 1].button.setText("true");

                                if (!(x == finalX && y == finalY)) {
                                    if (x >= 0 && x <= width && y >= 0 && y <= height) {
                                        fields[x][y].button.setText("true2");
                                    }
                                }
                            }
                        } */
                });
                myButton.setPrefWidth(50);
                myButton.setPrefHeight(50);
                myButton.setLayoutX(50 * x);
                myButton.setLayoutY(50 * y);
            }
        }
    }
    void triggerWin() {
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("You won the game");
        alert2.setHeaderText(" You cleared the minefield and won the game! ");
        alert2.setContentText("Press OK to reset, cancel to exit");
        mainPane.setDisable(true);
        alert2.showAndWait();
    }
    void triggerLoss(){
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("End Screen");
        alert1.setHeaderText("You hit a bomb and lost the game");
        alert1.setContentText("Press OK to reset, cancel to end the game");
        Optional<ButtonType> result = alert1.showAndWait();
        if( result.get()== ButtonType.OK){
            resetBoard();
        }
        //alert1.showAndWait();
    }
    void resetBoard(){

        mainPane.getChildren().clear();
        initialize();
    }
} // Main end
