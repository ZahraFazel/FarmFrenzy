package Model.Transportation;

import Model.Constants;
import javafx.stage.Screen;

public class Truck extends Transportations
{

    public Truck(int volume)
    {
        this.setVolume(volume);
        this.setCurrentVolume(volume);
        this.x = Screen.getPrimary().getVisualBounds().getWidth() / 2 - 400;
        this.y = Screen.getPrimary().getVisualBounds().getHeight() - 200;
        this.setWorkingTime(Constants.TRUCK_WORKING_TIME);
        this.setUpgradeCost(Constants.TRUCK_BASE_UPGRADE_COST);
    }


}
