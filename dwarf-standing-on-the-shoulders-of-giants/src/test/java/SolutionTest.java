import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class SolutionTest {


    @Test
    public void test1()  {

        String[] args = null;
        final InputStream original = System.in;
        final PrintStream org = System.out;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test1.txt");
        ByteArrayOutputStream redirectedOut = new ByteArrayOutputStream();
        System.setIn(fips);
        System.setOut(new PrintStream(redirectedOut));
        Solution.main(args);
        System.setIn(original);
        System.setOut(org);
        int result = Integer.parseInt(redirectedOut.toString().replace("\n", ""));
        Assertions.assertEquals(3, result);
    }
    @Test
    public void test2()  {

        String[] args = null;
        final InputStream original = System.in;
        final PrintStream org = System.out;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test2.txt");
        ByteArrayOutputStream redirectedOut = new ByteArrayOutputStream();
        System.setIn(fips);
        System.setOut(new PrintStream(redirectedOut));
        Solution.main(args);
        System.setIn(original);
        System.setOut(org);
        int result = Integer.parseInt(redirectedOut.toString().replace("\n", ""));
        Assertions.assertEquals(4, result);
    }

    @Test
    public void test3()  {

        String[] args = null;
        final InputStream original = System.in;
        final PrintStream org = System.out;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test3.txt");
        ByteArrayOutputStream redirectedOut = new ByteArrayOutputStream();
        System.setIn(fips);
        System.setOut(new PrintStream(redirectedOut));
        Solution.main(args);
        System.setIn(original);
        System.setOut(org);
        int result = Integer.parseInt(redirectedOut.toString().replace("\n", ""));
        Assertions.assertEquals(3, result);
    }

}
