package View.Graphic;

import Controller.Controller;
import Model.Player;
import View.View;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Start
{
    private Group group = new Group();
    private Scene scene = new Scene(group, Menu.WIDTH, Menu.HEIGHT);
    private Player player;
    //todo set players name
    class levelHandler implements EventHandler<MouseEvent>{
        private final int number;
        private Stage stage;
        private boolean newGame = false;
        levelHandler(int n, Stage stage){
            this.number = n;
            this.stage = stage;
        }
        @Override
        public void handle(MouseEvent event) {
            if (player.getLastLevel() < number){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("This level is not opened for you!");
                alert.setContentText("Choose a lower level");
                alert.showAndWait();
                return;
            }

            Controller controller = new Controller();
            try{
                controller.setPlayer(player);
                controller.setLevel(number);
                String path = "src\\SavedGames\\"+player.getName()+"-"+Integer.toString(player.getId())+"-"+Integer.toString(number);
                controller.canGameBeContinued(path);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Would you like to continue?");
                alert.setHeaderText("You have started this level before");
                ButtonType b1 = new ButtonType("Yes");
                ButtonType b2 = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(b1, b2);
                alert.showAndWait().ifPresent(result -> {
                    if (result == b1) {
                        try {
                            controller.loadGameHandler(path);
                        }
                        catch (Exception e){
                        }
                        //todo make the game from controller
                    }
                    if (result == b2)
                        newGame = true;
                });
            }
            catch (Exception e){
                newGame = true;
            }
            finally {
                if (newGame){
                    //todo ask if player wanted custom workshop
                    try{
                        String path2 = "src\\Resources\\Levels\\Level"+Integer.toString(number)+".txt";
                        controller.loadCustomHandler(path2);
                    }
                    catch (Exception e) {
                    }
                    finally {
                        try {
                            controller.runHandler();
                        } catch (Exception e) {

                        }
                    }
                }
                View view = new View(controller, stage);
            }

        }
    }

    public Scene getScene()
    {
        return scene;
    }

    public Start(Stage stage, Menu menu, Player player)
    {
        //todo make players
        this.player = player;
        insertBack(stage,menu);
        insertLevels(stage);
    }

    private void insertLevels(Stage stage)
    {
        Text[] level = new Text[10];
        try
        {
            Image image = new Image(new FileInputStream("src\\Resources\\Graphic\\Level.png")
                    , Menu.WIDTH, Menu.HEIGHT, false, true);
            ImageView levels = new ImageView(image);
            levels.setY(0);
            levels.setX(0);
            group.getChildren().addAll(levels);
            for( int i = 0 ; i < 10 ; i++ )
            {
                level[i] = new Text( ( i % 2) * ( Menu.WIDTH - 200 ) + ( ( i + 1 ) % 2 ) * 200 , 120 + i / 2 * 100
                        , "Level "+Integer.toString(i + 1));
                level[i].setFont(Font.font("Segoe Print", FontWeight.BOLD, FontPosture.REGULAR,30));
                group.getChildren().addAll(level[i]);
            }
        }
        catch ( IOException e ){}
        finally {
            level[0].setOnMouseClicked(new levelHandler(1, stage));
            level[1].setOnMouseClicked(new levelHandler(2, stage));
            level[2].setOnMouseClicked(new levelHandler(3, stage));
            level[3].setOnMouseClicked(new levelHandler(4, stage));
            level[4].setOnMouseClicked(new levelHandler(5, stage));
            level[5].setOnMouseClicked(new levelHandler(6, stage));
            level[6].setOnMouseClicked(new levelHandler(7, stage));
            level[7].setOnMouseClicked(new levelHandler(8, stage));
            level[8].setOnMouseClicked(new levelHandler(9, stage));
            level[9].setOnMouseClicked(new levelHandler(10, stage));
        }
    }

    private void insertBack( Stage stage , Menu menu )
    {
        try
        {
            Image image = new Image(new FileInputStream("src\\Resources\\Graphic\\back.png")
                    , 100, 70, false, true);
            ImageView back = new ImageView(image);
            back.setY(Menu.HEIGHT - 200);
            back.setX(Menu.WIDTH - 400);
            group.getChildren().addAll(back);
            back.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    stage.setScene(menu.getScene());
                }
            });
        }
        catch ( IOException e ){}
    }
}
