package Controller;

import Model.Farm;
import View.View;

public class Controller {
    View view = new View();
    Farm farm ;
    String[] command ;
    public void commandHandler()
    {

    }

    public void buyHandler(String animalName)
    {
        switch (animalName)
        {
            case "sheep":
                if (!farm.addSheep())
                    view.printError("Not Enough Money! :'( ");
                break;
            case "cow":
                if (!farm.addCow())
                    view.printError("Not Enough Money! :'( ");
                break;
            case "hen":
                if (!farm.addHen())
                    view.printError("Not Enough Money! :'( ");
                break;
            case "cat":
                if (!farm.addCat())
                    view.printError("Not Enough Money! :'( ");
                break;
            case "dog":
                if (!farm.addDog())
                    view.printError("Not Enough Money! :'( ");
                break;
        }
    }

    public void pickUpHandler(double x , double y)
    {
        if(!farm.pickUp(x,y))
            view.printError("Warehouse is full! :'(");
    }

    public void cageHandler(double x , double y)
    {
        if(!farm.putCage(x,y))
            view.printError("No wild animal is here!");
    }

    public void plantHandler(double x , double y)
    {
        if(!farm.plantGrass(x,y))
            view.printError("Not Enough Money! :'( ");
    }

    public void wellHandler()
    {
        if(!farm.fullWell())
            view.printError("Not Enough Money! :'( ");
    }

    public void upgradeHandler(String entityName)
    {
        if( !farm.upgrade(entityName) )
            view.printError("Not Enough Money! :'( ");
    }

    public void turnHandler(int n)
    {
        boolean isLevelFinished = false;
        for (int i = 0 ; i < n && !isLevelFinished ; i++)
            if(farm.turn())
                isLevelFinished = true;
        if (isLevelFinished)
            view.levelIsFinished();
    }

    public void loadHandler(String path){}

    public void runHandler(String mapName){}

    public void saveHandler(String path){}

    public void loadGameHandler(String path){}

    public void saveGameHandler(String path){}

    public void printHandler(String what)
    {
        switch (what)
        {
            case "info":view.printInfo(farm.printInfo());break;
            case "map"://TODO view.printInfo(farm.printMap());
                break;
            case "levels":view.printInfo(farm.printLevel());break;
            case "warehouse":view.printInfo(farm.printWareHouse());break;
            case "well":view.printInfo(farm.printWell());break;
            case "workshops"://TODO view.printInfo(farm.printWorkshops());
                break;
            case "helicopter"://TODO view.printInfo(farm.printHelicopter());
                break;
            case "truck"://TODO view.printInfo(farm.printTruck());
                break;
        }
    }

    public void addToTransportationHandler(String  vehicle , String name , int count)
    {
        if (vehicle.equals("helicopter"))
        {
            int c = farm.addToHellicopter(name,count);
            if( c < count )
                view.printError("More than helicopter capacity! "+Integer.toString(c)+" is added.");
        }
        else if(vehicle.equals("truck"))
        {
            int c = farm.addToTruck(name,count);
            if( c < count )
                view.printError("More than helicopter capacity! "+Integer.toString(c)+" is added.");
        }
    }

    public void clearFromTransportationHandler(String vehicle)
    {
        if (vehicle.equals("helicopter"))
            farm.clearFromHelicopter();
        else if(vehicle.equals("truck"))
            farm.clearFromTruck();
    }

    public void goHandler(String vehicle)
    {
        farm.goTransportation(vehicle.equals("truck"));
    }

    public void startWorkShopHandler(String name)
    {
        farm.startWorkShop(name);
    }

}
