package com.example;

import java.util.ArrayList;
import java.util.Objects;

public class Logic
{
    private int longestAction = 0;
    private final ArrayList<Rover> roverList= new ArrayList<>();
    private final ArrayList<ArrayList<String>> roverActionList = new ArrayList<>();

    public void AddRover(Rover rover)
    {
        roverList.add(rover);
    }

    public void AddRoverAction(ArrayList<String> action)
    {
        roverActionList.add(action);
    }

    public void Clear()
    {
        longestAction = 0;
        roverList.clear();
        roverActionList.clear();
    }
    
    public void UpdateLongestAction(int size)
    {

        if(longestAction < size)
            longestAction = size;
    }

    public boolean CheckRoverCollision()
    {
        for(int i = 0; i < roverList.size(); ++i)
        {
            for(int j = 0; j < roverList.size(); ++j)
            {
                if( i == j)
                    continue;
                
                if(roverList.get(i).getX() == roverList.get(j).getX() && roverList.get(i).getY() == roverList.get(j).getY())
                {
                    System.out.println("Rover " + (i + 1) + " collided with rover " + (j+1) + " at " + roverList.get(i).getX() + "," + roverList.get(i).getY() +".");
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean ExecuteRoverActionSuccessful()
    {
        for(int i = 0; i < longestAction ; ++i)
        {
            for(int j =0 ; j < roverActionList.size() ; ++j)
            {
                if(i < roverActionList.get(j).size())
                {
                    String action = roverActionList.get(j).get(i);
                    
                    if(Objects.equals(action, "f"))
                        roverList.get(j).MoveForward();
                    if(Objects.equals(action, "b"))
                        roverList.get(j).MoveBackward();
                    if(Objects.equals(action, "l"))
                        roverList.get(j).RotateLeft();
                    if(Objects.equals(action, "r"))
                        roverList.get(j).RotateRight();
                    
                }
            }
            
            if(CheckRoverCollision())
                return false;
            
        }
        return true;
    }

    public void PrintAllRoverInfo()
    {
        for(int i =0; i < roverList.size(); ++i)
        {
            System.out.println("Rover: " + (i+1));
            System.out.println("Final Coordinate: " + roverList.get(i).getX() + "," + roverList.get(i).getY());
            System.out.println("Final Direction: " + roverList.get(i).GetFacingString());
        }
    }
}
