import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class SolutionTest {

    @Test
    public void test1() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test1.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }

    @Test
    public void test2() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test3.txt");
        System.setIn(fips);
        Solution.main(args);
        System.setIn(original);
    }


}

