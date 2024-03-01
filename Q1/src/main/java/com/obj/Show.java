package com.obj;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class Show
{

    private int ticketNumberGenerate;

    private final int row;
    private final int col;
    private final long cancelMinWin;
    private final boolean[][] seatOccupied;

    private final Map<Integer, Ticket> ticketInfo = new TreeMap<>();

    public  Show(int r, int c, long min)
    {
        ticketNumberGenerate = 0;
        row =r;
        col = c;
        seatOccupied = new boolean[row][col];
        /* millisecond work well in this scenario */
        cancelMinWin = min * 60  * 1000 ;

    }

    public int GenerateNewTicketNumber()
    {
        ++ticketNumberGenerate;
        return ticketNumberGenerate;
    }

    /*True mean occupied */
    public boolean GetSeatSOccupied(int rowIndex, int colIndex)
    {
        return seatOccupied[rowIndex][colIndex];
    }

    public void setSeatSOccupied(int rowIndex, int colIndex, boolean status)
    {
        seatOccupied[rowIndex][colIndex] = status;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void BookShow(Integer phoneNo, String seatInfo)
    {
        ticketInfo.put(phoneNo, new Ticket(ticketNumberGenerate, LocalDateTime.now(), seatInfo));
    }

    public void CancelTicket(Integer key)
    {
        ticketInfo.remove(key);
    }

    public long getCancelMinWin()
    {
        return cancelMinWin;
    }
    public Map<Integer, Ticket> getTicketInfo()
    {
        return ticketInfo;
    }

    public StringBuilder PrintBuyerInfo()
    {
        StringBuilder sb = new StringBuilder();
        if(ticketInfo.isEmpty())
            sb.append("No buyer.");
        ticketInfo.forEach((k, v) -> sb.append("Ticket Number:" +  v.getTicketNo() + " Phone Number:" + k + " Seat Number:" + v.getSeatInfo() + "\n"));
        sb.append("================================================================================");

        return sb;
    }

    public StringBuilder PrintAllAvailableSeat()
    {
        String letters = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder printInfo = new StringBuilder();
        printInfo.append("\n ");
        for(int c = 0; c < col ; ++c)
        {
            printInfo.append(" ").append(c + 1).append(" ");
        }
        for(int r = 0; r < row; ++r)
        {
            printInfo.append("\n").append(letters.charAt(r + 1));
            for(int c =0 ; c < col; ++c)
            {
                if(seatOccupied[r][c])
                    printInfo.append("[X]");
                else
                    printInfo.append("[ ]");
            }
        }
        printInfo.append("\n");

        return printInfo;
    }


}
