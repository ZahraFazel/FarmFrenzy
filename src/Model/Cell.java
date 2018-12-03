package Model;

import Model.Animals.Domestic.*;
import Model.Animals.*;
import Model.Animals.Wild.Wild;
import Model.Items.Item;

import java.util.ArrayList;

public class Cell
{
    private ArrayList<Entity> stuffs;

    Cell( ArrayList<Entity> stuffs )
    {
        this.stuffs = stuffs;
    }
    public ArrayList<Entity> getStuffs()
    {
        return stuffs;
    }
    public void add(Entity newStuff)
    {
        stuffs.add(newStuff);
    }
    public void remove(Entity stuff)
    {
        if( stuffs.size() > 0 )
            stuffs.remove(stuff);
    }
    public void clear()
    {
        stuffs.clear();
    }
    public boolean[] status()
    {
        boolean[] stuffs = new boolean[5];
        for( Entity e : this.stuffs )
        {
            if( e instanceof Wild )
                stuffs[0] = true;
            else if( e instanceof Item )
                stuffs[1] = true;
            else if( e instanceof Domestic )
                stuffs[2] = true;
            else if( e instanceof Grass )
                stuffs[3] = true;
            else if( e instanceof Dog )
                stuffs[4] = true;
        }
        return stuffs;
    }
    public void update( ArrayList<Entity> stuffs )
    {
        this.stuffs.clear();
        this.stuffs = stuffs;
    }
}
