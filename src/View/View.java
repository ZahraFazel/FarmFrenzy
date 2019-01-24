package View;

import View.Graphic.Menu;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;


public class View
{
    private Group group = new Group();
    private Scene scene = new Scene(group, Menu.WIDTH, Menu.HEIGHT);

    public Scene getScene()
    {
        return scene;
    }

    public Group getGroup()
    {
        return group;
    }

    public void goHelicopter(ImageView fixedHelicopter,boolean back)
    {
        fixedHelicopter.setFitWidth(40);
        fixedHelicopter.setFitHeight(40);
        double x1 = -300,x2 = 0;
        if( back )
        {
            x1 = 0;
            x2 = -300;
        }
        MoveTransition pathTransition = new MoveTransition(fixedHelicopter, x1, 5, x2, 5, 15000);
        pathTransition.setAutoReverse(false);
        pathTransition.setCycleCount(1);
        pathTransition.play();
    }
}
