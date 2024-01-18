import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.InputStream;

public class PlayerTest {

    @Test
    public void test1() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test1.txt");
        System.setIn(fips);
        Player.main(args);
        System.setIn(original);
    }

    @Test
    public void test2() throws IOException {

        String[] args = null;
        final InputStream original = System.in;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream("test2.txt");
        System.setIn(fips);
        Player.main(args);
        System.setIn(original);
    }


}

