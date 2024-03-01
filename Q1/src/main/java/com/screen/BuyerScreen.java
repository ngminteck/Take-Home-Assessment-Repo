package com.screen;

import com.obj.Show;

import java.util.ArrayList;
import java.util.Map;

public class BuyerScreen extends Screen
{
    public String DisplayOption()
    {
        return "================================================================================\n"
                + "Buyer\n"
                + "================================================================================\n"
                + "1. Check Availability & Book\n"
                + "2. Cancel\n"
                + "0. Return to menu.\n"
                + "================================================================================";
    }

    public String DisplayBookInstruction()
    {
        return "================================================================================\n"
                + "Please enter as below format to setup or enter 0 only to return.\n"
                + "<Phone number> <Comma separated list of seats>\n"
                + "For making thing not so complicated, for now just make phone number accept any positive whole number and unique.\n" + "Example 6588454484 A1,A2\n"
                + "================================================================================";
    }

    public String DisplayCancelBookingScreenInstruction()
    {
        return "================================================================================\n"
                        + "Please enter as below format to setup or enter 0 only to return.\n"
                        + "<Ticket#>  <Phone#>\n"
                        + "================================================================================";


    }
}
