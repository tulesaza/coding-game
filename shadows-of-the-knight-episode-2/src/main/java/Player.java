import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // width of the building.
        int H = in.nextInt(); // height of the building.
        in.nextInt(); // maximum number of turns before game over.
        int X0 = in.nextInt();
        int Y0 = in.nextInt();

        Batman batman = new Batman(W, H, X0, Y0, in);
        // game loop
        while (true) {
            batman.move();
        }
    }

    static class Interval {
        final int absoluteMax;
        final int absoluteMin;
        final int minBoundaryIncl;
        final int maxBoundaryIncl;
        final int mid;

        public Interval(int minBoundaryIncl, int maxBoundaryIncl, int absoluteMax) {
            if (maxBoundaryIncl < minBoundaryIncl) {
                throw new RuntimeException(String.format("max boundary %d cannot be less than min boundary %d", maxBoundaryIncl, minBoundaryIncl));
            }
            this.minBoundaryIncl = minBoundaryIncl;
            this.maxBoundaryIncl = maxBoundaryIncl;
            this.absoluteMax = absoluteMax;
            this.absoluteMin = 0;
            this.mid = (maxBoundaryIncl + minBoundaryIncl) / 2;
        }


        public boolean isValueOnAbsoluteBoundary(int value) {
            return value == absoluteMax || value == absoluteMin;
        }

        public int getEquidistantValue(int value) {
            int dist = Math.abs(mid - value);
            return value <= mid ? Math.min(absoluteMax, mid + dist) : Math.max(absoluteMin, mid - dist);
        }

        public boolean canBeDivided() {
            return maxBoundaryIncl > minBoundaryIncl;
        }

        public int getMid() {
            return mid;
        }

        public int getMinBoundaryIncl() {
            return minBoundaryIncl;
        }

        public Interval update(Predicate<Integer> condition) {
            int newMin = minBoundaryIncl, newMax = maxBoundaryIncl;
            int prev = minBoundaryIncl;
            for (int i = minBoundaryIncl; i <= maxBoundaryIncl; i++) {
                if (!condition.test(prev) && condition.test(i)) {
                    newMin = i;
                } else if (condition.test(prev) && !condition.test(i)) {
                    newMax = prev;
                    break;
                }
                prev = i;
            }
            return new Interval(newMin, newMax, absoluteMax);
        }


        @Override
        public String toString() {
            return String.format("[ %d ; %d ], mid: %d", minBoundaryIncl, maxBoundaryIncl, mid);
        }
    }

    static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getPosition() {
            return x + " " + y;
        }
    }


    static class Batman {
        int w;
        int h;
        Interval xInterval;
        Interval yInterval;
        Position prevPosition;
        Position currentPosition;
        final Scanner in;


        public Batman(int W, int H, int X0, int Y0, Scanner in) {
            this.w = W;
            this.h = H;
            currentPosition = new Position(X0, Y0);
            xInterval = new Interval(0, w - 1, w - 1);
            yInterval = new Interval(0, h - 1, h - 1);
            this.in = in;
        }


        public void move() {
            String bombDir = in.next();
            updateIntervals(bombDir);
            int newX = currentPosition.getX(), newY = currentPosition.getY();

            // x is not resolved yet;
            if (xInterval.canBeDivided()) {
                System.err.println("Dividing x");
                int x = currentPosition.getX();
                if (xInterval.isValueOnAbsoluteBoundary(x)) {
                    newX = xInterval.getMid();
                    System.err.println("x " + x + " is lying on absolute boundaries.New x is " + newX);
                } else {
                    newX = xInterval.getEquidistantValue(x);
                    System.err.println("x " + x + ". New x is equidistant " + newX);
                }
                if (newX == x) {
                    System.err.println("Fixed x, +1");
                    newX = Math.min(w - 1, newX + 1);
                }


                // x resolved
            } else {
                // transition to y
                if (newX != xInterval.getMinBoundaryIncl()) {
                    System.err.println("Last x move");
                    newX = xInterval.getMinBoundaryIncl();
                    currentPosition = new Position(newX, newY);
                    System.out.println(currentPosition.getPosition());
                    in.next(); // ignored
                    System.err.println("Started searching y");
                    prevPosition = currentPosition;
                }

                if (yInterval.canBeDivided()) {
                    System.err.println("Dividing y");
                    int y = currentPosition.getY();
                    if (yInterval.isValueOnAbsoluteBoundary(y)) {
                        newY = yInterval.getMid();
                        System.err.print("y " + y + " is lying on absolute boundaries.New y is " + newY);
                    } else {
                        newY = yInterval.getEquidistantValue(y);
                        System.err.print("y " + y + ".New y is equidistant " + newY);
                    }
                    if (newY == y) {
                        System.err.println("Fixed y, +1");
                        newY = Math.min(h - 1, newY + 1);
                    }
                } else {
                    System.err.println("Last move in game");
                    newY = yInterval.getMinBoundaryIncl();
                }
            }
            prevPosition = currentPosition;
            currentPosition = new Position(newX, newY);
            System.out.println(currentPosition.getPosition());

        }

        public void updateIntervals(String bombDir) {
            System.err.printf("Before update, X is %s, Y is %s\n",xInterval,yInterval);
            if(bombDir.equals("UNKNOWN")){
                return;
            }
            if (xInterval.canBeDivided()) {
                xInterval = xInterval.update(createCondition(bombDir, prevPosition.getX(), currentPosition.getX()));
            } else if (yInterval.canBeDivided()) {
                yInterval = yInterval.update(createCondition(bombDir, prevPosition.getY(), currentPosition.getY()));
            }
            System.err.printf("After update, X is %s, Y is %s\n",xInterval,yInterval);
        }


        private Predicate<Integer> createCondition(String bombdir, int prev, int current) {
            switch (bombdir) {
                case "SAME":
                    return value -> Math.abs(prev - value) == Math.abs(current - value);
                case "COLDER":
                    return value -> Math.abs(prev - value) < Math.abs(current - value);
                case "WARMER":
                    return value -> Math.abs(prev - value) > Math.abs(current - value);
                default:
                    return value -> true;
            }
        }

    }
}