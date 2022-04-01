package project1.jcscheufele.dbs2_d22.cs.wpi.java;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    public String[] args = {"3"};
    
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testRun()
    {
        App app = new App(args);
        assertEquals(3, app.poolSize);

    }

    @Test
    public void testCharArray()
    {
        String some = "some";
        char[] somech = some.toCharArray();
        assertEquals('o', somech[1]);
    }

    @Test
    public void testInputParser_GET()
    {
        App app = new App(args);
        int[] commands = app.inputParser("GET 430");
        assertEquals(1, commands[0]);
    }

    @Test
    public void testInputParser_GET_430()
    {
        App app = new App(args);
        int[] commands = app.inputParser("GET 430");
        assertEquals(430, commands[1]);
    }

    @Test
    public void testInputParser_SET()
    {
        App app = new App(args);
        int[] commands = app.inputParser("SET 430 \"F05-Rec450, Jane Do, 10 Hill Rd, age020.\"");
        assertEquals(2, commands[0]);
    }

    @Test
    public void testInputParser_SET_430()
    {
        App app = new App(args);
        int[] commands = app.inputParser("SET 430 \"F05-Rec450, Jane Do, 10 Hill Rd, age020.\"");
        assertEquals(430, commands[1]);
    }

    @Test
    public void testRecordParser()
    {
        App app = new App(args);
        String test = "\"F05-Rec450, Jane Do, 10 Hill Rd, age020.\"";
        String record = app.recordParser("SET 430 \"F05-Rec450, Jane Do, 10 Hill Rd, age020.\"");
        assertEquals(test, record);
    }

    @Test
    public void testInputParser_PIN()
    {
        App app = new App(args);
        int[] commands = app.inputParser("PIN 5");
        assertEquals(3, commands[0]);
    }

    @Test
    public void testInputParser_PIN_5()
    {
        App app = new App(args);
        int[] commands = app.inputParser("PIN 5");
        assertEquals(5, commands[1]);
    }


    @Test
    public void testInputParser_UNPIN()
    {
        App app = new App(args);
        int[] commands = app.inputParser("UNPIN 3");
        assertEquals(4, commands[0]);
    }

    @Test
    public void testInputParser_UNPIN_3()
    {
        App app = new App(args);
        int[] commands = app.inputParser("UNPIN 3");
        assertEquals(3, commands[1]);
    }

    @Test
    public void testInputParser_EXIT()
    {
        App app = new App(args);
        int[] commands = app.inputParser("EXIT 3");
        assertEquals(5, commands[0]);
    }

}
