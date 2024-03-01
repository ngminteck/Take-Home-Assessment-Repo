package com.example;

public class Rover
{
    private int x;
    private int y;
    private int dir;




    Rover(int posX, int posY, int facing)
    {
        x = posX;
        y = posY;
        dir = facing;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public String GetFacingString()
    {
        if(dir == 1)
            return "North";
        if(dir == 2)
            return "East";
        if(dir == 3)
            return "South";
        if(dir == 4)
            return "West";

        return "";
    }

    public void RotateLeft()
    {
        --dir;
        if(dir < 1)
            dir = 4;
    }

    public void RotateRight()
    {
        ++dir;
        if(dir > 4)
            dir = 1;
    }
    public void MoveForward()
    {
        if(dir == 1)
            y = y + 1;
        if(dir == 2)
            x = x + 1;
        if(dir == 3)
            y = y -1;
        if(dir == 4)
            x = x - 1;
    }

    public void MoveBackward()
    {
        if(dir == 1)
            y = y - 1;
        if(dir == 2)
            x = x - 1;
        if(dir == 3)
            y = y  + 1;
        if(dir == 4)
            x = x  + 1;
    }

}
