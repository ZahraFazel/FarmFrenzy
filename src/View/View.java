package View;

import java.util.Scanner;

public class View
{
    String command;
    public String getCommand()
    {
        return command;
    }
    public void printError(String error)
    {
        System.out.println(error);
    }
    public void levelIsFinished()
    {
        System.out.println("Level is finished.  Congratulations! :) ");
    }
    public void printInfo(String info)
    {
        System.out.println(info);
    }
    public void setCommand()
    {
        Scanner scanner = new Scanner(System.in);
        command = scanner.nextLine();
        scanner.close();
    }
}
