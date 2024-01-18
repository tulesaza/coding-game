import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {


    @Test
    public void testAQ() {
        doTest("aq.txt","AQ");
    }


    @Test
    public void testRandom() {
        doTest("random.txt","FH CH EQ BQ EH EH FQ AQ CH AQ EH DQ DQ CH EH CH FQ EQ CQ GH CH EQ FQ EH BQ GH BH FQ CQ FH AH DH GQ AH DQ FH FQ GH DH CH EQ GH EH EH GH BH GQ BH FH CQ CQ FH DH BH EQ CQ GQ CQ DH FH AH FQ CH DH FH EQ EQ BH DQ FQ GQ DH CH GH FQ EH CQ EQ AQ GQ DH EQ CQ FH AQ DQ FH AH FQ EQ");
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
        //assertEquals(expectedResult, result);

    }

}
