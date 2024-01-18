import java.util.*;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int width = in.nextInt(); // width of the firewall grid
        int height = in.nextInt(); // height of the firewall grid
        MapProcessor mapProcessor = new MapProcessor(width, height);
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String mapRow = in.nextLine(); // one line of the firewall grid
            mapProcessor.readMapLine(i, mapRow);
        }

        MapData mapData = mapProcessor.calculatePlantPlaces();

        int maxRounds = in.nextInt();
        int maxBombs = in.nextInt();
        Bomber bomber = new Bomber(maxBombs, maxRounds, mapData);

        while (true) {
            bomber.destroy();
            int rounds = in.nextInt(); // number of rounds left before the end of the game
            int bombs = in.nextInt(); // number of bombs left
        }

    }


    static class Bomber {

        final static int DELAY = 2;

        static int count = 0;

        LinkedList<PlantingPoint> bombQueue;
        final List<PlantingPoint> plants;
        final Set<Node> toBeDestroyed;
        final int maxBombs;
        final int maxRounds;

        Bomber(int maxBombs, int maxRounds, MapData mapData) {
            this.maxBombs = maxBombs;
            this.maxRounds = maxRounds;
            this.plants = mapData.plants;
            this.toBeDestroyed = mapData.toBeDestroyed;
            bombQueue = calculateBombQueueWithDelays();
        }

        public void destroy() {
            if (bombQueue.isEmpty()) {
                System.out.println("WAIT");
                return;
            }
            PlantingPoint bomb = bombQueue.pop();
            if (bomb != null) {
                bomb.putTheBomb();
            } else {
                System.out.println("WAIT");
            }

        }

        private LinkedList<PlantingPoint> calculateBombQueueWithDelays() {
            LinkedList<PlantingPoint> rawQueue = runDFS();
            LinkedList<PlantingPoint> queueWithDelays = new LinkedList<>();
            queueWithDelays.add(rawQueue.removeFirst());
            while (!rawQueue.isEmpty()) {
                PlantingPoint raw = rawQueue.pop();
                if (queueWithDelays.getLast().nodes.contains(raw.bomb)) {
                    for (int i = 0; i < DELAY; i++) {
                        // add delay for next 3 rounds;
                        queueWithDelays.add(null);
                    }
                }
                queueWithDelays.add(raw);
            }
            return queueWithDelays;
        }


        private List<PlantingPoint> getAdjacent(List<PlantingPoint> plants, PlantingPoint without) {
            List<PlantingPoint> abj = new ArrayList<>(plants);
            abj.remove(without);
            return abj;
        }

        private LinkedList<PlantingPoint> runDFS() {

            LinkedList<PlantingPoint> result = new LinkedList<>();
            int depth = 0;
            boolean solved = false;
            LinkedList<Set<PlantingPoint>> visited = new LinkedList<>();
            LinkedList<LinkedList<PlantingPoint>> stack = new LinkedList<>();
            LinkedList<Set<Node>> destr = new LinkedList<>();

            LinkedList<PlantingPoint> currentStack;
            Set<PlantingPoint> currentVisited;
            Set<Node> currentDestr;
            PlantingPoint prevLevel1Plant = null;

            List<PlantingPoint> plantsWithhout;
            Set<Node> toBeDestoroyedWithout;

            for (PlantingPoint plant : plants) {

                toBeDestoroyedWithout = new HashSet<>(toBeDestroyed);
                toBeDestoroyedWithout.removeAll(plant.nodes);
                plantsWithhout = plants
                        .stream()
                        .map(e->e.copyWithout(plant.nodes))
                        .filter(Objects::nonNull)
                        .sorted( Comparator
                                .comparing(PlantingPoint::getLen)
                                .thenComparing(PlantingPoint::getFree)
                                .thenComparing(PlantingPoint::getRow)
                                .thenComparing(PlantingPoint::getCol)
                                .reversed())
                        .collect(Collectors.toList());



                visited.push(new HashSet<>(Collections.singletonList(plant)));
                if (prevLevel1Plant != null) {
                    visited.get(0).add(prevLevel1Plant);
                }
                prevLevel1Plant = plant;
                stack.push(new LinkedList<>(getAdjacent(plantsWithhout, plant)));
                destr.push(new HashSet<>());
                result.add(plant);
                depth++;

                while (!stack.isEmpty()) {
                    currentStack = stack.get(0);
                    currentVisited = visited.get(0);
                    currentDestr = destr.get(0);

                    while (!currentStack.isEmpty() && depth < maxBombs) {

                        PlantingPoint current = currentStack.pop();
                        if (currentVisited.contains(current)) {
                            continue;
                        }
                        count++;
                        // save destroyed, visited and stack for this level and go deeper
                        Set<PlantingPoint> newVisited = new HashSet<>(currentVisited);
                        newVisited.add(current);
                        Set<Node> newDestr = new HashSet<>(currentDestr);
                        newDestr.addAll(current.nodes);
                        visited.push(newVisited);
                        destr.push(newDestr);
                        stack.push(new LinkedList<>(getAdjacent(plantsWithhout, current)));
                        result.add(current);

                        depth++;
                        currentStack = stack.get(0);
                        currentVisited = visited.get(0);
                        currentDestr = destr.get(0);
                    }
                    // if we reached max depth and we can destroy all nodes return
                    if (depth <= maxBombs && currentDestr.size() == toBeDestoroyedWithout.size()) {
                        solved = true;
                        break;
                    }

                    //go to previous level when all combinations checked for this level is checked
                    depth--;
                    stack.pop();
                    visited.pop();
                    destr.pop();
                    result.removeLast();
                }

                if (solved) {
                    break;
                }
            }
            return result;
        }


    }


    static class MapProcessor {
        final int[][] map;
        final Map<String, Node> nodes;

        MapProcessor(int w, int h) {
            map = new int[h][w];
            nodes = new HashMap<>();
        }

        public void readMapLine(int i, String mapRow) {
            for (int j = 0; j < mapRow.length(); j++) {
                char current = mapRow.charAt(j);
                if (current == '@') {
                    addNodeToMap(i, j);
                    nodes.put(i + "-" + j, new Node(i, j));
                }
                if (current == '#') {
                    map[i][j] -= 9;
                }
            }
        }


        public MapData calculatePlantPlaces() {

            return new MapData(removeInclSubGraphs(findPlantPlace()), nodes);

        }


        private void addNodeToMap(int r, int c) {

            for (int i = Math.max(r - 3, 0); i < r; i++) {
                map[i][c]++;
            }
            for (int i = r + 1; i <= Math.min(r + 3, map.length - 1); i++) {
                map[i][c]++;
            }

            for (int j = Math.max(c - 3, 0); j < c; j++) {
                map[r][j]++;
            }

            for (int j = c + 1; j <= Math.min(c + 3, map[0].length - 1); j++) {
                map[r][j]++;
            }

        }

        private TreeSet<PlantingPoint> findPlantPlace() {
            TreeSet<PlantingPoint> plantPlaces = new TreeSet<PlantingPoint>(
                    Comparator
                            .comparing(PlantingPoint::getLen)
                            .thenComparing(PlantingPoint::getFree)
                            .thenComparing(PlantingPoint::getRow)
                            .thenComparing(PlantingPoint::getCol)
                            .reversed());

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] >= 1) {
                        Set<Node> set = findNodesForPlantPlace(i, j);
                        if (!set.isEmpty()) {
                            int free = nodes.containsKey(i + "-" + j) ? 0 : 1;
                            plantPlaces.add(new PlantingPoint(new Node(i, j), free, set));
                        }
                    }
                }

            }
            return plantPlaces;
        }

        private Set<Node> findNodesForPlantPlace(int r, int c) {
            Set<Node> result = new HashSet<>();
            for (int i = Math.max(r - 1, 0); i >= Math.max(r - 3, 0); i--) {
                if (map[i][c] < 0) break;
                if (i == r) continue;
                Node a = nodes.getOrDefault(i + "-" + c, null);
                if (a != null) {
                    result.add(a);
                }
            }

            for (int i = Math.min(r + 1, map.length - 1); i <= Math.min(r + 3, map.length - 1); i++) {
                if (map[i][c] < 0) break;
                if (i == r) continue;
                Node a = nodes.getOrDefault(i + "-" + c, null);
                if (a != null) {
                    result.add(a);
                }
            }

            for (int j = Math.max(c - 1, 0); j >= Math.max(c - 3, 0); j--) {
                if (map[r][j] < 0) break;
                if (j == c) continue;
                Node a = nodes.getOrDefault(r + "-" + j, null);
                if (a != null) {
                    result.add(a);
                }
            }

            for (int j = Math.min(c + 1, map[0].length - 1); j <= Math.min(c + 3, map[0].length - 1); j++) {
                if (map[r][j] < 0) break;
                if (j == c) continue;
                Node a = nodes.getOrDefault(r + "-" + j, null);
                if (a != null) {
                    result.add(a);
                }
            }

            return result;
        }


        private List<PlantingPoint> removeInclSubGraphs(TreeSet<PlantingPoint> plantPlaces) {
            List<PlantingPoint> list = new ArrayList<PlantingPoint>(plantPlaces);
            for (int i = list.size() - 1; i >= 0; i--) {
                for (int j = 0; j < list.size(); j++) {
                    if (i != j && list.get(j).nodes.containsAll(list.get(i).nodes)) {

                        list.remove(i);
                        break;
                    }
                }
            }
            int maxDestrNodes = list.get(0).nodes.size();

            return list;

        }


    }

    static class MapData {
        final List<PlantingPoint> plants;
        final Set<Node> toBeDestroyed;

        MapData(List<PlantingPoint> plants, Map<String, Node> nodes) {
            this.plants = plants;
            this.toBeDestroyed = new HashSet<>(nodes.values());
        }

    }


    static class PlantingPoint {
        final Node bomb;
        final Set<Node> nodes;
        final int isFree;

        PlantingPoint(Node b, int free, Set<Node> nodes) {
            this.bomb = b;
            this.nodes = nodes;
            this.isFree = free;
        }

        public Set<Node> getNodes() {
            return nodes;
        }

        public int getLen() {
            return nodes.size();
        }

        public int getRow() {
            return bomb.r;
        }

        public int getCol() {
            return bomb.c;
        }

        public int getFree() {
            return isFree;
        }

        public void putTheBomb() {
            System.out.println(bomb.c + " " + bomb.r);
        }


        public PlantingPoint copyWithout(Set<Node> withOut) {
            Set<Node> modified = new HashSet<>(nodes);
            modified.removeAll(withOut);
            if (!modified.isEmpty()) return new PlantingPoint(bomb, isFree, modified);
            else return null;
        }

        @Override
        public String toString() {
            return bomb.toString() + ":" + nodes.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            PlantingPoint other = (PlantingPoint) o;
            return this.bomb.equals(other.bomb);
        }

    }

    static class Node {
        final int r;
        final int c;

        Node(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", r, c);
        }

        @Override
        public int hashCode() {
            return (r + "-" + c).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return this.r == node.r
                    && this.c == node.c;
        }
    }


}