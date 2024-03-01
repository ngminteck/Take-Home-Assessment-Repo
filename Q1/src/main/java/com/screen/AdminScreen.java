package com.screen;

public class AdminScreen extends Screen
{
    public String DisplayOption()
    {
        return "================================================================================\n"
                + "Admin\n"
                + "================================================================================\n"
                + "1. Setup\n"
                + "2. View\n"
                + "0. Return to menu.\n"
                + "================================================================================\n";
    }
    public String DisplaySetupInstruction()
    {
        return "================================================================================\n"
                + "Admin\n"
                + "================================================================================\n"
                + "Please enter as below format to setup or enter 0 only to return.\n"
                + "<Show Number> <Number of Rows> <Number of seats per row>  <Cancellation window in minutes>\n"
                + "For making thing simple, make show number unique and only accept positive whole number and non zero and window minutes period positive whole number\n"
                + "Example 123 20 10 60.\n"
                + "================================================================================";
    }




}
