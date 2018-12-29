package Controller;

import Model.Farm;
import View.View;
import com.gilecode.yagson.YaGson;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;

public class Controller {
    private View view = new View();
    private Farm farm ;
    private String command ;
    private String path;
    private boolean isLevelFinished = false;

    public void getCommand(Scanner scanner)
    {
        while(!isLevelFinished)
        {
            view.setCommand(scanner);
            command = view.getCommand();
            try
            {
                commandHandler();
            }
            catch ( Exception e )
            {
                view.printError(e.getMessage());
            }
        }
    }

    private void commandHandler() throws Exception
    {
        command = command.toLowerCase();
        if( command.matches("buy sheep|cow|hen|cat|dog") )
            buyHandler(command.substring(4));
        else if( command.matches("pickup [0-9]+ [0-9]+"))
            pickUpHandler(Double.parseDouble(command.substring(7,8)),Double.parseDouble(command.substring(9)));
        else if ( command.matches("cage [0-9]+ [0-9]+"))
            cageHandler(Double.parseDouble(command.substring(5,6)),Double.parseDouble(command.substring(7)));
        else if( command.matches("plant [0-9]+ [0-9]+") )
            plantHandler(Double.parseDouble(command.substring(6,7)),Double.parseDouble(command.substring(8)));
        else if( command.matches("well") )
            wellHandler();
        else if( command.matches("start " +
                "eggpowderplant|cakebaker|cookiebakery|customfactory|sweingfactory|spinnery|weavingfactory") )
            startWorkShopHandler(command.substring(6));
        else if( command.matches("upgrade " +
                "eggpowderplant|cakebaker|cookiebakery|customfactory|sweingfactory|spinnery|weavingfactory|" +
                "cat|well|truck|helicopter|warehouse"))
            upgradeHandler(command.substring(7));
        else if( command.startsWith("load game "))
            loadGameHandler(command.substring(10));
        else if( command.startsWith("save game "))
            saveGameHandler(command.substring(10));
        else if( command.startsWith("run ") )
            runHandler(command.substring(4));
        else if( command.matches("turn [0-9]+") )
            turnHandler(Integer.parseInt(command.substring(5)));
        else if( command.matches("truck|helicopter add [{a-z, }]+ [0-9]+") )
            addToTransportationHandler(command.startsWith("truck"),command.substring(command.indexOf("add")+4,command.indexOf("add")));
        else if( command.matches("truck|helicopter clear"))
            clearFromTransportationHandler(command.startsWith("truck"));
        else if( command.matches("truck|helicopter go") )
            goHandler(command.startsWith("truck"));
        else if( command.matches("print info|map|levels|warehouse|well|workshops|truck|helicopter") )
            printHandler(command.substring(6));
        else if( command.startsWith("load custom ") )
            loadCustomHandler(command.substring(12));
        else
            throw new Exception("Wrong Command Format");
    }

    private void buyHandler(String animalName) throws Exception
    {
        boolean isBought = false;
        switch (animalName)
        {
            case "sheep":isBought = farm.addSheep(true);break;
            case "cow": isBought = farm.addCow(true);break;
            case "hen": isBought = farm.addHen(true);break;
            case "cat": isBought = farm.addCat(true);break;
            case "dog": isBought = farm.addDog(true);break;
        }
        if( !isBought )
            throw new Exception("Not Enough Money! :'( ");
    }

    private void pickUpHandler(double x , double y) throws Exception
    {
        if(!farm.pickUp(x,y))
            throw new Exception("Warehouse is full! :'(");
    }

    private void cageHandler(double x , double y) throws Exception
    {
        if(!farm.putCage(x,y))
            throw new Exception("No wild animal is here!");
    }

    private void plantHandler(double x , double y) throws Exception
    {
        if(!farm.plantGrass(x,y))
            throw new Exception("Well is empty! :'( ");
    }

    private void wellHandler() throws Exception
    {
        if(!farm.fullWell())
            throw new Exception("Not Enough Money! :'( ");
    }

    private void upgradeHandler(String entityName) throws Exception
    {
        int result = farm.upgrade(entityName);
        if( result == 1 )
            throw new Exception("Not Enough Money! :'( ");
        else if( result == 2 )
            throw new Exception(entityName+" is at maximum level!");
        else if( result == 3 )
            throw new Exception(entityName+" doesn't exits");
    }

    private void turnHandler(int n)
    {
        for (int i = 0 ; i < n && !isLevelFinished ; i++)
            if(farm.turn())
                isLevelFinished = true;
        if (isLevelFinished)
            view.levelIsFinished();
    }

