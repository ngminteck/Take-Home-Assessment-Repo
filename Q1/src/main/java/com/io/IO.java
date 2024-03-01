package com.io;

import com.logic.Logic;
import com.obj.Show;
import com.obj.Ticket;
import com.screen.Screen;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class IO
{
    private final Scanner sc;

    private final Logic logic = new Logic();


    public IO()
    {
        sc = new Scanner(System.in).useDelimiter("\n");
    }

    public void ScreenContinue()
    {
        System.out.println("Key any key to continue.");
        sc.next();
    }


    public int UserIntChoice(String msg, int maxValidChoice)
    {
        int userOption;
        do {
            userOption = -1;
            System.out.println(msg);
            if(sc.hasNextInt()) {
                userOption = sc.nextInt();

                if(userOption < 0 && userOption > maxValidChoice)
                    System.out.println("Invalid request!");

            }
            else {
                sc.next();
                System.out.println("Invalid request!");
            }
        }
        while (userOption < 0 && userOption > maxValidChoice);
        return userOption;
    }

    public int UserShowSelection(String msg, Map<Integer, Show> showList)
    {
        int userOption;
        do {
            System.out.println(msg);
            if(sc.hasNextInt()) {
                userOption = sc.nextInt();

                if(userOption == 0 || showList.containsKey(userOption))
                    break;
                else
                    System.out.println("Invalid request!");

            }
            else {
                sc.next();
                System.out.println("Invalid request!");
            }
        }
        while (true);
        return userOption;
    }

    public void SetupInput(String msg, Map<Integer, Show> showList)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;

            if(VerifySetupInput(input, showList))
            {
                System.out.println("Key any key to continue.");
                sc.next();
                break;
            }

        }
        while (true);
    }

    public boolean VerifySetupInput(String input, Map<Integer, Show> showList)
    {
        String[] data = input.split(" ");
        String dataTypeErrorMsg= "";

        if(data.length !=4)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int [] number = new int[4];
        boolean invalidDataType = false;

        /* check correct type data */
        for(int i =0; i < data.length; ++i)
        {
            try
            {
                number[i] = Integer.parseInt(data[i]);
            }
            catch (NumberFormatException ignored)
            {
                invalidDataType = true;
                dataTypeErrorMsg = data[i] + " is not a number.";
                break;
            }
        }
        if(invalidDataType)
        {
            System.out.println(dataTypeErrorMsg);
            return false;
        }

        /* check setup constraint*/
        return logic.VerifySetup(number[0],number[1],number[2],number[3], showList);

    }




    public void BookingInput(String msg, int showNumber, Show show)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;


            boolean status = VerifyBookingInput(input, showNumber, show);
            System.out.println("Key any key to continue.");
            sc.next();
            if(status)
             {

                break;
             }

        }
        while (true);
    }

    public boolean VerifyBookingInput(String input, int showNumber, Show show)
    {
        String[] data = input.split(" ");
        if(data.length !=2)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int phoneNumber;
        try
        {
            phoneNumber= Integer.parseInt(data[0]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println(data[0] + " is not a number.");
            return false;
        }

        return logic.VerifyBooking(showNumber, phoneNumber, data[1], show);

    }


    public void CancelBookingInput(String msg, Map<Integer, Show> showList)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;

            if(VerifyCancelBookingInput(input,showList))
            {
                System.out.println("Key any key to continue.");
                sc.next();
                break;
            }

        }
        while (true);
    }

    public boolean VerifyCancelBookingInput(String input, Map<Integer, Show> showList)
    {
        String[] data = input.split(" ");

        if(data.length !=2)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        int phoneNumber;
        try
        {
            phoneNumber= Integer.parseInt(data[1]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println(data[1] + " is not a number.");
            return false;
        }

        String[] no = data[0].split("-");
        if(no.length !=2)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int showNumber;
        try
        {
            showNumber= Integer.parseInt(no[0]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        int ticketNumber;
        try
        {
            ticketNumber= Integer.parseInt(no[1]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        return logic.VerifyCancelBooking(showNumber,phoneNumber,ticketNumber, showList);


    }

}

