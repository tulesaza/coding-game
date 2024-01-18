import java.util.*;
import java.util.function.Function;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int L = in.nextInt();
        int C = in.nextInt();
        char[][] map = new char[L][C];
        List<Point> points = new ArrayList<>(2);
        int destrObstacles = 0;
        int startR = 0;
        int startC = 0;
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < L; i++) {
            char[] row = in.nextLine().toCharArray();
            for (int j = 0; j < row.length; j++) {
                map[i][j] = row[j];
                if (row[j] == '@') {
                    startR = i;
                    startC = j;
                }
                if (row[j] == 'T') {
                    points.add(new Point(i, j));
                }
                if (row[j] == 'X') {
                    destrObstacles++;
                }
            }
        }

        Map<Point, Point> teleportMap = new HashMap<>();
        if (points.size() == 2) {
            teleportMap.put(points.get(0), points.get(1));
            teleportMap.put(points.get(1), points.get(0));
        }

        BendersState initial = new BendersState(startR, startC, teleportMap, map, destrObstacles);

        BendersBrain bendersBrain = new BendersBrain(initial);

        bendersBrain.start();
    }

    static class BendersBrain {

        final static Map<Character, Function<BendersState, BendersState>> TRANSITIONS = new HashMap<>() {{
            put(' ', BendersState::move);
            put('@', BendersState::move);
            put('T', BendersState::teleport);
            put('B', BendersState::bear);
            put('I', BendersState::invert);
            put('#', BendersState::turn);
            put('X', BendersState::breakObstacle);
            put('S', BendersState::changeDirectionS);
            put('N', BendersState::changeDirectionN);
            put('W', BendersState::changeDirectionW);
            put('E', BendersState::changeDirectionE);
            put('$', BendersState::die);

        }};

        final LinkedHashSet<BendersState> stateMemory = new LinkedHashSet<>();

        BendersState currentState;

        public BendersBrain(BendersState currentState) {
            this.currentState = currentState;
            stateMemory.add(currentState);
        }

        public void start() {
            boolean loop = false;
            while (currentState.isAlive) {
                char nextChar = currentState.readNextCell();
                BendersState nextState = TRANSITIONS.get(nextChar).apply(currentState);

                if (!stateMemory.add(nextState)) {
                    loop = true;
                    break;
                } else {
                    currentState = nextState;
                }
            }
            if (loop) {
                System.out.println("LOOP");
                return;
            }
            for (String step : currentState.steps) {
                System.out.println(step);
            }

        }

    }

    enum Direction {
        SOUTH, EAST, NORTH, WEST
    }


    static class Point {
        final int row;
        final int column;

        Point(int r, int c) {
            this.row = r;
            this.column = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            Point point = (Point) o;
            return row == point.row &&
                    column == point.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    static class BendersState {
        final static boolean DEFAULT_BREAKER = false;
        final static boolean DEFAULT_INVERTED = false;
        final static boolean DEFAULT_ALIVE = true;
        final static Direction DEFAULT_INIT_DIRECTION = Direction.SOUTH;
        final static int DEFAULT_PRIORITY = 0;
        final static Direction[] PRIORITIES = {Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST};
        final static Direction[] INVERTED_PRIORITIES = {Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH};


        int destrObstacles;
        final int row;
        final int column;
        final boolean breakerMode;
        final boolean inverted;
        final boolean isAlive;
        final Direction direction;
        final int priority;
        final Map<Point, Point> teleportMap;
        final char[][] map;
        final LinkedList<String> steps;

        public BendersState(int row, int column, boolean breakerMode, boolean inverted, boolean isAlive,
                            Direction direction, int priority, Map<Point, Point> teleportMap,
                            char[][] map, LinkedList<String> steps, int destrObstacles) {
            this.row = row;
            this.column = column;
            this.breakerMode = breakerMode;
            this.inverted = inverted;
            this.isAlive = isAlive;
            this.direction = direction;
            this.priority = priority;
            this.teleportMap = teleportMap;
            this.map = map;
            this.steps = steps;
            this.destrObstacles = destrObstacles;
        }

        BendersState(int row, int column, Map<Point, Point> teleportMap, char[][] map, int destrObstacles) {
            this.row = row;
            this.column = column;
            this.teleportMap = teleportMap;
            this.map = map;
            this.breakerMode = DEFAULT_BREAKER;
            this.inverted = DEFAULT_INVERTED;
            this.isAlive = DEFAULT_ALIVE;
            this.direction = DEFAULT_INIT_DIRECTION;
            this.priority = DEFAULT_PRIORITY;
            this.steps = new LinkedList<>();
            this.destrObstacles = destrObstacles;
        }

        private int getNextRow() {
            int newRow = this.row;
            switch (this.direction) {
                case SOUTH:
                    newRow++;
                    break;
                case NORTH:
                    newRow--;
                    break;
                default:
                    break;
            }
            return newRow;
        }

        private int getNextColumn() {
            int newColumn = this.column;
            switch (this.direction) {
                case WEST:
                    newColumn--;
                    break;
                case EAST:
                    newColumn++;
                    break;
                default:
                    break;
            }
            return newColumn;
        }

        public BendersState move() {
            steps.add(this.direction.toString());
            return new BendersState(getNextRow(), getNextColumn(),
                                    this.breakerMode, this.inverted, this.isAlive,
                                    this.direction, DEFAULT_PRIORITY, this.teleportMap, this.map, this.steps,
                                    this.destrObstacles);
        }

        public BendersState teleport() {
            BendersState current = move();
            Point currentLocation = new Point(current.row, current.column);
            Point remote = teleportMap.get(currentLocation);

            return new BendersState(remote.row, remote.column,
                                    current.breakerMode, current.inverted, current.isAlive,
                                    current.direction, current.priority, current.teleportMap, current.map,
                                    current.steps, this.destrObstacles);
        }

        public BendersState bear() {
            BendersState current = move();
            return new BendersState(current.row, current.column,
                                    !current.breakerMode, current.inverted, current.isAlive,
                                    current.direction, current.priority, current.teleportMap, current.map,
                                    current.steps, this.destrObstacles);
        }

        public BendersState invert() {
            BendersState current = move();
            return new BendersState(current.row, current.column,
                                    current.breakerMode, !current.inverted, current.isAlive,
                                    current.direction, current.priority, current.teleportMap, current.map,
                                    current.steps, this.destrObstacles);
        }

        public BendersState turn() {
            Direction[] currentPriorities = inverted ? INVERTED_PRIORITIES : PRIORITIES;
            Direction currentDirectionByPriority = currentPriorities[this.priority];

            Direction nextDirection = this.direction.equals(
                    currentDirectionByPriority) ? currentPriorities[(this.priority + 1) % 4] : currentDirectionByPriority;
            int nextPriority = this.direction.equals(
                    currentDirectionByPriority) ? this.priority : (this.priority + 1) % 4;


            return new BendersState(this.row, this.column,
                                    this.breakerMode, this.inverted, this.isAlive,
                                    nextDirection, nextPriority, this.teleportMap, this.map, this.steps,
                                    this.destrObstacles);
        }

        public BendersState breakObstacle() {
            if (breakerMode) {
                map[getNextRow()][getNextColumn()] = ' ';
                this.destrObstacles--;
                return move();
            } else {
                return turn();
            }
        }

        public BendersState changeDirectionS() {
            return changeDirection('S');
        }

        public BendersState changeDirectionN() {
            return changeDirection('N');
        }

        public BendersState changeDirectionW() {
            return changeDirection('W');
        }

        public BendersState changeDirectionE() {
            return changeDirection('E');
        }


        public BendersState die() {
            BendersState current = move();
            return new BendersState(current.row, current.column,
                                    current.breakerMode, current.inverted, false,
                                    current.direction, current.priority, current.teleportMap, current.map,
                                    current.steps, this.destrObstacles);
        }

        private BendersState changeDirection(char dir) {
            BendersState current = move();
            Direction nextDirection = null;
            if (dir == 'S') nextDirection = Direction.SOUTH;
            if (dir == 'N') nextDirection = Direction.NORTH;
            if (dir == 'W') nextDirection = Direction.WEST;
            if (dir == 'E') nextDirection = Direction.EAST;

            return new BendersState(current.row, current.column,
                                    current.breakerMode, current.inverted, current.isAlive,
                                    nextDirection, current.priority, current.teleportMap, current.map, current.steps,
                                    this.destrObstacles);

        }

        public char readNextCell() {
            int nextRow = getNextRow();
            int nextColumn = getNextColumn();
            return map[nextRow][nextColumn];
        }


        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            BendersState state = (BendersState) o;
            return row == state.row &&
                    column == state.column &&
                    breakerMode == state.breakerMode &&
                    inverted == state.inverted &&
                    isAlive == state.isAlive &&
                    direction.equals(state.direction) &&
                    priority == state.priority &&
                    destrObstacles == state.destrObstacles;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column, breakerMode, inverted, isAlive, direction, priority, destrObstacles);
        }


    }


}