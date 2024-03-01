package com.screen;

import com.obj.Show;

import java.util.Map;

public class Screen
{

    public String DisplayOption()
    {
        return "================================================================================\n"
                + "MainMenu\n"
                + "================================================================================\n"
                + "1. Admin\n"
                + "2. User\n"
                + "0. Exit\n"
                + "================================================================================";
    }

    public String PrintShowNumberHeader(int showNumber)
    {
        return "================================================================================\n"
                + "Show Number:"
                + showNumber
                + "\n================================================================================";

    }

    public String DisplayShowList(Map<Integer, Show> showList)
    {

        StringBuilder displayShowList = new StringBuilder();
        for (int key : showList.keySet())
        {
            displayShowList.append(key).append(". Show Number:").append(key).append("\n");
        }

        if(showList.isEmpty())
            displayShowList.append("There are no any available show.\n");

        return "================================================================================\n"
                + "Show List\n"
                + "================================================================================\n"
                + displayShowList
                + "0. Return to menu.\n"
                + "================================================================================\n";
    }


}
