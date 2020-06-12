/*
 * SNAKE GAME
AUTHOR: ILOTech
DATE: November 2019
 */
package snakegame;

import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import java.io.*;
public class SnakeGame extends Application implements SnakeDirections {
    GridPane field = new GridPane();
    //an araaylist of snake cells forming a snake
    ArrayList<Cell> snake;
    int cellLength, level = 0, score;
    int locsPerLine = 20;
    Pane[][] grid = new Pane[locsPerLine][locsPerLine];
    Rec food;
    Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> move()));;
    TextField scoreField = new TextField();
    Scene scene = new Scene(initialPage(), 500, 400);
    TextField levelField = new TextField();
    boolean moved = false;
    @Override
    public void start(Stage primaryStage) {
        animation.setCycleCount(Timeline.INDEFINITE);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setIconified(true);
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("Blackvariant-Button-Ui-Requests-13-Snake.ico")));
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void move() {
        int tempRow =snake.get(0).row;
        int tempColumn = snake.get(0).column;

        //moving first cell
        //grid[tempRow][tempColumn].getChildren().remove(0);
        switch (snake.get(0).direction) {
            case UP: {
                snake.get(0).row--;
                break;
            }
            case DOWN: {
                snake.get(0).row++;
                break;
            }
            case LEFT: {
                snake.get(0).column--;
                break;
            }
            case RIGHT: {
                snake.get(0).column++;
                break;
            }
        }
        //Checking if it has reached food block
        if (snake.get(0).row == food.x && snake.get(0).column == food.y) {
            //adding score
            score+= level;
            scoreField.setText(score + "");


            try {
                //adding new cell
                snake.add(new Cell());
            }
            catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
            //creating a new food block
            food = new Rec();
            grid[snake.get(0).row][snake.get(0).column].getChildren().remove(0);
        }

        //Checking if it has reached the walls
        else if (snake.get(0).row >= grid[0].length || snake.get(0).row < 0 ||
                snake.get(0).column >= grid[0].length  || snake.get(0).column < 0) {
            animation.stop();
            gameOver();
            return;
        }

        //checking if it is hittting itself
        else if (!grid[snake.get(0).row][snake.get(0).column].getChildren().isEmpty()) {
            animation.stop();
            gameOver();
            return;
        }
        //assigning the first cell to a new grid
        grid[snake.get(0).row][snake.get(0).column].getChildren().add(snake.get(0).cell);


        //moving the remaining cells
        for (int i = 1; i < snake.size(); i++) {
            int x = snake.get(i).row, y = snake.get(i).column;
            //grid[x][y].getChildren().remove(0);
            snake.get(i).column = tempColumn;
            snake.get(i).row = tempRow;
            grid[tempRow][tempColumn].getChildren().add(snake.get(i).cell);
            tempRow = x;
            tempColumn = y;

        }
        moved = true;
    }

    //Starting the game
    public BorderPane initialPage() {
        //STARTING PAGE
        BorderPane initialRoot = new BorderPane();
        initialRoot.setStyle("-fx-background-color: cornsilk");

        //starting button
        StackPane startBtn = new StackPane();
        startBtn.setPrefSize(150, 100);
        Ellipse ellipse = new Ellipse(startBtn.getWidth()/2, startBtn.getHeight()/2, 60, 35);
        ellipse.setFill(Color.AZURE);
        ellipse.setStroke(Color.BLACK);
        startBtn.getChildren().addAll(ellipse, new Text("START GAME"));
        Label lbl = new Label("Click here to start.");
        lbl.setGraphic(startBtn);
        lbl.setContentDisplay(ContentDisplay.BOTTOM);
        lbl.setOnMouseClicked(e -> {
            //Starting if level is selected
            if (level > 0) {
                BorderPane gamePage = gamePage();
                scene.setRoot(gamePage);
                gamePage.requestFocus();
            }
        });
        initialRoot.setCenter(lbl);

        //level selection section
        VBox levelPane = new VBox();
        levelPane.setSpacing(25);
        levelPane.setPrefWidth(100);
        RadioButton lvl1 = new RadioButton("Level 1");
        RadioButton lvl2 = new RadioButton("Level 2");
        RadioButton lvl3 = new RadioButton("Level 3");
        RadioButton lvl4 = new RadioButton("Level 4");
        RadioButton lvl5 = new RadioButton("Level 5");
        ToggleGroup levels = new ToggleGroup();
        lvl1.setToggleGroup(levels);
        lvl2.setToggleGroup(levels);
        lvl3.setToggleGroup(levels);
        lvl4.setToggleGroup(levels);
        lvl5.setToggleGroup(levels);
        Label llbl = new Label("Select level:");

        levelPane.getChildren().addAll(llbl, lvl1, lvl2, lvl3, lvl4, lvl5);
        initialRoot.setRight(levelPane);
        //Responding to selections
        lvl1.setOnAction(e -> {
            if (lvl1.isSelected()) {
                level = 1;
                animation.setRate(level * 1.5);
                levelField.setText(level + "");
            }

        });
        lvl2.setOnAction(e -> {
            if (lvl2.isSelected()) {
                level = 2;
                animation.setRate(level * 1.5);
                levelField.setText(level + "");
            }

        });
        lvl3.setOnAction(e -> {
            if (lvl3.isSelected()) {
                level = 3;
                animation.setRate(level * 1.5);
                levelField.setText(level + "");
            }

        });
        lvl4.setOnAction(e -> {
            if (lvl4.isSelected()) {
                level = 4;
                animation.setRate(level * 1.5);
                levelField.setText(level + "");
            }

        });
        lvl5.setOnAction(e -> {
            if (lvl5.isSelected()) {
                level = 5;
                animation.setRate(level * 1.5);
                levelField.setText(level + "");
            }

        });
         return initialRoot;
    }
    //Handling game over
    public void gameOver() {
        BorderPane root = new BorderPane();
        VBox gameOverPane = new VBox();
        //restart button
         Button restartBtn = new Button("RESTART");
         restartBtn.setOnAction(e -> {
            restart();
         });
         Text text = new Text();
         
         //Updating the scores
         RandomAccessFile inout;
         try {
            inout = new RandomAccessFile("hscore.snake", "rw");
            int oldScore = 0;
            if (inout.length() > 0) {
               oldScore = inout.readInt();
                
            }
            
            if (score > oldScore) {
                text.setText("Congratulations! \nNew High Score: " + score);
                inout.seek(0);
                inout.writeInt(score);
            }
            else {
                text.setText("Total Score: " + score + "\nHigh Score: " + oldScore);
            }
         }
         catch (IOException ex) {
             
             ex.printStackTrace();
         }
         gameOverPane.getChildren().addAll(
        new Text("Game Over!"), text, restartBtn);

        gameOverPane.setStyle("-fx-background-color: darkgrey;");
        gameOverPane.setSpacing(20);
        gameOverPane.setAlignment(Pos.CENTER);
        root.setCenter(gameOverPane);
        scene.setRoot(root);
    }
    public void restart() {
         levelField.clear();
             scoreField.clear();
             score = 0;
             level = 0;
             scene.setRoot(initialPage());
    }
    public void pause() {
        animation.pause();
        Parent gamePane = scene.getRoot();
        BorderPane pausePage = new BorderPane();
        VBox buttons = new VBox();
        //create a play button
        Button playbtn = new Button("PLAY");
        playbtn.setOnAction(e -> {
           scene.setRoot(gamePane);
           gamePane.requestFocus();
           animation.play();
        });
        //create restart button
        Button restartbtn = new Button("RESTART");
        restartbtn.setOnAction(e -> {
           restart(); 
        });
        //create an exit button
        Button exitbtn = new Button("EXIT");
        exitbtn.setOnAction(e -> {
           System.exit(0); 
        });
        //add buttons to pane
        buttons.getChildren().addAll(playbtn, restartbtn, exitbtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);
        
        pausePage.setCenter(buttons);
        pausePage.setStyle("-fx-background-color: aquamarine");
        scene.setRoot(pausePage);
    }
    public BorderPane gamePage() {
        snake = new ArrayList<>();
        BorderPane root = new BorderPane();
        field.setPrefSize(400, 400);
        field.setGridLinesVisible(false);
        Insets padd = new Insets(5, 5, 5, 5);
        field.setPadding(padd);
        field.setStyle("-fx-border-color: black");
        field.setVgap(0);
        field.setHgap(0);
        cellLength = 20;

        //Controls and information section
        VBox ctls = new VBox();
        ctls.setPrefSize(100, 400);
        ctls.setStyle("-fx-background-color: burlywood");
        GridPane infoPane = new GridPane();
        infoPane.setPrefSize(100, 100);

        infoPane.add(new Text("LEVEL") , 0, 0);
        infoPane.add(levelField, 1, 0);
        infoPane.add(new Text("SCORE") , 0, 1);
        infoPane.add(scoreField, 1, 1);
        ctls.getChildren().add(infoPane);

        //create the field
        for (int x = 0; x < locsPerLine; x++) {
            for (int y = 0; y < locsPerLine; y++) {
                grid[x][y] = new Pane();
                grid[x][y].setPrefSize(cellLength, cellLength);
                grid[x][y].setStyle("-fx-background-color: cadetblue;");
                field.add(grid[x][y], y, x);

            }
        }

        //create a snake
       for (int i = 0; i < 5; i++) {
           Cell cell = new Cell();
           snake.add(cell);
       }

       //create initial food block
       food = new Rec();
       
       Text instr = new Text("Press enter to Start.");
       ctls.getChildren().add(instr);
       //Creating controls
        root.setOnKeyPressed(e -> {
           System.out.println("Key pressed");
           if (null != e.getCode())
               switch (e.getCode()) {
                case ENTER:
                if (instr.getText().contains("Start")) {
                    animation.play();
                    instr.setText("Press Esc to pause\ngame.");
                }
                    break;
                case ESCAPE:
                    pause();
                    break;
               case UP:
                    turnSnake(UP);
                    break;
                case DOWN:
                    turnSnake(DOWN);
                    break;
                case RIGHT:
                    turnSnake(RIGHT);
                    break;
                case LEFT:
                    turnSnake(LEFT);
                    break;
                default:
                    break;
            }

       });
        
        root.setLeft(field);
        root.setRight(ctls);

        return root;
    }

    //Turning the snake
    public void turnSnake(int newDirection) {
        int snakeOldDirection = snake.get(0).direction;

        //Proceeding if new direction is not equal or opposite to old direction and it has moved atleast one
        //step after the previous turn
        //The direction constants are in a way that opposites are perfect squares of each other
        if (snakeOldDirection != newDirection &&
                (Math.pow(newDirection, 2) != snakeOldDirection &&
                Math.sqrt(newDirection) != snakeOldDirection) && moved) {

            snake.get(0).direction = newDirection;
            System.out.println("Turned to: " + newDirection);

        }
        moved = false;
    }

    class Cell {
       int direction;

       int row, column;
       Rectangle cell = new Rectangle();
       Cell next;
       public Cell() {
           cell.setHeight(cellLength);
           cell.setWidth(cellLength);

           //setting direction and display for first cell
           if (snake.size() < 1) {
               cell.setStroke(Color.BROWN);
               cell.setFill(Color.BROWN);
               direction = RIGHT;
               column = locsPerLine/2 + 2;
               row = locsPerLine/2;
               next = null;

           }

           //setting direction and display for last cell
           else {
               cell.setFill(Color.BLACK);
               cell.setStroke(Color.BLACK);
               next = snake.get(snake.size()-1);
               direction = next.direction;

               switch (direction) {
                   case RIGHT: {
                       column = next.column - 1;
                       row = next.row;
                       break;
                   }
                   case LEFT: {
                       column = next.column + 1;
                       row = next.row;
                       break;
                   }
                   case UP: {
                       row = next.row + 1;
                       column = next.column;
                       break;
                   }
                   case DOWN: {
                       column = next.column;
                       row = next.row - 1;
                       break;
                   }
               }
           }

           grid[row][column].getChildren().add(cell);


       }

    }
    class Rec {
        private final Rectangle rec = new Rectangle();
        int x,y;
        public Rec() {
            //Selecting a random position for a food block
            while (true) {
                x = (int)(Math.random() * 20);
                y = (int)(Math.random() * 20);
                if (grid[x][y].getChildren().isEmpty()) {
                    break;
                }

            }
            rec.setFill(Color.RED);
            rec.setWidth(cellLength);
            rec.setHeight(cellLength);
            rec.setStroke(Color.WHITE);
            grid[x][y].getChildren().add(rec);
            System.out.println(x + ", " + y);
        }
    }

}