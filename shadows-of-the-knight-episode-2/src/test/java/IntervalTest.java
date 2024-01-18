import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    @Test
    public void test1() {
        Player.Interval interval = new Player.Interval(0, 32, 32);

        Predicate<Integer> condition = value -> Math.abs(10 - value) > Math.abs(20 - value);

        interval = interval.update(condition);

        assertEquals(16, interval.minBoundaryIncl);
        assertEquals(32, interval.maxBoundaryIncl);
    }


    @Test
    public void test2() {
        Player.Interval interval = new Player.Interval(2, 3, 32);


        Predicate<Integer> condition = value -> Math.abs(2 - value) < Math.abs(3 - value);

        interval = interval.update(condition);

        assertEquals(2, interval.minBoundaryIncl);
        assertEquals(2, interval.maxBoundaryIncl);
    }


    @Test
    public void test3() {
        Player.Interval interval = new Player.Interval(2, 3, 32);


        Predicate<Integer> condition = value -> Math.abs(2 - value) > Math.abs(3 - value);

        interval = interval.update(condition);

        assertEquals(3, interval.minBoundaryIncl);
        assertEquals(3, interval.maxBoundaryIncl);
    }


}