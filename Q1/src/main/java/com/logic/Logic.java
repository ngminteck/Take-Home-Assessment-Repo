package com.logic;

import com.obj.Show;
import com.obj.Ticket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

public class Logic {
    public boolean VerifySetup(int showNumber, int row, int col, int windowMin, Map<Integer, Show> showList)
    {
        /* make show number cannot be less than 1, 0 for exit option.*/
        if(showNumber < 1)
        {
            System.out.println("Show Number cannot be negative or 0.");
            return false;
        }

        if(row<1 || row> 26)
        {
            System.out.println("Row must be between 1 to 26.");
            return false;
        }

        if(col <1 || col > 10)
        {
            System.out.println("Row per seat must be between 1 to 10.");
            return false;
        }

        if(windowMin < 0)
        {
            System.out.println("Cancel window cannot be negative.");
            return false;
        }

        if(showList.containsKey(showNumber))
        {
            System.out.println("Show number " + showNumber + " already exist.");
            return false;
        }

        showList.put(showNumber, new Show(row,col,windowMin));
        System.out.println("Show number:" + showNumber + "\nNumber of Rows:" + row + "\nNumber of seats per row:" + col + "\nCancellation window in minutes:" + windowMin + "\nSetup sucess.");
        return true;
    }

    public boolean VerifyBooking(int showNumber, int phoneNumber, String seatInput, Show show)
    {
        if(phoneNumber < 0)
        {
            System.out.println("Phone number " + phoneNumber + " cannot be negative.");
            return false;
        }

        if(show.getTicketInfo().get(phoneNumber) != null)
        {
            System.out.println(phoneNumber + " already exist.");
            return false;
        }

        seatInput = seatInput.toUpperCase();
        String[] seat = seatInput.split(",");
        ArrayList<Integer> numberPair= new ArrayList<>();
        /*check valid format*/
        for (String s : seat) {
            if (s.length() == 3 && Character.isAlphabetic(s.charAt(0)) && s.charAt(1) == '1' && s.charAt(2) == '0') {
                if (show.getCol() == 10) {
                    int rowNumber = s.charAt(0) - 64;
                    if (show.getRow() >= rowNumber) {
                        if (show.GetSeatSOccupied(rowNumber - 1, 9)) {
                            System.out.println(s + " seat is unavailable");
                            return false;
                        } else {
                            numberPair.add(rowNumber - 1);
                            numberPair.add(9);
                        }
                    } else {
                        System.out.println("No " + rowNumber + " row number in show number" + showNumber + ". Please review seat " + s + ".");
                        return false;
                    }
                } else {
                    System.out.println("No " + 10 + " column number in show number " + showNumber + ". Please review seat " + s + ".");
                    return false;
                }
            } else if (s.length() == 2 && Character.isAlphabetic(s.charAt(0)) && Character.isDigit(s.charAt(1)) && s.charAt(1) != '0') {
                /* 48 ascii table is 0 */
                int colNumber = s.charAt(1) - 48;

                if (show.getCol() >= colNumber) {
                    int rowNumber = s.charAt(0) - 64;
                    if (show.getRow() >= rowNumber) {

                        if (show.GetSeatSOccupied(rowNumber - 1, colNumber - 1)) {
                            System.out.println(s + " seat is unavailable");
                            return false;
                        } else {
                            numberPair.add(rowNumber - 1);
                            numberPair.add(colNumber - 1);
                        }
                    } else {
                        System.out.println("No " + rowNumber + " row number in show number " + showNumber + ". Please review seat " + s + ".");
                        return false;
                    }
                } else {
                    System.out.println("No " + colNumber + " column number in show number " + showNumber + ". Please review seat " + s + ".");
                    return false;
                }
            } else {
                System.out.println(s + " is invalid format for seat.");
                return false;
            }
        }
        /* mark all available seat occupied */
        for(int i = 0; i < numberPair.size(); i+=2)
        {
            show.setSeatSOccupied(numberPair.get(i),numberPair.get(i + 1), true);
        }

        String ticketText = "Ticker Number:" + showNumber + "-" + show.GenerateNewTicketNumber();
        show.BookShow(phoneNumber, seatInput);
        System.out.println("================================================================================");
        System.out.println("Show Number:" + showNumber);
        System.out.println(ticketText);
        System.out.println("Phone Number:" + phoneNumber);
        System.out.println("Seat:" + seatInput);
        System.out.println("Book Success!");
        System.out.println("================================================================================");
        return true;
    }

    public boolean VerifyCancelBooking(int showNumber, int phoneNumber, int ticketNumber, Map<Integer, Show> showList)
    {
        /* make show number cannot be less than 1, 0 for exit option.*/
        if(showNumber < 1)
        {
            System.out.println("Phone number " + phoneNumber + " cannot be negative.");
            return false;
        }

        if(phoneNumber < 0)
        {
            System.out.println(phoneNumber + " cannot be negative.");
            return false;
        }

        /* check show exist in the collection or not */
        if(!showList.containsKey(showNumber))
        {
            System.out.println("Show number " + showNumber + " not found.");
            return false;
        }

        Show show = showList.get(showNumber);
        /* check phone number exist in the collection or not */
        if(!show.getTicketInfo().containsKey(phoneNumber))
        {
            System.out.println("Phone number "+ phoneNumber + " not found in show number " + showNumber + ".");
            return false;
        }

        Ticket ticket = show.getTicketInfo().get(phoneNumber);
        if(ticketNumber != ticket.getTicketNo())
        {
            System.out.println("Ticket number not match.");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        long diffInMinutes = ChronoUnit.MILLIS.between(ticket.getDateTime(), now);


        if(diffInMinutes > show.getCancelMinWin())
        {
            System.out.println("Cancel window period exceeded.");
            return false;
        }

        String seatInfo = ticket.getSeatInfo();
        show.CancelTicket(phoneNumber);
        String[] seatData = seatInfo.split(",");
        for (String seat : seatData)
        {
            if(seat.length()==3)
            {
                int rowNumber = seat.charAt(0) - 64;
                show.setSeatSOccupied(rowNumber - 1, 9, false);
            }
            if(seat.length() == 2)
            {
                int rowNumber = seat.charAt(0) - 64;
                int colNumber = seat.charAt(1) - 48;
                show.setSeatSOccupied(rowNumber - 1, colNumber - 1, false);
            }
        }
        System.out.println("Ticket Number:" + showNumber + "-" + ticketNumber + " cancel success.");
        return true;
    }
}
