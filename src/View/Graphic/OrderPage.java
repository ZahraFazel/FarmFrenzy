package View.Graphic;

import Model.Constants;
import Model.Farm;
import Network.ClientSender;
import View.View;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class OrderPage
{
    private Group group = new Group();
    private Scene scene = new Scene(group, Constants.WIDTH,Constants.HEIGHT);
    private int height = 0 , itemNumber = 0 , width = 0;
    private HashMap<String,Integer> firstMarket = new HashMap<>();

    public Scene getScene(Stage stage, View view, Farm farm, boolean isMultiplayer , HashMap<String , Integer> market,
                          ConcurrentHashMap<String ,Image> items, ImageView rightHelicopter, ImageView fixedHelicopter, AnimationTimer aTimer, ClientSender clientSender)
    {
        height = 0;
        itemNumber = 0;
        width = 0;
        for( String s : market.keySet() )
            firstMarket.put(s,market.get(s));
        try
        {
            Image order = new Image(new FileInputStream("src\\Resources\\Graphic\\Service\\Helicopter\\order"
                    +farm.getHelicopter().getLevel()+".png"), Menu.WIDTH, Menu.HEIGHT, false, true);
            ImageView orderView = new ImageView(order);
            orderView.setX(0);
            orderView.setY(0);
            group.getChildren().addAll(orderView);
            insertBack(farm,stage,view , aTimer,market);
            insertOk(farm,stage,view,rightHelicopter,fixedHelicopter,aTimer, clientSender);
            insertItems(farm,items,isMultiplayer,market);
        }
        catch ( Exception e ) { e.printStackTrace(); }
        return scene;
    }

    private void insertBack(Farm farm , Stage stage , View view,AnimationTimer animationTimer,HashMap<String , Integer> market)
    {
        try
        {
            Image back = new Image(new FileInputStream("src\\Resources\\Graphic\\Game UI\\backButton.png")
                    , 80, 80, false, true);
            ImageView backView = new ImageView(back);
            backView.setX(Constants.WIDTH - 200);
            backView.setY(Constants.HEIGHT - 100);
            backView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    farm.clearHelicopterBeforeGo();
                    group.getChildren().clear();
                    for( String s : firstMarket.keySet() )
                        market.put(s,firstMarket.get(s));
                    animationTimer.start();
                    stage.setScene(view.getScene());
                }
            });
            group.getChildren().addAll(backView);
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }

    private void insertOk(Farm farm,Stage stage,View view,ImageView rightHelicopter, ImageView fixedHelicopter
            ,AnimationTimer animationTimer, ClientSender clientSender)
    {
        try
        {
            Image ok = new Image(new FileInputStream("src\\Resources\\Graphic\\Game UI\\okButton.png"),
                    150, 79, false, true);
            ImageView okView = new ImageView(ok);
            okView.setX(Constants.WIDTH - 400);
            okView.setY(Constants.HEIGHT - 100);
            okView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if( farm.getHelicopter().getSpentMoney() > 0 )
                    {
                        if (clientSender != null)
                            clientSender.buyItemsFromMarket(farm.getHelicopter().getItems());
                        farm.goTransportation(false);
                        farm.getHelicopter().setPrevMovingX(350);
                        farm.getHelicopter().setNextMovingX(350 + farm.getHelicopter().getMovingScale());
                        view.getGroup().getChildren().remove(fixedHelicopter);
                        animationTimer.start();
                        stage.setScene(view.getScene());
                    }
                }
            });
            group.getChildren().addAll(okView);
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }

    private void insertItems(Farm farm,ConcurrentHashMap<String,Image> items,boolean isMultiplayer, HashMap<String , Integer> market)
    {
        try
        {
            height = 1;
            double scale = ( Constants.HEIGHT - 80 ) / 18;
            for( String item : Constants.ITEM_NAMES )
            {
                if( height > 17 )
                {
                    height = 1;
                    width = 1;
                }
                itemNumber = 0;
                ImageView itemView = new ImageView(items.get(item));
                itemView.setX(30 + width * ( 3 * scale + 100));
                itemView.setY(scale * ( height - 1 ) + 85);
                itemView.setFitHeight(scale);
                itemView.setFitWidth(scale);
                Label numberOfItems = new Label(Integer.toString(market.get(item)));
                ImageView plusView = new ImageView(
                        new Image(new FileInputStream("src\\Resources\\Graphic\\Game UI\\plusButton.png")
                                , scale, scale, false, true));
                ImageView minusView = new ImageView(
                        new Image(new FileInputStream("src\\Resources\\Graphic\\Game UI\\minusButton.png")
                                , scale, scale, false, true));
                if( isMultiplayer )
                {
                    if( market.get(item) > 0 )
                    {
                        numberOfItems.setTextFill(Color.rgb(54, 16, 0));
                        numberOfItems.relocate(45 + scale + width * (4 * scale + 80), scale * (height - 1) + 80);
                        numberOfItems.setFont(Font.font("Segoe Print", FontWeight.BOLD, FontPosture.REGULAR, 14));
                        plusView.setX(60 + width * ( 4 * scale + 80) + 2 * scale);
                        plusView.setY(scale * ( height - 1 ) + 80);
                        minusView.setX(3 * scale + 75 + width * ( 4 * scale + 80));
                        minusView.setY(scale * ( height - 1 ) + 80);
                        group.getChildren().addAll(itemView,numberOfItems,plusView,minusView);
                    }
                }
                else
                {
                    plusView.setX(50 + width * (3 * scale + 100) + scale);
                    plusView.setY(scale * (height - 1) + 80);
                    minusView.setX(2 * scale + 60 + width * (3 * scale + 100));
                    minusView.setY(scale * (height - 1) + 80);
                    group.getChildren().addAll(itemView,plusView,minusView);
                }
                ImageView itemView1 = new ImageView(items.get(item));
                Label number = new Label("");
                itemView1.setX(500 + width * ( 3 * scale + 100));
                itemView1.setY(scale * ( height - 1 ) + 80);
                itemView1.setFitHeight(scale);
                itemView1.setFitWidth(scale);
                number.setTextFill(Color.rgb(54,16,0));
                number.relocate(520 + scale + width * ( 3 * scale + 100) ,scale * ( height - 1 ) + 80);
                number.setFont(Font.font("Segoe Print", FontWeight.BOLD, FontPosture.REGULAR,14));
                height++;
                plusView.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        boolean canBuy = false;
                        if( market.get(item) > 0 || !isMultiplayer )
                        {
                            if( !farm.getHelicopter().contains(item) )
                                itemNumber = 0;
                            if( farm.getHelicopter().getCurrentVolume() > 0 )
                            {
                                group.getChildren().removeAll(itemView1,number);
                                if( farm.addToHelicopter(item,1) == 1 )
                                {
                                    itemNumber++;
                                    number.setText(Integer.toString(itemNumber));
                                    group.getChildren().addAll(itemView1,number);
                                    canBuy = true;
                                }
                                else if( itemNumber > 0 )
                                    group.getChildren().addAll(itemView1,number);
                            }
                            if( isMultiplayer && canBuy )
                            {
                                group.getChildren().remove(numberOfItems);
                                market.replace(item,market.get(item) - 1);
                                numberOfItems.setText(Integer.toString(market.get(item)));
                                group.getChildren().add(numberOfItems);
                            }
                        }
                    }
                });
                minusView.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        if( isMultiplayer )
                        {
                            if( market.get(item) < firstMarket.get(item) )
                            {
                                group.getChildren().remove(numberOfItems);
                                market.replace(item,market.get(item) + 1);
                                numberOfItems.setText(Integer.toString(market.get(item)));
                                group.getChildren().add(numberOfItems);
                            }
                        }
                        if( itemNumber > 1 )
                        {
                            group.getChildren().removeAll(itemView1,number);
                            itemNumber--;
                            farm.clearOneItemFromHelicopter(item);
                            number.setText(Integer.toString(itemNumber));
                            group.getChildren().addAll(itemView1,number);
                        }
                        else if( itemNumber == 1 )
                        {
                            farm.clearOneItemFromHelicopter(item);
                            group.getChildren().removeAll(number,itemView1);
                            itemNumber = 0;
                        }
                    }
                });
            }
        }
        catch ( Exception e ) { e.printStackTrace(); }
    }
}
