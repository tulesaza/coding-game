import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {


    @ParameterizedTest
    @CsvSource(value = {
            "test1.txt, results1.txt",
            "test2.txt, results2.txt",
            "test3.txt, results3.txt",
            "test4.txt, results4.txt",
            "test5.txt, results5.txt"
    })
    public void test(String testDataFileName, String expectedDataFileName) throws URISyntaxException {
        var result = runTest(testDataFileName);
        var expectedResult = readExpectedResult(expectedDataFileName);
        assertEquals(expectedResult, result);
    }

    private List<String> runTest(String fileName) {
        String[] args = null;
        final InputStream original = System.in;
        final PrintStream org = System.out;
        final InputStream fips = getClass().getClassLoader().getResourceAsStream(fileName);
        ByteArrayOutputStream redirectedOut = new ByteArrayOutputStream();
        System.setIn(fips);
        System.setOut(new PrintStream(redirectedOut));
        Solution.main(args);
        System.setIn(original);
        System.setOut(org);
        return Arrays.asList(redirectedOut.toString(StandardCharsets.UTF_8).split("\n"));
    }

    private List<String> readExpectedResult(String fileName) throws URISyntaxException {
        final URL resultsUrl = getClass().getClassLoader().getResource(fileName);
        assert resultsUrl != null;
        File resultsFile = new File(resultsUrl.toURI());

        List<String> lines = null;
        try {
            lines = Files.readAllLines(resultsFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