    private void loadCustomHandler(String path) throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(path);
            this.path = path;
            File file = new File(path+"\\custom.txt");
            if( file.exists() )
            {
                Scanner scanner = new Scanner(inputStream);
                String name = scanner.next() , input = scanner.nextLine().substring(8) , output = scanner.next();
                ArrayList<String> inputs = new ArrayList<>();
                for( String s : input.split(" ") )
                    inputs.add(s);
                farm.makeCustomWorkshop(name,inputs,output);
            }
        }
        catch ( FileNotFoundException e )
        {
            throw new Exception("No such directory exists!");
        }
        finally
        {
            inputStream.close();
        }
    }

    private void runHandler( String mapName ) throws Exception
    {
        try
        {
            mapHandler(mapName);
            goalsHandler();
            workShopHandler();
        }
        catch ( Exception e )
        {
            throw new Exception("No such directory exists!");
        }
    }

    private void mapHandler( String mapName ) throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(path + "\\" + mapName + ".txt");
            Scanner scanner = new Scanner(inputStream);
            int length = 0;
            while(scanner.hasNext())
            {
                String string = scanner.next();
                switch (string)
                {
                    case "length":
                        length = scanner.nextInt();
                        break;
                    case "width":
                        farm = new Farm(length, scanner.nextInt());
                        break;
                    case "well":
                        farm.makeWell(scanner.nextInt());
                        break;
                    case "wareHouse":
                        farm.makeWareHouse(scanner.nextInt());
                        break;
                    case "truck":
                        farm.makeTruck(scanner.nextInt());
                        break;
                    case "helicopter":
                        farm.makeHelicopter(scanner.nextInt());
                        break;
                    case "money":
                        farm.increaseMoney(scanner.nextInt());
                        break;
                    case "hen":
                        int count = scanner.nextInt();
                        for (int i = 0; i < count; i++)
                            farm.addHen(false);
                        break;
                    case "cow":
                        count = scanner.nextInt();
                        for (int i = 0; i < count; i++)
                            farm.addCow(false);
                        break;
                    case "sheep":
                        count = scanner.nextInt();
                        for (int i = 0; i < count; i++)
                            farm.addSheep(false);
                        break;
                    case "cat":
                        count = scanner.nextInt();
                        for (int i = 0; i < count; i++)
                            farm.addCat(false);
                        break;
                    case "dog":
                        count = scanner.nextInt();
                        for (int i = 0; i < count; i++)
                            farm.addDog(false);
                        break;
                    default:
                }
            }
        }
        catch ( FileNotFoundException e )
        {
            throw e;
        }
        finally
        {
            if (inputStream != null)
                inputStream.close();
        }
    }

    private void goalsHandler() throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(path+"\\goals.txt");
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext())
            {
                String name = scanner.next();
                int count = scanner.nextInt();
                farm.getGoals().put(name, count);
            }
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        finally
        {
            if (inputStream != null)
                inputStream.close();
        }
    }

    private void workShopHandler() throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(path+"\\workShops.txt");
            Scanner scanner = new Scanner(inputStream);
            ArrayList<String> workShops = new ArrayList<>();
            while(scanner.hasNext())
            {
                workShops.add(scanner.nextLine());
            }
            farm.makeWorkShops(workShops);
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        finally
        {
            if (inputStream != null)
                inputStream.close();
        }
    }

    private void saveGameHandler(String path) throws Exception
    {
        Date date = new Date();
        try
        {
            OutputStream outputStream = new FileOutputStream(path + "\\game" + Long.toString(date.getTime()) + ".txt");
            Formatter formatter = new Formatter(outputStream);
            YaGson yaGson = new YaGson();
            String savedFarm = yaGson.toJson(farm);
            formatter.format(savedFarm);
            formatter.flush();
            formatter.close();
        }
        catch ( IOException e )
        {
            throw new Exception("No such directory exists!");
        }

    }

    private void loadGameHandler(String path) throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);
            YaGson yaGson = new YaGson();
            String savedFarm = scanner.nextLine();
            farm = yaGson.fromJson(savedFarm,Farm.class);
        }
        catch ( IOException e )
        {
            throw new Exception("No such directory exsits!");
        }
        finally
        {
            if( inputStream != null )
                inputStream.close();
        }
    }

    private void printHandler(String what)
    {
        switch (what)
        {
            case "info":view.printInfo(farm.printInfo());break;
            case "map":view.printInfo(farm.printMap());break;
            case "levels":view.printInfo(farm.printLevel());break;
            case "warehouse":view.printInfo(farm.printWareHouse());break;
            case "well":view.printInfo(farm.printWell());break;
            case "workshops":view.printInfo(farm.printWorkshop(what));break;
            case "helicopter":view.printInfo(farm.printTransportation(false));break;
            case "truck":view.printInfo(farm.printTransportation(true));break;
        }
    }

    private void addToTransportationHandler(boolean  vehicle , String name_count) throws Exception
    {
        String[] tmp = name_count.split(" ");
        int count = Integer.parseInt(tmp[tmp.length - 1]);
        String name = "";
        for( int  i = 0 ; i < tmp.length - 2 ; i++ )
            name = name.concat(tmp[i] + " ");
        name = name.concat(tmp[tmp.length - 2]);
        if (vehicle)
        {
            int c = farm.addToTruck(name,count);
            if( c == -1 )
                throw new Exception("Truck is moving right now!");
            else if( c < count )
                throw new Exception("More than truck capacity!      "+Integer.toString(c)+" is added.");
        }
        else
        {
            int c = farm.addToHelicopter(name,count);
            if( c == -1 )
                throw new Exception("Helicopter is moving right now!");
            else if( c < count )
                throw new Exception("More than helicopter capacity!      "+Integer.toString(c)+" is added.");
        }
    }

    private void clearFromTransportationHandler(boolean vehicle) throws Exception
    {
        if (vehicle)
        {
            if( !farm.clearFromTruck() )
                throw new Exception("Truck is moving right now!");
        }
        else
            if( !farm.clearFromHelicopter() )
                throw new Exception("Helicopter is moving right now!");
    }

    private void goHandler(boolean vehicle) throws Exception
    {
        if( !farm.goTransportation(vehicle) )
        {
            if( vehicle )
                throw new Exception("Truck is moving right now!");
            else
                throw new Exception("Helicopter is moving right now!");
        }
    }

    private void startWorkShopHandler(String name) throws Exception
    {
        int result = farm.startWorkShop(name);
        if( result == -2 )
            throw new Exception("This workshop is working now!");
        else if( result == -1 )
            throw new Exception("This workshop doesn't exist!");
        else if( result == 0 )
            throw new Exception("Inputs of this workshop don't exist in warehouse");
    }

}
