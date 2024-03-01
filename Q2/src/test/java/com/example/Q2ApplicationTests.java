package com.example;


import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.Assert.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class Q2ApplicationTests {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setStreams() {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @After
    public void restoreInitialStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @Test
    void contextLoads()
    {
        Logic logic = new Logic();
        InputVerfiy inputVerfiy = new InputVerfiy();

        boolean result= inputVerfiy.VerfiyInput("\"3,4,N\" \"f,f,r,f,f\"", logic);
        assertEquals(true, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\" \"f,f,r,f,f\" \"3,4,N\" \"f,f,r,f,f\"", logic);
        assertEquals(true, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\" \"f,f,r,f,f\" \"3,5,N\" \"r\"", logic);
        assertEquals(true, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\" \"f,f,r,f,f\" \"-3,-4,N\" \"f,f,r,f\"", logic);
        assertEquals(true, result);
        System.out.println();


        result= inputVerfiy.VerfiyInput("\"3,4,n\" \"f,f,r,f,f\"", logic);
        assertEquals(false, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\" \"F,f,r,f,f\"", logic);
        assertEquals(false, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\" \"f,f,r,ff\"", logic);
        assertEquals(false, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"f,f,r,f,f\" \"3,4,N\"", logic);
        assertEquals(false, result);
        System.out.println();
        result= inputVerfiy.VerfiyInput("\"3,4,N\"", logic);
        assertEquals(false, result);
    }

}
