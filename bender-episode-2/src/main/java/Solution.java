import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        Map<String, Room> rooms = new HashMap<>(N);
        for (int i = 0; i < N; i++) {
            String[] data = in.nextLine().split(" ");
            String roomNumber = data[0];
            int cost = Integer.parseInt(data[1]);
            if (!rooms.containsKey(roomNumber)) {
                rooms.put(roomNumber, new Room(roomNumber, cost));
            } else {
                rooms.get(roomNumber).setCost(cost);
            }

            for (int j = 2; j <= 3; j++) {
                String parent = data[j];
                if (rooms.containsKey(parent)) {
                    rooms.get(parent).addChild(roomNumber);
                } else {
                    Room parentRoom = new Room(parent);
                    parentRoom.addChild(roomNumber);
                    rooms.put(parent, parentRoom);
                }
            }

        }
        int[] gain = new int[N];
        gain[0] = rooms.get("0").getCost();
        for (int i = 1; i < N; i++) {
            Room currentRoom  = rooms.get(String.valueOf(i));
            // if we found room with no children, that means it's start room, for this task
            // ONLY "0" room can be a start room, so break.
            if (currentRoom.getChildren().size()==0) {
                break;
            }
            gain[i] = currentRoom.getCost() +
                    currentRoom.getChildren()
                            .stream()
                            .map(child -> gain[Integer.parseInt(child)])
                            .max(Integer::compare)
                            .orElse(0);
        }

        int maxOnExit = rooms.get("E").getChildren().stream()
                .map(child -> gain[Integer.parseInt(child)])
                .max(Integer::compare)
                .get();
        System.out.println(maxOnExit);
    }

    static class Room {
        final String roomNumber;
        final Set<String> children;
        int cost;

        public Room(String roomNumber) {
            this.roomNumber = roomNumber;
            this.children = new HashSet<>();
        }

        public void setCost(int cost) {
            this.cost = cost;
        }


        public Room(String roomNumber, int cost) {
            this.roomNumber = roomNumber;
            this.cost = cost;
            this.children = new HashSet<>();
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public int getCost() {
            return cost;
        }

        public Set<String> getChildren() {
            return children;
        }

        public void addChild(String child) {
            children.add(child);
        }
    }
}