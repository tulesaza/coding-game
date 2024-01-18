import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {


    @Test
    public void testConstant() {

        doTest("testConstant.txt","O(1)");
    }

    @Test
    public void testLog() {

        doTest("testLog.txt","O(log n)");
    }

    @Test
    public void testLinear() {

        doTest("testLinear.txt","O(n)");
    }

    @Test
    public void testNLog() {

        doTest("testNlog.txt","O(n log n)");
    }

    @Test
    public void testSquare() {

        doTest("testSquare.txt","O(n^2)");
    }


    @Test
    public void testN2Log() {

        doTest("testN2log.txt","O(n^2 log n)");
    }

    @Test
    public void testN3() {

        doTest("testN3.txt","O(n^3)");
    }

    @Test
    public void testPow2() {

        doTest("testPow2.txt","O(2^n)");
    }







    private void doTest(String testFileName, String expectedResult) {

        final InputStream testInput = getClass().getClassLoader().getResourceAsStream(testFileName);
        final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        System.setIn(testInput);
        System.setOut(new PrintStream(testOutput));
        Solution.main(null);
        System.setIn(System.in);
        System.setOut(System.out);
        String result = testOutput.toString().replace("\n","");
        assertEquals(expectedResult, result);

    }
}
