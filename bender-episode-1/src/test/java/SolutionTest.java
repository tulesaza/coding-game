import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class SolutionTest {


    @Test
    public void test2() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("testMultipleLoops.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }

    @Test
    public void test3() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("testInverter.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }


    @Test
    public void test4() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("testLoop.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }


    @Test
    public void test5() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("testPathModifier.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }

    @Test
    public void test6() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("testBreaker.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }

    @Test
    public void test44() throws IOException {

      System.out.println(0 % 4);
        System.out.println(1 % 4);
        System.out.println(2 % 4);
        System.out.println(3 % 4);
        System.out.println(4 % 4);
        System.out.println(5 % 4);
    }
}
