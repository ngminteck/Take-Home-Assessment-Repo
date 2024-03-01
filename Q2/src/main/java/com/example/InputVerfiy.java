package com.example;

import java.util.ArrayList;
import java.util.Objects;

public class InputVerfiy
{
    public boolean VerfiyInput(String input, Logic logic)
    {
        String[] inputData = input.split(" ");

        if(inputData.length == 0 || inputData.length % 2 !=0)
        {
            System.out.println("Invalid format. Thw command cannot bw 0 and must be even command.");
            return false;
        }

        boolean notValidInput = false;

        for(int i = 0; i < inputData.length; i +=2)
        {
            if(inputData[i].length() < 7 || inputData[i].charAt(0) != '"' || inputData[i].charAt(inputData[i].length() -1) != '"')
            {
                System.out.println("Invalid command:" + inputData[i]);
                notValidInput = true;
                break;
            }
            if(inputData[i+1].length() < 3 || inputData[i+1].charAt(0) != '"' || inputData[i+1].charAt(inputData[i+1].length() -1) != '"' || inputData[i+1].length() %2 ==0)
            {
                System.out.println("Invalid command:" + inputData[i+1]);
                notValidInput = true;
                break;
            }

            String startInfoInput = inputData[i].substring(1,inputData[i].length() - 1);
            String[] startInfo = startInfoInput.split(",");
            if(startInfo.length != 3)
            {
                System.out.println("Invalid command:" + inputData[i]);
                notValidInput = true;
                break;
            }

            int x;
            int y;
            try
            {
                x = Integer.parseInt(startInfo[0]);
                y = Integer.parseInt(startInfo[1]);
            }
            catch (NumberFormatException ignored)
            {
                System.out.println("Invalid command:" + inputData[i]);
                notValidInput = true;
                break;
            }
            if(!Objects.equals(startInfo[2], "N") && !Objects.equals(startInfo[2], "S") && !Objects.equals(startInfo[2], "E") && !Objects.equals(startInfo[2], "W"))
            {
                System.out.println("Invalid command:" + inputData[i]);
                notValidInput = true;
                break;
            }

            String actionInput = inputData[i+1].substring(1,inputData[i+1].length() - 1);
            String[] action = actionInput.split(",");

            ArrayList<String> actionList = new ArrayList<>();


            for (String s : action) {
                if (!Objects.equals(s, "f") && !Objects.equals(s, "b") && !Objects.equals(s, "l") && !Objects.equals(s, "r")) {
                    System.out.println("Invalid command:" + inputData[i + 1]);
                    notValidInput = true;
                    break;
                }

                actionList.add(s);
            }

            logic.UpdateLongestAction(actionList.size());
            logic.AddRoverAction(actionList);

            int f = 0;
            if(Objects.equals(startInfo[2], "N"))
                f = 1;
            if(Objects.equals(startInfo[2], "E"))
                f = 2;
            if(Objects.equals(startInfo[2], "S"))
                f = 3;
            if(Objects.equals(startInfo[2], "W"))
                f = 4;
            logic.AddRover(new Rover(x,y,f));

        }

        if(notValidInput)
        {
            logic.Clear();
            return false;
        }

        if(!logic.CheckRoverCollision())
        {
            if (logic.ExecuteRoverActionSuccessful())
                logic.PrintAllRoverInfo();
        }

        logic.Clear();

        return true;
    }


}
