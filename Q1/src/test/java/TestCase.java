import com.io.IO;
import com.obj.Show;
import com.screen.Screen;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;


public class TestCase
{

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
    public void TestAllSuccessCase()
    {

        Map<Integer, Show> showList = new TreeMap<>();
        IO io = new IO();
        Screen screen = new Screen();



        /* correct setup input */
        boolean result = io.VerifySetupInput("1 10 10 10",showList);
        assertEquals(true, result);

        /* show number already exist */
        result = io.VerifySetupInput("1 10 10 10",showList);
        assertEquals(false, result);

        /*row exceed 26 limit*/
        result = io.VerifySetupInput("2 27 10 10",showList);
        assertEquals(false, result);

        /* col must be more than 1 */
        result = io.VerifySetupInput("2 26 0 10",showList);
        assertEquals(false, result);

        /* window cannot be negative */
        result = io.VerifySetupInput("2 26 10 -10",showList);
        assertEquals(false, result);

        /* success setup */
        result = io.VerifySetupInput("2 26 10 0",showList);
        assertEquals(true, result);

        /* test show list display */
        String currentShowListString ="================================================================================\n"
                + "Show List\n"
                + "================================================================================\n"
                + "1. Show Number:1\n"
                + "2. Show Number:2\n"
                + "0. Return to menu.\n"
                + "================================================================================\n";

        assertEquals(currentShowListString, screen.DisplayShowList(showList));


        /* no Z10 seat in show 1*/
        result = io.VerifyBookingInput("123 z1", 1, showList.get(1));
        assertEquals(false, result);

        /* incorrect seat input format*/
        result = io.VerifyBookingInput("123 a1-a2", 1, showList.get(1));
        assertEquals(false, result);

        /* invalid phone number*/
        result = io.VerifyBookingInput("-123 a1,a2", 1, showList.get(1));
        assertEquals(false, result);

        /* success book */
        result = io.VerifyBookingInput("123 A10,c1", 1, showList.get(1));
        assertEquals(true, result);

        /* same phone number existed in same show */
        result = io.VerifyBookingInput("123 d10,e1", 1, showList.get(1));
        assertEquals(false, result);

        /* A10 already taken in show 1*/
        result = io.VerifyBookingInput("456 A9,A10", 1, showList.get(1));
        assertEquals(false, result);

        /* book success */
        result = io.VerifyBookingInput("456 b10,b9", 1, showList.get(1));
        assertEquals(true, result);

        /* success book show 2 */
        result = io.VerifyBookingInput("123 a1,a2", 2, showList.get(2));
        assertEquals(true, result);


        /* test buyer list in show 1 */
        String str = "Ticket Number:1 Phone Number:123 Seat Number:A10,C1\nTicket Number:2 Phone Number:456 Seat Number:B10,B9\n================================================================================";
        assertEquals(str, showList.get(1).PrintBuyerInfo().toString());


        /* show not exist */
        result = io.VerifyCancelBookingInput("3-1 123", showList);
        assertEquals(false, result);

        /* ticket not match */
        result = io.VerifyCancelBookingInput("1-2 123", showList);
        assertEquals(false, result);

        /* phone not match */
        result = io.VerifyCancelBookingInput("1-1 456", showList);
        assertEquals(false, result);

        /* success cancel */
        result = io.VerifyCancelBookingInput("1-1 123", showList);
        assertEquals(true, result);

        /* phone no found in show 1 */
        result = io.VerifyCancelBookingInput("1-1 123", showList);
        assertEquals(false, result);

        /*  window period exceeding */
        result = io.VerifyCancelBookingInput("2-1 123", showList);
        assertEquals(false, result);

        /* buyer info updated*/
        str = "Ticket Number:2 Phone Number:456 Seat Number:B10,B9\n================================================================================";
        assertEquals(str, showList.get(1).PrintBuyerInfo().toString());

    }
}
