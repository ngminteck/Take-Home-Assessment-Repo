package com.obj;

import java.time.LocalDateTime;

public class Ticket
{

    private final int ticketNo;
    private final LocalDateTime dateTime;

    private final String seatInfo;

    public Ticket(int no, LocalDateTime now, String seatInfo)
    {
        ticketNo = no;
        dateTime = now;
        this.seatInfo = seatInfo;
    }

    public int getTicketNo() {
        return ticketNo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getSeatInfo() {
        return seatInfo;
    }


}
